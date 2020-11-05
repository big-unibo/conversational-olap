package it.unibo.conversational.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unibo.conversational.Utils;
import it.unibo.conversational.Utils.DataType;

/**
 * Read the data from the fact database.
 */
public class DBreader {
  private static final Logger L = LoggerFactory.getLogger(DBreader.class);
  private static final int TSHCATEGORY = 30;

  private final String fact;
  private final DBLoader dbloader;
  private int factID = -1;
  private final String ip;
  private final int port;
  private final String factdb;
  private final String username;
  private final String password;
  private final String host;
  private final String dataDBstringConnection;

  /**
   * Initialize the database reader and loader.
   * @param ip ip
   * @param port port
   * @param username username
   * @param password password
   * @param indb fact database
   * @param factTable name of the fact table
   * @param schemadb where to load the metadataset
   * @throws Exception in case of error
   */
  public DBreader(final String ip, final String port, final String username, final String password, final String indb, final String factTable, final String schemadb) throws Exception {
    this.fact = factTable;
    this.ip = ip;
    this.port = Integer.parseInt(port);
    this.username = username;
    this.password = password;
    this.factdb = indb;
    host = "jdbc:mysql:// " + ip + ":" + port;
    dataDBstringConnection = host + "/" + factdb + "?serverTimezone=UTC";
    dbloader = new DBLoader();
  }

  private void analyzeTable(final DatabaseMetaData dataDBMetaData, final String tabName, final int hieID, final int tableID, final int hieTableID) {
    L.debug("Table: " + tabName);
    final List<String> pks = new ArrayList<>();
    final List<String> fks = new ArrayList<>();
    final List<String> fksRefersTo = new ArrayList<String>();
    try (Connection connDataDB = DriverManager.getConnection(dataDBstringConnection, username, password)) {
      // Salvo chiavi primarie ed esterne
      final ResultSet resK = dataDBMetaData.getPrimaryKeys(factdb, null, tabName);
      while (resK.next()) {
        pks.add(resK.getString("COLUMN_NAME"));
      }
      resK.close();

      final ResultSet resFK = dataDBMetaData.getImportedKeys(factdb, null, tabName);
      while (resFK.next()) {
        fks.add(resFK.getString("FKCOLUMN_NAME"));
        fksRefersTo.add(resFK.getString("PKTABLE_NAME"));
      }
      resFK.close();

      dbloader.insertLevel("all_" + tabName, JDBCType.NULL.getName(), hieID, tableID, tabName);
      // Leggo tutte le colonne della tabella
      ResultSet resC = dataDBMetaData.getColumns(factdb, null, tabName, null);
      while (resC.next()) {
        final String colType;
        final String colName = resC.getString("COLUMN_NAME");
        // Se non sono chiavi esterne le salvo come livello delle gerarchia e come colonne
        if (!fks.contains(colName)) {
          int numtype = resC.getInt("DATA_TYPE");
          colType = JDBCType.valueOf(numtype).getName();

          final DataType dt = Utils.getDataType(JDBCType.valueOf(numtype));
          // Nome gerarchia = nome tabella
          final int idLevel = dbloader.insertLevel(colName, colType, hieID, tableID, pks.contains(colName) ? tabName : null);

          Statement stmt = connDataDB.createStatement();
          Statement stmt1 = connDataDB.createStatement();
          String sql = "SELECT distinct(" + colName + ") FROM " + tabName + ";";
          ResultSet res = stmt1.executeQuery(sql);
          res.last();
          int card = res.getRow();
          res.beforeFirst();

          // In base al tipo del livello salvo informazioni diverse
          if (dt.equals(DataType.STRING)) { // I livelli di tipo testuale li considero come categorici quindi ne salvo i possibili valori
            dbloader.insertCategoricalMembers(res, colName, idLevel);
          } else if (dt.equals(DataType.NUMERIC)) {
            // Se è di tipo numerico ma ha una cardinalità elevata lo considero come categorico

            sql = "SELECT min(" + colName + ") AS v FROM " + tabName + ";";
            ResultSet resI = stmt.executeQuery(sql);
            resI.first();
            double min = resI.getDouble("v");
            sql = "SELECT max(" + colName + ") AS v FROM " + tabName + ";";
            resI = stmt.executeQuery(sql);
            resI.first();
            double max = resI.getDouble("v");
            sql = "SELECT avg(" + colName + ") AS v FROM " + tabName + ";";
            resI = stmt.executeQuery(sql);
            resI.first();
            double avg = resI.getDouble("v");

            if (card > TSHCATEGORY) {
              dbloader.modifyNumericLevel(colName, idLevel, min, max, avg, card);
            } else {
              dbloader.modifyNumericLevel(res, colName, idLevel, min, max, avg, card);
            }
          } else if (dt.equals(DataType.DATE)) {
            sql = "SELECT min(" + colName + ") AS v FROM " + tabName + ";";
            ResultSet resI = stmt.executeQuery(sql);
            resI.first();
            Date min = resI.getDate("v");
            sql = "SELECT max(" + colName + ") AS v FROM " + tabName + ";";
            resI = stmt.executeQuery(sql);
            resI.first();
            Date max = resI.getDate("v");
            dbloader.modifyDateLevel(colName, idLevel, max, min, card);
          } else {
            // Se è di un altro tipo salvo solo la cardinalita'
            res.last();
            int n = res.getRow();
            dbloader.updateLevelCardinality(idLevel, n);
          }
        } else {
          // Se è una chiave esterna lo salvo solamente come colonna
          int numtype = resC.getInt("DATA_TYPE");
          colType = JDBCType.valueOf(numtype).getName();
          dbloader.insertColumn(colName, colType, Optional.of(tableID), true);
        }
      }
    } catch (final SQLException e) {
      e.printStackTrace();
    }

  }

