package it.unibo.conversational;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.json.JSONObject;

import edu.stanford.nlp.util.StringUtils;
import it.unibo.conversational.algorithms.MarriageProblem;
import it.unibo.conversational.algorithms.Parser.Type;
import it.unibo.conversational.datatypes.Mapping;
import it.unibo.conversational.datatypes.Ngram;

/**
 * Utility class.
 */
public final class Utils {
  private Utils() {
  }

  /** Decimal formatter. */
  public static final DecimalFormat DF = new DecimalFormat();
  static {
    final DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
    otherSymbols.setDecimalSeparator('.');
    DF.setMinimumFractionDigits(0);
    DF.setMaximumFractionDigits(2);
    DF.setDecimalFormatSymbols(otherSymbols);
    DF.setRoundingMode(RoundingMode.HALF_DOWN);
    DF.setGroupingUsed(false);
  }

  /**
   * Write ngrams and mappings on file.
   * @param folderName folder
   * @param fileName file
   * @param ngrams ngrams
   */
  public static void writeMappings(final String folderName, final String fileName, final List<Ngram> ngrams) {
    try {
      final String filePath = "resources\\test\\" + folderName + "\\" + fileName.replace("?", "") + ".csv";
      final FileWriter csvWriter = new FileWriter(filePath);
      csvWriter.append("N-gram");
      csvWriter.append(";");
      csvWriter.append("Pos");
      csvWriter.append(";");
      csvWriter.append("Tabella");
      csvWriter.append(";");
      csvWriter.append("Similarità");
      csvWriter.append(";");
      csvWriter.append("\n");
      for (final Ngram res : ngrams) {
        csvWriter.append(res.tokens);
        csvWriter.append(";");
        csvWriter.append("[" + res.pos().getLeft() + "-" + res.pos().getRight() + "]");
        csvWriter.append(";");
        csvWriter.append(res.type + ": " + res.mde().nameInTable() + ": " + res.typeInDB());
        csvWriter.append(";");
        csvWriter.append(String.valueOf(res.similarity()));
        csvWriter.append("\n");
      }
      csvWriter.flush();
      csvWriter.close();
    } catch (IOException e) {
      // e.printStackTrace();
    }
  }

  /**
   * Write sentence on file.
   * @param folderName folder
   * @param fileName file
   * @param sentences sentences
   */
  public static void writeParsing(final String folderName, final String fileName, final List<Mapping> sentences) {
    final Locale currentLocale = Locale.getDefault();
    final DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
    otherSymbols.setDecimalSeparator(',');
    final DecimalFormat df = new DecimalFormat("#.########", otherSymbols);
    df.setRoundingMode(RoundingMode.HALF_DOWN);
    try {
      final String filePath = "resources\\test\\" + folderName + "\\" + fileName.replace("?", "") + ".csv";
      final FileWriter csvWriter = new FileWriter(filePath);
      for (final Mapping set : sentences.stream().sorted((Mapping s1, Mapping s2) -> {
                final int s1f = s1.ngrams.stream().anyMatch(n -> n.type.equals(Type.GPSJ)) ? 1 : 0;
                final int s2f = s2.ngrams.stream().anyMatch(n -> n.type.equals(Type.GPSJ)) ? 1 : 0;
                final int c = -Integer.compare(s1f, s2f);
                if (c == 0) {
                    return -Integer.compare(s1.ngrams.size(), s2.ngrams.size());
                }
                return c;
            }).collect(Collectors.toList())) {
        csvWriter.append(set.toString());
        csvWriter.append("\n");
      }
      csvWriter.flush();
      csvWriter.close();
    } catch (final IOException e) {
      // e.printStackTrace();
    }
  }

