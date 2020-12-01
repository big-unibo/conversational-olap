package test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unibo.conversational.Validator;
import it.unibo.conversational.algorithms.Parser;
import it.unibo.conversational.algorithms.Parser.Type;
import it.unibo.conversational.database.Config;
import it.unibo.conversational.database.Cube;
import it.unibo.conversational.database.DBmanager;
import it.unibo.conversational.database.QueryGenerator;
import it.unibo.conversational.datatypes.Entity;
import it.unibo.conversational.datatypes.Mapping;
import it.unibo.conversational.datatypes.Ngram;
import it.unibo.conversational.datatypes.Ngram.AnnotationType;
import it.unibo.conversational.olap.Operator;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TestOLAP {
    private final Cube cube = Config.getCube("sales_fact_1997");
    private static final double tau = 0.5;
    private List<Triple<AnnotationType, Ngram, Ngram>> log = Lists.newArrayList();

    @AfterEach
    public void after() {
        Parser.TEST = false;
        Parser.resetIds();
    }

    @BeforeEach
    public void before() {
        Parser.resetIds();
        Parser.TEST = true;
        log = Lists.newArrayList();
    }

    private void checkSerializedSession(final String sessionid, final Mapping fullquery, final Mapping session, int steps) throws IOException {
        final Map<String, Object> statistics = QueryGenerator.getSessionStatistics(cube, sessionid, fullquery, session);
        assertTrue((long) statistics.get("fullquery_time") >= 0);
        assertTrue((long) statistics.get("session_time") >= 0);
        assertEquals(1.0, (double) statistics.get("fullquery_sim"), 0.001);
        assertEquals(1.0, (double) statistics.get("session_sim"), 0.001);
        assertEquals(steps * 1.0, (double) statistics.get("session_iterations"), 0.001);
    }

    private Mapping execute(final Mapping prevTree, final Operator op, final String expectedForest) {
        try {
            if (op != null) {
                assertEquals(0, op.countAnnotationsInTree(), op.getAnnotationsInTree().toString());
                op.apply(prevTree.bestNgram);
            }
            prevTree.toJSON(cube);
            final Mapping res = Validator.getBest(cube, expectedForest);
            try {
                assertEquals(res.toStringTree(), prevTree.toStringTree());
            } catch (final AssertionError ex) {
                System.out.println(res.toStringTree());
                System.out.println(prevTree.toStringTree());
                assertEquals(res.getNMatched(), prevTree.getNMatched());
            }
            return prevTree;
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return null;
    }

    private Mapping execute(final Mapping prevTree, final String operator, final List<AnnotationType> annotations) {
        try {
            final Operator op = (Operator) Validator.parseAndTranslate(cube, Operator.class, prevTree, tau, log, operator).getNgrams().get(0);
            assertEquals(Sets.newLinkedHashSet(annotations), op.getAnnotationsInTree().stream().map(e -> e.getValue().getKey()).collect(Collectors.toSet()));
            assertEquals(annotations.size(), op.countAnnotationsInTree(), op.getAnnotationsInTree().toString());
            return prevTree;
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return null;
    }

    private Mapping execute(final Mapping prevTree, final String operator, final List<AnnotationType> annotations, final List<Pair<String, String>> disambiguations, final String expectedForest) {
        try {
            final Operator op = (Operator) Validator.parseAndTranslate(cube, Operator.class, prevTree, tau, log, operator).getNgrams().get(0);
            assertEquals(annotations.size(), op.countAnnotationsInTree(), op.getAnnotationsInTree().toString());
            assertEquals(Sets.newLinkedHashSet(annotations), op.getAnnotationsInTree().stream().map(e -> e.getValue().getKey()).collect(Collectors.toSet()));
            for (final Pair<String, String> p : disambiguations) {
                op.disambiguate(p.getKey(), p.getValue(), log);
            }
            return execute(prevTree, op, expectedForest);
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return null;
    }

    private Mapping execute(final Mapping prevTree, final String operator, final String expectedForest) {
        try {
            if (!operator.isEmpty()) {
                final Operator op = (Operator) Validator.parseAndTranslate(cube, Operator.class, prevTree, tau, log, operator).getNgrams().get(0);
                return execute(prevTree, op, expectedForest);
            } else {
                return execute(prevTree, (Operator) null, expectedForest);
            }
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return null;
    }

    private Mapping execute(final String prevForest, final String operator, final List<AnnotationType> annotations) {
        try {
            final Mapping prevTree = Validator.parseAndTranslate(cube, prevForest);
            return execute(prevTree, operator, annotations);
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return null;
    }

    private Mapping execute(final String prevForest, final String operator, final List<AnnotationType> annotations, final List<Pair<String, String>> disambiguations, final String expectedForest) {
        try {
            final Mapping prevTree = Validator.parseAndTranslate(cube, prevForest);
            return execute(prevTree, operator, annotations, disambiguations, expectedForest);
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return null;
    }

    private Mapping execute(final String prevForest, final String operator, final String expectedForest) {
        try {
            final Mapping prevTree = Validator.parseAndTranslate(cube, prevForest);
            return execute(prevTree, operator, expectedForest);
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return null;
    }

    /**
     * Populate log with a disambiguation on a Full query
     *
     * @param source ambiguous full query
     * @throws Exception in case of error
     */
    public void populateLog(final String source) throws Exception {
        int l = log.size();
        for (int i = l; i < l + 2; i++) {
            Mapping prevTree = Validator.parseAndTranslate(cube, source, tau, log);
            Parser.automaticDisambiguate(prevTree, log);
        }
    }

    /**
     * Populate log with a disambiguation on a OLAP operator
     *
     * @param source   full query
     * @param operator ambiguous OLAP operator
     * @param value    disambiguation value
     * @throws Exception in case of error
     */
    public void populateLog(final String source, final String operator, final String value) throws Exception {
        int l = log.size();
        for (int i = l; i < l + 2; i++) {
            Mapping prevTree = Validator.parseAndTranslate(cube, source, tau, log);
            Operator op = (Operator) Validator.parseAndTranslate(cube, Operator.class, prevTree, tau, log, "rollup product id").getNgrams().get(0);
            op.disambiguate("i" + i, value, log);
        }
    }

    /**
     * Test add and remove
     */
    @Test
    public void test01() {
        Mapping prevTree = execute("sum unit sales for country USA", "add product family", "sum unit sales for country USA by product family");
        execute(prevTree, "remove product family", "sum unit sales for country USA");

        execute(prevTree, "add sum store sales", "sum unit sales sum store sales for country USA");
        execute(prevTree, "remove sum store sales", "sum unit sales for country USA");

        execute(prevTree, "add sum store sales", "sum unit sales sum store sales for country USA");
        execute(prevTree, "remove store sales", "sum unit sales for country USA");
        execute(prevTree, "remove USA", "sum unit sales");

        prevTree = execute("sum unit sales", "add store sales", Lists.newArrayList(AnnotationType.MA), Lists.newArrayList(Pair.of("i0", "max")), "sum unit sales max store sales");
        execute(prevTree, "add store sales", Lists.newArrayList(AnnotationType.EAE, AnnotationType.MA));
        execute(prevTree, "add min store sales", "sum unit sales max store sales min store sales");
        execute(prevTree, "remove store sales", Lists.newArrayList(AnnotationType.MA), Lists.newArrayList(Pair.of("i3", "max")), "sum unit sales min store sales");

        prevTree = execute("sum unit sales for country USA", "add product subcategory beer", "sum unit sales for country USA and product subcategory beer");
        execute(prevTree, "remove product subcategory beer", "sum unit sales for country USA");
    }

    /**
     * Test replace
     */
    @Test
    public void test02() {
        final Mapping prevTree = execute("sum unit sales by product category for country = USA", "replace product category with product family", "sum unit sales by product family for country = USA");
        execute(prevTree, "replace product family with product category", "sum unit sales by product category for country = USA");

        execute(prevTree, "replace sum unit sales with sum store sales", "sum store sales by product category for country = USA");
        execute(prevTree, "replace sum store sales with sum unit sales", "sum unit sales by product category for country = USA");

        execute(prevTree, "replace country = USA with product subcategory = Beer", "sum unit sales by product category for product subcategory = Beer");
        execute(prevTree, "replace product subcategory = Beer with country = USA", "sum unit sales by product category for country = USA");
    }

    /**
     * Test a session with ambiguity
     */
    @Test
    public void test03() {
        final Mapping prevTree = execute("sum unit sales by product category", "drill product category to product subcategory", "sum unit sales by product subcategory");
        execute(prevTree, "drill product subcategory", "sum unit sales by product id");
        execute(prevTree, "rollup product id", Lists.newArrayList(AnnotationType.BA));
        execute(prevTree, "rollup product id to product category", "sum unit sales by product category");
        execute(prevTree, "add month", "sum unit sales by product category month");
        execute(prevTree, "rollup month", "sum unit sales by product category year");
    }

    /**
     * Test hint on OLAP operator
     */
    @Test
    public void test04() throws Exception {
        execute("sum unit sales", "add store sales", Lists.newArrayList(AnnotationType.MA), Lists.newArrayList(Pair.of("i0", "max")), "sum unit sales max store sales");
        execute("sum unit sales", "add store sales", Lists.newArrayList(AnnotationType.MA), Lists.newArrayList(Pair.of("i1", "max")), "sum unit sales max store sales");
        execute("sum unit sales", "add store sales", "sum unit sales max store sales");
        //    DO NOT consider hint on BA
        //    log = Lists.newArrayList();
        //    Parser.resetIds();
        //
        //    populateLog("sum store cost by product id", "rollup product id", "product_subcategory");
        //
        //    Mapping prevTree = Validator.parseAndTranslate(cube, "sum store cost by product id", tau, log);
        //    Operator op0 = (Operator) Validator.parseAndTranslate(cube, Operator.class, prevTree, tau, log, "rollup product id").getNgrams().get(0);
        //    Operator op1 = (Operator) Validator.parseAndTranslate(cube, Operator.class, prevTree, tau, log, "rollup product id to product subcategory").getNgrams().get(0);
        //    assertEquals(op1.toStringTree(), op0.toStringTree());
        //    op0.disambiguate("i2", "product_name", log);
        //    op1 = (Operator) Validator.parseAndTranslate(cube, Operator.class, prevTree, tau, log, "rollup product id to product name").getNgrams().get(0);
        //    assertEquals(op1.toStringTree(), op0.toStringTree());
    }

    /**
     * Test hint on parse forest
     *
     * @throws Exception in case of error
     */
    @Test
    public void test05() throws Exception {
        populateLog("store cost");

        Mapping prevTree = Validator.parseAndTranslate(cube, "store cost", tau, log);
        assertEquals(Validator.parseAndTranslate(cube, "avg store cost").toStringTree(), prevTree.toStringTree());
        prevTree.disambiguate("i2", "stdev", log);
        assertEquals(Validator.parseAndTranslate(cube, "stdev store cost").toStringTree(), prevTree.toStringTree());

        populateLog("sum store cost for USA");

        prevTree = Validator.parseAndTranslate(cube, "sum store cost for USA", tau, log);
        assertEquals(Validator.parseAndTranslate(cube, "sum store cost for country = USA").toStringTree(), prevTree.toStringTree());
        prevTree.disambiguate("i5", "store_country", log);
        assertEquals(Validator.parseAndTranslate(cube, "sum store cost for store country = USA").toStringTree(), prevTree.toStringTree());

        populateLog("sum store cost for product subcategory USA");

        prevTree = Validator.parseAndTranslate(cube, "sum store cost for product subcategory USA", tau, log);
        assertEquals(Validator.parseAndTranslate(cube, "sum store cost for product subcategory = Acetominifen").toStringTree(), prevTree.toStringTree());
        prevTree.disambiguate("t3", "Beer", log);
        assertEquals(Validator.parseAndTranslate(cube, "sum store cost for product subcategory = Beer").toStringTree(), prevTree.toStringTree());
    }

    /**
     * Test all OLAP operator ambiguities
     */
    @Test
    public void test06() {
        execute("sum unit sales by product id", "rollup product id", Lists.newArrayList(AnnotationType.BA), Lists.newArrayList(Pair.of("i0", "product_subcategory")), "sum unit sales by product subcategory");
        execute("sum store sales by family category", "drill down to subcategory", Lists.newArrayList(AnnotationType.CA), Lists.newArrayList(Pair.of("i1", "product_category")), "sum unit sales by product family product subcategory");
        execute("sum store sales by family", "drill down to subcategory", "sum unit sales by product subcategory");
        execute("sum store sales by family", "add family", Lists.newArrayList(AnnotationType.EAE));
        execute("sum store sales by product subcategory", "drill down product subcategory to product category", Lists.newArrayList(AnnotationType.GSA));
        execute("sum unit sales by product id", "drill product id to country", Lists.newArrayList(AnnotationType.A2));
        execute("sum unit sales by product id", "drill product id", Lists.newArrayList(AnnotationType.A3));
        execute("sum unit sales by product category", "rollup product category", "sum unit sales");
        execute("sum unit sales by product category", "rollup product category to product category", Lists.newArrayList(AnnotationType.EAE));
        execute("sum unit sales by product category", "drill down product category to product category", Lists.newArrayList(AnnotationType.EAE));
    }

    /**
     * Test all OLAP operator ambiguities
     */
    @Test
    public void test06bis() {
        // Test "all"
        execute("sum unit sales by product id", "rollup product id to all products", "sum unit sales");
        execute("sum unit sales", "drill down all products to product id", "sum unit sales by product id");
        execute("sum unit sales", "drill down product id", "sum unit sales by product id");

        // Infer FROM attribute (i.e., when given attr is already in GC)
        execute("sum unit sales by month", "rollup month", "sum unit sales by year");
        execute("sum unit sales by date", "rollup date", Lists.newArrayList(AnnotationType.BA), Lists.newArrayList(Pair.of("i0", "month")), "sum unit sales by month");
        execute("sum unit sales by year", "roll up year", "sum unit sales");
        execute("sum unit sales by month", "drill down month", "sum unit sales by date");
        execute("sum unit sales by year", "drill down year", Lists.newArrayList(AnnotationType.BA), Lists.newArrayList(Pair.of("i1", "month")), "sum unit sales by month");
        execute("sum unit sales by product id", "drill product id", Lists.newArrayList(AnnotationType.A3));

        // Infer TO attribute
        // 1 finer attribute
        execute("sum unit sales by month", "roll up year", "sum unit sales by year");
        // 2 finer attributes
        execute("sum unit sales by month quarter", "roll up year", Lists.newArrayList(AnnotationType.CA), Lists.newArrayList(Pair.of("i3", "month")), "sum unit sales by quarter year");
//  }
//  @Test
//  public void test061bis() {

        // 0 finer attributes, 1 coarser attribute
        execute("sum unit sales by year", "roll up date", Lists.newArrayList(AnnotationType.GSA));
        // 0 finer attributes, 0 coarser attribute
        execute("sum unit sales", "roll up year", Lists.newArrayList(AnnotationType.GSA));
        // 1 coarser attribute
        execute("sum unit sales by month", "drill down date", "sum unit sales by date");
        // 2 coarser attributes
        execute("sum unit sales by month quarter", "drill down date", Lists.newArrayList(AnnotationType.CA), Lists.newArrayList(Pair.of("i4", "month")), "sum unit sales by quarter date");
        // 0 coarser attributes, 1 finer attribute
        execute("sum unit sales by date", "drill down month", Lists.newArrayList(AnnotationType.GSA));
        // 0 finer attributes, 0 coarser attribute
        execute("sum unit sales", "drill down year", "sum unit sales by year");
    }

    /**
     * Test all OLAP operator ambiguities
     */
    @Test
    public void test07() {
        Mapping prevTree = execute("sum store sales", "slice on usa", Lists.newArrayList(AnnotationType.AA), Lists.newArrayList(Pair.of("i0", "country")), "sum store sales for country = USA");
        execute(prevTree, "slice on product subcategory beer", "sum store sales for country = USA and product subcategory = beer");
    }

    /**
     * Test all OLAP operator ambiguities
     */
    @Test
    public void test08() {
        Mapping prevTree = execute("sum store sales", "add avg store sales", "sum store sales avg store sales");
        execute(prevTree, "drop avg store sales", "sum store sales");

        prevTree = execute("sum store sales", "add avg store sales", "sum store sales avg store sales");
        execute(prevTree, "drop sum store sales", "avg store sales");

        prevTree = execute("sum store sales", "add avg store sales", "sum store sales avg store sales");
        execute(prevTree, "drop store sales", Lists.newArrayList(AnnotationType.MA), Lists.newArrayList(Pair.of("i0", "avg")), "sum store sales");

        prevTree = execute("sum store sales", "add avg store sales", "sum store sales avg store sales");
        execute(prevTree, "drop store sales", Lists.newArrayList(AnnotationType.MA), Lists.newArrayList(Pair.of("i1", "sum")), "avg store sales");
    }

    /**
     * Test all OLAP operator ambiguities
     */
    @Test
    public void test09() {
        execute("sum store sales for country = USA", "add country = USA", Lists.newArrayList(AnnotationType.EAE));
        execute("sum store sales", "remove country = USA", Lists.newArrayList(AnnotationType.ENE));
    }

    /**
     * Test all OLAP operator ambiguities
     */
    @Test
    public void test10() {
        Mapping prev = execute("return average store cost and average store sales per category", "", "average store cost average store sales by product category");
        execute(prev, "drill down from category", "average store cost average store sales by product subcategory");

        prev = execute("return average store cost and average store sales per category", "", "average store cost average store sales by product category");
        execute(prev, "drill down category", "average store cost average store sales by product subcategory");

        execute(prev, "slice on 1997", "average store cost average store sales by product subcategory where year = 1997");

        execute("average store cost average store sales by product subcategory", "slice on year 1997", "average store cost average store sales by product subcategory where year = 1997");
    }

    /**
     * Test Q1
     */
    @Test
    public void test11() {
        Mapping prev = execute("avg unit sales for year = 1997", "", "average unit sales where year = 1997");
        execute(prev, "drill down subcategory", "average unit sales where year = 1997 by product subcategory");
        execute(prev, "rollup category", "average unit sales where year = 1997 by product category");

        prev = execute("avg unit sales for year = 1997", "", "average unit sales where year = 1997");
        execute(prev, "drill down subcategory", "average unit sales where year = 1997 by product subcategory");
        execute(prev, "rollup subcategory", Lists.newArrayList(AnnotationType.BA), Lists.newArrayList(Pair.of("i0", "product_category")), "average unit sales where year = 1997 by product category");

        prev = execute("return average unit sales on 1997", "", "average unit sales where year = 1997");
        execute(prev, "add subcategory", "average unit sales where year = 1997 by product subcategory");
        execute(prev, "rollup subcategory to category", "average unit sales where year = 1997 by product category");
    }

    /**
     * Test Q2
     */
    @Test
    public void test12() {
        Mapping prev = execute("return average unit sales for Beer and Wine", "", "average unit sales where product category = Beer and Wine");
        execute(prev, "replace unit sales with maximum of store sales", "max store sales where product category = Beer and Wine");
        execute(prev, "remove Beer and Wine", "maximum store sales");
    }

    /**
     * Test Q3
     */
    @Test
    public void test13() {
        try {
            Validator.parseAndTranslate(cube, "avg unit sales category = Beer and Wine");
            Validator.parseAndTranslate(cube, "return sum of store sales for each month where category is beer and wine");
            Validator.parseAndTranslate(cube, "return sum of store sales by month where category is beer and wine");
            Validator.parseAndTranslate(cube, "return sum of store sales by month for category beer and wine");
            Validator.parseAndTranslate(cube, "return sum of store sales by month for beer and wine");
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Fix standard deviation aggregation operator
     */
    @Test
    public void test14() {
        try {
            Validator.parseAndTranslate(cube, "stdev store sales").toJSON(cube);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Fix measures
     */
    @Test
    public void test15() {
        execute("sum store sales avg store sales", "", "sum store sales avg store sales");
    }

    /**
     * Fix measures
     */
    @Test
    public void test17() {
        execute("sum store sales avg store sales", "", "sum store sales avg store sales");
    }

    /**
     * Fix drop sc by attribute
     */
    @Test
    public void test16() {
        Mapping prev = execute("avg unit sales for category beer and wine", "remove category", "avg unit sales");
        execute(prev, "replace unit sales with store cost", Lists.newArrayList(AnnotationType.MA), Lists.newArrayList(Pair.of("i0", "min")), "min store cost");
    }

    /**
     * Fix quote error
     */
    @Test
    public void test18() {
        try {
            Validator.parseAndTranslate(cube, "select average unit sales for products of category \"Beer and Wine\"").toJSON(cube);
            Validator.parseAndTranslate(cube, "select average unit sales for products of category 'Beer and Wine'").toJSON(cube);
            Validator.parseAndTranslate(cube, "select average unit sales for products of category Beer and Wine\"").toJSON(cube);
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    /**
     * Test all OLAP operator ambiguities
     */
    @Test
    public void test19() {
        execute("select the subcategory with highest unit sales", "what about gender?", "max unit sales by product subcategory gender");
    }

//  /** TODO: Need to add type checking before computing scorePFM in parsing. In this way we can distinguish between mappings with(out) annotations */
//  @Test
//  public void test19() {
//    execute("sum unit sales", "slice on gender m", "sum unit sales where gender = M");
//  }

    /**
     * Test containment
     */
    @Test
    public void testContainment() {
        final Ngram mea = new Ngram("mea", Type.MEA, new Entity("mea"), 1.0, Ngram.DUMMY_POSITION);
        final Ngram agg = new Ngram("agg", Type.AGG, new Entity("agg"), 1.0, Ngram.DUMMY_POSITION);
        final Ngram mc = new Ngram(Type.MC, Lists.newArrayList(agg, mea));
        final Ngram gpsj = new Ngram(Type.GPSJ, Lists.newArrayList(mc));

        final Ngram mea2 = new Ngram("mea", Type.MEA, new Entity("mea"), 0.9, Ngram.DUMMY_POSITION);
        final Ngram mc2 = new Ngram(Type.MC, Lists.newArrayList(mea2));
        assertTrue(Ngram.contains(gpsj, mc));
        assertTrue(Ngram.contains(gpsj, mc2, true));
    }

    /**
     * Test serialization
     *
     * @throws Exception in case of error
     */
    @Test
    public void testMappingSerialization() throws Exception {
        final Mapping prev = execute("return average unit sales on 1997", "", "average unit sales where year = 1997");

        // write mapping to Byte stream
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream outStream = new ObjectOutputStream(baos);
        outStream.writeObject(prev);
        // read mapping
        final ObjectInputStream objectInput = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        final Mapping deserialized = (Mapping) objectInput.readObject();

        assertFalse(deserialized.toJSON(cube).isEmpty());
        assertEquals(prev, deserialized);
    }

    /**
     * Test serialization
     *
     * @throws Exception in case of error
     */
    @Test
    public void testOperatorSerialization() throws Exception {
        final Mapping prev = execute("return average unit sales on 1997", "", "average unit sales where year = 1997");
        final Operator op1 = (Operator) Validator.parseAndTranslate(cube, Operator.class, prev, 0.5, Lists.newArrayList(), "add store sales").getBest();

        // write mapping to Byte stream
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream outStream = new ObjectOutputStream(baos);
        outStream.writeObject(op1);
        // read mapping
        final ObjectInputStream objectInput = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        final Operator deserialized = (Operator) objectInput.readObject();

        assertFalse(deserialized.toJSON(cube).isEmpty());
        assertEquals(op1, deserialized);
    }

    /**
     * Test serialization
     *
     * @throws Exception in case of error
     */
    @Test
    public void testSessionSerializationAndEvaluation01() throws Exception {
        final String sessionid = "foo1-" + UUID.randomUUID().toString();
        QueryGenerator.dropSession(cube, sessionid);

        QueryGenerator.saveSession(cube, sessionid, null, "read", null, null, null, null);

        final Mapping session = execute("avg unit sales on 1997", "", "average unit sales where year = 1997");
        QueryGenerator.saveSession(cube, sessionid, null, "avg unit sales on 1997", "media delle unità vendute nel 1997", "100", session, null);
        QueryGenerator.saveSession(cube, sessionid, null, "navigate", null, null, session, null);

        final Operator op2 = (Operator) Validator.parseAndTranslate(cube, Operator.class, session, 0.5, Lists.newArrayList(), "slice on beer and wine").getBest();
        op2.apply(session.bestNgram);
        QueryGenerator.saveSession(cube, sessionid, null, "slice on beer and wine", "selezione birra e vino", null, session, op2);

        QueryGenerator.saveSession(cube, sessionid, null, "reset", null, null, session, op2);

        final Mapping true_fullquery = execute("avg unit sales on 1997", "", "avg unit sales where year = 1997");
        final Mapping true_session = execute("avg unit sales on 1997 and category is Beer and Wine", "", "avg unit sales where year = 1997 and product_category = Beer and Wine");
        checkSerializedSession(sessionid, true_fullquery, true_session, 2);

        DBmanager.executeQuery(cube, "delete from OLAPsession where session_id = '" + sessionid + "'");
    }

    /**
     * Test serialization
     *
     * @throws Exception in case of error
     */
    @Test
    public void testSessionSerializationAndEvaluation02() throws Exception {
        final String sessionid = "foo-" + UUID.randomUUID().toString();
        QueryGenerator.dropSession(cube, sessionid);

        QueryGenerator.saveSession(cube, sessionid, null, "read", null, null, null, null);

        final Mapping session = execute("avg unit sales on 1997", "", "average unit sales where year = 1997");
        QueryGenerator.saveSession(cube, sessionid, null, "avg unit sales on 1997", "media delle unità vendute nel 1997", "100", session, null);
        QueryGenerator.saveSession(cube, sessionid, null, "navigate", null, null, session, null);

        final Operator op2 = (Operator) Validator.parseAndTranslate(cube, Operator.class, session, 0.5, Lists.newArrayList(), "add category").getBest();
        op2.apply(session.bestNgram);
        QueryGenerator.saveSession(cube, sessionid, null, "add category", "aggiungi categoria", null, session, op2);

        QueryGenerator.saveSession(cube, sessionid, null, "reset", null, null, session, op2);

        final Mapping true_fullquery = execute("avg unit sales on 1997", "", "avg unit sales where year = 1997");
        final Mapping true_session = execute("avg unit sales on 1997 by category", "", "avg unit sales where year = 1997 by product_category");
        checkSerializedSession(sessionid, true_fullquery, true_session, 2);

        DBmanager.executeQuery(cube, "delete from OLAPsession where session_id = '" + sessionid + "'");
    }

    @Test
    public void testSessionSerializationAndEvaluation03() throws Exception {
        final Mapping true_fullquery = execute("avg unit sales on 1997", "", "avg unit sales where year = 1997");
        final Mapping true_session = execute("avg unit sales on 1997 by category", "", "avg unit sales where year = 1997 by product_category");
        checkSerializedSession("test123@test.test_q1", true_fullquery, true_session, 3);
    }
}