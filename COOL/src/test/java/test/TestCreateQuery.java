package test;

import it.unibo.conversational.Utils;
import it.unibo.conversational.database.Config;
import it.unibo.conversational.database.Cube;
import it.unibo.conversational.database.DBmanager;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

/**
 * Test JSON to SQL translation.
 */
public class TestCreateQuery {
    private final Cube cube = Config.getCube("sales_fact_1997");

    /**
     * Convert and execute an SQL query
     *
     * @param json JSON object representing the query
     */
    private void execute(final String json) {
        final JSONObject obj = new JSONObject(json);
        DBmanager.executeDataQuery(cube, Utils.createQuery(cube, obj, true), res -> {
            //
        });
    }

    /**
     * Test a sentence.
     */
    @Test
    public void test1() {
        execute("{" +
                "\"GC\": [\"STORE_ID\", \"the_year\"]," +
                "\"MC\": [{\"AGG\": \"sum\", \"MEA\": \"unit_sales\"}, {\"AGG\": \"avg\", \"MEA\": \"unit_sales\"}]," +
                "\"SC\": [{\"ATTR\": \"product_id\", \"COP\": \"=\", \"VAL\": [\"1\"]}, {\"ATTR\": \"the_year\", \"COP\": \"=\", \"VAL\": [\"1998\"]}]" +
                "}");
    }

    /**
     * Test a sentence.
     */
    @Test
    public void test2() {
        execute("{\"GC\": [],\"MC\": [{\"AGG\": \"sum\", \"MEA\": \"unit_sales\"}],\"SC\": [{\"ATTR\": \"product_id\", \"COP\": \"=\", \"VAL\": [\"1\"]}, {\"ATTR\": \"the_year\", \"COP\": \"=\", \"VAL\": [\"1998\"]}]}");
    }

    /**
     * Test a sentence.
     */
    @Test
    public void test3() {
        execute("{\"GC\": [],\"MC\": [{\"AGG\": \"sum\", \"MEA\": \"unit_sales\"}],\"SC\": []}");
    }

    /**
     * Test a sentence.
     */
    @Test
    public void test4() {
        execute("{\"GC\": [\"STORE_ID\", \"the_year\"],\"MC\": [{\"AGG\": \"sum\", \"MEA\": \"unit_sales\"}, {\"AGG\": \"avg\", \"MEA\": \"unit_sales\"}],\"SC\": []}");
    }
}