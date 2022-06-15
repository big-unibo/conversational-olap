package it.unibo.conversational.database;

import com.google.common.collect.Lists;
import it.unibo.conversational.Utils;
import it.unibo.conversational.Utils.DataType;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static it.unibo.conversational.database.DBmanager.executeDataQuery;

/**
 * Read the data from the fact database.
 */
public class DBreader {
    private static final Logger L = LoggerFactory.getLogger(DBreader.class);
    private static final int TSHCATEGORY = 30;

    private final Cube c;
    private final DBLoader dbloader;
    private String factID = "-1";

    /**
     * Initialize the database reader and loader.
     *
     * @param cube cube
     */
    public DBreader(final Cube cube) {
        this.c = cube;
        dbloader = new DBLoader(cube);
    }

    private void analyzeTable(final DatabaseMetaData dataDBMetaData, final String tabName, final String hieID, final String tableID, final String hieTableID) throws SQLException {
        L.debug("Table: " + tabName);
        final List<String> pks = new ArrayList<>();
        final List<String> fks = new ArrayList<>();
        final List<String> fksRefersTo = new ArrayList<>();
        // Salvo chiavi primarie ed esterne
        final ResultSet resK = dataDBMetaData.getPrimaryKeys(c.getDataMart(), null, tabName);
        while (resK.next()) {
            pks.add(resK.getString("COLUMN_NAME"));
        }
        resK.close();

        final ResultSet resFK = dataDBMetaData.getImportedKeys(c.getDataMart(), null, tabName);
        while (resFK.next()) {
            fks.add(resFK.getString("FKCOLUMN_NAME"));
            fksRefersTo.add(resFK.getString("PKTABLE_NAME"));
        }
        resFK.close();

        dbloader.insertLevel("all_" + tabName, JDBCType.NULL.getName(), hieID, tableID, tabName);
        // Leggo tutte le colonne della tabella
        final ResultSet resC = dataDBMetaData.getColumns(c.getDataMart(), null, tabName, null);
        while (resC.next()) {
            final String colType;
            final String colName = resC.getString("COLUMN_NAME");
            // Se non sono chiavi esterne le salvo come livello delle gerarchia e come colonne
            if (!fks.contains(colName)) {
                int numtype = resC.getInt("DATA_TYPE");
                colType = JDBCType.valueOf(numtype).getName();

                final DataType dt = Utils.getDataType(JDBCType.valueOf(numtype));
                // Nome gerarchia = nome tabella
                final String idLevel = dbloader.insertLevel(colName, colType, hieID, tableID, pks.contains(colName) ? tabName : null);
                if (c.getImportMembers()) {
                    executeDataQuery(c, "SELECT distinct(`" + colName + "`) FROM " + tabName + ";", res -> {
                        res.last();
                        final int card = res.getRow();
                        res.beforeFirst();
                        final List<Triple<Double, Double, Double>> statAcc = Lists.newLinkedList();
                        switch (dt) {
                            case STRING:
                                dbloader.insertCategoricalMembers(res, colName, idLevel);
                                break;
                            case NUMERIC:
                                executeDataQuery(c, "SELECT min(`" + colName + "`) AS min, max(`" + colName + "`) AS max, avg(`" + colName + "`) AS avg FROM " + tabName + ";", resI -> {
                                    resI.first();
                                    final Double min = resI.getDouble("min");
                                    final Double max = resI.getDouble("max");
                                    final Double avg = resI.getDouble("avg");
                                    if (card > TSHCATEGORY) {
                                        dbloader.modifyNumericLevel(colName, idLevel, min, max, avg, card);
                                    } else {
                                        if (c.getImportMembers()) {
                                            dbloader.modifyNumericLevel(res, colName, idLevel, min, max, avg, card);
                                        }
                                    }
                                });
                                break;
                            case DATE:
                                executeDataQuery(c, "SELECT min(" + colName + "), max(" + colName + ") FROM " + tabName + ";", resI -> {
                                    resI.first();
                                    final Date min = resI.getDate(1);
                                    final Date max = resI.getDate(2);
                                    dbloader.modifyDateLevel(colName, idLevel, max, min, card);
                                });
                                break;
                            default:
                                // Se è di un altro tipo salvo solo la cardinalita'
                                res.last();
                                dbloader.updateLevelCardinality(idLevel, res.getRow());
                                break;
                        }
                    });
                }
            } else {
                // Se è una chiave esterna lo salvo solamente come colonna
                int numtype = resC.getInt("DATA_TYPE");
                colType = JDBCType.valueOf(numtype).getName();
                dbloader.insertColumn(colName, colType, Optional.of(tableID), true);
            }
        }
    }

