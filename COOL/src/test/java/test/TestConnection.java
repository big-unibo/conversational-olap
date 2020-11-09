package test;

import com.google.common.collect.Lists;
import it.unibo.conversational.database.*;
import it.unibo.conversational.datatypes.Ngram;
import org.junit.jupiter.api.Test;

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
                DBsynonyms.initSynonyms(cube);
                assertTrue(QueryGenerator.syns(cube).containsKey(Lists.newArrayList("sum")));
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
            fail();
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
