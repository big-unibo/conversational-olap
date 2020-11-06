package test;

import com.google.common.collect.Lists;
import it.unibo.conversational.database.*;
import it.unibo.conversational.datatypes.Ngram;
import org.junit.jupiter.api.Test;

import java.util.List;

import static it.unibo.conversational.database.DBmanager.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test database connection.
 */
public class TestConnection {
    private final Cube cube = Config.getCube("sales_fact_1997");

    private void count(final String table) {
        DBmanager.executeMetaQuery(cube, "select * from `" + table + "`", res -> {
            assertTrue(res.first(), table + " is empty");
        });
    }

    private void count(final String table, final int rows) {
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
            DBsynonyms.initSynonyms(cube);
            assertTrue(DBsynonyms.syns.containsKey(Lists.newArrayList("sum")));
            assertFalse(DBsynonyms.getEntities(cube, Ngram.class, Lists.newArrayList("sum"), 1.0, 1.0, 1, 1).isEmpty());
            assertFalse(QueryGenerator.getOperatorOfMeasure(cube).isEmpty());
            DBmanager.closeAllConnections();
            assertFalse(QueryGenerator.getMembersOfLevels(cube).isEmpty());
            DBmanager.closeAllConnections();
            assertFalse(QueryGenerator.getLevelsOfMembers(cube).isEmpty());
            DBmanager.closeAllConnections();
            assertFalse(QueryGenerator.getYearLevels().isEmpty());
            assertFalse(QueryGenerator.getLevels(cube).isEmpty());
            assertFalse(QueryGenerator.describeLevel(cube, "product_family", 5).isEmpty());
            assertFalse(QueryGenerator.describeLevel(cube, "product_id", 5).isEmpty());
            assertFalse(QueryGenerator.getTable(cube, "product_subcategory", "product_category").isEmpty());
            assertFalse(QueryGenerator.getFunctionalDependency(cube, "product_subcategory", "product_category").isEmpty());
            QueryGenerator.getLevel(cube, "the_year");
            DBsynonyms.getEntities(cube, Ngram.class, Lists.newLinkedList(), 1.0, 1.0, 1, 1);
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
        count(tabTABLE);
        if (cube.getFactTable().equals("sales_fact_1997")) {
            count(tabTABLE, 6);
        }
        count(tabRELATIONSHIP);
        count(tabCOLUMN);
        count(tabDATABASE);
        count(tabFACT);
        count(tabHIF);
        count(tabHIERARCHY);
        count(tabLEVEL);
        count(tabMEMBER);
        count(tabMEASURE);
        count(tabGROUPBYOPERATOR);
        count(tabSYNONYM);
        count(tabGRBYOPMEASURE);
        count(tabLANGUAGEPREDICATE);
        count(tabLANGUAGEOPERATOR);
        // count(tabLEVELROLLUP);
        // count(tabQUERY);
        // count(tabOLAPsession);
    }
}
