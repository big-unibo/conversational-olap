package it.unibo.conversational;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.log4j.Logger;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import it.unibo.conversational.algorithms.Mapper;
import it.unibo.conversational.algorithms.Parser;
import it.unibo.conversational.database.DBmanager;
import it.unibo.conversational.datatypes.Mapping;
import it.unibo.conversational.datatypes.Ngram;
import it.unibo.conversational.datatypes.Ngram.AnnotationType;
import it.unibo.conversational.olap.Operator;
import zhsh.Tree;

/**
 * Validate CBO against a dataset of queries.
 */
public class Validator {

  private FileWriter csvWriterTest;

  /**
   * Initialize a validator.
   * @throws Exception in case of error.
   */
  public Validator() {
  }

  /**
   * Initialize a validator.
   * @param csvWriterTest where to write the output
   * @throws Exception in case of error
   */
  public Validator(final FileWriter csvWriterTest) {
    this.csvWriterTest = csvWriterTest;
  }

  private static final Logger L = Logger.getLogger(Validator.class);

  /**
   * Validate against dataset.
   * @param dataset dataset
   * @param thrSimilarityMember similarity wrt members
   * @param thrSimilarityMetadata similarity wrt metadata
   * @param synMember members synonyms
   * @param synMeta metadata synonyms
   * @param percMissingPhrase percentage of sentence that must be covered
   * @param maxDistInPhrase maximum distance between ngrams
   * @param nTopInterpretation top N queries
   * @param ngramSize max ngram size
   * @param nGramSimThr ngram similarity threshold
   * @throws Exception in case or error
   */
  public void validateAll(final String dataset, final double thrSimilarityMember, final double thrSimilarityMetadata, final int synMember, final int synMeta,
      final double percMissingPhrase, final int maxDistInPhrase, final int nTopInterpretation, final int ngramSize, final double nGramSimThr, final int run) throws Exception {
    final String sql = "SELECT * FROM `" + dataset + "` WHERE `" + DBmanager.colQueryGPSJ + "` = \"y\" ORDER BY length(" + DBmanager.colQueryText + ") asc";
    try (
        Statement stmt = DBmanager.getMetaConnection().createStatement();
        ResultSet queries = stmt.executeQuery(sql)
    ) {
      while (queries.next()) {
        final int id = queries.getInt(DBmanager.colQueryID);
        final String query = queries.getString(DBmanager.colQueryText);
        String gbset = queries.getString(DBmanager.colQueryGBset);
        gbset = gbset == null ? "" : gbset;
        final String measures = queries.getString(DBmanager.colQueryMeasClause);
        String predicate = queries.getString(DBmanager.colQuerySelClause);
        predicate = predicate == null ? "" : predicate;
        try {
          validate(dataset, id, query, gbset, measures, predicate, thrSimilarityMember, thrSimilarityMetadata, synMember, synMeta, percMissingPhrase, maxDistInPhrase, nTopInterpretation, ngramSize, nGramSimThr, run);
        } catch (final Exception e) {
          e.printStackTrace();
        }
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Validate against dataset.
   * @param dataset dataset
   * @param id query id
   * @param query query in natural language
   * @param gbset group by set
   * @param measures measure clause
   * @param predicate selection clause
   * @param thrSimilarityMember similarity wrt members
   * @param thrSimilarityMetadata similarity wrt metadata
   * @param synMember members synonyms
   * @param synMeta metadata synonyms
   * @param percMissingPhrase percentage of sentence that must be covered
   * @param maxDistInPhrase maximum distance between ngrams
   * @param nTopInterpretation top N queries
   * @param ngramSize max ngram size
   * @param nGramSimThr ngram similarity threshold
   * @return tree edit distance and similarity
   * @throws Exception in case or error
   */
  public Pair<Integer, Double> validate(final String dataset, final int id, final String query, final String gbset, final String measures,
      final String predicate, final double thrSimilarityMember, final double thrSimilarityMetadata, final int synMember, final int synMeta,
      final double percMissingPhrase, final int maxDistInPhrase, final int nTopInterpretation, final int ngramSize, final double nGramSimThr, final int run)
      throws Exception {
    L.warn(query);
    final Map<String, Object> stats = Maps.newLinkedHashMap();
    final Mapping correctSentence = getBest(gbset, predicate, measures);
    final List<Pair<Mapping, Mapping>> parsings = parseAndTranslate(Ngram.class, null, 1, null, query, thrSimilarityMember, thrSimilarityMetadata, synMember, synMeta, percMissingPhrase, maxDistInPhrase, nTopInterpretation, ngramSize, nGramSimThr, stats, false);
    if (!parsings.isEmpty()) {
      for (int k = 0; k < parsings.size(); k++) {
        for (int d = 0; d <= 3; d++) {
          if (d > 0) {
            Parser.automaticDisambiguate(parsings.get(k).getLeft());
          }
          final int distance = Tree.ZhangShasha(correctSentence.toStringTree(), parsings.get(k).getLeft().toStringTree());
          final double sim = 1.0 - 1.0 * distance / Math.max(parsings.get(k).getLeft().countNodes(), correctSentence.countNodes());
          write(dataset, id, query, gbset, measures, predicate, thrSimilarityMember, thrSimilarityMetadata, synMember, synMeta, percMissingPhrase, maxDistInPhrase, nTopInterpretation, ngramSize, stats, correctSentence, parsings.get(k), parsings.size() - k, d, sim, run);
        }
        L.debug("ngrams: " + parsings.get(k).getRight());
        L.debug("top  " + k + ": " + parsings.get(k).getLeft());
      }
      L.debug("correct: " + correctSentence);
      final int distance = Tree.ZhangShasha(correctSentence.toStringTree(), parsings.get(0).getLeft().toStringTree());
      return Pair.of(distance, 1.0 - 1.0 * distance / Math.max(parsings.get(0).getLeft().countNodes(), correctSentence.countNodes()));
    } else { // no mappings
      write(dataset, id, query, gbset, measures, predicate, thrSimilarityMember, thrSimilarityMetadata, synMember, synMeta, percMissingPhrase, maxDistInPhrase, nTopInterpretation, ngramSize, stats, correctSentence, null, 1, 0, 0, run);
      return null;
    }
  }

  private void write(final String dataset, final int id, final String query, final String gbset, final String measures, final String predicate, final double thrSimilarityMember,
      final double thrSimilarityMetadata, final int synMember, final int synMeta, final double percMissingPhrase, final int maxDistInPhrase,
      final int nTopInterpretation, final int ngramSize, final Map<String, Object> stats, final Mapping correctSentence,
      final Pair<Mapping, Mapping> parsing, final int k, final int disambiguationStep, final double sim, final int run) throws IOException {
    if (id >= 0) {
      final List<Object> toWrite = Lists.newArrayList(thrSimilarityMember, thrSimilarityMetadata, synMember, synMeta, percMissingPhrase, maxDistInPhrase, ngramSize, 
              id, k, disambiguationStep, parsing == null ? 0 : parsing.getLeft().getScorePFM(), parsing == null ? 0 : parsing.getLeft().getScoreM(), dataset, sim, correctSentence, measures,
              predicate, gbset, query, parsing == null ? "" : parsing.getRight(), parsing == null ? "" : parsing.getLeft(),
              parsing == null ? 0 : parsing.getRight().ngrams.size(),
              stats.get("lemmatization_time"), stats.get("lemmatization_sentence"), stats.get("match_time"), stats.get("match_count"), stats.get("match_confident_count"), stats.get("sentence_time"), stats.get("sentence_count"), stats.get("sentence_count_pruned"), stats.get("pruned"),
              stats.get("mapping_time"), stats.get("parsing_time"), stats.get("total_time"),
              parsing != null && parsing.getLeft().getMatched().stream().map(n -> n.mde()).collect(Collectors.toSet()).containsAll(parsing.getRight().ngrams.stream().map(n -> n.mde()).collect(Collectors.toSet())),
              parsing == null ? -1 : parsing.getLeft().getAnnotatedNgrams().size(), run);
      csvWriterTest.write(toWrite.stream().map(a -> a.toString()).reduce((a, b) -> a + ";" + b).get().toString() + "\n");
      csvWriterTest.flush();
    }
  }

  /**
   * @param gbc GC
   * @param sc SC
   * @param mc MC
   * @return the best parsing
   * @throws Exception in case of error
   */
  public static Mapping getBest(final String gbc, final String sc, final String mc) throws Exception {
    final List<Pair<Mapping, Mapping>> res = parseAndTranslate(Ngram.class, null, 1.0, null, (mc + (gbc.isEmpty() ? "" : " by " + gbc) + " " + sc).replace("_", " ").replace("\"", "").replace(",", ""), 1.0, 1.0, 1, 1, 1, 2, 1, 3, 1.0, Maps.newLinkedHashMap(), true);
    if (res.isEmpty()) {
      throw new IllegalArgumentException("Could not translate the query! " + gbc + " " + sc + " " + mc);
    }
    return res.get(0).getLeft();
  }

  /**
   * @param gbc GC
   * @param sc SC
   * @param mc MC
   * @return the best parsing
   * @throws Exception in case of error
   */
  public static Mapping getBest(final String query) throws Exception {
    final List<Pair<Mapping, Mapping>> res = parseAndTranslate(Ngram.class, null, 1.0, null, query.replace("_", " ").replace("\"", "").replace(",", ""), 1.0, 1.0, 1, 1, 1, 2, 1, 3, 1.0, Maps.newLinkedHashMap(), true);
    if (res.isEmpty()) {
      throw new IllegalArgumentException("Could not translate the query! " + query);
    }
    return res.get(0).getLeft();
  }

  /**
   * Return a list of the top k pairs <translation, original sentence>.
   * @param query nl query
   * @return list of the top k pairs <translation, original sentence>
   * @throws Exception in case or error
   */
  public static Mapping parseAndTranslate(final String query) throws Exception {
    return parseAndTranslate(Ngram.class, null, 1.0, null, query, THR_MEMBER, THR_META, N_SYNMEMBER, N_SYNMETA, THR_COVERAGE, THR_NGRAMDIST, 1, NGRAM_SIZE, NGRAMSYNTHR, Maps.newLinkedHashMap(), false).get(0).getLeft();
  }

  /**
   * Return a list of the top k pairs <translation, original sentence>.
   * @param query nl query
   * @return list of the top k pairs <translation, original sentence>
   * @throws Exception in case or error
   */
  public static Mapping parseAndTranslate(final String query, final double tau, final List<Triple<AnnotationType, Ngram, Ngram>> log) throws Exception {
    return parseAndTranslate(Ngram.class, null, tau, log, query, THR_MEMBER, THR_META, N_SYNMEMBER, N_SYNMETA, THR_COVERAGE, THR_NGRAMDIST, 1, NGRAM_SIZE, NGRAMSYNTHR, Maps.newLinkedHashMap(), false).get(0).getLeft();
  }

  /**
   * Return a list of the top k pairs <translation, original sentence>.
   * @param query nl query
   * @return list of the top k pairs <translation, original sentence>
   * @throws Exception in case or error
   */
  public static Mapping parseAndTranslate(final Class toParse, final Mapping prevTree, final double tau, final List<Triple<AnnotationType, Ngram, Ngram>> log, final String query) throws Exception {
    return parseAndTranslate(toParse, prevTree, tau, log, query, THR_MEMBER, THR_META, N_SYNMEMBER, N_SYNMETA, THR_COVERAGE, THR_NGRAMDIST, 1, NGRAM_SIZE, NGRAMSYNTHR, Maps.newLinkedHashMap(), false).get(0).getLeft();
  }

  /**
   * Return a list of the top k pairs <translation, original sentence>.
   * @param query nl query
   * @param k top N queries
   * @return list of the top k triples <(hint) translation, original translation, sentence>
   * @throws Exception in case or error
   */
  public static List<Pair<Mapping, Mapping>> parseAndTranslate(final String query, final int k) throws Exception {
    return parseAndTranslate(Ngram.class, null, 1.0, null, query, THR_MEMBER, THR_META, N_SYNMEMBER, N_SYNMETA, THR_COVERAGE, THR_NGRAMDIST, k, NGRAM_SIZE, NGRAMSYNTHR, Maps.newLinkedHashMap(), false);
  }

  /**
   * Return a list of the top k pairs <translation, sentence>.
   * @param query nl query
   * @param thrSimilarityMember similarity wrt members
   * @param thrSimilarityMetadata similarity wrt metadata
   * @param synMember members synonyms
   * @param synMeta metadata synonyms
   * @param percMissingPhrase percentage of sentence that must be covered
   * @param maxDistInPhrase maximum distance between ngrams
   * @param nTopInterpretation top N queries
   * @param ngramSize max ngram size
   * @param nGramSimThr ngram similarity threshold
   * @param stats map of statistics
   * @param skipCleaning 
   * @return list of the top k triples <(hint) translation, original translation, sentence>
   * @throws Exception in case or error
   */
  public static List<Pair<Mapping, Mapping>> parseAndTranslate(
      final Class toParse,
      final Mapping prevTree, final double tau, final List<Triple<AnnotationType, Ngram, Ngram>> log,
      final String query,
      final double thrSimilarityMember, final double thrSimilarityMetadata,
      final int synMember, final int synMeta,
      final double percMissingPhrase, final int maxDistInPhrase,
      final int nTopInterpretation, final int ngramSize, final double nGramSimThr,
      final Map<String, Object> stats, final boolean skipCleaning) throws Exception {
    final long totalElapsedTime = System.currentTimeMillis();
    // From the ngrams generate all possible sentence interpretations
    List<Mapping> mappings = compactMappings(Mapper.createMappings(toParse, query, thrSimilarityMember, thrSimilarityMetadata, synMember, synMeta, percMissingPhrase, maxDistInPhrase, ngramSize, nGramSimThr, stats, skipCleaning));
    stats.put("mapping_time", System.currentTimeMillis() - totalElapsedTime);
    stats.put("sentence_count_pruned", mappings.size());
    // Sort interpretations by length, we start to translate from the longest sentences
    mappings.sort((Mapping s1, Mapping s2) -> -Double.compare(s1.getScoreM(), s2.getScoreM()));
    final Map<Mapping, Mapping> topKtranslations = Maps.newLinkedHashMap();
    int checked = 0;
    final long parsingTime = System.currentTimeMillis();
    while (!mappings.isEmpty()) {
      if (++checked % 1000 == 0) {
        L.debug("Done " + checked);
        L.debug("Remaining: " + mappings.size());
      }
      final Mapping currentSentence = mappings.remove(0);
      final Optional<Mapping> bestTranslation = toParse.equals(Ngram.class)? Parser.parse(currentSentence) : Operator.parse(currentSentence); // , mo.getOperatorOfMeasure()
      if (bestTranslation.isPresent()) {
        bestTranslation.get().getScorePFM();
        topKtranslations.put(bestTranslation.get(), currentSentence);
        if (topKtranslations.size() > nTopInterpretation) {
          final Mapping toRemove = topKtranslations.keySet().stream().min(Mapping::compareMappings).get();
          // System.out.println(toRemove);
          // topKtranslations.keySet().stream().min(Mapping::compareMappings);
          topKtranslations.remove(toRemove);
          mappings = mappings.stream().filter(s -> s.getScoreM() > toRemove.getScorePFM()).collect(Collectors.toList());
        }
      }
    }
    stats.put("pruned", checked);
    stats.put("parsing_time", System.currentTimeMillis() - parsingTime);
    L.debug("Pruned: " + stats.get("pruned") + "/" + stats.get("sentence_count_pruned") + "/" + stats.get("sentence_count"));
    final List<Pair<Mapping, Mapping>> parsings = 
        topKtranslations.keySet().stream()
          .sorted(Mapping::compareMappings)
          .map(m -> {
            final Mapping mapping = topKtranslations.get(m);
            Parser.infer(m, prevTree);
            Parser.typeCheck(m);
            if (log != null) {
              m.hint(tau, log);
            }
            return Pair.of(m, mapping);
          })
          .collect(Collectors.toList());
    stats.put("total_time", System.currentTimeMillis() - totalElapsedTime);
    return parsings;
  }

  /**
   * Remove the mappings that corresponds to the same list of entities. For equal mappings keep only the one with the highest score.
   * @param orig original list of mappings
   * @return pruned list of mappings
   */
  public static List<Mapping> compactMappings(final List<Mapping> orig) {
    return orig.stream() //
        .collect(Collectors.groupingBy(x -> x.ngrams.stream().map(n -> n.mde()).collect(Collectors.toList()))).values().stream()
        // the same mapping can be generated in multiple ways (e.g., through different tokens)
        .map(equalMappings -> equalMappings.stream().max((m1, m2) -> {
          int c = Double.compare(m1.getScoreM(), m2.getScoreM());
          return c == 0 ? m1.toString().compareTo(m2.toString()) : c;
        })) // keep only the one with the highest score
        .map(o -> o.get()).collect(Collectors.toList());
  }

  /** Default TAU. */
  public static final double TAU = 0.5;
  /** Default THR_MEMBER. */
  public static final double THR_MEMBER = 0.8;
  /** Default THR_META. */
  public static final double THR_META = 0.41;
  /** Default N_SYNMEMBER. */
  public static final int N_SYNMEMBER = 1;
  /** Default N_SYNMETA. */
  public static final int N_SYNMETA = 5;
  /** Default THR_COVERAGE. */
  public static final double THR_COVERAGE = 0.7;
  /** Default THR_NGRAMDIST. */
  public static final int THR_NGRAMDIST = 3;
  /** Default NGRAM_SIZE. */
  public static final int NGRAM_SIZE = 3;
  /** Default NGRAMSYNTHR. */
  public static final double NGRAMSYNTHR = 0.95;
  /** Default K. */
  public static final int K = 5;

  // Test parameters
  private static final int N_RUNS = 2;
  private static final int[] N_SYNMETAS = new int[]{5, 3, 1};
  private static final double[] THR_METAS = new double[]{0.4, 0.5, 0.6};
  private static final double[] THR_MEMBERS = new double[]{0.8, 0.9};

  /** Run the validation and store the test results.
   * @param args arguments
   * @throws Exception in case of error
   */
  public static void main(final String[] args) {
    final String filePathTest = "resources\\test\\results_IS\\test.csv";
    try (FileWriter csvWriterTest = new FileWriter(filePathTest)) {
      final List<Object> toWrite = Lists.newArrayList("simMember", "simMeta", "synMember", "synMeta", "%missing", "maxDistance", "ngramSize",
          "id", "k", "disambiguationStep", 
          "score", "potentialscore", "dataset",
          "similarity", "correctParsing",
          "measures", "predicate", "gbset", "nl_query", "ngrams", "sentence_parsed",
          "ngrams_count", "lemmatization_time", "lemmatization_sentence", "match_time", "match_count", "match_confident_count",
          "sentence_time", "sentence_count", "sentence_count_pruned", "sentence_pruned", "mapping_time", "parsing_time", "total_time",
          "isFullyParsed", "countAnnotations", "run");
      csvWriterTest.write(toWrite.stream().map(a -> a.toString()).reduce((a, b) -> a + ";" + b).get().toString() + "\n");
      csvWriterTest.flush();
      for (final String dataset: Lists.newArrayList("dataset_patrick")) {
        for (int r = 0; r < N_RUNS; r++) {
          for (double thrMemb: THR_MEMBERS) {
            for (double thrMeta: THR_METAS) {
              for (int synMeta: N_SYNMETAS) {
                new Validator(csvWriterTest).validateAll(dataset, thrMemb, thrMeta, N_SYNMEMBER, synMeta, THR_COVERAGE, THR_NGRAMDIST, K, NGRAM_SIZE, NGRAMSYNTHR, r);
              }
            }
          }
        }
      }
    } catch (final Exception e1) {
      e1.printStackTrace();
    }
  }
}
