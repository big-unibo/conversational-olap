package it.unibo.conversational.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import it.unibo.conversational.Utils;

/**
 * Load metadata to the database.
 */
public final class DBLoader extends DBmanager {

  private static final Logger L = LoggerFactory.getLogger(DBmanager.class);
  private int factTABLEid;

  /**
   * Initialize the database loader.
   * @throws Exception in case of error
   */
  public DBLoader() {
    super();
  }

  //Metodo che aggiunge i roll-up transazionali tra i livelli
  private void addRollupTransational() {
    boolean mod = false;
    String queryAll, queryID, queryInsert;
    int idStart, idEnd, idApp;
    do {
      mod = false;
      queryAll = "SELECT * FROM `" + tabLEVELROLLUP + "`";
      try (
          Statement stmt = getMetaConnection().createStatement();
          ResultSet res = stmt.executeQuery(queryAll)
      ) {
        while (res.next()) {
          idStart = res.getInt(colLEVELRUSTART);
          idApp = res.getInt(colLEVELRUTO);
          queryID = "SELECT * FROM `" + tabLEVELROLLUP + "` WHERE `" + colLEVELRUSTART + "` = " + idApp;
          try (
              Statement stmtIn = getMetaConnection().createStatement();
              ResultSet resIn = stmtIn.executeQuery(queryID)
          ) {
            while (resIn.next()) {
              idEnd = resIn.getInt(colLEVELRUTO);
              queryID = "SELECT * FROM `" + tabLEVELROLLUP + "` WHERE `" + colLEVELRUSTART + "` = " + idStart + " AND `" + colLEVELRUTO + "` = " + idEnd;
              try (
                  Statement stmtAlready = getMetaConnection().createStatement();
                  ResultSet resAlready = stmtIn.executeQuery(queryID)
              ) {
                resAlready.next();
                if (resAlready.getRow() == 0) {
                  queryInsert = "INSERT INTO `" + tabLEVELROLLUP + "` (`" + colLEVELRUSTART + "`, `" + colLEVELRUTO + "`)" + " values(" + idStart + ", " + idEnd
                      + ")";
                  executeQuery(queryInsert);
                  L.debug("Inserito roll up");
                  mod = true;
                }
              } catch (final SQLException e) {
                e.printStackTrace();
              }
            }
          } catch (final SQLException e) {
            e.printStackTrace();
          }

        }
      } catch (final SQLException e) {
        e.printStackTrace();
      }
    } while (mod);
  }

  /* Metodo che aggiunge a un certo elemento (id) in una certa tabella (tabName)
   * i suoi sinonimi passati in input*/
  private void addWordSynonyms(final String tabName, final int id, final Set<String> syns) {
    if (syns.size() > 0) {
      JSONArray ja = new JSONArray();
      for (String s : syns) {
        ja.put(s);
      }
      String query = "UPDATE `" + tabName + "` SET `" + id(tabName) + "` = '" + ja + "' WHERE `" + synonyms(tabName) + "` = " + id;
      executeQuery(query);
    }
  }

//  /*Metodo che carica i sinonimi da WordNet e li salva nel db*/
//  private void findWordsSynonyms(String name, String tabName, String colIDname, String colSYNname, int id) {
//    Set<String> syns = WordNet.getWordSynonyms(name);
//    addWordSynonyms(tabName, colIDname, colSYNname, id, syns);
//  }

  public int insertFactTable(String tabName, int dbid) {
    String query = "SELECT * FROM `" + tabFACT + "` WHERE `" + name(tabFACT) + "` = \"" + tabName + "\"";
    int insertedKey;
    try (
        Statement stmt = getMetaConnection().createStatement();
        ResultSet res = stmt.executeQuery(query)
    ) {
      res.last();
      if (res.getRow() == 0) {
        //Se non è già presente inserisco il nome del fatto sia nella tabella FATTO che come TABELLA
        query = "INSERT INTO " + tabFACT + " (" + name(tabFACT) + ", " + id(tabDATABASE) + ")" + " values(\"" + tabName + "\", " + dbid + ");";
        insertedKey = executeQueryReturnID(query);
        L.debug("Inserted " + tabName + " in table \"" + tabFACT + "\" with the ID " + insertedKey);
        //findWordsSynonyms(tabName, tabFACT, id(tabFACT), colFACTSYNS, insertedKey);
        query = "INSERT INTO `" + tabTABLE + "` (`" + name(tabTABLE) + "`, `" + type(tabTABLE) + "`, `" + id(tabFACT) + "`)" + " values(\"" + tabName + "\", \"" + TableTypes.FT + "\", " + insertedKey + ");";
        factTABLEid = executeQueryReturnID(query);
        L.debug("Inserted " + tabName + " in table \"" + tabTABLE + "\"");
        return insertedKey;
      } else {
        res.beforeFirst();
        while (res.next()) {
          int oldID = res.getInt(id(tabFACT) + "");
          factTABLEid = oldID;
          L.debug("Il fatto " + tabName + "c'e' gia' con ID" + oldID);
          return oldID;
        }
      }
    } catch (final SQLException e) {
      System.out.println("Errore nell'inserimento del fatto: " + e.getMessage());
    }
    return -1;
  }