  /**
   * Get credentials from resources/credentials.txt.
   * 
   * File must contain:
   * IP\n
   * PORT\n
   * USENAME\n
   * PASSWORD\n
   * FACTDB\n
   * FACTTABLE\n
   * METADB\n
   * PYTHONPATH
   * @return array with credentials
   */
  public static String[] credentialsFromFile() {
    try (
        BufferedReader b = new BufferedReader(new InputStreamReader(Utils.class.getClassLoader().getResourceAsStream("credentials.txt")))
    ) {
      String readLine = "";
      final String[] credentials = new String[8];
      int i = 0;
      while ((readLine = b.readLine()) != null) {
        credentials[i++] = readLine;
      }
      b.close();
      return credentials;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Data type handled by our system.
   */
  public enum DataType {
    /** A number. */
    NUMERIC,
    /** A string. */
    STRING,
    /** A date. */
    DATE,
    /** A MC / SC / GBC clause. */
    OTHER
  }

//  /**
//   * Data type for query items.
//   */
//  public enum OperatorType {
//    /** Drill down, roll up, replace, add, drop. */
//    OPERATOR,
//    /** to support operator construction. */
//    ACCESSORY
//  }

//  /**
//   * Data type for query items.
//   */
//  public enum PredicateType {
//    /** Select statement. */
//    SELECT,
//    /** >=, <=, =, ... */
//    PREDICATE, 
//    /** avg, sum, min, max, ... */
//    GROUPBYOPERATOR, 
//    /** by. */
//    GROUPBYTERM,
//    /** ??? */
//    BOOLEANOPEAROR,
//    /** where. */
//    SELECTIONTERM,
//    /** count, count distinct. */
//    COUNTOPERATOR
//  }

  /**
   * Convert string to DataType type.
   * @param t type in string
   * @return datatype
   */
  public static DataType getDataType(final String t) {
    // mancano: BINARY, VARBINARY, LONGVARBINARY, NULL, OTHER, JAVA_OBJECT, DISTINCT, STRUCT, ARRAY,
    // BLOB, CLOB, REF, DATALINK, BOOLEAN, ROWID, NCLOB, SQLXML, REF_CURSOR, BIGINT
    if (t.equals(JDBCType.CHAR.toString()) || t.equals(JDBCType.LONGVARCHAR.toString()) || t.equals(JDBCType.VARCHAR.toString())
        || t.equals(JDBCType.NCHAR.toString()) || t.equals(JDBCType.NVARCHAR.toString()) || t.equals(JDBCType.LONGNVARCHAR.toString())) {
      return DataType.STRING;
    } else if (t.equals(JDBCType.BIT.toString()) || t.equals(JDBCType.TINYINT.toString()) || t.equals(JDBCType.SMALLINT.toString()) || t.equals(JDBCType.INTEGER.toString())
        || t.equals(JDBCType.FLOAT.toString()) || t.equals(JDBCType.REAL.toString()) || t.equals(JDBCType.DOUBLE.toString())
        || t.equals(JDBCType.NUMERIC.toString()) || t.equals(JDBCType.DECIMAL.toString())) {
      return DataType.NUMERIC;
    } else if (t.equals(JDBCType.DATE.toString()) || t.equals(JDBCType.TIME.toString()) || t.equals(JDBCType.TIMESTAMP.toString())
        || t.equals(JDBCType.TIME_WITH_TIMEZONE.toString()) || t.equals(JDBCType.TIMESTAMP_WITH_TIMEZONE.toString())) {
      return DataType.DATE;
    } else {
      // throw new IllegalArgumentException(t);
      return DataType.OTHER;
    }
  }

  /**
   * Convdert MySQL type to DataType type.
   * @param t type in string
   * @return datatype
   */
  public static DataType getDataType(final JDBCType t) {
    // mancano: BINARY, VARBINARY, LONGVARBINARY, NULL, OTHER, JAVA_OBJECT, DISTINCT, STRUCT, ARRAY,
    // BLOB, CLOB, REF, DATALINK, BOOLEAN, ROWID, NCLOB, SQLXML, REF_CURSOR, BIGINT
    if (t.equals(JDBCType.CHAR) || t.equals(JDBCType.LONGVARCHAR) || t.equals(JDBCType.VARCHAR) || t.equals(JDBCType.NCHAR) || t.equals(JDBCType.NVARCHAR)
        || t.equals(JDBCType.LONGNVARCHAR)) {
      return DataType.STRING;
    } else if (t.equals(JDBCType.TINYINT) || t.equals(JDBCType.SMALLINT) || t.equals(JDBCType.INTEGER) || t.equals(JDBCType.FLOAT) || t.equals(JDBCType.REAL)
        || t.equals(JDBCType.DOUBLE) || t.equals(JDBCType.NUMERIC) || t.equals(JDBCType.DECIMAL)) {
      return DataType.NUMERIC;
    } else if (t.equals(JDBCType.DATE) || t.equals(JDBCType.TIME) || t.equals(JDBCType.TIMESTAMP) || t.equals(JDBCType.TIME_WITH_TIMEZONE)
        || t.equals(JDBCType.TIMESTAMP_WITH_TIMEZONE)) {
      return DataType.DATE;
    }
    return DataType.OTHER;
  }

  /**
   * Word similarity based on levhenstein distance.
   * @param first a word
   * @param second another word
   * @return similarity
   */
  public static double tokenSimilarity(final String first, final String second) {
    double maxl = first.length() > second.length() ? first.length() : second.length();
    return 1 - (StringUtils.levenshteinDistance(first, second) / maxl);
  }

  /**
   * Similarity between two lists of tokens.
   * @param aTokens a list 
   * @param bTokens another list
   * @return similarity score
   */
  public static double tokenSimilarity(final String[] aTokens, final String[] bTokens) {
    return tokenSimilarity(Arrays.asList(aTokens), Arrays.asList(bTokens));
  }

  /**
   * Similarity between two lists of tokens.
   * @param aTokens a list 
   * @param bTokens another list
   * @return similarity score
   */
  public static double tokenSimilarity(final List<String> aTokens, final List<String> bTokens) {
    //La similarità tra due termini viene calcolando sommando la similarità di ogni coppia di parole associate moltiplicata per la lunghezza massima delle due parole
    // tutto fratto la lunghezza totale delle parole non matchate più la somma delle lunghezze massime
    final Map<Integer, Integer> matching = MarriageProblem.getBestMatch(aTokens, bTokens);
    double weightedMatch = 0.0, sumLen = 0.0;
    int unmatched = 0;
    for (final Entry<Integer, Integer> e : matching.entrySet()) {
      String s1 = aTokens.get(e.getKey());
      String s2 = bTokens.get(e.getValue());
      final int maxl = Math.max(s1.length(), s2.length());
      weightedMatch += (tokenSimilarity(s1, s2) * maxl);
      sumLen += maxl;
    }
    for (int i = 0; i < aTokens.size() && aTokens.size() > bTokens.size(); i++) {
      if (!matching.containsKey(i)) {
        unmatched += aTokens.get(i).length();
      }
    }
    for (int i = 0; i < bTokens.size() && aTokens.size() < bTokens.size(); i++) {
      if (!matching.containsValue(i)) {
        unmatched += bTokens.get(i).length();
      }
    }
    return weightedMatch / (sumLen + unmatched);
  }
  
  /**
   * Convert result set to json object
   * @param rs result set
   * @return string of the json object
   * @throws SQLException in case of error
   */
  public static JSONObject resultSet2Json(final ResultSet rs) throws SQLException {
    final ResultSetMetaData meta = rs.getMetaData();
    final int columns = meta.getColumnCount();
    final List<String> columnNames = new ArrayList<String>();
    final JSONObject obj = new JSONObject();
    for (int i = 1; i <= columns; i++) {
      columnNames.add(meta.getColumnName(i));
    }
    obj.put("schema", columnNames);
    final List<List<String>> records = new ArrayList<>();
    while (rs.next()) { // convert each object to an human readable JSON object
      List<String> record = new ArrayList<String>();
      for (int i = 1; i <= columns; i++) {
        // String key = columnNames.get(i - 1);
        record.add(rs.getString(i));
      }
      records.add(record);
    }
    obj.put("records", records);
    return obj;
  }
}
