package it.unibo.conversational.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import it.unibo.conversational.Utils;
import it.unibo.conversational.datatypes.Entity;
import it.unibo.conversational.datatypes.Mapping;
import it.unibo.conversational.olap.Operator;

/**
 * Interacting with the database SQL query.
 */
public final class QueryGeneratorChecker extends DBmanager {

  public static final Map<String, Set<Entity>> operatorOfMeasure = QueryGeneratorChecker.getOperatorOfMeasure();
  public static final Map<String, Set<Entity>> membersofLevels = QueryGeneratorChecker.getMembersOfLevels();
  public static final Map<String, Set<Entity>> levelsOfMembers = QueryGeneratorChecker.getLevelsOfMembers();
  public static final Map<String, Entity> string2level = QueryGeneratorChecker.getLevels();
  public static final Set<Entity> yearLevels = QueryGeneratorChecker.getYearLevels();

  private QueryGeneratorChecker() {
  }

  /**
   * @return get the fact
   */
  public static Pair<Integer, String> getFactTable() {
    final String query = "SELECT * FROM `" + tabTABLE + "` WHERE `" + type(tabTABLE) + "` = \"" + TableTypes.FT + "\"";
    try (
        Statement stmt = getMetaConnection().createStatement();
        ResultSet res = stmt.executeQuery(query)
    ) {
      res.first();
      return Pair.of(res.getInt(id(tabTABLE)), res.getString(name(tabTABLE)));
    } catch (final SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

//  /**
//   * @param m
//   *          member
//   * @return get the level of the given member
//   */
//  public static Ngram getLevelOfMember(final Ngram m) {
//    final String query = "SELECT * FROM `" + tabLEVEL + "` L, `" + tabCOLUMN + "` C WHERE C." + id(tabCOLUMN) + " = L." + id(tabCOLUMN) + " AND " + id(tabLEVEL) + " = " + m.mde().refToOtherTable();
//    try (
//        Statement stmt = getMetaConnection().createStatement();
//        ResultSet res = stmt.executeQuery(query)
//    ) {
//      res.first();
//      final String name = res.getString(DBmanager.name(tabLEVEL));
//      return new Ngram(name, Type.ATTR, new Entity(m.mde().refToOtherTable(), name, res.getInt(id(tabTABLE)), Utils.getDataType(res.getString(type(tabLEVEL))), res.getString(name(tabTABLE))), null);
//    } catch (final SQLException e) {
//      e.printStackTrace();
//    }
//    return null;
//  }

  /**
   * @param id
   *          id of the language predicate
   * @return the <type, name> of the language predicate
   */
  public static Pair<String, String> getOperator(final int id) {
    final String query = "SELECT " + type(tabLANGUAGEOPERATOR) + ", " + name(tabLANGUAGEOPERATOR) + " FROM `" + tabLANGUAGEOPERATOR + "` WHERE " + id(tabLANGUAGEOPERATOR) + " = " + id;
    try (
        Statement stmt = getMetaConnection().createStatement();
        ResultSet res = stmt.executeQuery(query)
    ) {
      while (res.next()) {
        return Pair.of(res.getString(type(tabLANGUAGEOPERATOR)), res.getString(name(tabLANGUAGEOPERATOR)));
      }
    } catch (final SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param id
   *          id of the language predicate
   * @return the <type, name> of the language predicate
   */
  public static Pair<String, String> getPredicate(final int id) {
    final String query = "SELECT " + type(tabLANGUAGEPREDICATE) + ", " + name(tabLANGUAGEPREDICATE) + " FROM `" + tabLANGUAGEPREDICATE + "` WHERE " + id(tabLANGUAGEPREDICATE) + " = " + id;
    try (
        Statement stmt = getMetaConnection().createStatement();
        ResultSet res = stmt.executeQuery(query)
    ) {
      while (res.next()) {
        return Pair.of(res.getString(type(tabLANGUAGEPREDICATE)), res.getString(name(tabLANGUAGEPREDICATE)));
      }
    } catch (final SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @return a map <measure, operators> (i.e., the operators that are appliable to the given measure)
   */
  public static Map<String, Set<Entity>> getOperatorOfMeasure() {
    final Map<String, Set<Entity>> map = Maps.newLinkedHashMap();
    final String query = "select gm." + id(tabGROUPBYOPERATOR) + ", gm." + id(tabMEASURE) + ", " + name(tabMEASURE) + ", " + name(tabGROUPBYOPERATOR) + " "
        + "from `" + tabGRBYOPMEASURE + "` gm, `" + tabMEASURE + "` m, `" + tabGROUPBYOPERATOR + "` g " + "where g." + id(tabGROUPBYOPERATOR) + " = gm."
        + id(tabGROUPBYOPERATOR) + " and gm." + id(tabMEASURE) + " = m." + id(tabMEASURE) + "";
    try (
        Statement stmt = getMetaConnection().createStatement();
        ResultSet res = stmt.executeQuery(query)
    ) {
      while (res.next()) { // for each group by operator
        final String mea = res.getString(name(tabMEASURE));
        final int id = res.getInt(id(tabGROUPBYOPERATOR));
        final String op = res.getString(name(tabGROUPBYOPERATOR));
        final Set<Entity> val = map.getOrDefault(mea, Sets.newLinkedHashSet());
        val.add(new Entity(id, op, tabGROUPBYOPERATOR));
        map.put(mea, val);
      }
    } catch (final SQLException e) {
      e.printStackTrace();
    }
    return map;
  }

  /**
   * Save a query in the dataset.
   * 
   * @param query
   *          nl query
   * @param gbset
   *          correct group by set
   * @param predicate
   *          correct selection clause
   * @param selclause
   *          correct measure clause
   */
  public static void saveQuery(final String query, final String gbset, final String predicate, final String selclause) {
    final String sql = "INSERT INTO `" + tabQuery + "` (`" + colQueryText + "`, `" + colQueryGBset + "`, `" + colQueryMeasClause + "`, `" + colQuerySelClause + "`) " + "VALUES (\"" + query + "\", \"" + gbset + "\", \"" + selclause + "\", \"" + predicate + "\")";
    executeQuery(sql);
  }

  private static String checkNull(final Object toCheck, final String res) {
    return (toCheck == null ? "" : res);
  }

  /**
   * Save a OLAP session in the dataset.
   * 
   * @param sessionid session id
   * @param annotationid annotation id
   * @param valueEn value in English
   * @param valueIta value in Italian
   * @param limit limit
   * @param fullquery full query to serialize
   * @param operator OLAP operator to serialize
   * @throws IOException in case of serialization error
   */
  public static void saveSession(final String sessionid, final String annotationid, final String valueEn, final String valueIta, final String limit, final Mapping fullquery, final Operator operator) throws IOException {
    final String sql = 
        "INSERT INTO `" + tabOLAPsession + "` (`timestamp`, `session_id`, `value_en`"
            + checkNull(annotationid, ", `annotation_id`")
            + checkNull(valueIta,     ", `value_ita`")
            + checkNull(limit,        ", `limit`")
            + checkNull(fullquery,    ", `fullquery_serialized`")
            + checkNull(fullquery,    ", `fullquery_tree`")
            + checkNull(operator,     ", `olapoperator_serialized`")
            + ")"
            + "VALUES (" + //
                + System.currentTimeMillis() + "," //
                + "\"" + (sessionid == null ? "" : sessionid) + "\","//
                + "\"" + (valueEn == null ? "" : valueEn) + "\""//
                + checkNull(annotationid, ", \"" + annotationid + "\"")
                + checkNull(valueIta, ", \"" + valueIta + "\"")
                + checkNull(limit, ", \"" + limit + "\"")
                + (fullquery != null ? ", ?" : "")
                + (fullquery != null ? ", ?" : "")
                + (operator != null ? ", ?" : "")
            + ")";
    try (PreparedStatement pstmt = getMetaConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      if (fullquery != null) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(baos);
        oout.writeObject(fullquery);
        pstmt.setBytes(1, baos.toByteArray());
        oout.close();
        pstmt.setString(2, fullquery.toStringTree());
      }
      if (operator != null) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(baos);
        oout.writeObject(operator);
        pstmt.setBytes(3, baos.toByteArray());
        oout.close();
      }
      pstmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static Pair<String, String> getTabDetails(int idFT, int idTable) {
    final String query = "SELECT * FROM `" + tabTABLE + "` WHERE `" + id(tabTABLE) + "` = " + idTable;
    final String query1 = "SELECT * FROM `" + tabCOLUMN + "` C INNER JOIN `" + tabRELATIONSHIP + "` R ON C." + id(tabRELATIONSHIP) + " = R." + id(tabRELATIONSHIP) + " WHERE `" + colRELTAB1 + "` = " + idFT + " AND `" + colRELTAB2 + "` = " + idTable;
    try (
        Statement stmt = getMetaConnection().createStatement();
        ResultSet resDet = stmt.executeQuery(query);
        Statement stmt1 = getMetaConnection().createStatement();
        ResultSet resCol = stmt1.executeQuery(query1)
    ) {
      resDet.first();
      resCol.first();
      return Pair.of(resDet.getString(name(tabTABLE)), resCol.getString(name(tabCOLUMN)));
    } catch (final SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getTable(final String... attributes) {
    final String query = "select distinct `table_name` from `level` l join `column` c on l.level_name = c.column_name join `table` t on c.table_id = t.table_id where level_name in (" + Arrays.asList(attributes).stream().reduce((a, b) -> "'" + a + "','" + b + "'").get() + ")";
    try (
        final Statement stmt = getMetaConnection().createStatement();
        final ResultSet res = stmt.executeQuery(query);
    ) {
      // final List<String> tables = Lists.newLinkedList();
      while (res.next()) {
        return res.getString(name(tabTABLE));
        // tables.add(res.getString(name(tabTABLE)));
      }
      // return tables;
    } catch (final SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Functional dependency
   * @param specific specific attribute
   * @param generic generic attribute
   * @return Map of <generic value, specific values> entries
   */
  public static Map<String, List<String>> getFunctionalDependency2(final String specific, final String generic) {
    final String query = "select distinct " + specific + ", " + generic + " from " + getTable(specific, generic);
    try (
        final Statement stmt = getDataConnection().createStatement();
        final ResultSet res = stmt.executeQuery(query);
    ) {
      final Map<String, List<String>> tables = Maps.newLinkedHashMap();
      while (res.next()) {
        if (!tables.containsKey(res.getString(generic))) {
          tables.put(get(res, 2), Lists.newArrayList(get(res, 1)));
        } else {
          tables.get(get(res, 2)).add(get(res, 1));
        }
      }
      return tables;
    } catch (final SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Functional dependency
   * @param specific specific attribute
   * @param generic generic attribute
   * @return Map of <specific value, generic value> entries
   */
  public static Map<String, String> getFunctionalDependency(final String specific, final String generic) {
    final String query = "select distinct " + specific + ", " + generic + " from " + getTable(specific, generic);
    try (
        final Statement stmt = getDataConnection().createStatement();
        final ResultSet res = stmt.executeQuery(query);
    ) {
      final Map<String, String> tables = Maps.newLinkedHashMap();
      while (res.next()) {
        if (tables.containsKey(res.getString(specific))) {
          throw new IllegalArgumentException("Overriding " + res.getString(specific));
        }
        tables.put(get(res, 1), get(res, 2));
      }
      return tables;
    } catch (final SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static String get(ResultSet res, int idx) throws SQLException {
    switch (res.getMetaData().getColumnClassName(idx)) {
    case "java.sql.Timestamp":
      final Date date = new Date();
      date.setTime(res.getTimestamp(idx).getTime());
      return new SimpleDateFormat("yyyy-MM-dd").format(date);
    default:
      return res.getString(idx);
    }
  }

  /**
   * Returns the member of each level.
   * 
   * @return set members (i.e., entities) for each levels
   */
  public static Map<String, Set<Entity>> getMembersOfLevels() {
    final String query = "select * from `level` l, `column` c, `table` t, `member` m where c.table_id = t.table_id and l.column_id = c.column_id and l." + id(tabLEVEL) + " = m." + id(tabLEVEL);
    try (
        Statement stmt = getMetaConnection().createStatement();
        ResultSet res = stmt.executeQuery(query);
    ) {
      final Map<String, Set<Entity>> members = Maps.newLinkedHashMap();
      while (res.next()) {
        final Set<Entity> tmp = members.getOrDefault(res.getString(name(tabLEVEL)), Sets.newLinkedHashSet());
        tmp.add(new Entity(
            res.getInt(id(tabMEMBER)),
            res.getString(name(tabMEMBER)),
            res.getInt(id(tabLEVEL)), 
            res.getString(name(tabCOLUMN)),
            Utils.getDataType(res.getString(type(tabLEVEL))), 
            tabMEMBER,
            res.getString(name(tabTABLE))));
        members.put(res.getString(name(tabLEVEL)), tmp);
      }
      return members;
    } catch (final SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Returns the member of each level.
   * 
   * @return set members (i.e., entities) for each levels
   */
  public static Map<String, Set<Entity>> getLevelsOfMembers() {
    final String query = "select * from `member` m, `level` a, `column` c, `table` t where c.table_id = t.table_id and a.column_id = c.column_id and m.level_id = a.level_id";
    try (
        Statement stmt = getMetaConnection().createStatement();
        ResultSet res = stmt.executeQuery(query);
    ) {
      final Map<String, Set<Entity>> attributes = Maps.newLinkedHashMap();
      while (res.next()) {
        final Set<Entity> tmp = attributes.getOrDefault(res.getString(name(tabMEMBER)), Sets.newLinkedHashSet());
        tmp.add(new Entity(
            res.getInt(id(tabLEVEL)),
            res.getString(name(tabLEVEL)),
            res.getInt(id(tabTABLE)), 
            res.getString(name(tabCOLUMN)),
            Utils.getDataType(res.getString(type(tabLEVEL))),
            tabLEVEL,
            res.getString(name(tabTABLE))));
        attributes.put(res.getString(name(tabMEMBER)), tmp);
      }
      return attributes;
    } catch (final SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Describe a level
   * 
   * @param level
   *          level name
   * @param topN
   *          number of entries to retrieve
   * @return JSON object
   */
  public static JSONObject describeLevel2JSON(final String level, final int topN) {
    final JSONObject o = new JSONObject();
    QueryGeneratorChecker.describeLevel(level, topN).forEach(p -> o.put(p.getLeft(), p.getRight()));
    return o;
  }

  /**
   * Describe a level
   * 
   * @param level
   *          level name
   * @param topN
   *          number of entries to retrieve
   * @return Set of pairs (e.g., {(min, 1), (max, 10), ...})
   */
  public static Set<Pair<String, Object>> describeLevel(final String level, final int topN) {
    final String query = "select level_type, level_description, cardinality, min, max, avg, isDescriptive, mindate, maxdate, member_name from `level` l left join `member` m on (l.level_id = m.level_id) where level_name = \"" + level + "\" limit " + topN;
    try (
        Statement stmt = getMetaConnection().createStatement();
        ResultSet res = stmt.executeQuery(query);
    ) {
      final Set<Pair<String, Object>> attributes = Sets.newLinkedHashSet();
      final BiFunction<String, Set<Pair<String, Object>>, Object> get = new BiFunction<String, Set<Pair<String, Object>>, Object>() {
        @Override
        public Object apply(String t, Set<Pair<String, Object>> u) {
          try {
            if (res.getObject(t) != null) {
              attributes.add(Pair.of(t, res.getObject(t)));
            }
          } catch (final SQLException e) {
            e.printStackTrace();
          }
          return null;
        }
      };
      final Set<String> members = Sets.newLinkedHashSet();
      while (res.next()) {
        get.apply("min", attributes);
        get.apply("max", attributes);
        get.apply("mindate", attributes);
        get.apply("maxdate", attributes);
        get.apply("avg", attributes);
        get.apply("isDescriptive", attributes);
        get.apply("level_description", attributes);
        if (res.getString("member_name") != null) {
          members.add(res.getString("member_name"));
        }
      }
      if (!members.isEmpty()) {
        attributes.add(Pair.of("members", members));
      }
      return attributes;
    } catch (final SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Get levels with type "year". TODO: in this version only the level 'the_year' is retrieved. This is because it not easy to find "year" levels.
   * 
   * @return levels with type "year"
   */
  public static Set<Entity> getYearLevels() {
    return Sets.newHashSet(string2level.get("the_year"));
  }

  /**
   * @param level level
   * @return the entity corresponding to the level
   */
  public static Entity getLevel(final String level) {
    return string2level.get(level.toLowerCase());
  }

  public static Map<String, Entity> getLevels() {
    final String query = "select * from `level` l, `column` c, `table` t where c.table_id = t.table_id and l.column_id = c.column_id";
    try (
        Statement stmt = getMetaConnection().createStatement();
        ResultSet res = stmt.executeQuery(query);
    ) {
      final Map<String, Entity> attributes = Maps.newLinkedHashMap();
      while (res.next()) {
        attributes.put(res.getString(name(tabLEVEL)).toLowerCase(), 
            new Entity(
                res.getInt(id(tabLEVEL)),
                res.getString(name(tabLEVEL)),
                res.getInt(id(tabTABLE)), 
                res.getString(name(tabCOLUMN)),
                Utils.getDataType(res.getString(type(tabLEVEL))),
                tabLEVEL,
                res.getString(name(tabTABLE))));
      }
      return attributes;
    } catch (final SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param sessionid session to be dropped
   */
  public static void dropSession(final String sessionid) {
    final String sql = "DELETE FROM OLAPsession WHERE session_id = \"" + sessionid + "\"";
    executeQuery(sql);
  }

  private static final Logger L = LoggerFactory.getLogger(QueryGeneratorChecker.class);

  /**
   * @param sessionid session to check
   * @param fullquery ground truth on full query
   * @param session ground truth on session result
   * @return map of statistics
   * @throws IOException in case or error
   */
  public static Map<String, Object> getSessionStatistics(final String sessionid, final Mapping fullquery, final Mapping session) throws IOException {
    final String sql = 
        "select `value_en`, `timestamp`, `fullquery_serialized`, `olapoperator_serialized` "
        + "from OLAPsession where `value_en` in (\"read\", \"navigate\", \"reset\") and session_id = \"" + sessionid + "\" "
        + "and `timestamp` >= (select `timestamp` from OLAPsession where session_id = \"" + sessionid + "\" and value_en = \"read\" order by 1 desc limit 1)";
    final Map<String, Long> lookupTime = Maps.newLinkedHashMap();
    final Map<String, Mapping> lookupQuery = Maps.newLinkedHashMap();
    final Map<String, Operator> lookupOperator = Maps.newLinkedHashMap();
    try (
        Statement stmt = getMetaConnection().createStatement();
        ResultSet res = stmt.executeQuery(sql);
    ) {
      while (res.next()) {
        lookupTime.put(res.getString(1), res.getLong(2));
        if (res.getBytes(3) != null) {
          final ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(res.getBytes(3)));
          lookupQuery.put(res.getString(1), (Mapping) objectIn.readObject());
        }
        if (res.getBytes(4) != null) {
          final ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(res.getBytes(4)));
          lookupOperator.put(res.getString(1), (Operator) objectIn.readObject());
        }
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
    if (lookupOperator.isEmpty()) {
      throw new IllegalArgumentException("Could not find session: " + sessionid);
    }
    final Map<String, Object> statistics = Maps.newLinkedHashMap();
    statistics.put("fullquery_time", (lookupTime.get("navigate") - lookupTime.get("read")) / 1000);
    statistics.put("session_time", (lookupTime.get("reset") - lookupTime.get("read")) / 1000);
    statistics.put("operator_time", ((long) statistics.get("session_time") - (long) statistics.get("fullquery_time")) / 2);
    statistics.put("fullquery_sim", lookupQuery.get("navigate").similarity(fullquery));
    statistics.put("session_sim", lookupQuery.get("reset").similarity(session));
    statistics.put("fullquery", lookupQuery.get("navigate").toStringTree());
    statistics.put("session", lookupQuery.get("reset").toStringTree());
    statistics.put("fullquery_gt", fullquery.toStringTree());
    statistics.put("session_gt", session.toStringTree());
    statistics.putAll(getSessionCounts(sessionid));
    L.debug("GTR: " + fullquery.toStringTree());
    L.debug("RET: " + lookupQuery.get("navigate").toStringTree());
    L.debug("GTR: " + session.toStringTree());
    L.debug("RET: " + lookupQuery.get("reset").toStringTree());
    return statistics;
  }

  /**
   * @param sessionid session to check
   * @return number of steps in section
   */
  public static Map<String, Double> getSessionCounts(final String sessionid) {
    final Map<String, Double> statistics = Maps.newLinkedHashMap();

    String sql = 
        "select count(*) as steps, count(distinct case when annotation_id is null or annotation_id = '' then null else annotation_id end) as annotations "
        + "from OLAPsession "
        + "where `value_en` <> \"navigate\" and session_id = \"" + sessionid + "\" and "
        + "    `timestamp` < (select `timestamp` from OLAPsession where session_id = \"" + sessionid + "\" and value_en = \"reset\" order by 1 desc limit 1) and "
        + "    `timestamp` > (select `timestamp` from olapsession where session_id = \"" + sessionid + "\" and value_en = \"read\"  order by 1 desc limit 1)";
    try (
        final Statement stmt = getMetaConnection().createStatement();
        final ResultSet res = stmt.executeQuery(sql);
    ) {
      while (res.next()) {
        statistics.put("session_iterations", res.getInt(1) * 1.0);
        statistics.put("session_ambiguities", res.getInt(2) * 1.0);
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }

    sql = 
        "select count(*) as steps, count(distinct case when annotation_id is null or annotation_id = '' then null else annotation_id end)"
        + " from OLAPsession " 
        + "where session_id = \"" + sessionid + "\" and `value_en` <> \"navigate\" and"
        + "    `timestamp` > (select `timestamp` from OLAPsession where session_id = \"" + sessionid + "\" and value_en = \"read\" order by 1 desc limit 1) and "
        + "    `timestamp` <= (select `timestamp` from olapsession where session_id = \"" + sessionid + "\" and value_en <> \"navigate\" and `timestamp` >= (select `timestamp` from olapsession where session_id = \"" + sessionid + "\" and value_en = \"navigate\" order by 1 desc limit 1) order by 1 asc limit 1)";
    // System.out.println(sql);
    try (
        final Statement stmt = getMetaConnection().createStatement();
        final ResultSet res = stmt.executeQuery(sql);
    ) {
      while (res.next()) {
        statistics.put("fullquery_iterations", res.getInt(1) * 1.0);
        statistics.put("fullquery_ambiguities", res.getInt(2) * 1.0);
        statistics.put("operator_iterations", (statistics.get("session_iterations") - res.getInt(1)) / 2.0);
        statistics.put("operator_ambiguities", (statistics.get("session_ambiguities") - res.getInt(2)) / 2.0);
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }

    return statistics;
  }
}