  public int insertColumn(String colName, String colType, Optional<Integer> tabID, boolean isKey) {
    int tableID;
    //Se non ho l'ID della tabella sto inserendo il fatto, 
    // di cui ho già memorizzato l'ID della tabella corrispondente
    if (!tabID.isPresent()) {
      tableID = factTABLEid;
    } else {
      tableID = tabID.get();
    }
    return insertColumn(colName, colType, isKey, tableID);
  }

  //Inserisco la colonna se non e' gia' presente
  private int insertColumn(final String colName, final String colType, final boolean isKey, final int tableID) {
    String query = "SELECT * FROM `" + tabCOLUMN + "` WHERE `" + name(tabCOLUMN) + "` = \"" + colName + "\" AND `" + id(tabTABLE) + "` = " + tableID;
    try (
        Statement stmt = getMetaConnection().createStatement();
        ResultSet res = stmt.executeQuery(query)
    ) {
      res.last();
      if (res.getRow() == 0) {
        query = "INSERT INTO `" + tabCOLUMN + "` (`" + name(tabCOLUMN) + "`, `" + id(tabTABLE) + "`, `" + type(tabCOLUMN) + "`, `" + colCOLISKEY + "`)" + " values(\"" + colName + "\", " + tableID + ", \"" + colType + "\", " + isKey + ");";
        int k = executeQueryReturnID(query);
        L.debug("Inserted " + colName + " in table \"" + tabCOLUMN + "\" with the ID " + k);
        return k;
      } else {
        res.first();
        L.debug(colName + " already exists " + res.getInt(id(tabCOLUMN)));
        return res.getInt(id(tabCOLUMN));
      }
    } catch (final SQLException e) {
      e.printStackTrace();
    }
    return -1;
  }

  public void insertMeasure(final String colName, final String colType, final int factID) {
    String query;
    // Prima salvo la colonna relativa alla misura nella tabella del fatto
    int columnKey = insertColumn(colName, colType, false, factTABLEid);
    query = "SELECT * FROM `" + tabMEASURE + "` WHERE `" + id(tabCOLUMN) + "` = " + columnKey;
    try (
        Statement stmt = getMetaConnection().createStatement();
        ResultSet res = stmt.executeQuery(query)
    ) {
      res.last();
      if (res.getRow() == 0) {
        query = "INSERT INTO `" + tabMEASURE + "` (`" + name(tabMEASURE) + "`, `" + id(tabFACT) + "`, `" + id(tabCOLUMN) + "`)" + " values(\"" + colName + "\", " + factID + ", " + columnKey + ");";
        L.debug(query);
        executeQuery(query);
      } else {
        res.first();
      }
      //findWordsSynonyms(colName, tabMEASURE, colMEASID, colMEASSYNS, measKey);
    } catch (final SQLException e) {
      e.printStackTrace();
    }
  }

