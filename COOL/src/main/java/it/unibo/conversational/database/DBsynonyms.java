package it.unibo.conversational.database;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.unibo.conversational.Utils;
import it.unibo.conversational.datatypes.Entity;
import it.unibo.conversational.datatypes.Ngram;
import it.unibo.conversational.olap.Operator;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import smile.neighbor.BKTree;
import smile.neighbor.Neighbor;

import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import static it.unibo.conversational.database.DBmanager.*;

/**
 * Handle synonyms in the database.
 */
public final class DBsynonyms {

    private static Map<Object[], List<Triple<Entity, Double, String>>> cache = Maps.newLinkedHashMap();

    /**
     * Initialize the synonym management.
     */
    private DBsynonyms() {
    }

    public static void cleanCache() {
        cache = Maps.newLinkedHashMap();
    }

    /**
     * Map the list of tokens (i.e., ngram) to a set of md_elements.
     *
     * @param tokens                ngram
     * @param thrSimilarityMember   min similarity for members
     * @param thrSimilarityMetadata min similarity for metadata
     * @param synMember             number of members
     * @param synMeta               number of metadata
     * @return list of synonyms for the given token
     */
    public static List<Triple<Entity, Double, String>> getEntities(final Cube cube, final Class toParse, final List<String> tokens, final double thrSimilarityMember, final double thrSimilarityMetadata, final int synMember, final int synMeta) {
        final Object[] lookup = new Object[]{tokens, thrSimilarityMember, thrSimilarityMetadata, synMember, synMeta};
        return cache.computeIfAbsent(lookup, k -> {
            final List<Triple<Entity, Double, String>> acc = searchSequential(cube, tokens, Math.min(thrSimilarityMember, thrSimilarityMetadata));
            final LongAdder memberCount = new LongAdder();
            final LongAdder metaCount = new LongAdder();
            final Set<Triple<Entity, Double, String>> res = Sets.newHashSet();
            acc.stream()
                    .sorted((p1, p2) -> -Double.compare(p1.getMiddle(), p2.getMiddle())) // sort by similarity
                    .forEach(t -> {
                        final Entity e = t.getLeft();
                        final Double sim = t.getMiddle();
                        if (e.metaTable().equals(tabMEMBER)) { // è un membro con sim suff
                            if (memberCount.intValue() < synMember && sim >= thrSimilarityMember) {
                                res.add(t);
                                memberCount.add(1L);
                            }
                        } else if (metaCount.intValue() < synMeta && sim >= thrSimilarityMetadata) { // è un metadato con sim suff (level, >=, by, etc.)
                            // do not add entities from `tabLANGUAGEOPERATOR` while parsing a complete query
                            if (toParse.equals(Operator.class) || toParse.equals(Ngram.class) && !e.metaTable().equals(tabLANGUAGEOPERATOR)) {
                                res.add(t);
                            }
                            metaCount.add(1L);
                        }
                    });
            return new ArrayList<>(res);
        });
    }

    /**
     * Return similar entities using sequential scan
     * @param tokens string to find
     * @param thr similarity threshold
     * @return similar entities
     */
    @NotNull
    public static List<Triple<Entity, Double, String>> searchSequential(final Cube cube, final List<String> tokens, final double thr) {
        final Map<List<String>, List<Entity>> syns = QueryGenerator.syns(cube);
        final List<Triple<Entity, Double, String>> acc = Lists.newArrayList();
        syns.forEach((synonym, referredEntities) -> {
            final double sim = Utils.tokenSimilarity(tokens, synonym); // estimate the similarity
            if (sim >= thr) {
                referredEntities.forEach(e -> acc.add(Triple.of(e, sim, String.join(" ", synonym))));
            }
        });
        return acc;
    }

    /**
     * Return similar entities using BKTree
     * @param tokens string to find
     * @param thr similarity threshold
     * @return similar entities
     */
    @NotNull
    public static List<Triple<Entity, Double, String>> searchBKtree(final Cube cube, final List<String> tokens, final double thr) {
        final BKTree<String> syns = QueryGenerator.bktree(cube);
        final List<Neighbor<String, String>> res = Lists.newArrayList();
        syns.range(String.join(" ", tokens), (int) Math.ceil(tokens.size() * 0.3), res);
        return res
                .stream()
                .flatMap(n -> QueryGenerator.syns(cube).get(Arrays.asList(n.value.split(" "))).stream().map(e -> Triple.of(e, 1 - (1.0 * n.distance / Math.max(n.key.length(), n.value.length())), String.join(" ", n.value))))
                .filter(t -> t.getMiddle() >= thr)
                .collect(Collectors.toList());
    }
}
