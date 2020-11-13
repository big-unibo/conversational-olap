package test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import edu.stanford.nlp.util.StringUtils;
import it.unibo.conversational.Utils;
import it.unibo.conversational.Utils.DataType;
import it.unibo.conversational.Validator;
import it.unibo.conversational.algorithms.Mapper;
import it.unibo.conversational.algorithms.MarriageProblem;
import it.unibo.conversational.algorithms.Parser;
import it.unibo.conversational.algorithms.Parser.Type;
import it.unibo.conversational.database.Config;
import it.unibo.conversational.database.Cube;
import it.unibo.conversational.datatypes.Entity;
import it.unibo.conversational.datatypes.Mapping;
import it.unibo.conversational.datatypes.Ngram;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import smile.math.distance.EditDistance;
import smile.math.distance.Metric;
import smile.neighbor.BKTree;
import smile.neighbor.Neighbor;
import zhsh.Tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests on parsing.
 *
 */
public class TestEnforcingRules {
  private final Cube cube = Config.getCube("sales_fact_1997");
  private static final double PRECISION = 0.001;
  /**
   * Test match between two string.
   */
  @Test
  public void testBestMatch() {
    final List<String> l1 = Lists.newArrayList("club", "chockolate", "milk");
    final List<String> l2 = Lists.newArrayList("chockolate", "club", "milk");
    final List<String> l3 = Lists.newArrayList("club", "chockolate");
    final Map<Integer, Integer> m3 = Maps.newHashMap();
    m3.put(0, 0);
    m3.put(1, 1);
    m3.put(2, 2);
    assertEquals(m3, MarriageProblem.getBestMatch(l1, l1));
    final Map<Integer, Integer> m1 = Maps.newHashMap();
    m1.put(0, 1);
    m1.put(1, 0);
    m1.put(2, 2);
    assertEquals(m1, MarriageProblem.getBestMatch(l1, l2));
    final Map<Integer, Integer> m2 = Maps.newHashMap();
    m2.put(0, 1);
    m2.put(1, 0);
    assertEquals(m2, MarriageProblem.getBestMatch(l2, l3));
  }

  @Test
  public void testBKtree() {
    final BKTree<String> tree = new BKTree<>(new EditDistance());
    tree.add(Lists.newArrayList("foo", "foo", "foa", "foobar"));
    List<Neighbor<String, String>> res = Lists.newArrayList();
    tree.range("boo", 1, res);
    assertEquals(Lists.newArrayList("foo"), res.stream().map(n -> n.value).collect(Collectors.toList()));
    res = Lists.newArrayList();
    tree.range("boo", 2, res);
    assertEquals(Lists.newArrayList("foo", "foa"), res.stream().map(n -> n.value).collect(Collectors.toList()));
  }

  @Test
  public void testBKtreeEntity() {
    final Metric<String> distance = new EditDistance();
    final BKTree<Entity> tree = new BKTree<>((x , y) -> distance.d(x.nameInTable(), y.nameInTable()));
    tree.add(Lists.newArrayList(new Entity("foo"), new Entity("foa"), new Entity("foobar")));
    List<Neighbor<Entity, Entity>> res = Lists.newArrayList();
    tree.range(new Entity("boo"), 1, res);
    assertEquals(Lists.newArrayList("foo"), res.stream().map(n -> n.value.nameInTable()).collect(Collectors.toList()));
    res = Lists.newArrayList();
    tree.range(new Entity("boo"), 2, res);
    assertEquals(Lists.newArrayList("foo", "foa"), res.stream().map(n -> n.value.nameInTable()).collect(Collectors.toList()));
  }

  /** Test format. */
  @Test
  public void testFormat() {
    assertTrue(Utils.DF.format(4123.123).contains("."));
    assertFalse(Utils.DF.format(4123.123).contains(","));
  }