  //Inserisco la tabella relativa alla gerarchia
  public int insertTableHierarchy(String name, int hieID, String fk, int idTableParent) {
    int keyTable = -1, keyRelationship = -1;
    String query = "SELECT * FROM `" + tabTABLE + "` WHERE `" + name(tabTABLE) + "` = \"" + name + "\" AND `" + id(tabHIERARCHY) + "` = " + hieID + ";";
    try (
        Statement stmt = getMetaConnection().createStatement();
        ResultSet res = stmt.executeQuery(query)
    ) {
      res.next();
      if (res.getRow() == 0) {
        query = "INSERT INTO `" + tabTABLE + "` (`" + name(tabTABLE) + "`, `" + type(tabTABLE) + "`, `" + id(tabHIERARCHY) + "`)" + " values(\"" + name + "\", \""
            + TableTypes.DT + "\", " + hieID + ");";
        keyTable = executeQueryReturnID(query);
        L.debug("Inserted " + name + " in table \"" + tabTABLE + "\"");
      } else {
        keyTable = res.getInt(id(tabTABLE));
      }
    } catch (SQLException e1) {
      System.out.println("Errore nell'inserimento della tabella: " + e1.getMessage());
    }

    //Inserisco la relazione tra la tabella (del fatto) e quella della gerarchia
    query = "SELECT * FROM `" + tabRELATIONSHIP + "` WHERE `" + colRELTAB1 + "` = " + idTableParent + " AND `" + colRELTAB2 + "` = " + keyTable + ";";
    try (
        Statement stmt = getMetaConnection().createStatement();
        ResultSet res = stmt.executeQuery(query)
    ) {
      res.next();
      if (res.getRow() == 0) {
        query = "INSERT INTO `" + tabRELATIONSHIP + "` (`" + colRELTAB1 + "`, `" + colRELTAB2 + "`)" + " values(" + idTableParent + ", " + keyTable + ");";
        keyRelationship = executeQueryReturnID(query);
        L.debug("Inserted relationship in table \"" + tabRELATIONSHIP + "\" between table " + idTableParent + " and " + keyTable);
      } else {
        keyRelationship = res.getInt(id(tabRELATIONSHIP));
      }
    } catch (SQLException e1) {
      System.out.println("Errore nell'inserimento della relazione fatto-tabella gerarchia: " + e1.getMessage());
    }
    //Aggiorno la colonne della chiave con l'id della relazione
    query = "UPDATE `" + tabCOLUMN + "` set `" + id(tabRELATIONSHIP) + "` = " + keyRelationship + " WHERE `" + name(tabCOLUMN) + "` = \"" + fk + "\" AND `" + id(tabTABLE) + "` = " + idTableParent;
    executeQuery(query);
    L.debug("Inserted relationship connection in table \"" + tabCOLUMN + "\" in the column " + fk);
    return keyTable;
  }

  public Pair<Integer, Integer> insertHierarchy(final String tabName, final int factID, final String fk) {
    String queryTab1, queryInsertTable;
    int keyHierarchy = -1, keyHierarchyTable;
    queryTab1 = "SELECT * FROM `" + tabHIERARCHY + "` WHERE `" + name(tabHIERARCHY) + "` = \"" + tabName + "\"";
    try (
        Statement stmt = getMetaConnection().createStatement();
        ResultSet res = stmt.executeQuery(queryTab1)
    ) {
      res.first();
      // Se non e' gia' presente inserisco i dettagli della gerarchia
      if (res.getRow() > 0) {
        keyHierarchy = res.getInt(id(tabHIERARCHY));
      } else {
        queryTab1 = "INSERT INTO " + tabHIERARCHY + " (" + name(tabHIERARCHY) + ")" + " values(\"" + tabName + "\");";
        keyHierarchy = executeQueryReturnID(queryTab1);
        L.debug("Inserted " + tabName + " in table \"" + tabHIERARCHY + "\" with the ID " + keyHierarchy);
      }
    } catch (final SQLException e) {
      System.out.println("Errore nell'inserimento della gerarchia: " + e.getMessage());
    }
    // findWordsSynonyms(tabName, tabHIERARCHY, id(tabHIERARCHY), colHIERARCHYSYNS, keyHierarchy);
    // Salvo, se non c'è già, l'appartenenza della gerarchia al fatto
    queryInsertTable = "SELECT * FROM `" + tabHiF + "` WHERE `" + id(tabFACT) + "` = " + factID + " AND `" + id(tabHIERARCHY) + "` = " + keyHierarchy;
    try (
        Statement stmt = getMetaConnection().createStatement();
        ResultSet res = stmt.executeQuery(queryInsertTable)
    ) {
      res.last();
      if (res.getRow() == 0) {
        queryInsertTable = "INSERT INTO `" + tabHiF + "` (`" + id(tabFACT) + "`, `" + id(tabHIERARCHY) + "`)" + " values(" + factID + ", " + keyHierarchy + ");";
        executeQuery(queryInsertTable);
        L.debug("Inserted connection in table \"" + tabHiF + "\"");
      }
    } catch (final SQLException e) {
      System.out.println("Errore nell'inserimento della connessione gerarchia-fatto: " + e.getMessage());
    }
    keyHierarchyTable = insertTableHierarchy(tabName, keyHierarchy, fk, factTABLEid);
    return Pair.of(keyHierarchy, keyHierarchyTable);
  }

