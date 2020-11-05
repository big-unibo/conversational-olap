package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.describe.Describe;
import it.unibo.describe.DescribeExecute;

public class TestDescribe {

  private final static String path = "resources/describe/output/";

  @After
  public void after() {
    DescribeExecute.DEBUG = false;
  }

  @Before
  public void before() {
    DescribeExecute.DEBUG = true;
  }

  @Test
  public void drill() throws Exception {
    try {
      Describe d = DescribeExecute.parse("with sales_fact_1997 describe unit_sales, store_sales by product_category");
      DescribeExecute.execute(d, path);
      d = DescribeExecute.parse(d, "with sales_fact_1997 describe unit_sales, store_sales by product_subcategory");
      DescribeExecute.execute(d, path);
      d = DescribeExecute.parse(d, "with sales_fact_1997 describe unit_sales, store_sales by store_country");
      DescribeExecute.execute(d, path);
    } catch (final Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  public void rollup() throws Exception {
    try {
      Describe d = DescribeExecute.parse("with sales_fact_1997 describe unit_sales, store_sales by product_subcategory");
      DescribeExecute.execute(d, path);
      d = DescribeExecute.parse(d, "with sales_fact_1997 describe unit_sales, store_sales by product_category");
      DescribeExecute.execute(d, path);
      d = DescribeExecute.parse(d, "with sales_fact_1997 describe unit_sales, store_sales by store_country");
      DescribeExecute.execute(d, path);
    } catch (final Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void test01() throws Exception {
    try {
      Describe d = DescribeExecute.parse("with sales_fact_1997 describe unit_sales, store_sales by product_category");
      DescribeExecute.execute(d, path);
      d = DescribeExecute.parse(d, "with sales_fact_1997 describe unit_sales, store_sales for product_category = 'Bread' by product_category");
      DescribeExecute.execute(d, path);
    } catch (final Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  public void test02() {
    try {
      Describe d = DescribeExecute.parse("with sales_fact_1997 describe unit_sales, store_sales by product_category");
      DescribeExecute.execute(d, path);
      d = DescribeExecute.parse(d, "with sales_fact_1997 describe unit_sales, store_sales by product_family size 3");
      DescribeExecute.execute(d, path);
      d = DescribeExecute.parse(d, "with sales_fact_1997 describe unit_sales, store_sales by product_family size 4");
      DescribeExecute.execute(d, path);
    } catch (final Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  public void test03() {
    try {
      Describe d = DescribeExecute.parse("with sales_fact_1997 describe unit_sales, store_sales by the_month");
      DescribeExecute.execute(d, path);
      d = DescribeExecute.parse(d, "with sales_fact_1997 describe unit_sales, store_sales by the_date");
      DescribeExecute.execute(d, path);
      d = DescribeExecute.parse(d, "with sales_fact_1997 describe unit_sales, store_sales by the_year");
      DescribeExecute.execute(d, path);
      d = DescribeExecute.parse(d, "with sales_fact_1997 describe unit_sales, store_sales by the_date");
      DescribeExecute.execute(d, path);
    } catch (final Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  public void test04() {
    try {
      Describe d = DescribeExecute.parse("with sales_fact_1997 describe unit_sales, store_sales for quarter in ('Q1', 'Q2') by the_month");
      DescribeExecute.execute(d, path);
      d = DescribeExecute.parse("with sales_fact_1997 describe unit_sales, store_sales for the_date in ('1997-01-01', '1997-01-02', '1997-02-01') by the_date");
      DescribeExecute.execute(d, path);
      d = DescribeExecute.parse(d,
          "with sales_fact_1997 describe unit_sales, store_sales for the_date in ('1997-01-01', '1997-01-02', '1997-02-01') by gender");
      DescribeExecute.execute(d, path);
    } catch (final Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  public void test05() {
    try {
      Describe d = DescribeExecute.parse(
          "with sales_fact_1997 describe unit_sales for product_category in ('Fruit', 'Pizza') and gender in ('M', 'F') by product_subcategory size 2");
      DescribeExecute.execute(d, path);
      d = DescribeExecute.parse(d,
          "with sales_fact_1997 describe unit_sales for product_category in ('Fruit', 'Pizza') and gender in ('M', 'F') by product_category size 2");
      DescribeExecute.execute(d, path);
      d = DescribeExecute.parse(d,
          "with sales_fact_1997 describe unit_sales for product_category in ('Fruit', 'Pizza') and gender in ('M', 'F') by gender size 2");
      DescribeExecute.execute(d, path);
    } catch (final Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  public void test06() {
    try {
      Describe d = DescribeExecute
          .parse("with sales_fact_1997 describe unit_sales for product_category in ('Fruit', 'Pizza') and gender in ('M', 'F') by product_category size 2");
      DescribeExecute.execute(d, path);
      d = DescribeExecute.parse(d,
          "with sales_fact_1997 describe unit_sales for product_category in ('Fruit', 'Pizza') and gender in ('M', 'F') by product_subcategory size 2");
      DescribeExecute.execute(d, path);
      d = DescribeExecute.parse(d,
          "with sales_fact_1997 describe unit_sales for product_category in ('Fruit', 'Pizza') and gender in ('M', 'F') by gender size 2");
      DescribeExecute.execute(d, path);
    } catch (final Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  public void test07() {
    try {
      Describe d = DescribeExecute.parse("with sales_fact_1997 describe unit_sales by product_subcategory size 2");
      DescribeExecute.execute(d, path);
      assertEquals(0, d.getClause().size());
      d = DescribeExecute.parse(d, "with sales_fact_1997 describe unit_sales by gender size 2");
      DescribeExecute.execute(d, path);
      assertEquals(0, d.getClause().size());
      d = DescribeExecute.parse(d, "with sales_fact_1997 describe unit_sales for product_category in ('Pizza', 'Fruit') by gender size 2");
      assertEquals(1, d.getClause().size());
      DescribeExecute.execute(d, path);
      d = DescribeExecute.parse(d,
          "with sales_fact_1997 describe unit_sales for product_subcategory in ('Pizza', 'Canned Fruit', 'Canned-Fruit') by gender size 2");
      DescribeExecute.execute(d, path);
      assertEquals(1, d.getClause().size());
      d = DescribeExecute.parse(d, "with sales_fact_1997 describe unit_sales for gender in ('M') by gender size 2");
      DescribeExecute.execute(d, path);
      assertEquals(2, d.getClause().size());
    } catch (final Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  public void test08() {
    try {
      Describe d = DescribeExecute.parse(
          "with sales_fact_1997 describe unit_sales for the_month='1997-04' and product_category in ('Beer and Wine','Fruit','Meat','Bread') by product_subcategory");
      DescribeExecute.execute(d, path);
      d = DescribeExecute.parse(d,
          "with sales_fact_1997 describe unit_sales for the_month='1997-04' and product_category in ('Beer and Wine','Fruit','Meat','Bread') by product_category");
      DescribeExecute.execute(d, path);
    } catch (final Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  public void test09() {
    try {
//      Describe d;
//
//      d = DescribeExecute.parse("with COVID-19 describe deaths100k by countriesAndTerritories");
//      DescribeExecute.execute(d, path);
//
//      d = DescribeExecute.parse("with COVID-19 describe dEaThs for CoNtiNenTExp='Africa' by DaTeReP");
//      DescribeExecute.execute(d, path);
//      d = DescribeExecute.parse(d, "with COVID-19 describe dEaThs for CoNtiNenTExp='Africa' by MoNtH");
//      DescribeExecute.execute(d, path);
//
//      d = DescribeExecute.parse("with COVID-19 describe cases, deaths for daterep >= '04/01/2020' by month");
//      DescribeExecute.execute(d, path);
//
//
//      d = DescribeExecute.parse("with COVID-19 describe dEaThs for CoNtiNenTExp='Africa' by MoNtH");
//      DescribeExecute.execute(d, path);
//      d = DescribeExecute.parse(d, "with COVID-19 describe dEaThs for CoNtiNenTExp='Africa' by DaTeReP");
//      DescribeExecute.execute(d, path);
//
//      d = DescribeExecute.parse("with COVID-19 describe deaths for countriesandTerritories = 'France' by month");
//      DescribeExecute.execute(d, path);
//      d = DescribeExecute.parse("with COVID-19 describe deaths for countriesandTerritories = 'France' by countriesandTerritories");
//      DescribeExecute.execute(d, path);
//
//      d = DescribeExecute.parse("with COVID-19 describe deaths, cases by month");
//      DescribeExecute.execute(d, path);
//      d = DescribeExecute.parse(d, "with COVID-19 describe deaths, cases by geoId");
//      DescribeExecute.execute(d, path);
    } catch (final Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  public void testGrammar() {
    DescribeExecute.parse("with cube describe ma");
    DescribeExecute.parse("with cube describe ma, mb");
    DescribeExecute.parse("with cube describe ma, mb for c = -1");
    DescribeExecute.parse("with cube describe ma, mb for c = a");
    DescribeExecute.parse("with cube describe ma, mb for c >= 01/02/2001");
    DescribeExecute.parse("with cube describe ma, mb for c >= '01/02/2001'");
    DescribeExecute.parse("with cube describe ma, mb for c >= 01-01-2001");
    DescribeExecute.parse("with cube describe ma, mb for c >= '01-02-2001'");
    DescribeExecute.parse("with cube describe ma, mb for c >= -1.0");
    DescribeExecute.parse("with cube describe ma, mb for c >= a");
    DescribeExecute.parse("with cube describe ma, mb for c = TRUE");
    DescribeExecute.parse("with cube describe ma, mb for c = 1 by a");
    DescribeExecute.parse("with cube describe ma, mb for c = 1 by a using clustering");
    DescribeExecute.parse("with cube describe ma, mb for c = 1 by a using clustering, top-k");
    DescribeExecute.parse("with sales_fact_1997 describe unit_sales for customer_id = 10 and store_id = 11 and the_date = '1997-01-20' by customer_id");
  }
}
