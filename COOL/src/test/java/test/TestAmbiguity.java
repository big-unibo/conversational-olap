package test;

import com.google.common.collect.Lists;
import it.unibo.conversational.Validator;
import it.unibo.conversational.algorithms.Parser;
import it.unibo.conversational.database.Config;
import it.unibo.conversational.database.Cube;
import it.unibo.conversational.database.DBmanager;
import it.unibo.conversational.datatypes.Mapping;
import it.unibo.conversational.datatypes.Ngram;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/** Test the validation accuracy. */
public class TestAmbiguity {

  private final Cube cube = Config.getCube("sales_fact_1997");

  @AfterEach
  public void after() {
    Parser.resetIds();
  }

  private String ambiguitiesToString(final Mapping m) {
    return m.getAnnotatedNgrams().stream().map(Ngram::getAnnotations).collect(Collectors.toList()).toString();
  }

  private void checkEquals(final Mapping ambiguousMapping, final Mapping correctSentence, final boolean checkEquals) throws IOException {
    assertEquals(1, ambiguousMapping.ngrams.size());
    assertEquals(correctSentence.bestNgram.countNode(), ambiguousMapping.bestNgram.countNode());
    if (checkEquals) {
      assertEquals(1.0, correctSentence.similarity(ambiguousMapping), 0.001, correctSentence.toString() + "\n" + ambiguousMapping.toString());
    } else {
      System.out.println(ambiguousMapping.toStringTree());
      System.out.println(correctSentence.toStringTree());
    }
    final List<Ngram> ann = ambiguousMapping.getAnnotatedNgrams();
    assertTrue(ann.isEmpty(), ann.toString());
    try {
      DBmanager.executeDataQuery(cube, Parser.getSQLQuery(cube, ambiguousMapping), res -> {});
    } catch (final Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  private void checkQuery(final Mapping correctSentence, final String nlp, final int ambiguousNgrams, final List<Pair<String, String>> disambiguations) {
    checkQuery(correctSentence, nlp, ambiguousNgrams, disambiguations, true);
  }

  private void checkQuery(final Mapping correctSentence, final String nlp, final int ambiguousNgrams, final List<Pair<String, String>> disambiguations, final boolean checkEquals) {
    Parser.TEST = true;
    permutations(disambiguations).forEach(l -> {
      // System.out.println(l);
      Parser.resetIds();
      try {
        final Mapping ambiguousMapping = Validator.parseAndTranslate(cube, nlp);
        assertEquals(ambiguousNgrams, ambiguousMapping.getAnnotatedNgrams().size(), ambiguitiesToString(ambiguousMapping));
        for (Pair<String, String> d: l) {
          ambiguousMapping.disambiguate(d.getLeft(), d.getRight());
          try {
            new JSONObject(ambiguousMapping.toJSON(cube, nlp));
          } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
          }
        }
        checkEquals(ambiguousMapping, correctSentence, checkEquals);
      } catch (final Exception e) {
        e.printStackTrace();
        fail(e.getMessage());
      }
    });
    Parser.TEST = false;
  }

  private <E> List<List<E>> permutations(List<E> original) {
    if (original.isEmpty()) {
      final List<List<E>> result = new ArrayList<>();
      result.add(new ArrayList<>());
      return result;
    }
    final E firstElement = original.remove(0);
    final List<List<E>> returnValue = new ArrayList<>();
    final List<List<E>> permutations = permutations(original);
    for (List<E> smallerPermutated : permutations) {
      for (int index = 0; index <= smallerPermutated.size(); index++) {
        List<E> temp = new ArrayList<>(smallerPermutated);
        temp.add(index, firstElement);
        returnValue.add(temp);
      }
    }
    return returnValue;
  }

  private void test(final String nl, final String gc, final String sc, final String mc, final int expectedAnnotations) throws Exception {
    final Mapping ambiguousMapping = Validator.parseAndTranslate(cube, nl);
    new JSONObject(ambiguousMapping.toJSON(cube, nl));
    assertEquals(ambiguousMapping.getAnnotatedNgrams().size(), expectedAnnotations, ambiguitiesToString(ambiguousMapping));
    final Mapping correctSentence = Validator.getBest(cube, gc, sc, mc);
    new JSONObject(correctSentence.toJSON(cube));
    for (int i = 0; i < 4; i++) {
      Parser.automaticDisambiguate(ambiguousMapping);
      new JSONObject(ambiguousMapping.toJSON(cube, nl));
    }
    checkEquals(ambiguousMapping, correctSentence, true);
  }

//  /** Test disambiguation. */
//  @Test
//  public void test7() throws Exception {
//    test("unit sales by month in 2010 for Atomic Mints USA by store", "the_month, store_id", "the_year = 2010 and product_name = Atomic Mints and country = USA", "avg unit_sales");
//  }

  /** Test disambiguation.
   * @throws Exception in case of error 
   */
  @Test
  public void test01() throws Exception {
    test("sum unit sales by media type for USA", "media_type", "country = USA", "sum unit_sales", 1);
  }

  /** Test disambiguation.
   * @throws Exception in case of error 
   */
  @Test
  public void test02() throws Exception {
    test("sum unit sales by media type for USA and Mexico", "media_type", "country = USA and country = Mexico", "sum unit_sales", 2);
  }

  /** Test disambiguation.
   * @throws Exception in case of error 
   */
  @Test
  public void test03() throws Exception {
    test("sum unit sales by media type for Salem", "media_type", "city = Salem", "sum unit_sales", 1);
  }

  
  /** Test disambiguation.
   * @throws Exception in case of error 
   */
  @Test
  public void test04() throws Exception {
    test("unit sales for USA", "", "country = USA", "avg unit_sales", 2);
  }

  /** Test disambiguation.
   * @throws Exception in case of error 
   */
  @Test
  public void test05() throws Exception {
    test("unit sales for USA and state province Sheri Nowmere", "", "country = USA and state_province = BC", "avg unit_sales", 3);
  }

  /** Test disambiguation.
   * @throws Exception in case of error 
   */
  @Test
  public void test06() throws Exception {
    test("unit sales for USA and Sheri Nowmere as province", "", "country = USA and state_province = BC", "avg unit_sales", 3);
  }

  /** Test disambiguation.
   * @throws Exception in case of error 
   */
  @Test
  public void test08() throws Exception {
    test("unit sales in 2010 and Atomic Mints", "", "the_year = 2010 and product_name = Atomic Mints", "avg unit_sales", 1);
  }

  /** Test disambiguation.
   * @throws Exception in case of error 
   */
  @Test
  public void test09() throws Exception {
    test("unit sales for Sheri Nowmere", "", "fullname = Sheri Nowmer", "avg unit_sales", 1);
  }

  /** Test disambiguation.
   * @throws Exception in case of error 
   */
  @Test
  public void test10() throws Exception {
    test("sum unit sales by media type for USA country", "media_type", "country = USA", "sum unit_sales", 0);
  }

  /** Test disambiguation.
   * @throws Exception in case of error
   */
  @Test
  public void test11() throws Exception {
    final Mapping correctSentence = Validator.getBest(cube, "", "", "max unit_sales");
    checkQuery(correctSentence, "unit sales for Salem", 2, Lists.newArrayList(Pair.of("i0", "max"), Pair.of("i1", "drop")));
  }

  /** Test disambiguation.
   * @throws Exception in case of error 
   */
  @Test
  public void test12() throws Exception {
    test("by product_id unit_sales by product_category", "product_id", "", "avg unit_sales", 2);
  }

  /** Test disambiguation.
   * @throws Exception in case of error
   */
  @Test
  public void test13() throws Exception {
    final Mapping correctSentence = Validator.getBest(cube, "product_id, product_category", "", "avg unit_sales");
    checkQuery(correctSentence, "by product_id unit_sales by product_category", 2, Lists.newArrayList(Pair.of("i0", "avg"), Pair.of("u0", "add")));
  }

  /** Test disambiguation.
   * @throws Exception in case of error
   */
  @Test
  public void test14() throws Exception {
    final Mapping correctSentence = Validator.getBest(cube, "product_id", "", "avg unit_sales");
    checkQuery(correctSentence, "by product_id unit_sales by product_category", 2, Lists.newArrayList(Pair.of("i0", "avg"), Pair.of("u0", "drop")));
  }

  
  /** Test disambiguation.
   * @throws Exception in case of error
   */
  @Test
  public void test16() throws Exception {
    final Mapping correctSentence = Validator.getBest(cube, "", "", "max unit_sales");
    checkQuery(correctSentence, "unit sales store sales", 2, Lists.newArrayList(Pair.of("i0", "max"), Pair.of("i1", "drop")));
  }

  /** Test disambiguation.
   * @throws Exception in case of error 
   */
  @Test
  public void test17() throws Exception {
    test("store sales e store cost for USA", "", "country = USA", "avg store_sales, avg store_cost", 3);
  }

  /** Test tokenization.
   * @throws Exception in case of error 
   */
  @Test
  public void test18() throws Exception {
    test("total unit sales for gender=F", "", "gender = F", "sum unit_sales", 0);
  }

  /** Test tokenization.
   * @throws Exception in case of error 
   */
  @Test
  public void test19() throws Exception {
    test("count sales fact by customers", "customer_id", "", "count sales_fact_1997", 0);
  }

  /** Test disambiguation.
   * @throws Exception in case of error
   */
  @Test
  public void test20() throws Exception {
    final Mapping correctSentence = Validator.getBest(cube, "product_id", "", "avg unit_sales");
    checkQuery(correctSentence, "unit_sales by product_id store_cost", 2, Lists.newArrayList(Pair.of("i0", "avg"), Pair.of("u0", "drop")));
  }

  /** Test disambiguation.
   * @throws Exception in case of error
   */
  @Test
  public void test21() throws Exception {
    final Mapping correctSentence = Validator.getBest(cube, "product_id", "", "avg store_cost, avg unit_sales");
    checkQuery(correctSentence, "unit_sales by product_id store_cost", 2, Lists.newArrayList(Pair.of("i1", "avg"), Pair.of("i0", "avg"), Pair.of("u0", "add")), false);
  }

  /** Test disambiguation.
   * @throws Exception in case of error
   */
  @Test
  public void test22() throws Exception {
    final Mapping correctSentence = Validator.getBest(cube, "", "product_name = Atomic Mints", "avg unit_sales");
    checkQuery(correctSentence, "Atomic Mints unit_sales Atomic Mints", 2, Lists.newArrayList(Pair.of("i0", "avg"), Pair.of("u0", "drop")));
  }

  /** Test disambiguation.
   * @throws Exception in case of error
   */
  @Test
  public void test23() throws Exception {
    final Mapping correctSentence = Validator.getBest(cube, "", "product_name = Atomic Mints and product_name = Atomic Mints", "avg unit_sales");
    checkQuery(correctSentence, "Atomic Mints unit_sales Atomic Mints", 2, Lists.newArrayList(Pair.of("i0", "avg"), Pair.of("u0", "add")));
  }

  /** Test disambiguation.
   * @throws Exception in case of error
   */
  @Test
  public void test24() throws Exception {
    test("units sales by country for 2015 as year", "country", "the_year = 2015", "avg unit_sales", 1);
    test("sum unit sales by country for 2015 as year", "country", "the_year = 2015", "sum unit_sales", 0);
  }

  /** Test disambiguation.
   * @throws Exception in case of error
   */
  @Test
  public void test25() throws Exception {
    test("store sales for Sheri Nowmer as customer", "", "customer_id = -1", "avg store_sales", 2);
    test("store sales for 1 as fullname", "", "fullname = A. Catherine Binkley", "avg store_sales", 2);
  }

  /** Test disambiguation.
   * @throws Exception in case of error
   */
  @Test
  public void test26() throws Exception {
    test("max store sales by product family where occupation is usa and group by promotion name", "product_family", "occupation = Clerical", "max store_sales", 2);
  }

  /** Test disambiguation.
   * @throws Exception in case of error
   */
  @Test
  public void test27() throws Exception {
    test("sum store sales where category is new york", "", "", "sum store_sales", 0);
    test("sum store sales where category is Beer and wine", "", "product_category = Beer and Wine", "sum store_sales", 0);
  }

  /** Test disambiguation.
   * @throws Exception in case of error
   */
  @Test
  public void test28() throws Exception {
    test("sum store sales where occupation professional", "", "occupation = professional", "sum store_sales", 0);
    test("sum store sales with occupation professional", "", "occupation = professional", "sum store_sales", 0);
  }
}