  /** Test the tree expansion. */
  @Test
  public void testInferAndExpand() {
    final Ngram n1 = new Ngram("product", Type.ATTR, new Entity("1", "product", "-1", DataType.STRING), Pair.of(0, 0));
    final Ngram n2 = new Ngram("2019", Type.VAL, new Entity("-2", "2019", "1", DataType.STRING), Pair.of(1, 1));
    final Ngram n4 = new Ngram("sales", Type.MEA, new Entity("sales"), Pair.of(3, 3));
    final Ngram n5 = new Ngram(Type.SC, Lists.newArrayList(n1, n2));
    final Ngram n6 = new Ngram(Type.MC, Lists.newArrayList(n4));
    final Ngram n7 = new Ngram(Type.GPSJ, Lists.newArrayList(n5, n6));
    assertEquals(6, n7.countNode());
    final Map<String, Set<Entity>> opMeaConstraints = Maps.newLinkedHashMap();
    opMeaConstraints.put("sales", Sets.newHashSet(new Entity("sum")));
    Parser.infer(cube, n7, opMeaConstraints, Maps.newLinkedHashMap(), Maps.newLinkedHashMap(), Maps.newLinkedHashMap(), Sets.newLinkedHashSet());
    assertEquals(0, n5.getAnnotations().size());
    assertEquals(0, n6.getAnnotations().size());
    assertEquals(8, n7.countNode());
  }

  /** Test the tree expansion. */
  @Test
  public void testInferAndExpand2() {
    final Ngram n4 = new Ngram("sales", Type.MEA, new Entity("sales"), Pair.of(3, 3));
    final Ngram n6 = new Ngram(Type.MC, Lists.newArrayList(n4));
    final Ngram n7 = new Ngram(Type.GPSJ, Lists.newArrayList(n6));
    final Map<String, Set<Entity>> opMeaConstraints = Maps.newLinkedHashMap();
    opMeaConstraints.put("sales", Sets.newHashSet(new Entity("sum"), new Entity("max")));
    Parser.infer(cube, n7, opMeaConstraints, Maps.newLinkedHashMap(), Maps.newLinkedHashMap(), Maps.newLinkedHashMap(), Sets.newLinkedHashSet());
    assertEquals(1, n6.getAnnotations().size());
    assertEquals(3, n7.countNode());
  }

  /** Test the mapping score. */
  @Test
  public void testMapping() {
    final Ngram n1 = new Ngram("by", Type.BY, new Entity("by"), 1.0, Pair.of(0, 0));
    final Ngram n2 = new Ngram("customer id", Type.MEA, new Entity("customer id"), 1.0, Pair.of(1, 2));
    final Mapping m3 = new Mapping(cube, n1, n2);

    final Ngram n3 = new Ngram("by", Type.BY, new Entity("by"), 1.0, Pair.of(0, 0));
    final Ngram n4 = new Ngram("customer", Type.MEA, new Entity("customer id"), 0.8, Pair.of(1, 1));
    final Mapping m4 = new Mapping(cube, n3, n4);

    final List<Mapping> interpretationSentence = 
        Lists.newArrayList(m3, m4).stream().collect(Collectors.groupingBy(x -> x)).values().stream() // the same mapping can be generated in multiple ways (e.g., through different tokens)
          .map(equalMappings -> equalMappings.stream().max((m1, m2) -> Double.compare(m1.getScoreM(), m2.getScoreM())).get()) // keep only the one with the highest score
          .collect(Collectors.toList());

    assertFalse(m3.equals(m4));
    assertFalse(m3.hashCode() == m4.hashCode());
    assertEquals(2, interpretationSentence.size());
    assertEquals(1, Validator.compactMappings(Lists.newArrayList(m3, m4)).size());
  }

