package test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unibo.conversational.database.*;
import it.unibo.conversational.datatypes.Entity;
import it.unibo.conversational.datatypes.Ngram;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static it.unibo.conversational.Utils.string2ngram;
import static it.unibo.conversational.database.DBmanager.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test database connection.
 */
public class TestConnection {
    private void count(final Cube cube, final String table) {
        DBmanager.executeMetaQuery(cube, "select * from `" + table + "`", res -> {
            assertTrue(res.first(), table + " is empty");
        });
    }

    private void count(final Cube cube, final String table, final int rows) {
        DBmanager.executeMetaQuery(cube, "select count(*) from `" + table + "`", res -> {
            assertTrue(res.next(), table + " is empty");
            assertEquals(res.getInt(1), rows, "Rows mismatch");
        });
    }

    /**
     * Test functions.
     *
     * @throws Exception in case of error
     */
    @Test
    public void testFunctions() {
        try {
            for (final Cube cube: Config.getCubes()) {
                assertTrue(QueryGenerator.syns(cube).containsKey(Lists.newArrayList("sum")));
                // Set<Triple<Entity, Double, String>> resBK = Sets.newLinkedHashSet(DBsynonyms.searchBKtree(cube, Lists.newArrayList("sum"), 0.6));
                // Set<Triple<Entity, Double, String>> resSeq = Sets.newLinkedHashSet(DBsynonyms.searchSequential(cube, Lists.newArrayList("sum"), 0.6));
                // assertEquals(resSeq.size(), resBK.size(), "Results mismatch");
                final List<Triple<Entity, Double, String>> resBK = DBsynonyms.searchBKtree(cube, Lists.newArrayList("drill", "down"), 0.4);
                assertFalse(DBsynonyms.searchBKtree(cube, string2ngram("year"), 0.6).isEmpty());
                assertFalse(DBsynonyms.searchBKtree(cube, string2ngram("the_year"), 0.6).isEmpty());
                assertFalse(DBsynonyms.searchBKtree(cube, string2ngram("the_month"), 0.6).isEmpty());
                assertFalse(DBsynonyms.getEntities(cube, Ngram.class, Lists.newArrayList("sum"), 1.0, 1.0, 1, 1).isEmpty());
                assertFalse(QueryGenerator.getOperatorOfMeasure(cube).isEmpty());
                DBmanager.closeAllConnections();
                assertFalse(QueryGenerator.getMembersOfLevels(cube).isEmpty());
                DBmanager.closeAllConnections();
                assertFalse(QueryGenerator.getLevelsOfMembers(cube).isEmpty());
                DBmanager.closeAllConnections();
                assertFalse(QueryGenerator.getYearLevels(cube).isEmpty());
                assertFalse(QueryGenerator.getLevels(cube).isEmpty());
                if (cube.getFactTable().equalsIgnoreCase("sales_fact_1997")) {
                    assertFalse(DBsynonyms.searchBKtree(cube, string2ngram("unit sold"), 0.6).isEmpty());
                    assertFalse(DBsynonyms.searchBKtree(cube, string2ngram("UNIT_SALES"), 0.6).isEmpty());
                    assertFalse(DBsynonyms.searchBKtree(cube, string2ngram("unit_sales"), 0.6).isEmpty());
                    assertFalse(DBsynonyms.searchBKtree(cube, string2ngram("unit sales"), 0.6).isEmpty());
                    assertFalse(QueryGenerator.describeLevel(cube, "product_family", 5).isEmpty());
                    assertFalse(QueryGenerator.describeLevel(cube, "product_id", 5).isEmpty());
                    assertFalse(QueryGenerator.getTable(cube, "product_subcategory", "product_category").isEmpty());
                    assertFalse(QueryGenerator.getFunctionalDependency(cube, "product_subcategory", "product_category").isEmpty());
                    QueryGenerator.getLevel(cube, "the_year");
                }
                DBsynonyms.getEntities(cube, Ngram.class, Lists.newLinkedList(), 1.0, 1.0, 1, 1);
            }
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test non empty tables.
     *
     * @throws Exception in case of error
     */
    @Test
    public void testNonEmptyTables() {
        for (final Cube cube: Config.getCubes()) {
            count(cube, tabTABLE);
            if (cube.getFactTable().equals("sales_fact_1997")) {
                count(cube, tabTABLE, 6);
            }
            count(cube, tabRELATIONSHIP);
            count(cube, tabCOLUMN);
            count(cube, tabDATABASE);
            count(cube, tabFACT);
            count(cube, tabHIF);
            count(cube, tabHIERARCHY);
            count(cube, tabLEVEL);
            count(cube, tabMEMBER);
            count(cube, tabMEASURE);
            count(cube, tabGROUPBYOPERATOR);
            count(cube, tabSYNONYM);
            count(cube, tabGRBYOPMEASURE);
            count(cube, tabLANGUAGEPREDICATE);
            count(cube, tabLANGUAGEOPERATOR);
            // count(tabLEVELROLLUP);
            // count(tabQUERY);
            // count(tabOLAPsession);
        }
    }
}
