package test;

import com.google.common.collect.Lists;
import it.unibo.conversational.database.Config;
import it.unibo.conversational.database.Cube;
import it.unibo.conversational.database.DBmanager;
import it.unibo.conversational.datatypes.Entity;
import it.unibo.conversational.datatypes.Ngram;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Test;

import java.util.List;

import static it.unibo.conversational.Utils.string2ngram;
import static it.unibo.conversational.database.DBmanager.*;
import static it.unibo.conversational.database.DBsynonyms.*;
import static it.unibo.conversational.database.QueryGenerator.*;
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
            for (final Cube cube : Config.getCubes()) {
                assertTrue(syns(cube).containsKey(Lists.newArrayList("sum")));
                assertFalse(searchBKtree(cube, string2ngram("year"), 0.6).isEmpty());
                assertFalse(searchBKtree(cube, string2ngram("the_year"), 0.6).isEmpty());
                assertFalse(searchBKtree(cube, string2ngram("the_month"), 0.6).isEmpty());
                assertFalse(getEntities(cube, Ngram.class, Lists.newArrayList("sum"), 1.0, 1.0, 1, 1).isEmpty());
                assertFalse(getOperatorOfMeasure(cube).isEmpty());
                closeAllConnections();
                assertFalse(getMembersOfLevels(cube).isEmpty());
                closeAllConnections();
                assertFalse(getLevelsOfMembers(cube).isEmpty());
                closeAllConnections();
                assertFalse(getYearLevels(cube).isEmpty());
                assertFalse(getLevels(cube).isEmpty());
                assertFalse(searchBKtree(cube, string2ngram("drill down"), 0.4).isEmpty());
                assertEquals(1, searchBKtree(cube, string2ngram("="), 1).size());
                if (cube.getFactTable().equalsIgnoreCase("sales_fact_1997")) {
                    assertFalse(searchSequential(cube, string2ngram("atomic mints"), 1).isEmpty());
                    assertFalse(searchBKtree(cube, string2ngram("atomic mints"), 1).isEmpty());
                    assertFalse(searchBKtree(cube, string2ngram("unit sold"), 0.6).isEmpty());
                    assertFalse(searchBKtree(cube, string2ngram("UNIT_SALES"), 1).isEmpty());
                    assertFalse(searchBKtree(cube, string2ngram("unit_sales"), 1).isEmpty());
                    assertFalse(searchBKtree(cube, string2ngram("unit sales"), 1).isEmpty());
                    assertFalse(describeLevel(cube, "product_family", 5).isEmpty());
                    assertFalse(describeLevel(cube, "product_id", 5).isEmpty());
                    assertFalse(getTable(cube, "product_subcategory", "product_category").isEmpty());
                    assertFalse(getFunctionalDependency(cube, "product_subcategory", "product_category").isEmpty());
                    getLevel(cube, "the_year");
                } else if (cube.getFactTable().equalsIgnoreCase("lineorder2")) {
                    assertFalse(searchSequential(cube, string2ngram("supp cost"), 0.6).isEmpty());
                    assertFalse(searchSequential(cube, string2ngram("Apolonia Car"), 0.6).isEmpty());
                    assertFalse(searchBKtree(cube, string2ngram("supp cost"), 0.6).isEmpty());
                    assertFalse(searchBKtree(cube, string2ngram("Apolonia Car"), 0.6).isEmpty());
                }
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
        for (final Cube cube : Config.getCubes()) {
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