    /**
     * Metodo che carica i dati e i meta-dati del cubo nel nuovo database.
     */
    public void loadDataAndMetadata() {
        final List<String> pks = new ArrayList<>();
        final List<String> fks = new ArrayList<>();
        final List<String> fksRefersTo = new ArrayList<>();
        try (Connection connDataDB = DBmanager.getDataConnection(c)) {
            // Salvo le informazioni relative al database
            String dbid = dbloader.insertDatabase(c.getIp(), c.getDataMart(), c.getPort());
            DatabaseMetaData dataDBMetaData = connDataDB.getMetaData();
            // Salvo i dettagli della fact table
            ResultSet resT = dataDBMetaData.getTables(c.getDataMart(), null, c.getFactTable().toUpperCase(), new String[]{"TABLE"});
            resT.next();
            String tabName = resT.getString("TABLE_NAME");
            factID = dbloader.insertFactTable(tabName, dbid);
            // Salvo le chiavi primarie ed esterne della FT
            final ResultSet resK = dataDBMetaData.getPrimaryKeys(c.getDataMart(), null, tabName);
            while (resK.next()) {
                pks.add(resK.getString("COLUMN_NAME"));
            }
            resK.close();
            if (pks.isEmpty()) {
                throw new IllegalArgumentException("Fact has no primary key (i.e., no dimensions)");
            }
            final ResultSet resFK = dataDBMetaData.getImportedKeys(c.getDataMart(), null, tabName);
            while (resFK.next()) {
                fks.add(resFK.getString("FKCOLUMN_NAME"));
                fksRefersTo.add(resFK.getString("PKTABLE_NAME"));
            }
            resFK.close();
            if (fks.isEmpty()) {
                throw new IllegalArgumentException("Fact has no foreign keys(i.e., is not connected to any dimension table)");
            }
            // Leggo tutte le colonne della FT
            final ResultSet resC = dataDBMetaData.getColumns(c.getDataMart(), null, tabName, null);
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
            for (String fk : fks) {
                String tableRef;
                if (pks.contains(fk)) {
                    // Ogni chiave esterna nel fatto da inizio a una nuova gerarchia
                    int idx = fks.indexOf(fk);
                    tableRef = fksRefersTo.get(idx);
                    Pair<String, String> res = dbloader.insertHierarchy(tableRef, factID, fk);
                    final String hieID = res.getLeft();
                    final String hieTableID = res.getRight();
                    // Analizzo poi tutte le tabelle collegate al fatto
                    analyzeTable(dataDBMetaData, tableRef, hieID, hieTableID, hieTableID);
                } else {
                    L.error(fk + " is not part of the primary key of the fact table " + pks.toString());
                }
            }
            dbloader.loadStaticSynonyms(c); //
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createOption(final Options options, final String... ss) {
        Arrays.stream(ss).forEach(s -> {
            Option db = new Option(s, s, true, s);
            db.setRequired(true);
            options.addOption(db);
        });
    }

    /**
     * Run the db reader.
     *
     * @param args arguments
     */
    public static void main(final String[] args) throws ParseException {
        if (args.length > 1) {
            Options options = new Options();
            createOption(options, "db", "ip", "port", "dbms", "user", "pwd", "ft");
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            final Cube cube = new Cube(cmd.getOptionValue("db"), cmd.getOptionValue("ft"), cmd.getOptionValue("dbms"), cmd.getOptionValue("ip"), Integer.parseInt(cmd.getOptionValue("port")), true, true);
            new DBreader(cube).loadDataAndMetadata();
        } else {
            for (Cube cube : Config.getCubes()) {
                if (cube.getCreate()) {
                    L.debug("-------------------------");
                    L.debug(cube.getFactTable().toUpperCase());
                    L.debug("-------------------------");
                    new DBreader(cube).loadDataAndMetadata();
                }
            }
        }
    }
}