  /**
   * Test mapping creation.
   */
  @Test
  public void testMappingCreation() {
    Ngram n1 = new Ngram("a", Type.BY, new Entity("a"), 1.0, Pair.of(0, 0));
    Ngram n2 = new Ngram("b", Type.ATTR, new Entity("b"), 0.5, Pair.of(1, 1));
    Ngram n3 = new Ngram("c", Type.ATTR, new Entity("c"), 0.5, Pair.of(2, 2));
    Ngram n4 = new Ngram("d", Type.ATTR, new Entity("d"), 0.5, Pair.of(3, 3));
    final List<Ngram> newDataSet = Lists.newArrayList(n1, n2, n3, n4);
    final List<Mapping> interpretationsSentence = new ArrayList<>();
    interpretationsSentence.addAll(Mapper.createMappings(cube, Lists.newArrayList(), 0, newDataSet, 4, 0.0, Integer.MAX_VALUE));
    assertEquals(15, interpretationsSentence.size(), interpretationsSentence.stream().map(s -> s.toString()).reduce((s1,  s2) -> s1 + "\n" + s2).get());

    assertEquals(7, Mapper.createMappings(cube, Lists.newArrayList(n1), 1, Lists.newArrayList(n2, n3, n4), 4, 0, Integer.MAX_VALUE).size());
    assertEquals(6, Mapper.createMappings(cube, Lists.newArrayList(n1), 1, Lists.newArrayList(n2, n3, n4), 4, 0, 2).size());
    assertEquals(3, Mapper.createMappings(cube, Lists.newArrayList(n1), 1, Lists.newArrayList(n2, n3, n4), 4, 0, 1).size());
    assertEquals(7, Mapper.createMappings(cube, Lists.newArrayList(n1), 1, Lists.newArrayList(n2, n3, n4), 4, 4 * 0.5, Integer.MAX_VALUE).size());
    assertEquals(4, Mapper.createMappings(cube, Lists.newArrayList(n1), 1, Lists.newArrayList(n2, n3, n4), 4, 4 * 0.75, Integer.MAX_VALUE).size());
    assertEquals(1, Mapper.createMappings(cube, Lists.newArrayList(n1), 1, Lists.newArrayList(n2, n3, n4), 4, 4, Integer.MAX_VALUE).size());

    n1 = new Ngram("a", Type.BY, new Entity("a"), 1.0, Pair.of(0, 0));
    n2 = new Ngram("b", Type.ATTR, new Entity("b"), 0.5, Pair.of(1, 1));
    n3 = new Ngram("c", Type.ATTR, new Entity("c"), 0.0, Pair.of(2, 2));
    n4 = new Ngram("d", Type.ATTR, new Entity("d"), 0.5, Pair.of(3, 3));
    final Ngram n5 = new Ngram("e", Type.ATTR, new Entity("e"), 0.5, Pair.of(3, 3));
    assertEquals(11, Mapper.createMappings(cube, Lists.newArrayList(n1), 1, Lists.newArrayList(n2, n3, n4, n5), 4, 0, Integer.MAX_VALUE).size());
    assertEquals( 9, Mapper.createMappings(cube, Lists.newArrayList(n1), 1, Lists.newArrayList(n2, n3, n4, n5), 4, 0, 2).size());
    assertEquals( 4, Mapper.createMappings(cube, Lists.newArrayList(n1), 1, Lists.newArrayList(n2, n3, n4, n5), 4, 0, 1).size());
    assertEquals(11, Mapper.createMappings(cube, Lists.newArrayList(n1), 1, Lists.newArrayList(n2, n3, n4, n5), 4, 4 * 0.5, Integer.MAX_VALUE).size());
    assertEquals( 7, Mapper.createMappings(cube, Lists.newArrayList(n1), 1, Lists.newArrayList(n2, n3, n4, n5), 4, 4 * 0.75, Integer.MAX_VALUE).size());
    assertEquals( 2, Mapper.createMappings(cube, Lists.newArrayList(n1), 1, Lists.newArrayList(n2, n3, n4, n5), 4, 4, Integer.MAX_VALUE).size());
  }

  /**
   * Test mapping creation with duplicates.
   */
  @Test
  public void testMappingCreation1() {

  }

  /**
   * Test ngrams.
   */
  @Test
  public void testNgramEquality() {
    assertEquals(//
        new Ngram("a", Type.BY, new Entity("foo"), Pair.of(0, 0)), //
        new Ngram("a", Type.BY, new Entity("foo"), Pair.of(0, 0)));
    assertNotEquals(//
        new Ngram("a", Type.BY, new Entity("foo"), Pair.of(0, 0)), //
        new Ngram("a", Type.BY, new Entity("foo"), Pair.of(1, 1)));
    assertNotEquals(//
        new Ngram("avg", Type.BY, new Entity("average"), 0.4, Pair.of(0, 0)), //
        new Ngram("average", Type.BY, new Entity("average"), Pair.of(0, 0)));
    assertNotEquals(//
        new Ngram("a", Type.BY, new Entity("foo"), Pair.of(0, 0)), //
        new Ngram("a", Type.BY, new Entity("bar"), 0.9, Pair.of(0, 0)));
    assertNotEquals(//
        new Ngram("a", Type.BY, new Entity("foo"), Pair.of(0, 0)), //
        new Ngram("b", Type.BY, new Entity("bar"), 0.9, Pair.of(0, 0)));
    assertEquals(2, Sets.newHashSet(//
        new Ngram("a", Type.BY, new Entity("foo"), Pair.of(0, 0)), //
        new Ngram("a", Type.BY, new Entity("foo"), 0.9, Pair.of(0, 0))).size());
    assertEquals(2, Sets.newHashSet(//
        new Ngram("a", Type.BY, new Entity("foo"), Pair.of(0, 0)), //
        new Ngram("a", Type.BY, new Entity("bar"), 0.9, Pair.of(0, 0))).size());
    assertEquals(2, Sets.newHashSet(//
        new Ngram("a", Type.BY, new Entity("foo"), Pair.of(0, 0)), //
        new Ngram("b", Type.BY, new Entity("foo"), 0.9, Pair.of(0, 0))).size());
  }

