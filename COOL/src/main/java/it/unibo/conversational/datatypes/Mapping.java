package it.unibo.conversational.datatypes;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.unibo.conversational.Utils;
import it.unibo.conversational.algorithms.Mapper;
import it.unibo.conversational.algorithms.Parser;
import it.unibo.conversational.algorithms.Parser.Type;
import it.unibo.conversational.database.Cube;
import it.unibo.conversational.database.DBmanager;
import it.unibo.conversational.datatypes.Ngram.AnnotationType;
import it.unibo.conversational.vocalization.main.VolapSession;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONObject;
import zhsh.Tree;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * A Mapping is a forest of ngrams.
 */
public final class Mapping implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2901750778219671739L;

    /**
     * State of the OLAP session
     */
    public enum State {
        /**
         * Wait Full Query, disambiguate Full Query
         */
        ENGAGE,
        /**
         * Wait OLAP operator, disambiguate Full Query
         */
        ENGAGE_HINT,
        /**
         * Wait OLAP operator, disambiguate OLAP operator
         */
        NAVIGATE
    }

    /**
     * Compare mappings by number of matched entities and average similarity (reversed).
     *
     * @param s1 a mapping
     * @param s2 another mapping
     * @return mapping comparison
     */
    public static int compareMappings(final Mapping s1, final Mapping s2) {
        int c = Double.compare(Math.round(s1.getScorePFM() * 10000), Math.round(s2.getScorePFM() * 10000));
        if (c == 0) {
            c = Double.compare(Math.round(s1.getScoreM() * 10000), Math.round(s2.getScoreM() * 10000));
        }
        if (c == 0) {
            c = s1.toString().compareTo(s2.toString());
        }
        return c;
    }

    /**
     * Add tokens to bin if they are not used.
     *
     * @param s   mapping
     * @param nlp nl sentence
     * @return extend the mappings with bin
     */
    private static List<Ngram> fillSentence(final Mapping s, final String nlp) {
        final List<Ngram> ngrams = Lists.newArrayList(s.ngrams);
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final Map<Integer, String> tokens = Maps.newLinkedHashMap();
        Arrays.asList(nlp.split(" ")).stream().forEach(t -> tokens.put(atomicInteger.getAndIncrement(), t));
        ngrams.stream()
                .flatMap(n -> Ngram.leaves(n).stream())
                .map(t -> ((Ngram) t).pos())//
                .forEach(p -> {
                    for (int i = p.getLeft(); i <= p.getRight(); i++) {
                        tokens.remove(i);
                    }
                });
        final List<Ngram> bin =
                tokens.entrySet().stream()
                        .map(e -> new Ngram(e.getValue(), Type.BIN, new Entity(e.getValue()), Pair.of(e.getKey(), e.getKey())))
                        .sorted(Ngram::compareNgrams)
                        .collect(Collectors.toList());
        ngrams.addAll(bin);
        return ngrams;
    }

    private static void recursiveFlatten(final Set<String> res, final List<String> names, final Ngram n, final Map<Type, Integer> counter) {
        final List<String> newNames = Lists.newArrayList(names);
        if (n.children.isEmpty()) {
            newNames.add(n.type.toString() + ":" + n.mde().nameInTable() + " [(" + n.pos().getLeft() + ";" + n.pos().getRight() + ");" + Utils.DF.format(n.similarity()) + "]");
            res.add(newNames.stream().reduce((c1, c2) -> c1 + "\\" + c2).get() + "," + 1);
            newNames.add(n.tokens);
            res.add(newNames.stream().reduce((c1, c2) -> c1 + "\\" + c2).get() + "," + 1);
        } else {
            final int id = counter.compute(n.type, (k, v) -> counter.getOrDefault(k, 0) + 1);
            newNames.add("" + n.type + id + (n.type.equals(Type.GPSJ) ? "[" + Ngram.countLeaves(n) + "; " + Utils.DF.format(n.similarity()) + "]" : ""));
            res.add(newNames.stream().reduce((c1, c2) -> c1 + "\\" + c2).get() + "," + 1);
            n.children.stream().forEach((Ngram c) -> {
                res.add(newNames.stream().reduce((c1, c2) -> c1 + "\\" + c2).get() + "," + 1);
                recursiveFlatten(res, newNames, c, counter);
            });
        }
    }

    ;

    /**
     * Create a CSV representing the mapping.
     *
     * @param m   translated sentence
     * @param nlp natural language query
     * @return csv string
     * @throws Exception in case of error
     */
    public static String toCsv(final Mapping m, final String nlp) throws Exception {
        final List<String> tokens = Mapper.cleanSentence(nlp, false);
        final List<Ngram> s = fillSentence(m, String.join(" ", tokens));
        final Ngram r = s.size() == 1 ? s.get(0) : new Ngram(Type.FOO, s);
        final Set<String> ret = Sets.newLinkedHashSet();
        final Map<Type, Integer> counter = Maps.newLinkedHashMap();
        recursiveFlatten(ret, Lists.newLinkedList(), r, counter);
        return "id,value\n" + ret.stream().reduce((c1, c2) -> c1 + "\n" + c2).get();
    }

    public final Ngram bestNgram;

    /**
     * List of ngrams in a mapping.
     */
    public ImmutableList<Ngram> ngrams;

    public String ngramTypes = "";

    /**
     * Create a mapping.
     *
     * @param ngrams a list of ngrams
     */
    public Mapping(final Cube cube, final List<Ngram> ngrams) {
        this.ngrams = ImmutableList.copyOf(ngrams);
        if (!ngrams.isEmpty()) {
            for (Ngram n : ngrams) {
                ngramTypes += n.type.toString() + " ";
            }
            this.bestNgram = ngrams.stream().max((n1, n2) -> Integer.compare(n1.countNode(), n2.countNode())).get();
        } else {
            this.bestNgram = null;
        }
    }

    /**
     * Create a sentence as a list of ngrams.
     *
     * @param ngrams list of ngrams
     */
    public Mapping(final Cube cube, final Ngram... ngrams) {
        this(cube, Arrays.asList(ngrams));
    }

    /**
     * Count the number of nodes in a sentence.
     *
     * @return number of nested node (plus self)
     */
    public int countNodes() {
        return ngrams.stream().mapToInt(Ngram::countNode).max().getAsInt();
    }

    /**
     * Disambiguate the ambiguity with the given id with the given value
     *
     * @param ambiguityId ambiguityId
     */
    public void disambiguate(final String ambiguityId, final String approximateVal) {
        disambiguate(ambiguityId, approximateVal, Lists.newLinkedList());
    }

    /**
     * Disambiguate the ambiguity with the given id with the given value
     *
     * @param annotationId id of the annotation
     * @param log          log
     */
    public void disambiguate(final String annotationId, final String approximateVal, final List<Triple<AnnotationType, Ngram, Ngram>> log) {
        final LongAdder adder = new LongAdder();
        final List<Ngram> toRemove = Lists.newArrayList();
        for (final Ngram tree : ngrams) {
            Ngram.traverse(tree,
                    (n, acc) -> n.getAnnotations().containsKey(annotationId) || n.getHints().containsKey(annotationId),
                    (n, ac) -> {
                        adder.add(1);
                        final Pair<AnnotationType, Set<Entity>> annotation = n.getAnnotations().getOrDefault(annotationId, n.getHints().get(annotationId));
                        Optional<Ngram> parent = Ngram.findParent(tree, n);
                        if ("drop".equals(approximateVal)) {
                            if (Ngram.findParent(tree, n).isPresent()) {
                                Ngram.removeNode(tree, n);
                            } else {
                                toRemove.add(n);
                            }
                        } else {
                            switch (annotation.getKey()) {
                                case UP:
                                    // Get the GPSJ children
                                    final List<Ngram> children = new ArrayList<>(bestNgram.children);
                                    switch (n.type) {
                                        case GC:
                                            // Find the old group by clause
                                            Ngram acc = children.stream().filter(nn -> nn.type.equals(Type.GC)).findAny().get();
                                            // ... remove it
                                            children.remove(acc);
                                            // Create a new selection clause
                                            Set<Ngram> attributes = Ngram.leaves(n).stream().filter(nn -> nn.type.equals(Type.ATTR)).collect(Collectors.toSet());
                                            for (Ngram a : attributes) {
                                                acc = new Ngram(Type.GC, ImmutableList.of(acc, a));
                                            }
                                            children.add(acc);
                                            break;
                                        case SC:
                                            // Find the old selection clause SC = < Where, SC>
                                            Ngram scInGpsj = children.stream().filter(nn -> nn.type.equals(Type.SC)).findAny().get();
                                            // Get internal Where and SC
                                            Ngram wh = scInGpsj.children.stream().filter(nn -> nn.type.equals(Type.WHERE)).findAny().get();
                                            Ngram sc = scInGpsj.children.stream().filter(nn -> nn.type.equals(Type.SC)).findAny().get();
                                            // ... remove it
                                            children.remove(scInGpsj);
                                            // Create a new selection clause
                                            final Ngram newSc = new Ngram(Type.SC, Lists.newArrayList(wh, new Ngram(Type.SC, ImmutableList.of(sc, Ngram.DUMMY_AND, n))));
                                            children.add(newSc);
                                            break;
                                        case MC:
                                            // Find the old measure clause
                                            final Ngram mc = children.stream().filter(nn -> nn.type.equals(Type.MC)).findAny().get();
                                            // An MC clause alone will always be considered as another GPSJ query, so if I add this clause to the primary GPSJ, I need to remove the existing query
                                            toRemove.add(parent.get()); // ngrams.remove(parent.get());
                                            // Create a new measure clause
                                            final Ngram newMc = new Ngram(Type.MC, ImmutableList.of(mc, n));
                                            children.add(newMc);
                                            // ... remove it
                                            children.remove(mc);
                                            break;
                                        default:
                                            throw new NotImplementedException("Unparsed clause not implemented for " + n.type);
                                    }
                                    bestNgram.setChildren(ImmutableList.copyOf(children));
                                    toRemove.add(n); // the current ngram has been added to an existing close, remove it from the list of ngrams
                                    // ngrams.remove(n);
                                    break;
                                default:
                                    n.disambiguate(annotation, approximateVal, log, n.getHints().containsKey(annotationId));
                            }
                        }
                        n.getAnnotations().remove(annotationId);
                        n.getHints().remove(annotationId);
                        return -1;
                    });
        }
        List<Ngram> ngramsCopy = ngrams.stream().filter(n -> !n.children.isEmpty()).collect(Collectors.toList());
        toRemove.forEach(ngramsCopy::remove);
        ngrams = ImmutableList.copyOf(ngramsCopy);
        if (adder.intValue() == 0) {
            // Parser.automaticDisambiguate(this);
            // disambiguate( annotationId, approximateVal, log);
            throw new IllegalArgumentException("Cannot find annotation with id: " + annotationId + ", available annotations are: " + getAnnotatedNgrams().stream().map(Ngram::getAnnotations).collect(Collectors.toList()));
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Mapping) {
            return this.ngrams.equals(((Mapping) obj).ngrams);
        }
        return false;
    }

    /**
     * @return ngrams with ambiguities
     */
    public List<Ngram> getAnnotatedNgrams() {
        final List<Ngram> annotatedNgrams = Lists.newLinkedList();
        for (Ngram n : ngrams) {
            Ngram.traverse(n, (c, acc) -> !c.getAnnotations().isEmpty(), (c, acc) -> {
                annotatedNgrams.add(c);
                return -1;
            });
        }
        return ImmutableList.copyOf(annotatedNgrams);
    }

    /**
     * @return ngrams with hints
     */
    public List<Ngram> getHintedNgrams() {
        final List<Ngram> annotatedNgrams = Lists.newLinkedList();
        for (Ngram n : ngrams) {
            Ngram.traverse(n, (c, acc) -> !c.getHints().isEmpty(), (c, acc) -> {
                annotatedNgrams.add(c);
                return -1;
            });
        }
        return ImmutableList.copyOf(annotatedNgrams);
    }

    /**
     * @return the average similarity of the ngrams contained in the parsing tree (i.e., the ngram with the highest number of leaves).
     */
    public double getAvgSimilarity() {
        return bestNgram.similarity();
    }

    /**
     * @return parse tree of the GPSJ Query
     */
    public Ngram getBest() {
        return bestNgram;
    }

    public String getMappedNgrams() {
        return ngramTypes;
    }

    /**
     * @return the matched ngrams in a sentence
     */
    public Set<Ngram> getMatched() {
        return Ngram.leaves(bestNgram);
    }

    ;

    public List<Ngram> getNgrams() {
        return ngrams;
    }

    /**
     * @return the number of matched ngrams in a sentence
     */
    public int getNMatched() {
        return Ngram.countLeaves(bestNgram);
    }

    /**
     * Mapping score (i.e., score of all the ngrams in the mapping)
     *
     * @return sum of ngrams similarities.
     */
    public double getScoreM() {
        double sum = 0;
        for (Ngram n : ngrams) {
            sum += Ngram.getScore(n);
        }
        return sum;
    }

    /**
     * Parse forest score (i.e., score of all the ngrams in the GPSJ tree)
     *
     * @return sum of ngrams similarities for the ngrams coexntained in the parsing tree.
     */
    public double getScorePFM() {
        return Ngram.getScore(bestNgram);
    }

    public State getState() {
        if (getAnnotatedNgrams().size() > 0) {
            return State.ENGAGE;
        } else if (getAnnotatedNgrams().size() == 0 && getHintedNgrams().size() > 0) {
            return State.ENGAGE_HINT;
        } else {
            return State.NAVIGATE;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(ngrams);
    }

    /**
     * Try to automatically resolve frequent disambiguations.
     *
     * @param tau frequency threshold
     * @param log list of known disambiguations
     */
    public void hint(final double tau, final List<Triple<AnnotationType, Ngram, Ngram>> log) {
        for (final Ngram tree : ngrams) {
            tree.hint(tau, log);
        }
    }

    public JSONObject JSONobj(final Cube cube, final String nlp, final Optional<Long> limit) throws Exception {
        final String sql = getAnnotatedNgrams().isEmpty() ? Parser.getSQLQuery(cube, this) : "";
        final List<JSONObject> result = Lists.newLinkedList();
        final JSONObject res = new JSONObject();
        final JSONObject vocalization = new JSONObject();
        if (!sql.isEmpty()) {
            final long startTime = System.currentTimeMillis();
            final String sqlwithlimit;
            if (limit.isPresent()) {
                if (cube.getDbms().equalsIgnoreCase("oracle")) {
                    sqlwithlimit = "select * from (" + sql + ") t where rownum <= " + limit.get();
                } else {
                    sqlwithlimit = sql + " limit " + limit.get();
                }
            } else {
                sqlwithlimit = sql;
            }
            DBmanager.executeDataQuery(cube, sqlwithlimit, queryRes -> result.add(Utils.resultSet2Json(queryRes)));
            res.put("execution_time", System.currentTimeMillis() - startTime);
        }
        ngrams.forEach(n -> {
            final JSONObject ngram = n.toJSON(cube);
            ngram.put("main_tree", n.equals(bestNgram));
            res.append("clauses", ngram);
        });
        // If the query is a GPSJ with no annotations, it can be executed. So return its SQL and query result.
        if (!sql.isEmpty()) {
            res.put("sql", sql);
            res.put("result", result.remove(0));
            try {
                Pair<String, Pair<Double, Double>> voc = VolapSession.getInstance().executeQuery(sql, false);
                vocalization.put("description", voc.getLeft());
                vocalization.put("error", voc.getRight().getLeft());
                vocalization.put("quality", voc.getRight().getRight());
            } catch (Exception e) {
                vocalization.put("message", e.getMessage());
            }
            res.put("vocalization", vocalization);
        }
        if (nlp != null) {
            res.put("tree_csv", toCsv(this, nlp));
        }
        return res;
    }

    public double similarity(final Mapping anotherMapping) {
        try {
            final int distance = Tree.ZhangShasha(toStringTree(), anotherMapping.toStringTree());
            return 1.0 - 1.0 * distance / Math.max(countNodes(), anotherMapping.countNodes());
        } catch (final Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public String toJSON(final Cube cube) throws Exception {
        return JSONobj(cube, null, Optional.absent()).toString();
    }

    public String toJSON(final Cube cube, final String nlp) throws Exception {
        return JSONobj(cube, nlp, Optional.absent()).toString();
    }

    public String toJSON(final Cube cube, final String nlp, final Optional<Long> limit) throws Exception {
        return JSONobj(cube, nlp, limit).toString();
    }

    @Override
    public String toString() {
        return ngrams.toString();
    }

    /**
     * Return the ngram with the highest number of children as a string tree parsable by zhsh.
     *
     * @return string tree parsable by zhsh.
     */
    public String toStringTree() {
        return bestNgram.toStringTree();
    }
}