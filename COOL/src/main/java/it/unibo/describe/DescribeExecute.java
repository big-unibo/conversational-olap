package it.unibo.describe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.ResultSetHelperService;

import it.unibo.antlr.gen.DescribeLexer;
import it.unibo.antlr.gen.DescribeParser;
import it.unibo.conversational.algorithms.Parser;
import it.unibo.conversational.database.QueryGeneratorChecker;
import it.unibo.conversational.datatypes.DependencyGraph;
import it.unibo.describe.antlr.DescribeListenerCustom;

public class DescribeExecute {
  public static void main(String[] args) throws Exception {
    final String path = "resources/describe/output/";
    //    Describe d = DescribeExecute.parse("with sales_fact_1997 describe unit_sales by store_id");
    //    DescribeExecute.execute(d, path);
    Describe d = DescribeExecute.parse("with sales_fact_1997 describe unit_sales by the_date");
    DescribeExecute.execute(d, path);
    d = DescribeExecute.parse(d, "with sales_fact_1997 describe unit_sales by product_id");
    DescribeExecute.execute(d, path);
    d = DescribeExecute.parse(d, "with sales_fact_1997 describe unit_sales by customer_id");
    DescribeExecute.execute(d, path);
  }

  public static boolean DEBUG = false;
  private static final Logger L = LoggerFactory.getLogger(DescribeExecute.class);

  public static Describe parse(final String input) {
    return parse(null, input);
  }

  public static Describe parse(Describe d, String input) {
    final DescribeLexer lexer = new DescribeLexer(new ANTLRInputStream(input)); // new ANTLRInputStream(System.in);
    final CommonTokenStream tokens = new CommonTokenStream(lexer); // create a buffer of tokens pulled from the lexer
    final DescribeParser parser = new DescribeParser(tokens); // create a parser that feeds off the tokens buffer
    final ParseTree tree = parser.describe(); // begin parsing at init rule
    final ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
    final DescribeListenerCustom extractor = new DescribeListenerCustom(d);
    walker.walk(extractor, tree); // initiate walk of tree with listener
    return extractor.getDescribe();
  }

  public static JSONObject execute(Describe d, final String path) throws Exception {
    return execute(d, path, "src/main/python/");
  }