  /**
   * Test ngrams.
   */
  @Test
  public void testNgrams() {
    final Ngram n1 = new Ngram("pippo", Type.BY, new Entity("pippo"), 1.0, Pair.of(0, 0));
    final Ngram n2 = new Ngram("pluto plutarco", Type.ATTR, new Entity("pippo"), 0.8, Pair.of(1, 2));
    final Ngram n3 = new Ngram("paperina", Type.ATTR, new Entity("pippo"), 0.6, Pair.of(3, 3));
    final Mapping family = new Mapping(cube, n1, n2, n3);
    assertEquals(1.0, family.getAvgSimilarity(), PRECISION);
    final Ngram n4 = new Ngram(Type.GC, Lists.newArrayList(n1, n2, n3));
    assertEquals(0.8, new Mapping(cube, n4).getAvgSimilarity(), PRECISION);
    assertEquals(0.6, n3.similarity(), PRECISION);
//    assertEquals(0.6, n3.minSimilarity(), PRECISION);
    assertEquals(0.8, n4.similarity(), PRECISION);
//    assertEquals(0.6, n4.minSimilarity(), PRECISION);
  }

  /**
   * Test parent.
   * @throws IOException in case of error
   */
  @Test
  public void testParent() throws Exception {
    Mapping mapping = Validator.parseAndTranslate(cube, "stdev unit_sales", 1).get(0).getLeft();
    assertEquals("GPSJ(MC(stdev unitsales))", mapping.toStringTree());
    assertEquals(Ngram.findParent(mapping.bestNgram, mapping.bestNgram.children.get(0)).get(), mapping.bestNgram);
    assertEquals(Ngram.findParent(mapping.bestNgram, mapping.bestNgram.children.get(0).children.get(0)).get(), mapping.bestNgram.children.get(0));

    mapping = Validator.parseAndTranslate(cube, "stdev unit_sales avg store_sales", 1).get(0).getLeft();
    assertEquals("GPSJ(MC(MC(stdev unitsales) MC(avg storesales)))", mapping.toStringTree());
    assertEquals(Ngram.findParent(mapping.bestNgram, mapping.bestNgram.children.get(0)).get(), mapping.bestNgram);
    assertEquals(Ngram.findParent(mapping.bestNgram, mapping.bestNgram.children.get(0).children.get(0)).get(), mapping.bestNgram.children.get(0));
  }

  /**
   * Check translation and query equality.
   */
  @Test
  public void testQ() {
    final Mapping s0 = new Mapping(cube, //
        new Ngram("sum", Type.AGG, new Entity("sum"), Pair.of(0, 0)), //
        new Ngram("sales", Type.MEA, new Entity("unit_sales"), Pair.of(1, 1)), //
        new Ngram("by", Type.BY, new Entity("by"), Pair.of(2, 2)), //
        new Ngram("customer", Type.ATTR, new Entity("customer_id"), Pair.of(3, 3)));
    // assertEquals(1, Rules.translate(s0).stream().collect(Collectors.toSet()).size());
    final Mapping t1 = Parser.parse(cube, s0).get();
    assertTrue(t1.ngrams.stream().anyMatch(n -> n.type.equals(Type.GPSJ)));
    assertEquals(7, t1.countNodes());
    final Mapping s1 = new Mapping(cube, //
        new Ngram("sales", Type.MEA, new Entity("unit_sales"), Pair.of(1, 1)), //
        new Ngram("by", Type.BY, new Entity("by"), Pair.of(2, 2)), //
        new Ngram("customer", Type.ATTR, new Entity("customer_id"), Pair.of(3, 3)));
    final Mapping t2 = Parser.parse(cube, s1).get();
    assertEquals(6, t2.countNodes());
    assertNotEquals(t1, t2);
  }

