package it.unibo.conversational.vocalization.main;

import it.unibo.conversational.vocalization.cache.Cache;
import it.unibo.conversational.vocalization.cache.CacheFactory;
import it.unibo.conversational.vocalization.data.Dimension;
import it.unibo.conversational.vocalization.data.Measure;
import it.unibo.conversational.vocalization.query.Query;
import it.unibo.conversational.vocalization.query.QueryValidator;
import it.unibo.conversational.vocalization.speech.Dummy;
import it.unibo.conversational.vocalization.speech.Speech;
import it.unibo.conversational.vocalization.uct.TreeFactory;
import it.unibo.conversational.vocalization.uct.UctNode;
import it.unibo.conversational.vocalization.utils.OracleDatabase;
import it.unibo.conversational.vocalization.utils.XmlParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;

public class VolapSession {

    private static final String INPUT_ERROR = "Input query is not supported by the vocalization system.";
    private static final String NO_ENTRY_ERROR = "Input query doesn't match any database record";
    private static final String TOO_FINE_ERROR = "Aggregation level is too fine for the vocalization system, go up to a coarser level.";
    private final CacheFactory cacheFactory;
    private final List<Dimension> dimensions;
    private final List<Measure> measures;

    public VolapSession(CacheFactory cacheFactory, List<Dimension> dimensions, List<Measure> measures) {
        this.cacheFactory = cacheFactory;
        this.dimensions = Collections.unmodifiableList(dimensions);
        this.measures = Collections.unmodifiableList(measures);
    }

    public static VolapSession initialize(String dbHost, String dbPort, String dbSid, String dbUser, String dbPw) throws Exception {
        OracleDatabase oracleDb = OracleDatabase.connect(dbHost, dbPort, dbSid, dbUser, dbPw);
        String xmlPath = Objects.requireNonNull(VolapSession.class.getClassLoader().getResource(Configuration.XML_DATA_MART)).getPath();
        XmlParser xmlParser = XmlParser.initialize(xmlPath);
        List<Dimension> dimensions = new ArrayList<>();
        for (int i = 0; i < xmlParser.countDimensions(); i++) {
            List<String> dim = xmlParser.parseDimension(i);
            List<List<String>> lev = xmlParser.parseLevels(i);
            dimensions.add(Dimension.initialize(oracleDb, dim.get(0), dim.get(1), lev.get(0), lev.get(1), lev.get(2)));
        }
        List<Measure> measures = xmlParser.parseMeasures(0).stream().map(m -> new Measure(m.get(0), m.get(1))).collect(Collectors.toList());
        CacheFactory cacheFactory = CacheFactory.getCache(oracleDb, xmlParser.parseFact(0).get(0), dimensions, measures);
        return new VolapSession(cacheFactory, dimensions, measures);
    }

    public Pair<String, Pair<Double, Double>> executeQuery(String input, boolean printSpeech) throws IllegalArgumentException {
        return this.executeQuery(input, Configuration.COMPLETE_TREE, printSpeech);
    }

    public Pair<String, Pair<Double, Double>> executeQuery(String input, boolean completeTree, boolean printSpeech) throws IllegalArgumentException {
        long startMillis = System.currentTimeMillis();
        Optional<Query> query = QueryValidator.validateInput(input, this.measures, this.dimensions);
        if (query.isEmpty()) throw new IllegalArgumentException(INPUT_ERROR);
        Optional<Cache> cache = this.cacheFactory.groupBy(query.get(), Configuration.MAX_DIMENSION_SIZE, Configuration.MAX_GROUP_SIZE);
        if (cache.isEmpty()) throw new IllegalArgumentException(TOO_FINE_ERROR);
        if (cache.get().groupedEntries.isEmpty()) throw new IllegalArgumentException(NO_ENTRY_ERROR);
        String preamble = query.get().getPreamble(this.dimensions);
        if (printSpeech) System.out.println(preamble);
        UctNode node = TreeFactory.getRoot(cache.get(), completeTree, Configuration.N_REFINEMENTS, Configuration.N_SIGNIFICANT_DIGITS, Configuration.ST_DEV_FACTOR, Configuration.P_RANGE_FACTOR);
        Optional<UctNode> next = node.nextNode(this.getSpokenTime(preamble) - (System.currentTimeMillis() - startMillis));
        while (next.isPresent()) {
            node = next.get();
            String speechFragment = node.getSpeech().getDescription();
            if (printSpeech && !(node.getSpeech() instanceof Dummy)) System.out.println(speechFragment);
            next = node.nextNode(this.getSpokenTime(speechFragment));
        }
        Speech output = node.getSpeech();
        double error = cache.get().evaluateSpeechError(output);
        double quality = cache.get().evaluateSpeechQuality(output);
        return Pair.of(preamble + "\n" + output.getFullDescription(), Pair.of(error, quality));
    }

    private long getSpokenTime(String output) {
        return output.length() * Configuration.MILLIS_FOR_CHARACTER;
    }

}