  public static JSONObject execute(Describe d, final String path, final String pythonPath) throws Exception {
    final int sessionStep = d.getSessionStep();
    final String filename = d.getFilename();
    Long startTime = System.currentTimeMillis();
    final Statement stmt = QueryGeneratorChecker.getDataConnection().createStatement();
    final String sql = Parser.createQuery(d.getJSON());
    L.warn(sql);
    final ResultSet res = stmt.executeQuery(sql);
    final CSVWriter writer = new CSVWriter(new FileWriter(path + filename + "_" + sessionStep + ".csv"));
    final ResultSetHelperService resultSetHelperService = new ResultSetHelperService();
    resultSetHelperService.setDateFormat("yyyy-MM-dd");
    resultSetHelperService.setDateTimeFormat("yyyy-MM-dd"); // HH:MI:SS
    writer.setResultService(resultSetHelperService);
    writer.writeAll(res, true);
    writer.close();
    stmt.close();

    L.warn("query_time," + (System.currentTimeMillis() - startTime));
    startTime = System.currentTimeMillis();
    final String commandPath = (pythonPath + "Describe/venv/Scripts/python.exe " + pythonPath + "Describe/models.py "); //.replace("/", File.separator);
    final String fullCommand = commandPath.replace("/", File.separator)//
            + " --path \"" + path.replace("\\", "/") + "\""//
            + " --file " + filename //
            + " --session_step " + sessionStep //
            + (d.getK().isPresent()? " --k " + d.getK().get() : "")//
            + (d.getModels().isEmpty()? "" : (" --models " + d.getModels().stream().reduce("", (a, b) -> a + " " + b)));
    L.warn(fullCommand);
    final Process proc = Runtime.getRuntime().exec(fullCommand);
    final int ret = proc.waitFor();
    L.warn("model_time," + (System.currentTimeMillis() - startTime));

    if (ret != 0) {
      final BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      String s;
      String error = "";
      while ((s = stdInput.readLine()) != null) {
        error += s + "\n";
      }
      final BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
      while ((s = stdError.readLine()) != null) {
        error += s + "\n";
      }
      throw new IllegalArgumentException(error);
    }

    startTime = System.currentTimeMillis();
    final Set<String> prevGc = d.getPreviousAttributes();
    final Set<String> currGc = d.getAttributes();
    // Read the current result
    final CSVReader currReader = new CSVReader(new FileReader(path + filename + "_" + sessionStep + "_ext.csv"));
    // its current schema
    final String[] currSchema = currReader.readNext();
    final int[] currKey = getIdx(currSchema, prevGc.size() != currGc.size() ? Sets.intersection(prevGc, currGc) : currGc);
    // and its data
    final List<List<String>> currData = currReader.readAll().stream().map(r -> Arrays.asList(r)).collect(Collectors.toList());
    currReader.close();
    L.warn("cardinality," + currData.size());

    /* ***********************************************************************
     * FIND ROLLUP/DRILLDOWN RELATIONSHIP BETWEEN CONSECUTIVE GBYSETS
     * **********************************************************************/
    final Map<String, String> spec2gen;
    final Map<String, List<String>> gen2spec;
    boolean rollup = false;
    if (!currGc.equals(prevGc) && currGc.size() == prevGc.size()) {
      final List<String> diff = Sets.symmetricDifference(prevGc, currGc).stream().collect(Collectors.toList());
      final String a0 = diff.get(0).toLowerCase();
      final String a1 = diff.get(1).toLowerCase();
      final String specific = DependencyGraph.lca(a0, a1).orNull();
      if (prevGc.contains(specific)) {
        rollup = true;
        gen2spec = QueryGeneratorChecker.getFunctionalDependency2(specific, specific.equals(a0) ? a1 : a0);
        spec2gen = null;
      } else {
        spec2gen = QueryGeneratorChecker.getFunctionalDependency(specific, specific.equals(a0) ? a1 : a0);
        gen2spec = null;
      }
    } else {
      spec2gen = null;
      gen2spec = null;
    }
    final Map<Set<String>, List<String>> prevData = Maps.newLinkedHashMap();
    String[] prevSchema = null;
    final int[] prevKey;

    /* ***********************************************************************
     * READ THE DATASETS
     * **********************************************************************/
    if (sessionStep >= 1) {
      // Read the previous file... its schema (i.e., csv header)
      final CSVReader prevReader = new CSVReader(new FileReader(path + filename + "_" + (sessionStep - 1) + "_enhanced.csv"));
      prevSchema = prevReader.readNext();
      // get the cell coordinates (i.e., unique key -- group by set)
      prevKey = getIdx(prevSchema, prevGc);
      // populate the cells with their coordinates
      prevReader.forEach(row -> prevData.put(getKey(prevKey, row), Arrays.asList(row)));
      prevReader.close();
    }

    /* ***********************************************************************
     * COMPUTE THE PROXY
     * **********************************************************************/
    // proxy: currentCoordinate -> previousRows
    final Map<List<String>, List<List<String>>> proxy = Maps.newLinkedHashMap();
    // iterate over current data to find proxy cells
    for (final List<String> row : currData) {
      // get the coordinates of the current cell
      final Set<String> key = getKey(currKey, row);
      // initialize the empty proxy
      final List<List<String>> acc = Lists.newArrayList();
      // there have been a rollup/drilldown within the group by set
      if (currGc.size() == prevGc.size() && !currGc.equals(prevGc)) {
        if (rollup) { // if is a rollup (i.e., from "by city" to "by region"
          final String rup = key.stream().filter(v -> gen2spec.containsKey(v)).findFirst().get(); // ER
          final List<String> values = gen2spec.get(rup); // List(CE, BO, ...)
          values.forEach(val -> { // for <Fanta, ER> proxy cells are {<Fanta, CE>, <Fanta, BO> }
            final Set<String> newKey = Sets.newLinkedHashSet(key);
            newKey.remove(rup);
            newKey.add(val);
            final List<String> p = prevData.get(newKey);
            if (p != null) { // not all the functional dependencies are included in the proxy cells
              acc.add(p);
            }
          });
        } else { // if is a drilldown (i.e., from "by region" to "by city"
          final String val = key.stream().filter(v -> spec2gen.containsKey(v)).findFirst().get(); // CE
          final String rup = spec2gen.get(val); // ER
          final Set<String> newKey = Sets.newLinkedHashSet(key);
          newKey.remove(val);
          newKey.add(rup);
          final List<String> p = prevData.get(newKey);
          if (p != null) { // not all the functional dependencies are included in the proxy cells
            acc.add(p); // for <Fanta, CE> proxy cells are {<Fanta, ER>}
          }
        }
      } else { // a new attribute has been added
        final List<String> prev = prevData.get(key);
        if (prev != null) {
          acc.add(prev);
        }
      }
      proxy.put(row, acc);
    }

    /* ***********************************************************************
     * ESTIMATE SURPRISE
     * **********************************************************************/
    // get the zscores of the previous and current cells
    final int[] prevZscore = sharedScores(prevSchema, currSchema);
    final int[] currZscore = sharedScores(currSchema, prevSchema);
    final double[] surprise = new double[proxy.size()];
    if (currGc.equals(prevGc) && d.getClause().equals(d.getPrevClause())) {
      // if the GC and SC are the same, probably the user is only changing the model or the size
      // don't suppress the surprise
      int c = 0;
      int surpriseIdx = getSurprise(prevSchema);
      for (List<String> r : prevData.values()) {
        surprise[c++] = surpriseIdx == -1 ? 0 : Double.parseDouble(r.get(surpriseIdx));
      }
    } else {
      int c = 0;
      for (Entry<List<String>, List<List<String>>> e : proxy.entrySet()) {
        double curSurprise = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < currZscore.length; i++) {
          // compute the avg zscore of the proxy cells
          double sumMeasureZScore = 0;
          int count = 0;
          for (final List<String> r: e.getValue()) {
            if (r == null) {
              throw new IllegalArgumentException("Mapping is null");
            }
            sumMeasureZScore += prevZscore.length == 0 ? 0 : Double.parseDouble(r.get(prevZscore[i]));
            count++;
          }
          double zscore = Double.parseDouble(e.getKey().get(currZscore[i]));
          // and subtract it to the zscore of the current cell current
          curSurprise = Math.max(curSurprise, zscore - (count == 0 ? 0 : sumMeasureZScore / count));
        }
        // estimate the surprise value of each cell as the maximum between all the measure's zscores
        surprise[c++] = Math.round(curSurprise * 1000) / 1000.0;
      }
    }
    /* ***********************************************************************
     * ESTIMATE MODEL INTEREST
     * **********************************************************************/
    final Map<String, Map<String, Double>> modelInterest = Maps.newLinkedHashMap();
    final JSONObject json = new JSONObject();
    for (final String model : getModels(currSchema)) {
      final int modelIdx = getModelIdx(currSchema, model);
      final Map<String, Double[]> components = Maps.newLinkedHashMap();
      for (final LongAdder i = new LongAdder(); i.intValue() < currData.size(); i.add(1)) {
        final List<String> row = currData.get(i.intValue());
        components.compute(row.get(modelIdx),//
            (k, v) -> v == null//
                ? new Double[] { surprise[i.intValue()], 1.0 }//
                : new Double[] { v[0] + surprise[i.intValue()], v[1] + 1 }//
        );
      }
      final Map<String, Double> componentInterest = components.entrySet().stream()//
          .map(e -> Pair.of(e.getKey(), e.getValue()[0] / e.getValue()[1]))//
          .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
      modelInterest.put(model, componentInterest);
    }
    L.warn("interest_time," + (System.currentTimeMillis() - startTime));