  /** Test the mapping score. */
  @Test
  public void testScore() {
    final Ngram n3 = new Ngram("foo", Type.ATTR, new Entity("foo"), 1.0, Pair.of(3, 3));
    final Ngram n4 = new Ngram("sales", Type.MEA, new Entity("sales"), 0.8, Pair.of(3, 3));
    final Ngram n6 = new Ngram(Type.MC, Lists.newArrayList(n4));
    final Ngram n7 = new Ngram(Type.GPSJ, Lists.newArrayList(n6));
    final Mapping m = new Mapping(cube, n3, n7);
    assertEquals(2, m.ngrams.size());
    assertEquals(1, m.getNMatched());
    assertEquals(0.8, m.getAvgSimilarity(), PRECISION);
    assertEquals(0.8, m.getScorePFM(), PRECISION);
    assertEquals(1.8, m.getScoreM(), PRECISION);
  }

  /**
   * Test work similarity.
   */
  @Test
  public void testTokenSimilarity() {
    assertEquals(0, StringUtils.levenshteinDistance("customer", "customer"));
    assertEquals(1, StringUtils.levenshteinDistance("cutomer", "customer"));
    assertEquals(3, StringUtils.levenshteinDistance("cost", "Robust"));
    assertEquals(3, StringUtils.levenshteinDistance("robust", "cost"));
    assertEquals(0.875, Utils.tokenSimilarity("cutomer", "customer"), PRECISION);
    assertEquals(0.4, Utils.tokenSimilarity("cost", "store_cost"), PRECISION);
    assertEquals(0.272, Utils.tokenSimilarity("sum store", "store sales"), PRECISION);
    assertEquals(0.636, Utils.tokenSimilarity("store sum", "store sales"), PRECISION);
    assertEquals(0.4, Utils.tokenSimilarity("cost", "store cost"), PRECISION);
    assertEquals(0.666, Utils.tokenSimilarity("media", "medium"), PRECISION);
    assertEquals(0.5, Utils.tokenSimilarity("media", "media type"), PRECISION);
    assertEquals(0.4, Utils.tokenSimilarity("type", "store type"), PRECISION);
    assertEquals(0.4, Utils.tokenSimilarity("type", "media type"), PRECISION);
    assertEquals(0.636, Utils.tokenSimilarity("media based", "media type"), PRECISION);
    assertEquals(0.09, Utils.tokenSimilarity("based media", "media type"), PRECISION);
    assertEquals(1, Utils.tokenSimilarity("media type", "media type"), PRECISION);
    assertEquals(0.5, Utils.tokenSimilarity("year", "the_year"), PRECISION);
    assertEquals(0.428, Utils.tokenSimilarity("greater", "year"), PRECISION);
    assertEquals(0.4, Utils.tokenSimilarity("cost", "store cost"), PRECISION);
    assertEquals(0.199, Utils.tokenSimilarity("store cost", "cost by"), PRECISION);
    assertEquals(0.199, Utils.tokenSimilarity("cost by", "store cost"), PRECISION);
    assertEquals(0.5, Utils.tokenSimilarity("year", "the year"), PRECISION);
    assertEquals(0.625, Utils.tokenSimilarity("for year", "the year"), PRECISION);
    assertEquals(0.1875, Utils.tokenSimilarity("average store cost".split(" "), new String[] {"avg"}), PRECISION);
    assertEquals(0.4375, Utils.tokenSimilarity("average store cost".split(" "), new String[] {"average"}), PRECISION);
    assertEquals(0.583, Utils.tokenSimilarity("average store".split(" "), "store_state".split("_")), PRECISION);
    assertEquals(0.571, Utils.tokenSimilarity("for year".split(" "), "the year".split(" ")), PRECISION);
  }

  /**
   * Test translation.
   */
  @Test
  public void testTranslate() {
    final Mapping s2 = new Mapping(cube, //
        new Ngram("a", Type.AGG, new Entity("sum"), Pair.of(2, 2)), // ;
        new Ngram("b", Type.MEA, new Entity("unit_sales"), Pair.of(3, 3)),
        new Ngram("by", Type.BY, new Entity("by"), Pair.of(0, 0)), //
        new Ngram("customer", Type.ATTR, new Entity("customer_id"), Pair.of(1, 1)),
        new Ngram("c", Type.AGG, new Entity("avg"), Pair.of(2, 2)), // ;
        new Ngram("d", Type.MEA, new Entity("store_cost"), Pair.of(3, 3)) //
    );
    final Mapping res = Parser.parse(cube, s2).get(); // , Arrays.asList(Pair.of(10, 3), Pair.of(12, 4))
    assertEquals(4, res.getNMatched());
    assertEquals(7, res.countNodes());
    // assertEquals(res.toString(), 2, res.ngrams.size()); // a query and a measure clause
  }

