package test;

import static org.junit.Assert.fail;

import java.sql.Statement;

import org.json.JSONObject;
import org.junit.Test;

import it.unibo.conversational.algorithms.Parser;
import it.unibo.conversational.database.QueryGeneratorChecker;

/** Test JSON to SQL translation. */
public class TestCreateQuery {

  /**
   * Convert and execute an SQL query
   * @param json JSON object representing the query
   */
  private void execute(final String json) {
    final JSONObject obj = new JSONObject(json);
    // System.out.println(obj.toString(2));
    try (
        Statement stmt = QueryGeneratorChecker.getDataConnection().createStatement()
    ) {
      System.out.println(Parser.createQuery(obj));
      stmt.execute(Parser.createQuery(obj));
    } catch (final Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  /**
   * Test a sentence.
   */
  @Test
  public void test1() throws Exception {
    execute("{\"GC\": [\"store_id\", \"the_year\"],\"MC\": [{\"AGG\": \"sum\", \"MEA\": \"unit_sales\"}, {\"AGG\": \"avg\", \"MEA\": \"unit_sales\"}],\"SC\": [{\"ATTR\": \"product_id\", \"COP\": \"=\", \"VAL\": [\"1\"]}, {\"ATTR\": \"the_year\", \"COP\": \"=\", \"VAL\": [\"1998\"]}]}");
  }

  /**
   * Test a sentence.
   */
  @Test
  public void test2() throws Exception {
    execute("{\"GC\": [],\"MC\": [{\"AGG\": \"sum\", \"MEA\": \"unit_sales\"}],\"SC\": [{\"ATTR\": \"product_id\", \"COP\": \"=\", \"VAL\": [\"1\"]}, {\"ATTR\": \"the_year\", \"COP\": \"=\", \"VAL\": [\"1998\"]}]}");
  }

  /**
   * Test a sentence.
   */
  @Test
  public void test3() throws Exception {
    execute("{\"GC\": [],\"MC\": [{\"AGG\": \"sum\", \"MEA\": \"unit_sales\"}],\"SC\": []}");
  }

  /**
   * Test a sentence.
   */
  @Test
  public void test4() throws Exception {
    execute("{\"GC\": [\"store_id\", \"the_year\"],\"MC\": [{\"AGG\": \"sum\", \"MEA\": \"unit_sales\"}, {\"AGG\": \"avg\", \"MEA\": \"unit_sales\"}],\"SC\": []}");
  }
}