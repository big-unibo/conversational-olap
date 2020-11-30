package it.unibo.conversational.database;

import com.google.common.collect.Lists;
import it.unibo.conversational.Utils;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static it.unibo.conversational.database.DBmanager.*;

/**
 * Load metadata to the database.
 */
public final class DBLoader {
    private static Long counter = 0L;
    private static final Logger L = LoggerFactory.getLogger(DBLoader.class);
    private final Cube cube;
    private String factTABLEid;

    public DBLoader(Cube cube) {
        this.cube = cube;
    }

    /* Metodo che aggiunge a un certo elemento (id) in una certa tabella (tabName)
     * i suoi sinonimi passati in input*/
    private void addWordSynonyms(final String tabName, final String id, final Set<String> syns) {
        if (syns.size() > 0) {
            JSONArray ja = new JSONArray();
            for (String s : syns) {
                ja.put(s);
            }
            String query = "UPDATE `" + tabName + "` SET `" + id(tabName) + "` = \"" + ja.toString() + "\" WHERE `" + synonyms(tabName) + "` = \"" + id + "\"";
            executeQuery(cube, query);
        }
    }

    public String insertFactTable(String tabName, String dbid) {
        final List<String> acc = Lists.newLinkedList();
        executeMetaQuery(cube, "SELECT * FROM `" + tabFACT + "` WHERE `" + name(tabFACT) + "` = \"" + tabName + "\"", res -> {
            res.last();
            if (res.getRow() == 0) { // Se non è già presente inserisco il nome del fatto sia nella tabella FATTO che come TABELLA
                final String insertedKey = executeQueryReturnID(cube, id(tabFACT), "INSERT INTO " + tabFACT + " (" + id(tabFACT) + ", " + name(tabFACT) + ", " + id(tabDATABASE) + ")" + " values(\"" + tabName + "\", \"" + tabName + "\", \"" + dbid + "\");");
                acc.add(insertedKey);
                factTABLEid = executeQueryReturnID(cube, id(tabTABLE), "INSERT INTO `" + tabTABLE + "` (`" + id(tabTABLE) + "`, `" + name(tabTABLE) + "`, `" + type(tabTABLE) + "`, `" + id(tabFACT) + "`)" + " values(\"" + tabName + "\", \"" + tabName + "\", \"" + TableTypes.FT + "\", \"" + insertedKey + "\");");
            } else {
                res.beforeFirst();
                while (res.next()) {
                    final String oldID = res.getString(id(tabFACT));
                    acc.add(oldID);
                    factTABLEid = oldID;
                }
            }
        });
        return acc.remove(0);
    }

    public String insertColumn(String colName, String colType, Optional<String> tabID, boolean isKey) {
        return insertColumn(colName, colType, isKey, tabID.orElseGet(() -> factTABLEid));
    }

    //Inserisco la colonna se non e' gia' presente
    private String insertColumn(final String colName, final String colType, final boolean isKey, final String tableID) {
        final List<String> acc = Lists.newLinkedList();
        executeMetaQuery(cube, "SELECT * FROM `" + tabCOLUMN + "` WHERE `" + name(tabCOLUMN) + "` = \"" + colName + "\" AND `" + id(tabTABLE) + "` = \"" + tableID + "\"", res -> {
            res.last();
            if (res.getRow() == 0) {
                acc.add(executeQueryReturnID(cube, id(tabCOLUMN), "INSERT INTO `" + tabCOLUMN + "` (`" + id(tabCOLUMN) + "`, `" + name(tabCOLUMN) + "`, `" + id(tabTABLE) + "`, `" + type(tabCOLUMN) + "`, `" + colCOLISKEY + "`)" + " values(\"" + (tableID + "_" + colName) + "\", \"" + colName + "\", \"" + tableID + "\", \"" + colType + "\", " + (isKey ? 1 : 0) + ");"));
            } else {
                res.first();
                acc.add(res.getString(id(tabCOLUMN)));
            }
        });
        return acc.remove(0);
    }