  /**
   * Test translation of similar trees.
   */
  @Test
  public void testTranslation() {
    final Mapping s0 = new Mapping(cube, //
        new Ngram("sum", Type.AGG, new Entity("sum"), Pair.of(0, 0)), //
        new Ngram("sales", Type.MEA, new Entity("unit_sales"), 0.9, Pair.of(1, 1)), //
        new Ngram("by", Type.BY, new Entity("by"), 0.8, Pair.of(2, 2)), //
        new Ngram("customer", Type.ATTR, new Entity("customer_id"), 0.7, Pair.of(3, 3)), //
        new Ngram("year", Type.ATTR, new Entity("the_year"), 0.4, Pair.of(4, 4)), //
        new Ngram(">=", Type.COP, new Entity(">="), 1.0, Pair.of(5, 5)), //
        new Ngram("2018", Type.VAL, new Entity("2018"), 0.8, Pair.of(6, 6)) //
    );
    final Mapping parsed = Parser.parse(cube, s0).get();
    assertTrue(s0.ngrams.stream().allMatch(n -> n.children.isEmpty()));
    assertEquals(7, parsed.getNMatched());
    assertEquals(0.8, parsed.getAvgSimilarity(), PRECISION);
    assertEquals(parsed.getScorePFM(), parsed.getAvgSimilarity() * parsed.getNMatched(), PRECISION);
    assertEquals(s0.ngrams.size(), parsed.getNMatched());

    final Mapping s1 = new Mapping(cube, //
        new Ngram("year", Type.ATTR, new Entity("the_year"), Pair.of(4, 4)), //
        new Ngram(">=", Type.COP, new Entity(">="), Pair.of(5, 5)), //
        new Ngram("2018", Type.VAL, new Entity("2018"), Pair.of(6, 6)), //
        new Ngram("sales", Type.MEA, new Entity("unit_sales"), Pair.of(1, 1)), //
        new Ngram("sum", Type.AGG, new Entity("sum"), Pair.of(0, 0)), //
        new Ngram("by", Type.BY, new Entity("by"), Pair.of(2, 2)), //
        new Ngram("customer", Type.ATTR, new Entity("customer_id"), Pair.of(3, 3)) //
    );
    assertEquals(s1.ngrams.size(), Parser.parse(cube, s0).get().getNMatched());
  }

  /**
   * Test tree distance.
   * @throws IOException in case of error
   */
  @Test
  public void testTree() throws IOException {
    final String tree1Str1 = "f(d(a c(b)) e)";
    final String tree1Str2 = "f(c(d(a b)) e)";
    final Tree tree1 = new Tree(tree1Str1);
    final Tree tree2 = new Tree(tree1Str2);
    final int distance1 = Tree.ZhangShasha(tree1, tree2);
    assertEquals(2, distance1);
    assertEquals(0, Tree.ZhangShasha(new Tree("f"), new Tree("f")));
    assertEquals(1, Tree.ZhangShasha(new Tree("f"), new Tree("g")));
    assertEquals(2, Tree.ZhangShasha(new Tree("Q(MC(O M) GC(GC(by c) y))"), new Tree("Q(MC(O M) GC(by c))")));

    Ngram n1 = new Ngram("pippo", Type.BY, new Entity("a"), 1.0, Pair.of(0, 0));
    Ngram n2 = new Ngram("pluto", Type.ATTR, new Entity("b"), 0.5, Pair.of(1, 1));
    Ngram n3 = new Ngram("paper", Type.ATTR, new Entity("c"), 0.5, Pair.of(2, 2));
    Ngram n4 = new Ngram(Type.GC, Lists.newArrayList(n1, n2, n3));
    assertEquals("a", n1.toStringTree());
    assertEquals("GC(a b c)", n4.toStringTree());
    assertEquals("GC(a b c)", new Mapping(cube, n4, n1).toStringTree());
    final Ngram n5 = new Ngram("pippo", Type.MEA, new Entity("unit_sales"), Pair.of(0, 0));
    assertEquals("GPSJ(MC(unitsales))", Parser.parse(cube, new Mapping(cube, n5)).get().toStringTree());

    n1 = new Ngram("unit_sales", Type.MEA, new Entity("unit_sales"), Pair.of(0, 0));
    n2 = new Ngram("year",       Type.ATTR, new Entity("year"), Pair.of(1, 1));
    n3 = new Ngram("=",          Type.COP, new Entity("="), Pair.of(2, 2));
    n4 = new Ngram("2019",       Type.VAL, new Entity("2019"), Pair.of(2, 2));
    assertEquals("GPSJ(MC(unitsales) SC(where SC(year e v2019)))", Parser.parse(cube, new Mapping(cube, n1, n2, n3, n4)).get().toStringTree());
    assertEquals(4, Tree.ZhangShasha("Q(MC(sum unitsales) SC(theyear e v2019))", "Q(MC(sum unitsales))"));
  }

