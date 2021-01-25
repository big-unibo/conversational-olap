package it.unibo.conversational.database;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.unibo.conversational.Utils;
import it.unibo.conversational.datatypes.Entity;
import it.unibo.conversational.datatypes.Ngram;
import it.unibo.conversational.olap.Operator;
import it.unibo.smile.neighborg.MyBKTree;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import smile.neighbor.Neighbor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import static it.unibo.conversational.Utils.ngram2string;
import static it.unibo.conversational.Utils.string2ngram;
import static it.unibo.conversational.database.DBmanager.tabLANGUAGEOPERATOR;
import static it.unibo.conversational.database.DBmanager.tabMEMBER;
import static it.unibo.conversational.database.QueryGenerator.search;

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
            final List<Triple<Entity, Double, String>> acc =
                    search.equals("bktree")
                            ? searchBKtree(cube, tokens, Math.min(thrSimilarityMember, thrSimilarityMetadata))
                            : searchSequential(cube, tokens, Math.min(thrSimilarityMember, thrSimilarityMetadata));
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
     *
     * @param tokens string to find
     * @param thr    similarity threshold
     * @return similar entities
     */
    @NotNull
    public static List<Triple<Entity, Double, String>> searchSequential(final Cube cube, final List<String> tokens, final double thr) {
        Map<List<String>, List<Entity>> syns = Maps.newHashMap();
        final List<Triple<Entity, Double, String>> acc = Lists.newArrayList();
        if (tokens.stream().mapToDouble(String::length).sum() == 1 || thr >= 0.99) {
            final List<Entity> res = QueryGenerator.syns(cube).get(tokens);
            if (res != null) {
                syns.put(tokens, res);
            }
        } else {
            syns = QueryGenerator.syns(cube);
        }
        syns.forEach((synonym, referredEntities) -> {
            final double sim = Utils.tokenSimilarity(tokens, synonym); // estimate the similarity
            if (sim >= thr) {
                referredEntities.forEach(e -> acc.add(Triple.of(e, sim, ngram2string(synonym))));
            }
        });
        return acc;
    }

    /**
     * Return similar entities using BKTree
     *
     * @param tokens string to find
     * @param thr    similarity threshold
     * @return similar entities
     */
    @NotNull
    public static List<Triple<Entity, Double, String>> searchBKtree(final Cube cube, final List<String> tokens, final double thr) {
        final double length = tokens.stream().mapToDouble(String::length).sum();
        if (length == 1 || thr >= 0.99) {
            return searchSequential(cube, tokens, thr);
        }
        final MyBKTree<String> syns = QueryGenerator.bktree(cube);
        final List<Neighbor<String, String>> res = Lists.newArrayList();
        syns.range(ngram2string(tokens), Math.min((int) Math.ceil(length * (1.0 - thr)), length - 1), res);
        return res
                .stream()
                .flatMap(n -> {
                    final List<String> syn = string2ngram(n.value);
                    final List<Entity> partialRes = QueryGenerator.syns(cube).get(syn);
                    if (partialRes == null) {
                        throw new IllegalArgumentException("No synonym found for " + syn);
                    }
                    return QueryGenerator.syns(cube).get(syn).stream().map(e -> Triple.of(e, Utils.tokenSimilarity(syn, tokens), n.value));
                })
                .filter(t -> t.getMiddle() >= thr)
                .collect(Collectors.toList());
    }
}