    final CSVReader propReader = new CSVReader(new FileReader(path + filename + "_" + sessionStep + "_properties.csv"));
    propReader.readNext();
    final Map<Pair<String, String>, List<String[]>> componentToProp = Maps.newLinkedHashMap();
    for (final String[] c : propReader.readAll()) {
      componentToProp.compute(Pair.of(c[0], c[1]), (k, v) -> {
        if (v == null) {
          List<String[]> acc = Lists.newLinkedList();
          acc.add(c);
          return acc;
        } else {
          v.add(c);
          return v;
        }
      });
    }
    propReader.close();

    final CSVWriter writerModel = new CSVWriter(new FileWriter(path + filename + "_" + sessionStep + "_model.csv"));
    modelInterest.entrySet().stream()//
        .flatMap(m -> m.getValue().entrySet().stream().map(e -> Triple.of(m.getKey(), e.getKey(), e.getValue())))//
        .sorted((a, b) -> {
          int c = -Double.compare(a.getRight(), b.getRight());
          return c != 0 ? c : (a.getLeft() + a.getMiddle()).compareTo(b.getLeft() + b.getMiddle());
        }).forEach(t -> {
          final JSONObject rowJson = new JSONObject();
          rowJson.put("component", t.getLeft() + "=" + t.getMiddle());
          rowJson.put("interest", Math.round(t.getRight() * 1000) / 1000.0);
          final JSONObject properties = new JSONObject();
          for (String[] p : componentToProp.getOrDefault(Pair.of(t.getLeft(), t.getMiddle()), Lists.newLinkedList())) {
            properties.put(p[2], p[3]);
          }
          rowJson.put("properties", properties);
          json.append("components", rowJson);
          writerModel.writeNext(new String[] { t.getLeft(), t.getMiddle(), t.getRight() + "" });
        });
    writerModel.close();

