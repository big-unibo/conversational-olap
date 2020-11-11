package it.unibo.conversational.database;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unibo.conversational.Utils;
import it.unibo.conversational.datatypes.Entity;
import it.unibo.conversational.datatypes.Ngram;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
        final Map<List<String>, List<Entity>> syns = QueryGenerator.syns(cube);
        final Object[] lookup = new Object[]{tokens, thrSimilarityMember, thrSimilarityMetadata, synMember, synMeta};
        List<Triple<Entity, Double, String>> cached = cache.get(lookup);
        if (cached == null) {
            List<Triple<Entity, Double, String>> memberAcc = Lists.newLinkedList();
            List<Triple<Entity, Double, String>> metadataAcc = Lists.newLinkedList();
            for (final Entry<List<String>, List<Entity>> entry : syns.entrySet()) { // iterate over synonyms
                final List<String> synonym = entry.getKey();
                final List<Entity> referredEntities = entry.getValue();
                final double sim = Utils.tokenSimilarity(tokens, synonym); // estimate the similarity
                if (sim >= Math.min(thrSimilarityMember, thrSimilarityMetadata)) {
                    for (final Entity entity : referredEntities) {
                        if (entity.metaTable().equals(tabMEMBER) && sim >= thrSimilarityMember) { // è un membro con sim suff
                            memberAcc.add(Triple.of(entity, sim, String.join(" ", synonym)));
                        } else if (!entity.metaTable().equals(tabMEMBER) && sim >= thrSimilarityMetadata) { // è un metadato con sim suff (level, >=, by, etc.)
                            if (!toParse.equals(Ngram.class)) {
                                metadataAcc.add(Triple.of(entity, sim, String.join(" ", synonym)));
                            } else {
                                if (!entity.metaTable().equals(tabLANGUAGEOPERATOR)) { // do not add entities from `language_operator` while parsing a complete query
                                    metadataAcc.add(Triple.of(entity, sim, String.join(" ", synonym)));
                                }
                            }
                        }
                    }
                }
            }
            final Set<Triple<Entity, Double, String>> res =
                    memberAcc.stream()
                            .sorted((p1, p2) -> -Double.compare(p1.getMiddle(), p2.getMiddle())) // sort by similarity
                            .limit(synMember) // select the best
                            .collect(Collectors.toSet());
            res.addAll(//
                    metadataAcc.stream()
                            .sorted((p1, p2) -> -Double.compare(p1.getMiddle(), p2.getMiddle())) // sort by similarity
                            .limit(synMeta) // select the best
                            .collect(Collectors.toSet()) //
            );
            cached = Lists.newArrayList(res);
            cache.put(lookup, cached);
        }
        return cached;
    }
}