  /**
   * Test the type checker.
   */
  @Test
  public void testTypeChecker() {
    final Ngram n1 = new Ngram("product", Type.ATTR, new Entity("product"), Pair.of(0, 0));
    final Ngram n2 = new Ngram("2019", Type.VAL, new Entity("2019", DataType.NUMERIC), Pair.of(1, 1));
    final Ngram n3 = new Ngram("sum", Type.AGG, new Entity("sum"), Pair.of(2, 2));
    final Ngram n4 = new Ngram("sales", Type.MEA, new Entity("sales"), Pair.of(3, 3));
    final Ngram n5 = new Ngram(Type.SC, Lists.newArrayList(n1, n2));
    final Ngram n6 = new Ngram(Type.MC, Lists.newArrayList(n3, n4));
    final Ngram n7 = new Ngram(Type.GPSJ, Lists.newArrayList(n5, n6));
    assertEquals(Sets.newHashSet(n5, n6), Ngram.simpleClauses(n7));
    final Map<String, Set<Entity>> opMeaConstraints = Maps.newLinkedHashMap();
    opMeaConstraints.put("sales", Sets.newHashSet(new Entity("min"), new Entity("max")));
    Parser.typeCheck(cube, n7, opMeaConstraints, Maps.newLinkedHashMap(), Maps.newLinkedHashMap());
    assertEquals(1, n5.getAnnotations().size());
    assertEquals(1, n6.getAnnotations().size());
  }

  /**
   * Test the type checker.
   */
  @Test
  public void testTypeCheckerMC() {
    final Ngram n3 = new Ngram("sum", Type.AGG, new Entity("sum"), Pair.of(2, 2));
    final Ngram n4 = new Ngram("sales", Type.MEA, new Entity("sales"), Pair.of(3, 3));
    final Ngram n6 = new Ngram(Type.MC, Lists.newArrayList(n3, n4));
    final Map<String, Set<Entity>> opMeaConstraints = Maps.newLinkedHashMap();
    opMeaConstraints.put("sales", Sets.newHashSet(new Entity("sum"), new Entity("min"), new Entity("max")));
    Parser.typeCheck(cube, n6, opMeaConstraints, Maps.newLinkedHashMap(), Maps.newLinkedHashMap());
    assertEquals(0, n6.getAnnotations().size());
  }

  /**
   * Test the type checker.
   */
  @Test
  public void testTypeCheckerSC() {
    Ngram n1 = new Ngram("product", Type.ATTR, new Entity("1", "product", "-1", DataType.STRING), Pair.of(0, 0));
    Ngram n2 = new Ngram("2019", Type.VAL, new Entity("-2", "2019", "2", DataType.STRING), Pair.of(1, 1));
    Ngram n5 = new Ngram(Type.SC, Lists.newArrayList(n1, n2));
    Parser.typeCheck(cube, n5, Maps.newLinkedHashMap(), Maps.newLinkedHashMap(), Maps.newLinkedHashMap());
    assertEquals(1, n5.getAnnotations().size());

    n1 = new Ngram("product", Type.ATTR, new Entity("1", "product", "-1", DataType.STRING), Pair.of(0, 0));
    n2 = new Ngram("2019", Type.VAL, new Entity("-2", "2019", "1", DataType.STRING), Pair.of(1, 1));
    n5 = new Ngram(Type.SC, Lists.newArrayList(n1, n2));
    Parser.typeCheck(cube, n5, Maps.newLinkedHashMap(), Maps.newLinkedHashMap(), Maps.newLinkedHashMap());
    assertEquals(0, n5.getAnnotations().size());
  }
}
