package it.unibo.conversational.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import com.google.common.collect.Sets;

import it.unibo.conversational.Utils;

/** Handling connection to database. */
public class DBmanager {

  /** Init dbmanager. */
  protected DBmanager() {
  }

  /**
   * @param s string
   * @return append _id to the string
   */
  protected static String id(final String s) {
    return s + "_id";
  }

  /**
   * @param s string
   * @return append _name to the string
   */
  protected static String name(final String s) {
    return s + "_name";
  }

  /**
   * @param s string
   * @return append _type to the string
   */
  protected static String type(final String s) {
    return s + "_type";
  }

  /**
   * @param s string
   * @return append _synonyms to the string
   */
  protected static String synonyms(final String s) {
    return s + "_synonyms";
  }

  /** Table types. */
  public enum TableTypes {
    /** Fact table. */
    FT,
    /** Dimension table. */
    DT
  }

  /****************************/
  /** Table tabTABLE. */
  public static final String tabTABLE = "table";
  /** Table tabRELATIONSHIP. */
  public static final String tabRELATIONSHIP = "relationship";
  /** Table tabCOLUMN. */
  public static final String tabCOLUMN = "column";
  /** Table tabDATABASE. */
  public static final String tabDATABASE = "database";
  /** Table tabFACT. */
  public static final String tabFACT = "fact";
  /** Table tabHiF. */
  public static final String tabHiF = "hierarchy_in_fact";
  /** Table tabHIERARCHY. */
  public static final String tabHIERARCHY = "hierarchy";
  /** Table tabLEVEL. */
  public static final String tabLEVEL = "level";
  /** Table tabMEMBER. */
  public static final String tabMEMBER = "member";
  /** Table tabMEASURE. */
  public static final String tabMEASURE = "measure";
  /** Table tabGROUPBYOPERATOR. */
  public static final String tabGROUPBYOPERATOR = "groupbyoperator";
  /** Table tabSYNONYM. */
  public static final String tabSYNONYM = "synonym";
  /** Table tabGRBYOPMEASURE. */
  public static final String tabGRBYOPMEASURE = "groupbyoperator_of_measure";
  /** Table tabLANGUAGEPREDICATE. */
  public static final String tabLANGUAGEPREDICATE = "language_predicate";
  /** Table tabLANGUAGEPREDICATE. */
  public static final String tabLANGUAGEOPERATOR = "language_operator";
  /** Table tabLEVELROLLUP. */
  public static final String tabLEVELROLLUP = "level_rollup";
  /** Table tabQuery. */
  public static final String tabQuery = "queries";
  /** Table tabQuery. */
  public static final String tabOLAPsession = "OLAPsession";
  /** List of table containing the synonyms column. */
  public static final Set<String> tabsWithSyns = //
      Sets.newHashSet(tabGROUPBYOPERATOR, tabFACT, tabLEVEL, tabMEASURE, tabMEMBER, tabLANGUAGEPREDICATE, tabLANGUAGEOPERATOR);

  public static final String colRELTAB1 = "table1";
  public static final String colRELTAB2 = "table2";
  public static final String colCOLISKEY = "isKey";
  public static final String colDBIP = "IPaddress";
  public static final String colDBPORT = "port";
  public static final String colLEVELCARD = "cardinality";
  public static final String colLEVELMIN = "min";
  public static final String colLEVELMAX = "max";
  public static final String colLEVELAVG = "avg";
  public static final String colLEVELMINDATE = "mindate";
  public static final String colLEVELMAXDATE = "maxdate";
  public static final String colLEVELRUSTART = "start";
  public static final String colLEVELRUTO = "level_to";
  public static final String colGROUPBYOPNAME = "operator";
  public static final String colSYNTERM = "term";
  public static final String colQueryID = "id";
  public static final String colQueryText = "query";
  public static final String colQueryGBset = "gc";
  public static final String colQuerySelClause = "sc";
  public static final String colQueryMeasClause = "mc";
  public static final String colQueryGPSJ = "gpsj";
  /****************************/

  private static Connection connMetaSchema;
  private static Connection connDataSchema;
  public static String metaDb = Utils.credentialsFromFile()[6];

  public static final Connection getConnection(final String schemadb) throws ClassNotFoundException, SQLException {
    final String ip = Utils.credentialsFromFile()[0];
    final String port = Utils.credentialsFromFile()[1];
    final String username = Utils.credentialsFromFile()[2];
    final String password = Utils.credentialsFromFile()[3];
    final String host = "jdbc:mysql://" + ip + ":" + port;
    final String schemaDBstringConnection = host + "/" + schemadb + "?serverTimezone=UTC&autoReconnect=true";
    Class.forName("com.mysql.cj.jdbc.Driver");
    return DriverManager.getConnection(schemaDBstringConnection, username, password);
  }

  /**
   * Close all database connections
   * @throws SQLException in case of error
   */
  public static void closeAllConnections() throws SQLException {
    if (connMetaSchema != null) {
      connMetaSchema.close();
    }
    if (connDataSchema != null) {
      connDataSchema.close();
    }
  }

  public static final Connection getMetaConnection() {
    try {
      if (connMetaSchema == null || connMetaSchema.isClosed()) {
        connMetaSchema = getConnection(Utils.credentialsFromFile()[6]);
      }
    } catch (final Exception | Error e) { // connection might have been timed out
      try {
        connMetaSchema = getConnection(Utils.credentialsFromFile()[6]);
      } catch (final Exception e1) {
        e1.printStackTrace();
        connMetaSchema = null;
      }
    }
    return connMetaSchema;
  }

  public static final Connection getDataConnection() {
    try {
      if (connDataSchema == null || connDataSchema.isClosed()) {
        connDataSchema = getConnection(Utils.credentialsFromFile()[4]);
      }
    } catch (final Exception | Error e) { // connection might have been timed out
      try {
        connDataSchema = getConnection(Utils.credentialsFromFile()[4]);
      } catch (final Exception e1) {
        e1.printStackTrace();
        connDataSchema = null;
      }
    }
    return connDataSchema;
  }

  /**
   * Execute the query and return a result.
   * @param query query to execute
   */
  public static final void executeQuery(final String query) {
    try (PreparedStatement pstmt = getMetaConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
      pstmt.executeUpdate();
    } catch (final SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(query + "\n" + e.getMessage());
    }
  }

  /**
   * Execute the query and return an integer.
   * @param query query to execute
   * @return integer
   */
  public static final int executeQueryReturnID(final String query) {
    try (PreparedStatement pstmt = getMetaConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
      pstmt.executeUpdate();
      final ResultSet generatedKeys = pstmt.getGeneratedKeys();
      generatedKeys.next();
      return generatedKeys.getInt(1);
    } catch (final SQLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(query + "\n" + e.getMessage());
    }
  }
}