  /**
   * Metodo che carica i dati e i meta-dati del cubo nel nuovo database.
   */
  public void loadDataAndMetadata() {
    final List<String> pks = new ArrayList<>();
    final List<String> fks = new ArrayList<>();
    final List<String> fksRefersTo = new ArrayList<String>();
    try (Connection connDataDB = DriverManager.getConnection(dataDBstringConnection, username, password)) {
      // Salvo le informazioni relative al database
      int dbid = dbloader.insertDatabase(ip, factdb, port);
      DatabaseMetaData dataDBMetaData = connDataDB.getMetaData();
      // Salvo i dettagli della fact table
      ResultSet resT = dataDBMetaData.getTables(factdb, null, fact, new String[] { "TABLE" });
      resT.next();
      String tabName = resT.getString("TABLE_NAME");
      factID = dbloader.insertFactTable(tabName, dbid);
      // Salvo le chiavi primarie ed esterne della FT
      final ResultSet resK = dataDBMetaData.getPrimaryKeys(factdb, null, tabName);
      while (resK.next()) {
        pks.add(resK.getString("COLUMN_NAME"));
      }
      resK.close();
      if (pks.isEmpty()) {
        throw new IllegalArgumentException("Fact has no primary key (i.e., no dimensions)");
      }
      final ResultSet resFK = dataDBMetaData.getImportedKeys(factdb, null, tabName);
      while (resFK.next()) {
        fks.add(resFK.getString("FKCOLUMN_NAME"));
        fksRefersTo.add(resFK.getString("PKTABLE_NAME"));
      }
      resFK.close();
      if (fks.isEmpty()) {
        throw new IllegalArgumentException("Fact has no foreign keys(i.e., is not connected to any dimension table)");
      }
      // Leggo tutte le colonne della FT
      final ResultSet resC = dataDBMetaData.getColumns(factdb, null, tabName, null);
      while (resC.next()) {
        final String colType;
        final String colName = resC.getString("COLUMN_NAME");
        // Se sono chiavi primarie le salvo solo come colonne, altrimenti sono misure
        if (!pks.contains(colName)) {
          int numtype = resC.getInt("DATA_TYPE");
          colType = JDBCType.valueOf(numtype).getName();
          dbloader.insertMeasure(colName, colType, factID);
        } else {
          int numtype = resC.getInt("DATA_TYPE");
          colType = JDBCType.valueOf(numtype).getName();
          dbloader.insertColumn(colName, colType, Optional.empty(), true);
        }
      }
      resC.close();
      // La chiave del fatto è l'insieme di tutte le sue chiavi esterne
      for (String pk : pks) {
        int hieID;
        int hieTableID;
        String tableRef;
        if (fks.contains(pk)) {
          // Ogni chiave esterna nel fatto da inizio a una nuova gerarchia
          int idx = fks.indexOf(pk);
          tableRef = fksRefersTo.get(idx);
          Pair<Integer, Integer> res = dbloader.insertHierarchy(tableRef, factID, pk);
          hieID = res.getLeft();
          hieTableID = res.getRight();
          // Analizzo poi tutte le tabelle collegate al fatto
          analyzeTable(dataDBMetaData, tableRef, hieID, hieTableID, hieTableID);
        } else {
          L.error("Caso che non so gestire con " + pk);
        }
      }

      dbloader.loadStaticSynonyms(); //
    } catch (final SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Run the db reader.
   * @param args arguments
   * @throws Exception in case of error
   */
  public static void main(final String[] args) throws Exception {
    final String[] c = Utils.credentialsFromFile();
    new DBreader(c[0], c[1], c[2], c[3], c[4], c[5], c[6]).loadDataAndMetadata();
  }
}
