package it.unibo.conversational.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import it.unibo.conversational.Utils;
import it.unibo.conversational.datatypes.Entity;
import it.unibo.conversational.datatypes.Ngram;

/**
 * Handle synonyms in the database.
 */
public final class DBsynonyms extends DBmanager {

  private static Map<List<String>, List<Entity>> syns = Maps.newLinkedHashMap();
  private static Map<Object[], List<Triple<Entity, Double, String>>> cache = Maps.newLinkedHashMap();

  /** Initialize the synonym management. */
  private DBsynonyms() {
  }

  public static void cleanCache() {
    cache = Maps.newLinkedHashMap();
  }

  /** Load synonyms from the datawarehouse (for members: store reference to the corresponding level, for levels: store reference to the corresponding table). */
  private static void initSynonyms() {
    // Add others
    for (final String table: tabsWithSyns.stream().filter(t -> !t.equals(tabMEMBER) && !t.equals(tabLEVEL)).collect(Collectors.toList())) {
      String query = "select s.term, " + id(table) + ", " + name(table) + ", table_name from synonym s, " + table + " where s.reference_id = " + id(table) + " and s.table_name = '" + table + "'";
      try (
          Statement stmt = getMetaConnection().createStatement();
          ResultSet res = stmt.executeQuery(query);
      ) {
        while (res.next()) {
          List<String> synonym = Arrays.asList(res.getString(colSYNTERM).replace("_", " ").split(" ")).stream().filter(t -> !t.isEmpty()).collect(Collectors.toList());
          List<Entity> tmp = syns.getOrDefault(synonym, Lists.newLinkedList());
          tmp.add(new Entity(res.getInt(id(table)), res.getString(name(table)), table));
          syns.put(synonym, tmp);
        }
      } catch (final SQLException e) {
        e.printStackTrace();
      }
    }

    // Add members
    String query = "select * from `synonym` s, `member` m, `level` l, `column` c, `table` t where s.table_name = 'member' and reference_id = m.member_id and m.level_id = l.level_id and c.table_id = t.table_id and l.column_id = c.column_id";
    try (
        Statement stmt = getMetaConnection().createStatement();
        ResultSet res = stmt.executeQuery(query);
    ) {
      while (res.next()) {
        List<String> synonym = Arrays.asList(res.getString(colSYNTERM).replace("_", " ").split(" ")).stream().filter(t -> !t.isEmpty()).collect(Collectors.toList());
        List<Entity> tmp = syns.getOrDefault(synonym, Lists.newLinkedList());
        tmp.add(new Entity(
            res.getInt(id(tabMEMBER)), 
            res.getString(name(tabMEMBER)), 
            res.getInt(id(tabLEVEL)), 
            res.getString(name(tabCOLUMN)), 
            Utils.getDataType(res.getString(type(tabLEVEL))),
            tabMEMBER,
            res.getString(name(tabTABLE))));
        syns.put(synonym, tmp);
      }
    } catch (final SQLException e) {
      e.printStackTrace();
    }

    // Add levels
    query = "select s.term, l.level_name from synonym s, `level` l where s.table_name = 'level' and s.reference_id = l.level_id";
    try (
        Statement stmt = getMetaConnection().createStatement();
        ResultSet res = stmt.executeQuery(query);
    ) {
      while (res.next()) {
        List<String> synonym = Arrays.asList(res.getString(colSYNTERM).replace("_", " ").split(" ")).stream().filter(t -> !t.isEmpty()).collect(Collectors.toList());
        List<Entity> tmp = syns.getOrDefault(synonym, Lists.newLinkedList());
        tmp.add(QueryGeneratorChecker.getLevel(res.getString(name(tabLEVEL))));
        syns.put(synonym, tmp);
      }
    } catch (final SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Map the list of tokens (i.e., ngram) to a set of md_elements.
   * @param tokens ngram
   * @param thrSimilarityMember min similarity for members
   * @param thrSimilarityMetadata min similarity for metadata
   * @param synMember number of members
   * @param synMeta number of metadata
   * @return list of synonyms for the given token
   */
  public static List<Triple<Entity, Double, String>> getEntities(final Class toParse, final List<String> tokens, final double thrSimilarityMember, final double thrSimilarityMetadata, final int synMember, final int synMeta) {
    if (syns.isEmpty()) {
      initSynonyms();
    }
    final Object[] lookup = new Object[] {tokens, thrSimilarityMember, thrSimilarityMetadata, synMember, synMeta};
    List<Triple<Entity, Double, String>> cached = cache.get(lookup);
    if (cached == null) {
      List<Triple<Entity, Double, String>> memberAcc = Lists.newLinkedList();
      List<Triple<Entity, Double, String>> metadataAcc = Lists.newLinkedList();
      for (final Entry<List<String>, List<Entity>> entry: syns.entrySet()) { // iterate over synonyms
        final List<String> synonym = entry.getKey();
        final List<Entity> referredEntities = entry.getValue();
        final double sim = Utils.tokenSimilarity(tokens, synonym); // estimate the similarity
        if (sim >= Math.min(thrSimilarityMember, thrSimilarityMetadata)) {
          for (final Entity entity: referredEntities) {
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
