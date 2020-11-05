package test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unibo.conversational.Utils.DataType;
import it.unibo.conversational.Validator;
import it.unibo.conversational.algorithms.Mapper;
import it.unibo.conversational.algorithms.Parser;
import it.unibo.conversational.algorithms.Parser.Type;
import it.unibo.conversational.database.Config;
import it.unibo.conversational.database.Cube;
import it.unibo.conversational.datatypes.Entity;
import it.unibo.conversational.datatypes.Mapping;
import it.unibo.conversational.datatypes.Ngram;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import zhsh.Tree;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Test the validation accuracy. */
public class TestValidator {
  private final Cube cube = Config.getCube("sales_fact_1997");
  private int checkSentence(final String phrase, final String gbset, final String predicate, final String measures) throws Exception {
    return checkSentence(phrase, gbset, predicate, measures, Validator.THR_MEMBER, Validator.THR_META, Validator.N_SYNMEMBER, Validator.N_SYNMETA, Validator.THR_COVERAGE, Validator.THR_NGRAMDIST, Validator.NGRAM_SIZE, Validator.NGRAMSYNTHR);
  }

  private int checkSentence(final String query, final String gbset, final String predicate, final String measures, // query
      final double thrSimilarityMember, final double thrSimilarityMetadata, final int synMember, final int synMeta, // effectiveness
      final double percPhrase, final int maxDist, final int ngramSize, final double nGramSimThr) throws Exception { // pruning
    return new Validator().validate(cube, "test_java", -1, query, gbset, measures, predicate, thrSimilarityMember, thrSimilarityMetadata, synMember, synMeta, percPhrase, maxDist, 1, ngramSize, nGramSimThr, -1).getLeft();
  }

  /**
   * Test two tree and their distance.
   * @throws Exception in case of error
   */
  @Test
  public void test1() throws Exception {
    final Mapping s1 = Parser.parse(cube, //
        new Mapping(cube, //
            new Ngram("sum", Type.AGG, new Entity("sum"), Pair.of(2, 2)), // ;
            new Ngram("unit_sales", Type.MEA, new Entity("unit_sales"), Pair.of(3, 3)),
            new Ngram("by", Type.BY, new Entity("by"), Pair.of(0, 0)), //
            new Ngram("level", Type.ATTR, new Entity("level"), Pair.of(1, 1)), //
            new Ngram("the_year", Type.ATTR, new Entity("the_year"), Pair.of(4, 4)) //
        )).get();
    assertEquals(9, s1.countNodes(), s1.toString());
    final Mapping s2 = Parser.parse(cube, //
        new Mapping(cube, //
            new Ngram("sum", Type.AGG, new Entity("sum"), Pair.of(2, 2)), // ;
            new Ngram("unit_sales", Type.MEA, new Entity("unit_sales"), 0.4, Pair.of(3, 3)),
            new Ngram("by", Type.BY, new Entity("by"), Pair.of(0, 0)), //
            new Ngram("level", Type.ATTR, new Entity("level"), Pair.of(1, 1)))).get();
    assertEquals(7, s2.countNodes());
    assertEquals("GPSJ(MC(sum unitsales) GC(by level))", s2.toStringTree());
    assertEquals(2, Tree.ZhangShasha(s1.toStringTree(), s2.toStringTree()));
    System.out.println(Tree.ZhangShasha("GPSJ(MC(MC(sum storecost) MC(sum unitsales)) GC(by themonth))", "GPSJ(MC(sum storesales) GC(by themonth) SC(where SC(productcategory e vBeerandWine)))"));
    System.out.println(Tree.ZhangShasha("GPSJ(MC(MC(sum storecost) MC(sum unitsales)) GC(by themonth))", "GPSJ(MC(sum storesales) GC(by themonth) SC(where SC(productcategory e vBeer and Wine)))"));
  }

  @Test
  public void test10() throws Exception {
    final Ngram n1 = new Ngram("Sheri Nowmer", Type.VAL, new Entity("Sheri Nowmer", DataType.STRING), Pair.of(0, 0));
    final Ngram m = new Ngram(Type.SC, Lists.newArrayList(n1));
    assertEquals(1, m.children.size());
    Parser.infer(cube, new Mapping(cube, m));
    assertEquals(3, m.children.size());
  }

  @Test
  public void test11() throws Exception {
    final Ngram n1 = new Ngram("Salem", Type.VAL, new Entity("Salem", DataType.STRING), Pair.of(0, 0));
    final Ngram m = new Ngram(Type.SC, Lists.newArrayList(n1));
    assertEquals(1, m.children.size());
    Parser.infer(cube, new Mapping(cube, m));
    assertEquals(1, m.children.size());
  }

  @Test
  public void test12() throws Exception {
    final Mapping m = Validator.parseAndTranslate(cube, "sum unit sales in 2019", 1).get(0).getLeft();
    Parser.infer(cube, m);
  }

  @Test
  public void test13() throws Exception {
    final Mapping m = Validator.parseAndTranslate(cube, "sum unit sales for Sheri Nowmer", 1).get(0).getLeft();
    Parser.infer(cube, m);
  }

  @Test
  public void test14() throws Exception {
    // Validator.parseAndTranslate("sum store cst by media for Nowmer as customer");
    Validator.parseAndTranslate(cube, "store sales by month for club choc as product", 10);
  }

  @Test
  public void test15() throws Exception {
    Validator.parseAndTranslate(cube, "store sales by month in 2010 for Atomic Mints USA by store", 10);
  }

  @Test
  public void test2() throws Exception {
    assertEquals(0, checkSentence("number of unit sold by gender", "gender", "", "sum unit_sales"));
  }

