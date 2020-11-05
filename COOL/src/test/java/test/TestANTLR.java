package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.unibo.conversational.Validator;

/** Test the validation accuracy. */
public class TestANTLR {

  @Test
  public void test01() throws Exception {
    assertEquals("GPSJ(MC(sum unitsales) SC(where SC(fullname e vSheriNowmer)))", Validator.parseAndTranslate("sum unit_sales where fullname = Sheri Nowmer").toStringTree());
    assertEquals("GPSJ(MC(sum unitsales) SC(where SC(fullname e vSheriNowmer)))", Validator.parseAndTranslate("sum unit_sales where fullname Sheri Nowmer").toStringTree());
    assertEquals("GPSJ(MC(sum unitsales) SC(where SC(fullname e vSheriNowmer)))", Validator.parseAndTranslate("sum unit_sales where Sheri Nowmer").toStringTree());
    assertEquals("GPSJ(MC(sum unitsales) SC(where SC(fullname e vSheriNowmer)))", Validator.parseAndTranslate("sum unit_sales Sheri Nowmer").toStringTree());
  }

  @Test
  public void test02() throws Exception {
    Validator.parseAndTranslate("unit_sales by product_id sum unit_sales");
    Validator.parseAndTranslate("by product_id unit_sales by product_id");
    Validator.parseAndTranslate("product_id unit_sales by product_id foo unit_sales product_id = foo");
  }

  @Test
  public void test03() throws Exception {
    assertEquals("GPSJ(MC(sum unitsales) SC(where SC(SC(fullname e vSheriNowmer) and SC(fullname e vSheriNowmer))))", Validator.parseAndTranslate("sum unit_sales where fullname = Sheri Nowmer and fullname = Sheri Nowmer").toStringTree());
    assertEquals("GPSJ(MC(sum unitsales) GC(by productid) SC(where SC(fullname e vSheriNowmer)))", Validator.parseAndTranslate("sum unit_sales by product_id fullname equals Sheri Nowmer").toStringTree());
  }

  @Test
  public void test04() throws Exception {
    assertEquals("GPSJ(MC(unitsales))", Validator.parseAndTranslate("unit_sales").toStringTree());
    assertEquals("GPSJ(MC(sum unitsales))", Validator.parseAndTranslate("sum unit_sales").toStringTree());
    assertEquals("GPSJ(MC(sum unitsales) GC(by productid))", Validator.parseAndTranslate("sum unit_sales by product_id").toStringTree());
    assertEquals("GPSJ(MC(sum unitsales) GC(by productid))", Validator.parseAndTranslate("sum unit_sales by product_id").toStringTree());
    assertEquals("GPSJ(MC(sum unitsales) GC(GC(by productid) productid))", Validator.parseAndTranslate("sum unit_sales by product_id product_id").toStringTree());
  }

  @Test
  public void test05() throws Exception {
    assertEquals("GPSJ(MC(MC(sum unitsales) MC(sum unitsales)))", Validator.parseAndTranslate("sum unit_sales sum unit_sales").toStringTree());
  }

  @Test
  public void test06() throws Exception {
    assertEquals("GPSJ(MC(MC(unitsales) MC(unitsales)))", Validator.parseAndTranslate("unit_sales e unit_sales").toStringTree());
  }
}