    public void insertMeasure(String colName, final String colType, final String factID) {
        // Prima salvo la colonna relativa alla misura nella tabella del fatto
        final String columnKey = insertColumn(colName, colType, false, factTABLEid);
        executeMetaQuery(cube, "SELECT * FROM `" + tabMEASURE + "` WHERE `" + id(tabCOLUMN) + "` = \"" + columnKey + "\"", res -> {
            res.last();
            if (res.getRow() == 0) {
                executeQuery(cube, "INSERT INTO `" + tabMEASURE + "` (`" + id(tabMEASURE) + "`, `" + name(tabMEASURE) + "`, `" + id(tabFACT) + "`, `" + id(tabCOLUMN) + "`)" + " values(\"" + colName + "\", \"" + colName + "\", \"" + factID + "\", \"" + columnKey + "\");");
            }
            //findWordsSynonyms(colName, tabMEASURE, colMEASID, colMEASSYNS, measKey);
        });
    }

    //Inserisco la tabella relativa alla gerarchia
    public String insertTableHierarchy(String name, String hieID, String fk, String idTableParent) {
        final List<String> acc = Lists.newLinkedList();
        executeMetaQuery(cube, "SELECT * FROM `" + tabTABLE + "` WHERE `" + name(tabTABLE) + "` = \"" + name + "\" AND `" + id(tabHIERARCHY) + "` = \"" + hieID + "\";", res -> {
            res.next();
            if (res.getRow() == 0) {
                acc.add(executeQueryReturnID(cube, id(tabTABLE), "INSERT INTO `" + tabTABLE + "` (`" + id(tabTABLE) + "`, `" + name(tabTABLE) + "`, `" + type(tabTABLE) + "`, `" + id(tabHIERARCHY) + "`)" + " values(\"" + name + "\", \"" + name + "\", \"" + TableTypes.DT + "\", \"" + hieID + "\");"));
            } else {
                acc.add(res.getString(id(tabTABLE)));
            }
        });
        final String keyTable = acc.remove(0);

        //Inserisco la relazione tra la tabella (del fatto) e quella della gerarchia
        executeMetaQuery(cube, "SELECT * FROM `" + tabRELATIONSHIP + "` WHERE `" + colRELTAB1 + "` = \"" + idTableParent + "\" AND `" + colRELTAB2 + "` = \"" + keyTable + "\";", res -> {
            res.next();
            if (res.getRow() == 0) {
                acc.add(executeQueryReturnID(cube, id(tabRELATIONSHIP), "INSERT INTO `" + tabRELATIONSHIP + "` (`" + id(tabRELATIONSHIP) + "`, `" + colRELTAB1 + "`, `" + colRELTAB2 + "`)" + " values(\"" + (idTableParent + "_" + keyTable) + "\", \"" + idTableParent + "\", \"" + keyTable + "\");"));
            } else {
                acc.add(res.getString(id(tabRELATIONSHIP)));
            }
        });
        final String keyRelationship = acc.remove(0);

        //Aggiorno la colonne della chiave con l'id della relazione
        executeQuery(cube, "UPDATE `" + tabCOLUMN + "` set `" + id(tabRELATIONSHIP) + "` = \"" + keyRelationship + "\" WHERE `" + name(tabCOLUMN) + "` = \"" + fk + "\" AND `" + id(tabTABLE) + "` = \"" + idTableParent + "\"");
        return keyTable;
    }

    public Pair<String, String> insertHierarchy(final String tabName, final String factID, final String fk) {
        final List<String> acc = Lists.newLinkedList();
        executeMetaQuery(cube, "SELECT * FROM `" + tabHIERARCHY + "` WHERE `" + name(tabHIERARCHY) + "` = \"" + tabName + "\"", res -> {
            res.first();
            if (res.getRow() > 0) { // Se non e' gia' presente inserisco i dettagli della gerarchia
                acc.add(res.getString(id(tabHIERARCHY)));
            } else {
                acc.add(executeQueryReturnID(cube, id(tabHIERARCHY), "INSERT INTO " + tabHIERARCHY + " (" + id(tabHIERARCHY) + ", " + name(tabHIERARCHY) + ")" + " values(\"" + tabName + "\", \"" + tabName + "\");"));
            }
        });
        final String keyHierarchy = acc.remove(0);

        // Salvo, se non c'è già, l'appartenenza della gerarchia al fatto
        executeMetaQuery(cube, "SELECT * FROM `" + tabHIF + "` WHERE `" + id(tabFACT) + "` = \"" + factID + "\" AND `" + id(tabHIERARCHY) + "` = \"" + keyHierarchy + "\"", res -> {
            res.last();
            if (res.getRow() == 0) {
                executeQuery(cube, "INSERT INTO `" + tabHIF + "` (`" + id(tabFACT) + "`, `" + id(tabHIERARCHY) + "`)" + " values(\"" + factID + "\", \"" + keyHierarchy + "\");");
            }
        });
        return Pair.of(keyHierarchy, insertTableHierarchy(tabName, keyHierarchy, fk, factTABLEid));
    }

