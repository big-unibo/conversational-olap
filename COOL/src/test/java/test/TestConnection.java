package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Test;

import com.google.common.collect.Lists;

import it.unibo.conversational.database.DBmanager;
import it.unibo.conversational.database.DBsynonyms;
import it.unibo.conversational.database.QueryGeneratorChecker;
import it.unibo.conversational.datatypes.Ngram;

/**
 * Test database connection.
 */
public class TestConnection {

  private void count(final String table) throws Exception {
    try (
        Statement stmt = DBmanager.getMetaConnection().createStatement();
        ResultSet res = stmt.executeQuery("select * from `" + table + "`")
    ) {
      assertTrue(table + " is empty", res.first());
    } catch (final Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  /**
   * Test functions.
   * @throws Exception in case of error
   */
  @Test
  public void testFunctions() {
    try {
      assertFalse(QueryGeneratorChecker.getOperatorOfMeasure().isEmpty());
      DBmanager.getMetaConnection().close();
      assertFalse(QueryGeneratorChecker.getMembersOfLevels().isEmpty());
      DBmanager.getMetaConnection().close();
      assertFalse(QueryGeneratorChecker.getLevelsOfMembers().isEmpty());
      DBmanager.getMetaConnection().close();
      assertFalse(QueryGeneratorChecker.getYearLevels().isEmpty());
      assertFalse(QueryGeneratorChecker.getLevels().isEmpty());
//      assertFalse(QueryGeneratorChecker.getMembers().isEmpty());
      assertFalse(QueryGeneratorChecker.describeLevel("product_family", 5).isEmpty());
      assertFalse(QueryGeneratorChecker.describeLevel("product_id", 5).isEmpty());
      assertFalse(QueryGeneratorChecker.getTable("product_subcategory", "product_category").isEmpty());
      assertFalse(QueryGeneratorChecker.getFunctionalDependency("product_subcategory", "product_category").isEmpty());
      QueryGeneratorChecker.getLevel("the_year");
      DBsynonyms.getEntities(Ngram.class, Lists.newLinkedList(), 1.0, 1.0, 1, 1);
    } catch (final Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  /**
   * Test non empty tables.
   * @throws Exception in case of error
   */
  @Test
  public void testNonEmptyTables() throws Exception {
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
