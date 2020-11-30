package test;

import it.unibo.conversational.Validator;
import it.unibo.conversational.database.Config;
import it.unibo.conversational.database.Cube;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test the validation accuracy.
 */
public class TestANTLR {

    private final Cube cube = Config.getCube("sales_fact_1997");

    @Test
    public void test01() throws Exception {
        assertEquals("GPSJ(MC(sum unitsales) SC(where SC(fullname e vSheriNowmer)))", Validator.parseAndTranslate(cube, "sum unit_sales where fullname = Sheri Nowmer").toStringTree());
        assertEquals("GPSJ(MC(sum unitsales) SC(where SC(fullname e vSheriNowmer)))", Validator.parseAndTranslate(cube, "sum unit_sales where fullname Sheri Nowmer").toStringTree());
        assertEquals("GPSJ(MC(sum unitsales) SC(where SC(fullname e vSheriNowmer)))", Validator.parseAndTranslate(cube, "sum unit_sales where Sheri Nowmer").toStringTree());
        assertEquals("GPSJ(MC(sum unitsales) SC(where SC(fullname e vSheriNowmer)))", Validator.parseAndTranslate(cube, "sum unit_sales Sheri Nowmer").toStringTree());
    }

    @Test
    public void test02() throws Exception {
        Validator.parseAndTranslate(cube, "unit_sales by product_id sum unit_sales");
        Validator.parseAndTranslate(cube, "by product_id unit_sales by product_id");
        Validator.parseAndTranslate(cube, "product_id unit_sales by product_id foo unit_sales product_id = foo");
    }

    @Test
    public void test03() throws Exception {
        assertEquals("GPSJ(MC(sum unitsales) SC(where SC(SC(fullname e vSheriNowmer) and SC(fullname e vSheriNowmer))))", Validator.parseAndTranslate(cube, "sum unit_sales where fullname = Sheri Nowmer and fullname = Sheri Nowmer").toStringTree());
        assertEquals("GPSJ(MC(sum unitsales) GC(by productid) SC(where SC(fullname e vSheriNowmer)))", Validator.parseAndTranslate(cube, "sum unit_sales by product_id fullname equals Sheri Nowmer").toStringTree());
    }

    @Test
    public void test04() throws Exception {
        assertEquals("GPSJ(MC(unitsales))", Validator.parseAndTranslate(cube, "unit_sales").toStringTree());
        assertEquals("GPSJ(MC(sum unitsales))", Validator.parseAndTranslate(cube, "sum unit_sales").toStringTree());
        assertEquals("GPSJ(MC(sum unitsales) GC(by productid))", Validator.parseAndTranslate(cube, "sum unit_sales by product_id").toStringTree());
        assertEquals("GPSJ(MC(sum unitsales) GC(by productid))", Validator.parseAndTranslate(cube, "sum unit_sales by product_id").toStringTree());
        assertEquals("GPSJ(MC(sum unitsales) GC(GC(by productid) productid))", Validator.parseAndTranslate(cube, "sum unit_sales by product_id product_id").toStringTree());
    }

    @Test
    public void test05() throws Exception {
        assertEquals("GPSJ(MC(MC(sum unitsales) MC(sum unitsales)))", Validator.parseAndTranslate(cube, "sum unit_sales sum unit_sales").toStringTree());
    }

    @Test
    public void test06() throws Exception {
        assertEquals("GPSJ(MC(MC(unitsales) MC(unitsales)))", Validator.parseAndTranslate(cube, "unit_sales e unit_sales").toStringTree());
    }
}
