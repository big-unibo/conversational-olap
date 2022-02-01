package test.kotlin

import it.unibo.conversational.Validator
import it.unibo.conversational.algorithms.Parser
import it.unibo.conversational.database.Config
import it.unibo.conversational.datatypes.Mapping
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.Optimizer
import it.unibo.vocalization.generation.modules.*
import it.unibo.vocalization.vocalize
import krangl.dataFrameOf
import org.apache.commons.lang3.tuple.Pair
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TestModule {

    val c1 = GPSJ(Config.getCube("SSBORA_TEST"), setOf("category"), setOf(Pair.of("sum", "quantity")), setOf())
    val c2 = GPSJ(Config.getCube("SSBORA_TEST"), setOf("product"), setOf(Pair.of("sum", "quantity")), setOf())

    @Test
    fun test01() {
        val c = Config.getCube("sales")
        val cube1 = GPSJ(c, setOf("product_category", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf())
        val cube2 = GPSJ(c, setOf("product_subcategory", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf())
        check(Assess.compute(cube1, cube2))
        check(Clustering.compute(cube1, cube2))
        check(TopK.compute(cube1, cube1))
    }

    @Test
    fun testSession01() {
        println("\n---\n")
        val c = Config.getCube("sales")
        var ci: GPSJ? = null
        var cj = GPSJ(c, setOf("store_city"), setOf(Pair.of("sum", "unit_sales")), setOf())
        var p = vocalize(ci, cj, null, 60)
        check(p)

        println("\n---\n")
        ci = cj
        cj = GPSJ(c, setOf("store_city", "product_category"), setOf(Pair.of("sum", "unit_sales")), setOf())
        p = vocalize(ci, cj, Operator(Parser.Type.DRILL), 120)
        check(p)
    }

    @Test
    fun testSession02() {
        println("\n---\n")
        val c = Config.getCube("sales")
        var ci: GPSJ? = null
        var cj = GPSJ(c, setOf("store_type"), setOf(Pair.of("sum", "unit_sales")), setOf())
        var p = vocalize(ci, cj, null, 60)
        check(p)

        println("\n---\n")
        ci = cj
        cj = GPSJ(c, setOf("store_type", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf())
        p = vocalize(ci, cj, Operator(Parser.Type.DRILL), 120)
        check(p)
    }

    @Test
    fun test02() {
        val cube = Config.getCube("sales_fact_1997")
        val mapping: Mapping = Validator.parseAndTranslate(cube, "sum unit_sales by product_category")
        val c = Parser.getClauses(cube, mapping.bestNgram)
        assertFalse(c.left.isEmpty())
        assertFalse(c.middle.isEmpty())
        assertTrue(c.right.isEmpty())
    }

    @Test
    fun test03() {
        val df = dataFrameOf("PRODUCT", "QUANTITY")(
            "Beer", 35.0, "Wine", 32.0, "Cola", 30.0, "Pizza", 6.0, "Bread", 5.0
        )
        val c = GPSJ(df, setOf("product"), setOf(Pair.of("sum", "quantity")), setOf())
        check(Preamble.compute(null, c))
        check(TopK.compute(null, c))
        check(Clustering.compute(null, c))
    }

    @Test
    fun testClustering() {
        check(Clustering.compute(null, c2))
        check(Clustering.compute(c1, c2))
    }

    @Test
    fun testAssess() {
        check(Assess.compute(c1, c2))
    }

    @Test
    fun testSkyline() {
        check(Skyline.compute(null, c2))
        check(Skyline.compute(c1, c2))
    }

    @Test
    fun testOutlierDetection() {
        check(OutlierDetection.compute(null, c2))
        check(OutlierDetection.compute(c1, c2))
    }

    fun check(t: Collection<IVocalizationPattern>) {
        t.forEach { println(it) }
        // assertTrue(t.isNotEmpty(), "Empty patterns")
        assertTrue(t.all { p -> p.int.toDouble() in 0.0..1.0 }, t.filter { p -> p.int.toDouble() < 0 || p.int.toDouble() > 1 }.toString())
        assertTrue(t.all { p -> p.cov in 0.0..1.0 }, t.filter { p -> p.cov < 0 || p.cov > 1 }.toString())
        if (t.isNotEmpty()) {
            Optimizer.getPatterns(listOf(t.toList()), 120)
        }
    }

    @Test
    fun testTopK() {
        check(TopK.compute(null, c2))
        check(TopK.compute(c1, c2))
    }

    @Test
    fun testBottomK() {
        check(BottomK.compute(null, c2))
        check(BottomK.compute(c1, c2))
    }
}