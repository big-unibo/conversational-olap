package test;

import com.google.common.collect.Lists;
import it.unibo.conversational.database.*;
import it.unibo.conversational.datatypes.Ngram;
import org.junit.jupiter.api.Test;

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

    /**
     * Test functions.
     *
     * @throws Exception in case of error
     */
    @Test
    public void testFunctions() {
        try {
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
        count(DBmanager.tabTABLE);
        count(DBmanager.tabRELATIONSHIP);
        count(DBmanager.tabCOLUMN);
        count(DBmanager.tabDATABASE);
        count(DBmanager.tabFACT);
        count(DBmanager.tabHiF);
        count(DBmanager.tabHIERARCHY);
        count(DBmanager.tabLEVEL);
        count(DBmanager.tabMEMBER);
        count(DBmanager.tabMEASURE);
        count(DBmanager.tabGROUPBYOPERATOR);
        count(DBmanager.tabSYNONYM);
        count(DBmanager.tabLANGUAGEPREDICATE);
        count(DBmanager.tabLANGUAGEOPERATOR);
        count(DBmanager.tabGRBYOPMEASURE); // MUST BY POPULATED MANUALLY
        // count(DBmanager.tabLEVELROLLUP); MUST BY POPULATED MANUALLY
    }
}