    public String insertLevel(String name, String type, String hierarchyID, String tableID, String hierarchyName) {
        final List<String> result = Lists.newLinkedList();
        final String colID = insertColumn(name, type, false, tableID); // Prima salvo la colonna relativa al livello
        executeMetaQuery(cube, "SELECT * FROM `" + tabLEVEL + "` WHERE `" + id(tabCOLUMN) + "` = \"" + colID + "\"", res -> {
            res.last();
            if (res.getRow() == 0) {
                final String keyLevel = executeQueryReturnID(cube, id(tabLEVEL), "INSERT INTO `" + tabLEVEL + "` (`" + id(tabLEVEL) + "`, `" + type(tabLEVEL) + "`, `" + name(tabLEVEL) + "`, `" + id(tabHIERARCHY) + "`, `" + id(tabCOLUMN) + "`)" + " values(\"" + (hierarchyID + "_" + name) + "\", \"" + type + "\", \"" + name + "\", \"" + hierarchyID + "\", \"" + colID + "\");");
                // if (hierarchyName != null) { // Add to the first level in hierarchy the synonyms from the hierarchy
                //     addWordSynonyms(tabLEVEL, keyLevel, Sets.newHashSet(hierarchyName)); // TODO if we want to get all the synonyms from the hierarchy it should be done here
                // }
                result.add(keyLevel);
            } else {
                res.first();
                L.debug("Level " + name + " already exists: " + res.getInt(id(tabLEVEL)));
                result.add(res.getString(id(tabLEVEL)));
            }
        });
        return result.remove(0);
    }

    // Per i livelli di tipo data memorizzo massimo, minimo oltre che lacardinalita'
    public void modifyDateLevel(String colName, String levelID, Date max, Date min, int c) {
        executeQuery(cube, "UPDATE `" + tabLEVEL + "` set `" + colLEVELMINDATE + "` = " + Utils.toDate(cube, colName, "'" + min.toString() + "'") + " , `" + colLEVELMAXDATE + "` = " + Utils.toDate(cube, colName, "'" + max.toString() + "'") + ", `" + colLEVELCARD + "` = " + c + " WHERE `" + id(tabLEVEL) + "` = \"" + levelID + "\"");
    }

    // Per i valori numerici oltre alla cardinalità salvo anche il massimo e il minimo
    public void modifyNumericLevel(String colName, String levelID, double min, double max, final double avg, final int c) {
        executeQuery(cube, "UPDATE `" + tabLEVEL + "` set `" + colLEVELMIN + "` = " + Utils.DF.format(min) + ", `" + colLEVELMAX + "` = " + Utils.DF.format(max) + ", `" + colLEVELAVG + "` = " + Utils.DF.format(avg) + ", `" + colLEVELCARD + "` = " + c + " WHERE `" + id(tabLEVEL) + "` = \"" + levelID + "\"");
    }

    // Per i valori numerici oltre alla cardinalità salvo anche il massimo e il minimo. Se sono categorici salvo anche tutti i possibili valori
    public void modifyNumericLevel(ResultSet values, String colName, String levelID, final double min, final double max, final double avg, final int c) throws SQLException {
        while (values.next()) {
            double v = values.getDouble(colName);
            executeMetaQuery(cube, "SELECT * FROM `" + tabMEMBER + "` WHERE `" + name(tabMEMBER) + "` = \"" + v + "\" AND `" + id(tabLEVEL) + "` = \"" + levelID + "\"", res -> {
                res.last();
                if (res.getRow() == 0) {
                    executeQueryReturnID(cube, id(tabMEMBER), "INSERT INTO `" + tabMEMBER + "` (`" + id(tabMEMBER) + "`, `" + name(tabMEMBER) + "`, `" + id(tabLEVEL) + "`)" + " values(" + counter++ + "," + v + ", \"" + levelID + "\");");
                }
            });
        }
        modifyNumericLevel(colName, levelID, min, max, avg, c);
    }