  public int insertLevel(String name, String type, int hierarchyID, int tableID, String hierarchyName) {
    int keyLevel;
    // Prima salvo la colonna relativa al livello
    int colID = insertColumn(name, type, false, tableID);
    String query;
    if (colID > 0) {
      // poi salvo il livello in quanto tale
      query = "SELECT * FROM `" + tabLEVEL + "` WHERE `" + id(tabCOLUMN) + "` = " + colID;
      try (
          Statement stmt = getMetaConnection().createStatement();
          ResultSet res = stmt.executeQuery(query)
      ) {
        res.last();
        if (res.getRow() == 0) {
          query = "INSERT INTO `" + tabLEVEL + "` (`" + type(tabLEVEL) + "`, `" + name(tabLEVEL) + "`, `" + id(tabHIERARCHY) + "`, `" + id(tabCOLUMN) + "`)" + " values(\"" + type + "\", \"" + name + "\", " + hierarchyID + ", " + colID + ");";
          keyLevel = executeQueryReturnID(query);
          L.debug("Inserted level " + name + " in table \"" + tabLEVEL + "\"");
          if (hierarchyName != null) {
            // Add to the first level in hierarchy the synonyms from the hierarchy
            addWordSynonyms(tabLEVEL, keyLevel, Sets.newHashSet(hierarchyName)); // TODO if we want to get all the synonyms from the hierarchy it should be done here
          }
          return keyLevel;
        } else {
          res.first();
          L.debug("Il livello " + name + "c'e' gia' con " + res.getInt(id(tabLEVEL)));
          return res.getInt(id(tabLEVEL));
        }
      } catch (final SQLException e) {
        e.printStackTrace();
        System.out.println("Errore nell'inserimento del livello: " + e.getMessage());
      }
    }
    return -1;
  }

  // Per i livelli di tipo data memorizzo massimo, minimo oltre che lacardinalita'
  public void modifyDateLevel(String colName, int levelID, Date max, Date min, int c) {
    final String query = "UPDATE `" + tabLEVEL + "` set `" + colLEVELMINDATE + "` = '" + min + "' , `" + colLEVELMAXDATE + "` = '" + max + "', `" + colLEVELCARD + "` = " + c + " WHERE `" + id(tabLEVEL) + "` = " + levelID;
    executeQuery(query);
  }

  // Per i valori numerici oltre alla cardinalità salvo anche il massimo e il minimo
  public void modifyNumericLevel(String colName, int levelID, double min, double max, final double avg, final int c) {
    final String query = "UPDATE `" + tabLEVEL + "` set `" + colLEVELMIN + "` = " + Utils.DF.format(min) + ", `" + colLEVELMAX + "` = " + Utils.DF.format(max) + ", `" + colLEVELAVG + "` = " + Utils.DF.format(avg) + ", `" + colLEVELCARD + "` = " + c + " WHERE `" + id(tabLEVEL) + "` = " + levelID;
    executeQuery(query);
  }