  @Test
  public void test3() throws Exception {
    assertEquals(0, checkSentence("number of unit sold by gender and year", "gender, the_year", "", "sum unit_sales"));
  }

  @Test
  public void test4() throws Exception {
    assertEquals(0, checkSentence("sum store_cost by promotion_id", "promotion_id", "", "sum store_cost"));
    assertEquals(0, checkSentence("medium store cost by promotion_id", "promotion_id", "", "avg store_cost"));
    assertEquals(0, checkSentence("average store_cost by promotion_id", "promotion_id", "", "avg store_cost"));
    assertEquals(0, checkSentence("medium store cost by promotion", "promotion_id", "", "avg store_cost"));
  }

  // @Before
  // public void before() {
  // DBsynonyms.cleanCache();
  // }

  @Test
  public void test5() throws Exception {
    assertEquals(0, checkSentence("Units sold by the media type of the promotion", "media_type, promotion_id", "", "avg unit_sales"));
  }

  @Test
  public void test6() throws Exception {
    assertEquals(0, checkSentence("Average unit sales", "", "", "avg unit_sales"));
    assertEquals(0, checkSentence("Minimum unit sales", "", "", "min unit_sales"));
    assertEquals(0, checkSentence("Maximum unit sales", "", "", "max unit_sales"));
    assertEquals(0, checkSentence("Sum unit sales", "", "", "sum unit_sales"));
  }

  @Test
  public void test7() throws Exception {
    assertEquals(0, checkSentence("medium stre cost by promotion", "promotion_id", "", "avg store_cost")); // medium cost matches with store_cost
  }

  @Test
  public void test8() throws Exception {
    assertEquals(0, checkSentence("sum unit sales for year", "the_year", "", "sum unit_sales"));
    assertEquals(0, checkSentence("sum unit sales by year", "the_year", "", "sum unit_sales"));
    assertEquals(0, checkSentence("sum unit sales per year", "the_year", "", "sum unit_sales"));
    // assertEquals(4, checkSentence("sum unit sales in 2019", "", "the_year = 2019", "sum unit_sales"));
    // assertEquals(4, checkSentence("sum unit sales for 2019", "", "the_year = 2019", "sum unit_sales"));
  }

  @Test
  public void test9() throws Exception {
    final Ngram n1 = new Ngram("2019", Type.VAL, new Entity("2019", DataType.NUMERIC), Pair.of(0, 0));
    final Ngram m = new Ngram(Type.SC, Lists.newArrayList(n1));
    assertEquals(1, m.children.size());
    Parser.infer(cube, new Mapping(cube, m));
    assertEquals(3, m.children.size());
  }

  @Test
  public void testAmbiguous() throws Exception {
    assertEquals(0, checkSentence("unit sales by media type for USA", "media_type", "country = USA", "avg unit_sales"));
  }

  @Test
  public void testCorrectTree() throws Exception {
    assertEquals("GPSJ(MC(sum unitsales) GC(by gender))", Validator.getBest(cube, "gender", "", "sum unit_sales").toStringTree());
    // This is not GPSJ assertEquals("GPSJ(MC(count customerid) GC(by gender))", Validator.getBest("gender", "", "count customer_id").toStringTree());
    // This is not GPSJ assertEquals("GPSJ(MC(count customerid) GC(by gender) SC(theyear e v2019))", Validator.getBest("gender", "the_year = 2019", "count customer_id").toStringTree());
  }

  @Test
  public void testCount() throws Exception {
    // This is not GPSJ assertEquals("GPSJ(MC(count customerid) GC(by gender))", Validator.parseAndTranslate("count customer by gender", 1).get(0).getLeft().toStringTree());
    assertEquals("GPSJ(MC(count salesfact1997) GC(by gender))", Validator.parseAndTranslate(cube, "count sales fact 1997 by gender", 1).get(0).getLeft().toStringTree());
  }

  /**
   * Test the cardinality of the interpretations.
   * @throws Exception in case of error.
   */
  @Test
  public void testGen() throws Exception {
    assertEquals(7, Mapper.createMappings(cube, Ngram.class, "by gender year", 1, 0.5, 1, 1, 0, Integer.MAX_VALUE, 1, 1, Maps.newLinkedHashMap(), false).size());
    assertEquals(1, Mapper.createMappings(cube, Ngram.class, "media type", 1, 0.5, 1, 1, 0, Integer.MAX_VALUE, 2, 1, Maps.newLinkedHashMap(), false).size());
  }

  @Test
  public void testNotAmbiguous() throws Exception {
    assertEquals(0, checkSentence("unit sales by country by month by provice for Sheri Nowmer", "country, the_month, state_province", "fullname = Sheri Nowmer", "avg unit_sales")); // medium cost matches with store_cost
  }

  @Test
  public void testNotAmbiguous2() throws Exception {
    assertEquals(0, checkSentence("unit sales by media type for Sheri Nowmer", "media_type", "fullname = Sheri Nowmer", "avg unit_sales")); // medium cost matches with store_cost
  }

  @Test
  public void testNotAmbiguous3() throws Exception {
    // assertEquals(0, checkSentence("unit sales for country as USA", "", "country = USA", "avg unit_sales"));
    assertEquals(0, checkSentence("by the_month, store_id the_year = 2010 and product_name = Atomic Mints unit_sales", "the_month, store_id", "the_year = 2010 and product_name = Atomic Mints", "avg unit_sales")); // medium cost matches with store_cost
    assertEquals(0, checkSentence("product_name = Club Chocolate Milk store_sales", "", "product_name = Club Chocolate Milk", "avg store_sales"));
  }
}
