package it.unibo.trummervocalization.main;

import it.unibo.conversational.database.Config;
import it.unibo.conversational.database.Cube;
import it.unibo.trummervocalization.cache.Cache;
import it.unibo.trummervocalization.cache.CacheFactory;
import it.unibo.trummervocalization.data.Dimension;
import it.unibo.trummervocalization.data.Measure;
import it.unibo.trummervocalization.query.Query;
import it.unibo.trummervocalization.query.QueryValidator;
import it.unibo.trummervocalization.speech.Dummy;
import it.unibo.trummervocalization.speech.Speech;
import it.unibo.trummervocalization.uct.TreeFactory;
import it.unibo.trummervocalization.uct.UctNode;
import it.unibo.trummervocalization.utils.XmlParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VolapSession {

    private volatile static VolapSession instance = null;
    private final CacheFactory cacheFactory;
    private final List<Dimension> dimensions;
    private final List<Measure> measures;

    private VolapSession(CacheFactory cacheFactory, List<Dimension> dimensions, List<Measure> measures) {
        this.cacheFactory = cacheFactory;
        this.dimensions = Collections.unmodifiableList(dimensions);
        this.measures = Collections.unmodifiableList(measures);
    }

    public synchronized static VolapSession getInstance() throws Exception {
        if (instance == null) {
            long startMillis = System.currentTimeMillis();
            Cube cube = Config.getCube(Configuration.CUBE_NAME);
            String xmlPath = Objects.requireNonNull(VolapSession.class.getClassLoader().getResource(Configuration.CUBE_XML)).getPath();
            XmlParser xmlParser = XmlParser.initialize(xmlPath);
            List<Dimension> dimensions = new ArrayList<>();
            for (int i = 0; i < xmlParser.countDimensions(); i++) {
                List<String> dim = xmlParser.parseDimension(i);
                List<List<String>> lev = xmlParser.parseLevels(i);
                dimensions.add(Dimension.initialize(cube, dim.get(0), dim.get(1), lev.get(0), lev.get(1), lev.get(2)));
            }
            List<Measure> measures = xmlParser.parseMeasures(0).stream().map(m -> new Measure(m.get(0), m.get(1))).collect(Collectors.toList());
            CacheFactory cacheFactory = CacheFactory.getCache(cube, xmlParser.parseFact(0).get(0), dimensions, measures);
            logTime("Initialized cache", startMillis);
            instance = new VolapSession(cacheFactory, dimensions, measures);
        }
        return instance;
    }

    public Pair<String, Pair<Double, Double>> executeQuery(String input, boolean printSpeech) throws IllegalArgumentException {
        return this.executeQuery(input, Configuration.COMPLETE_TREE, printSpeech, Configuration.N_REFINEMENTS);
    }

    public Pair<String, Pair<Double, Double>> executeQuery(String input, boolean completeTree, boolean printSpeech, int nRefinements) throws IllegalArgumentException {
        long startMillis = System.currentTimeMillis();
        Optional<Query> query = QueryValidator.validateInput(input, this.measures, this.dimensions);
        if (!query.isPresent()) throw new IllegalArgumentException(Configuration.INPUT_ERROR);
        Optional<Cache> cache = this.cacheFactory.groupBy(query.get(), Configuration.MAX_GROUPS);
        if (!cache.isPresent()) throw new IllegalArgumentException(Configuration.TOO_FINE_ERROR);
        if (cache.get().groupedEntries.isEmpty()) throw new IllegalArgumentException(Configuration.NO_ENTRY_ERROR);
        String preamble = query.get().getPreamble(this.dimensions);
        if (printSpeech) System.out.println(preamble);
        UctNode node = TreeFactory.getRoot(cache.get(), completeTree, Configuration.MAX_CHILDREN,
            nRefinements, Configuration.N_SIGNIFICANT_DIGITS, Configuration.ST_DEV_FACTOR, Configuration.P_RANGE_FACTOR);
        Optional<UctNode> next = node.nextNode(speechTime(preamble) - (System.currentTimeMillis() - startMillis));
        while (next.isPresent()) {
            node = next.get();
            String speechFragment = node.getSpeech().getDescription();
            if (printSpeech && !(node.getSpeech() instanceof Dummy)) System.out.println(speechFragment);
            next = node.nextNode(speechTime(speechFragment));
        }
        logTime("Evaluated query", startMillis);
        Speech output = node.getSpeech();
        double error = cache.get().evaluateSpeechError(output);
        double quality = cache.get().evaluateSpeechQuality(output);
        return Pair.of(preamble + "\n" + output.getFullDescription(), Pair.of(error, quality));
    }

    private static void logTime(String log, long startMillis) {
        long elapsedMillis = System.currentTimeMillis() - startMillis;
        long elapsedMinuts = elapsedMillis / 60000;
        elapsedMillis = elapsedMillis - elapsedMinuts * 60000;
        long elapsedSeconds = elapsedMillis / 1000;
        elapsedMillis = elapsedMillis - elapsedSeconds * 1000;
        Logger logger = LoggerFactory.getLogger(VolapSession.class);
        logger.debug(log + " in " + elapsedMinuts + " min, " + elapsedSeconds + " s, " + elapsedMillis + " ms");
    }

    public static long speechTime(String output) {
        return output.length() * Configuration.CHARACTER_MILLIS;
    }

}