    /**
     * Update level cardinality.
     *
     * @param levelID level id
     * @param c       cardinality increment
     */
    public void updateLevelCardinality(final String levelID, final int c) {
        executeQuery(cube, "UPDATE `" + tabLEVEL + "` set `" + colLEVELCARD + "` = " + c + " WHERE `" + id(tabLEVEL) + "` = \"" + levelID + "\"");
    }

    /**
     * Store members of a categorical level
     *
     * @param values  members
     * @param colName column name
     * @param levelID level id
     */
    public void insertCategoricalMembers(final ResultSet values, final String colName, final String levelID) throws SQLException {
        final List<String> acc = Lists.newLinkedList();
        while (values.next()) {
            final String s = values.getString(colName);
            if (s != null) {
                acc.add(s);
            }
        }
        values.close();
        insertMeta(cube, "INSERT INTO `" + tabMEMBER + "` (`" + id(tabMEMBER) + "`, `" + name(tabMEMBER) + "`, `" + id(tabLEVEL) + "`)" + " values(?, ?, ?)", ps -> {
            for (final String v : acc) {
                ps.setString(1, counter++ + "");
                ps.setString(2, Objects.requireNonNull(v));
                ps.setString(3, Objects.requireNonNull(levelID));
                ps.addBatch();
            }
            ps.executeBatch();
            updateLevelCardinality(levelID, acc.size()); // Modifico la cardinalita' del livello con i nuovi valori che ho aggiunto
        });
    }

    /**
     * Nel DB legge righe con sinonimi, e poi li carica in un tabellone unico (sinonimo -> riferimento in db).
     * In tabelle level/member ho i riferimenti a attributi/membri (anche quelli senza sinonimi)
     */
    public void loadStaticSynonyms(final Cube cube) {
        for (String tab : tabsWithSyns) {
            final String colID = id(tab);
            final String colSyn = synonyms(tab);
            final String colName = name(tab);
            executeMetaQuery(cube,
                    "SELECT `" + colID + "`, `" + colName + "`, `" + colSyn + "` FROM `" + tab + "`",
                    syns -> {
                        insertMeta(cube, "INSERT INTO `" + tabSYNONYM + "` (`" + id(tabSYNONYM) + "`, `" + colSYNTERM + "`, `" + name(tabTABLE) + "`, `REFERENCE_ID`) values(?, ?, ?, ?)", ps -> {
                            while (syns.next()) {
                                String refID = syns.getString(colID);
                                // Prima aggiungo il termine stesso
                                String n = syns.getString(colName);
                                // ps.setString(1, refID + "_" + n);
                                ps.setString(1, counter++ + "");
                                ps.setString(2, n);
                                ps.setString(3, tab);
                                ps.setString(4, refID);
                                ps.addBatch();
                                // Poi i suoi eventuali sinonimi
                                String s = syns.getString(colSyn);
                                if (s != null) {
                                    JSONArray js = new JSONArray(s);
                                    for (Object j : js) {
                                        ps.setString(1, counter++ + "");
                                        ps.setString(2, (String) j);
                                        ps.setString(3, tab);
                                        ps.setString(4, refID);
                                        ps.addBatch();
                                    }
                                }
                            }
                            ps.executeBatch();
                        });
                    });
        }
    }

    /**
     * Insert database.
     *
     * @param ip   ip address
     * @param name database name
     * @param port port
     * @return database id
     */
    public String insertDatabase(final String ip, final String name, final int port) {
        return executeQueryReturnID(cube, id(tabDATABASE), "INSERT INTO `" + tabDATABASE + "` (`" + id(tabDATABASE) + "`, `" + name(tabDATABASE) + "`, `" + colDBIP + "`, `" + colDBPORT + "`) " + " values(\"" + name + "\", \"" + name + "\", \"" + ip + "\", " + port + ");");
    }
}