    /* ***********************************************************************
     * WRITE TO FILE
     * **********************************************************************/
    startTime = System.currentTimeMillis();
    final CSVWriter writerEnh = new CSVWriter(new FileWriter(path + filename + "_" + sessionStep + "_enhanced.csv"));
    final String[] header = toArray(Lists.newArrayList(currSchema), "surprise");
    writerEnh.writeNext(header);
    Set<String> measures = getMeasures(header);
    for (int i = 0; i < currData.size(); i++) {
      final String[] array = toArray(Lists.newLinkedList(currData.get(i)), surprise[i]);
      final JSONObject rowJson = new JSONObject();
      for (int j = 0; j < array.length; j++) {
        rowJson.put(header[j], array[j]);
      }
      json.append("raw", rowJson);
      writerEnh.writeNext(array);

      for (final String m : measures) {
        final JSONObject redJson = new JSONObject();
        for (int j = 0; j < array.length; j++) {
          redJson.put(header[j], array[j]);
          if (header[j].equals(m)) {
            redJson.put("measure", header[j]);
            redJson.put("value", array[j]);
          }
        }
        json.append("red", redJson);
      }
    }
    writerEnh.close();
    d.incSessionStep();
    json.put("pivot", getPivot(getIdx(currSchema, currGc), currData, Arrays.asList(currSchema)));
    L.warn("pivot_time," + (System.currentTimeMillis() - startTime));
    return json;
  }

  private static String[] toArray(final List<String> row, Object... toAdd) {
    final String[] array = new String[row.size() + toAdd.length];
    int i = 0;
    for (; i < row.size(); i++) {
      array[i] = row.get(i);;
    }
    for (; i < array.length; i++) {
      array[i] = toAdd[i - row.size()].toString();
    }
    return array;
  }

  private static JSONObject getPivot(final int[] currSchema, final List<List<String>> data, final List<String> header) {
    // Sort the columns to get the order of the pivot table
    final List<Pair<Integer, Long>> idx = Lists.newLinkedList();
    final int[] sortedSchema = new int[currSchema.length];
    for (final int i : currSchema) {
      idx.add(Pair.of(i, data.stream().map((List<String> r) -> r.get(i)).distinct().count()));
    }
    idx.sort((a, b) -> Long.compare(a.getValue(), b.getValue()));
    final JSONObject headers = new JSONObject();
    int c = 0;
    for (int i = 0; i < idx.size(); i++) {
      final JSONObject dimension = new JSONObject();
      if (i % 2 == 0) {
        sortedSchema[c] = idx.get(i).getKey();
        headers.append("rows", header.get(idx.get(i).getKey()));
        dimension.put("attribute", header.get(idx.get(i).getKey()));
        dimension.put("cardinality", idx.get(i).getValue());
        headers.append("dimensions", dimension);
      } else {
        sortedSchema[c++ + idx.size() / 2 + idx.size() % 2] = idx.get(i).getKey();
        headers.append("columns", header.get(idx.get(i).getKey()));
        dimension.put("attribute", header.get(idx.get(i).getKey()));
        dimension.put("cardinality", idx.get(i).getValue());
        headers.append("dimensions", dimension);
      }
    }
    List<String> measures = Lists.newArrayList();
    for (int i = 0; i < header.size(); i++) {
      if (isMeasure(header.get(i))) {
        measures.add(header.get(i));
      } else if (header.get(i).contains("model")) {
        headers.append("models", header.get(i));
      }
    }
    headers.put("measures", measures.stream().sorted().collect(Collectors.toList()));
    final Map<List<String>, List<String>> currData = Maps.newLinkedHashMapWithExpectedSize(data.size());
    data.forEach(r -> currData.put(getSortedKey(sortedSchema, r), r));
    final int groupA = sortedSchema.length / 2 + sortedSchema.length % 2; 
    final int groupB = sortedSchema.length / 2;

    final Map<List<String>, Long> keyA = 
        Streams
          .mapWithIndex(currData.keySet().stream()
          .map(k -> k.subList(0, groupA))
          .distinct() //
          //.sorted((a, b) -> a.toString().compareTo(b.toString()))
          , (v, i) -> Pair.of(v, i)).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    final Map<List<String>, Long> keyB = 
        Streams
          .mapWithIndex(currData.keySet().stream()
          .map(k -> k.subList(groupA, groupA + groupB))
          .distinct()
          //.sorted((a, b) -> a.toString().compareTo(b.toString())), 
          , (v, i) -> Pair.of(v, i)).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    final JSONObject[][] matrix = new JSONObject[keyA.size() + groupB][keyB.size() + groupA];
    currData.entrySet().forEach(e -> {
      final long row = groupB + keyA.get(e.getKey().subList(0, groupA));
      final long col = groupA + keyB.get(e.getKey().subList(groupA, groupA + groupB));
      final JSONObject cell = new JSONObject();
      cell.put("type", "cell");
      for (int i = 0; i < e.getValue().size(); i++) {
        if (!header.get(i).contains("zscore") 
            && !header.get(i).contains("surprise") 
            && !header.get(i).contains("interest")) {
          cell.put(header.get(i), e.getValue().get(i));
        }
      }
      matrix[(int) row][(int) col] = cell;
    });

    for (final Entry<List<String>, Long> e: keyA.entrySet()) {
      for (int col = 0; col < e.getKey().size(); col++) {
        final long row = e.getValue();
        final JSONObject cell = new JSONObject();
        cell.put("type", "header");
        cell.put("attribute", e.getKey().get((int) col));
        matrix[(int) row + groupB][col] = cell;
      }
    }
    for (final Entry<List<String>, Long> e: keyB.entrySet()) {
      for (int row = 0; row < e.getKey().size(); row++) {
        final long col = e.getValue();
        final JSONObject cell = new JSONObject();
        cell.put("type", "header");
        cell.put("attribute", e.getKey().get((int) row));
        matrix[row][(int) col + groupA] = cell;
      }
    }
    final JSONObject empty = new JSONObject();
    final JSONArray m = new JSONArray();
    for (int i = 0; i < matrix.length; i++) {
      final JSONArray row = new JSONArray();
      for (int j = 0; j < matrix[i].length; j++) {
        row.put(matrix[i][j] == null ? empty : matrix[i][j]);
        matrix[i][j] = null;
      }
      m.put(row);
    }
    final JSONObject res = new JSONObject();
    res.put("table", m);
    res.put("headers", headers);
    return res;
  }

  private static boolean isMeasure(String string) {
    return string.contains("(") && !string.contains("model") && !string.contains("zscore");
  }

  private static Set<String> getMeasures(final String[] currSchema) {
    return Sets.newHashSet(currSchema).stream().filter(s -> isMeasure(s)).collect(Collectors.toSet());
  }

  private static List<String> getSortedKey(int[] currSchema, final List<String> row) {
    final List<String> key = Lists.newLinkedList();
    for (int i = 0; i < currSchema.length; i++) {
      key.add(row.get(currSchema[i]));
    }
    return key;
  }

  private static int[] getIdx(final String[] row, final Set<String> set) {
    if (set.isEmpty()) {
      return new int[] {};
    }
    final int[] res = new int[set.size()];
    int c = 0;
    int found = 0;
    final Set<String> lowerCaseSet = set.stream().map(s -> s.toLowerCase()).collect(Collectors.toSet());
    for (int i = 0; i < row.length; i++) {
      if (lowerCaseSet.contains(row[i].toLowerCase())) {
        res[c++] = i;
        found++;
      }
    }
    if (found != set.size()) {
      throw new IllegalArgumentException("Not all the elements have been found!");
    }
    return res;
  }

  private static Set<String> getKey(int[] currSchema, final String[] row) {
    return getKey(currSchema, Arrays.asList(row));
  }

  private static Set<String> getKey(int[] currSchema, final List<String> row) {
    final Set<String> key = Sets.newHashSet();
    for (int i = 0; i < currSchema.length; i++) {
      key.add(row.get(currSchema[i]));
    }
    return key;
  }

  private static int getSurprise(final String[] row) {
    for (int i = 0; i < row.length && row != null; i++) {
      if (row[i].equals("surprise")) {
        return i;
      }
    }
    return -1;
  }

  private static int[] sharedScores(String[] prevSchema, final String[] currSchema) {
    if (prevSchema == null || prevSchema.length == 0) {
      return getIdx(prevSchema, Sets.newHashSet());
    } else if (currSchema == null || currSchema.length == 0) {
      return getIdx(prevSchema, Sets.newHashSet(prevSchema).stream().filter(s -> s.contains("zscore")).collect(Collectors.toSet()));
    }
    return getIdx(prevSchema, Sets.intersection(Sets.newHashSet(prevSchema), Sets.newHashSet(currSchema)).stream().filter(s -> s.contains("zscore")).collect(Collectors.toSet()));
  }

  private static Set<String> getModels(final String[] currSchema) {
    return Sets.newHashSet(currSchema).stream().filter(s -> s.contains("model")).collect(Collectors.toSet());
  }

  private static int getIdx(final String[] currSchema, final String model, final boolean contains) {
    for (int i = 0; i < currSchema.length; i++) {
      if (currSchema[i].equals(model) || contains && currSchema[i].contains(model)) {
        return i;
      }
    }
    return -1;
  }

//  private static int getInterestIdx(final String[] currSchema, final String model) {
//    return getIdx(currSchema, "int_" + model, true);
//  }

  private static int getModelIdx(final String[] currSchema, final String model) {
    return getIdx(currSchema, model, false);
  }
}