  //Per i valori numerici oltre alla cardinalità salvo anche il massimo e il minimo
  // se sono categorici salvo anche tutti i possibili valori
  public void modifyNumericLevel(ResultSet values, String colName, int levelID, final double min, final double max, final double avg, final int c) {
    String query;
    try {
      while (values.next()) {
        double v = values.getDouble(colName);
        query = "SELECT * FROM `" + tabMEMBER + "` WHERE `" + name(tabMEMBER) + "` = \"" + v + "\" AND `" + id(tabLEVEL) + "` = " + levelID;
        try (
            Statement stmt = getMetaConnection().createStatement();
            ResultSet res = stmt.executeQuery(query)
        ) {
          res.last();
          if (res.getRow() == 0) {
            query = "INSERT INTO `" + tabMEMBER + "` (`" + name(tabMEMBER) + "`, `" + id(tabLEVEL) + "`)" + " values(" + v + ", " + levelID + ");";
            int nid = executeQueryReturnID(query);
            L.debug("Inserted " + v + " in table \"" + tabMEMBER + "\" with the ID " + nid);
          }
        } catch (final SQLException e) {
          e.printStackTrace();
        }
      }
      modifyNumericLevel(colName, levelID, min, max, avg, c);
    } catch (final SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Update level cardinality.
   * @param levelID level id
   * @param c cardinality increment
   */
  public void updateLevelCardinality(final int levelID, final int c) {
    executeQuery("UPDATE `" + tabLEVEL + "` set `" + colLEVELCARD + "` = " + c + " WHERE `" + id(tabLEVEL) + "` = " + levelID);
  }

  /**
   * Store members of a categorical level
   * @param values members
   * @param colName column name
   * @param levelID level id
   */
  public void insertCategoricalMembers(final ResultSet values, final String colName, final int levelID) {
    final String query = "INSERT INTO `" + tabMEMBER + "` (`" + name(tabMEMBER) + "`, `" + id(tabLEVEL) + "`)" + " values(?, ?)";
    L.debug(query);
    try {
      final List<String> acc = Lists.newLinkedList();
      while (values.next()) {
        final String s = values.getString(colName);
        if (s != null) {
          acc.add(s);
        }
      }
      values.close();
      try (PreparedStatement ps = DBmanager.getMetaConnection().prepareStatement(query)) {
        for (final String v : acc) {
          ps.setString(1, v);
          ps.setInt(2, levelID);
          ps.addBatch();
        }
        ps.executeBatch();
        updateLevelCardinality(levelID, acc.size()); // Modifico la cardinalita' del livello con i nuovi valori che ho aggiunto
      } catch (final SQLException e) {
        e.printStackTrace();
      }
    } catch (final SQLException e) {
      e.printStackTrace();
    }
  }

//  private int checkSynExixt(final String word) {
//    String query = "SELECT * FROM " + tabSYNONYM + " WHERE " + colSYNTERM + " = \"" + word + "\"";
//    try (
//        Statement stmt = getConnection().createStatement();
//        ResultSet res = stmt.executeQuery(query)
//    ) {
//      res.last();
//      if (res.getRow() > 0) {
//        return res.getInt(id(tabSYNONYM));
//      }
//    } catch (final SQLException e) {
//      e.printStackTrace();
//    }
//    return -1;
//  }

//  private int insertSynInTable(final String word, final int refID, final String table) {
//    final String query = "INSERT INTO `" + tabSYNONYM + "` (`" + colSYNTERM + "`, `" + name(tabTABLE) + "`, `reference_id`) values(\"" + word + "\", \"" + table + "\", " + refID + ")";
//    L.debug(query);
//    return executeQueryReturnID(query);
//  }

  /**
   * Nel DB legge righe con sinonimi, e poi li carica in un tabellone unico (sinonimo -> riferimento in db).
   * In tabelle level/member ho i riferimenti a attributi/membri (anche quelli senza sinonimi)
   */
  public void loadStaticSynonyms() {
    for (String tab : tabsWithSyns) {
      final String colID = id(tab);
      final String colSyn = synonyms(tab);
      final String colName = name(tab);
      final String query = "SELECT `" + colID + "`, `" + colName + "`, `" + colSyn + "` FROM `" + tab + "`";
      try (
          Statement stmt = getMetaConnection().createStatement();
          ResultSet syns = stmt.executeQuery(query);
          PreparedStatement ps = getMetaConnection().prepareStatement("INSERT INTO `" + tabSYNONYM + "` (`" + colSYNTERM + "`, `" + name(tabTABLE) + "`, `reference_id`) values(?, ?, ?)")
      ) {
        L.debug("INSERT INTO `" + tabSYNONYM + "` (`" + colSYNTERM + "`, `" + name(tabTABLE) + "`, `reference_id`) values(?, ?, ?)");
        while (syns.next()) {
          int refID = syns.getInt(colID);
          // Prima aggiungo il termine stesso
          String n = syns.getString(colName);

          ps.setString(1, n);
          ps.setString(2, tab);
          ps.setInt(3, refID);
          ps.addBatch();

          // Poi i suoi eventuali sinonimi
          String s = syns.getString(colSyn);
          if (s != null) {
            JSONArray js = new JSONArray(s);
            Iterator<Object> jsi = js.iterator();
            while (jsi.hasNext()) {
              ps.setString(1, (String) jsi.next());
              ps.setString(2, tab);
              ps.setInt(3, refID);
              ps.addBatch();
            }
          }
        }
        ps.executeBatch();
      } catch (final SQLException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Insert database.
   * @param ip ip address 
   * @param name database name
   * @param port port
   * @return database id
   */
  public int insertDatabase(final String ip, final String name, final int port) {
    String query = "INSERT INTO `" + tabDATABASE + "` (`" + name(tabDATABASE) + "`, `" + colDBIP + "`, `" + colDBPORT + "`) " + " values(\"" + name + "\", \"" + ip + "\", " + port + ");";
    int dbid = executeQueryReturnID(query);
    L.debug(query);
    return dbid;
  }
}
