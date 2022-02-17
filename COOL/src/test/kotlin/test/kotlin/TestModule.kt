package test.kotlin

import it.unibo.conversational.Validator
import it.unibo.conversational.algorithms.Parser
import it.unibo.conversational.database.Config
import it.unibo.conversational.datatypes.Mapping
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.Optimizer
import it.unibo.vocalization.generation.generatePatterns
import it.unibo.vocalization.generation.modules.GPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.intentiondriven.*
import it.unibo.vocalization.generation.modules.querydriven.*
import it.unibo.vocalization.vocalize
import krangl.dataFrameOf
import org.apache.commons.lang3.tuple.Pair
import org.apache.commons.lang3.tuple.Triple
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TestModule {

    val c1 = GPSJ(Config.getCube("SSBORA_TEST"), setOf("category"), setOf(Pair.of("sum", "quantity")), setOf())
    val c2 = GPSJ(Config.getCube("SSBORA_TEST"), setOf("product"), setOf(Pair.of("sum", "quantity")), setOf())
    val BUDGET = 120

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
        var p = vocalize(ci, cj, null, BUDGET)
        check(p)

        println("\n---\n")
        ci = cj
        cj = GPSJ(c, setOf("store_city", "product_category"), setOf(Pair.of("sum", "unit_sales")), setOf())
        p = vocalize(ci, cj, Operator(Parser.Type.DRILL), BUDGET)
        check(p)
    }

    @Test
    fun testSession02() {
        println("\n---\n")
        val c = Config.getCube("sales")
        var ci: GPSJ? = null
        var cj = GPSJ(c, setOf("store_type"), setOf(Pair.of("sum", "unit_sales")), setOf())
        var p = vocalize(ci, cj, null, BUDGET)
        check(p)

        println("\n---\n")
        ci = cj
        cj = GPSJ(c, setOf("store_type", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf())
        // generatePatterns(ci, cj, Operator(Parser.Type.DRILL), listOf(Intravariance, Univariance, Cardvariance)).flatten().forEach { println(it) }
        p = vocalize(ci, cj, Operator(Parser.Type.DRILL), BUDGET)
        check(p)

        println("\n---\n")
        ci = cj
        cj = GPSJ(c, setOf("store_type", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf(Triple.of("product_subcategory", "=", "'Beer'")))
        p = vocalize(ci, cj, Operator(Parser.Type.SAD), BUDGET)
        check(p)
        // generatePatterns(ci, cj, Operator(Parser.Type.SAD)).forEach { println(it) }
    }

    @Test
    fun testSession05() {
        println("\n---\n")
        val c = Config.getCube("sales")
        var ci: GPSJ? = null
        var cj = GPSJ(c, setOf("product_department"), setOf(Pair.of("sum", "unit_sales")), setOf())
        var p = vocalize(ci, cj, null, BUDGET)
        check(p)

        println("\n---\n")
        ci = cj
        cj = GPSJ(c, setOf("product_department", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf())
        // generatePatterns(ci, cj, Operator(Parser.Type.DRILL), listOf(Intravariance, Univariance, Cardvariance)).flatten().forEach { println(it) }
        p = vocalize(ci, cj, Operator(Parser.Type.DRILL), BUDGET)
        check(p)

        println("\n---\n")
        ci = cj
        cj = GPSJ(c, setOf("product_department", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf(Triple.of("occupation", "=", "'Professional'")))
        p = vocalize(ci, cj, Operator(Parser.Type.SAD), BUDGET)
        check(p)
        // generatePatterns(ci, cj, Operator(Parser.Type.SAD)).forEach { println(it) }
    }

    @Test
    fun testSession07() {
        println("\n---\n")
        val c = Config.getCube("sales")
        var ci: GPSJ? = null
        var cj = GPSJ(c, setOf("product_subcategory"), setOf(Pair.of("sum", "store_sales")), setOf())
        var p = vocalize(ci, cj, null, BUDGET)
        check(p)

        println("\n---\n")
        ci = cj
        cj = GPSJ(c, setOf("product_category"), setOf(Pair.of("sum", "store_sales")), setOf())
        // generatePatterns(ci, cj, Operator(Parser.Type.DRILL), listOf(Intravariance, Univariance, Cardvariance)).flatten().forEach { println(it) }
        p = vocalize(ci, cj, Operator(Parser.Type.ROLLUP), BUDGET)
        check(p)

        println("\n---\n")
        ci = cj
        cj = GPSJ(c, setOf("product_category"), setOf(Pair.of("sum", "store_sales"), Pair.of("sum", "store_cost")), setOf())
        p = vocalize(ci, cj, Operator(Parser.Type.ADD), BUDGET)
        check(p)
        // generatePatterns(ci, cj, Operator(Parser.Type.SAD)).forEach { println(it) }
    }

    @Test
    fun testSession03() {
        System.setProperty("file.encoding", "UTF-8")
        println("\n---\n")
        val c = Config.getCube("covid")
        var ci: GPSJ? = null
        var cj = GPSJ(c, setOf("country"), setOf(Pair.of("sum", "cases")), setOf())
        var p = vocalize(ci, cj, null, BUDGET)
        check(p)

        println("\n---\n")
        ci = cj
        cj = GPSJ(c, setOf("continent"), setOf(Pair.of("sum", "cases")), setOf())
        p = vocalize(ci, cj, Operator(Parser.Type.ROLLUP), BUDGET)
        check(p)

        println("\n---\n")
        ci = cj
        cj = GPSJ(c, setOf("continent"), setOf(Pair.of("sum", "cases"), Pair.of("sum", "deaths")), setOf())
        p = vocalize(ci, cj, Operator(Parser.Type.ADD), BUDGET)
        check(p)
    }



    @Test
    fun testSession04() {
        System.setProperty("file.encoding", "UTF-8")
        println("\n---\n")
        val c = Config.getCube("covid")
        var ci: GPSJ? = null
        var cj = GPSJ(c, setOf("continent"), setOf(Pair.of("sum", "cases")), setOf())
        var p = vocalize(ci, cj, null, BUDGET)
        check(p)

        println("\n---\n")
        ci = cj
        cj = GPSJ(c, setOf("country"), setOf(Pair.of("sum", "cases")), setOf())
        p = vocalize(ci, cj, Operator(Parser.Type.DRILL), BUDGET)
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
        t.forEach { println("${it.text}.\n") }
        // assertTrue(t.isNotEmpty(), "Empty patterns")
        assertTrue(t.all { p -> p.int.toDouble() in 0.0..1.0001 }, t.filter { p -> p.int.toDouble() < 0 || p.int.toDouble() > 1 }.toString())
        assertTrue(t.all { p -> p.cov in 0.0..1.0 }, t.filter { p -> p.cov < 0 || p.cov > 1 }.toString())
        if (t.isNotEmpty()) {
            Optimizer.getPatterns(listOf(t.toList()), BUDGET)
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

    @Test
    fun testIntravariance() {
        check(Intravariance.compute(c2, c1, Operator(Parser.Type.ROLLUP)))
        check(Intravariance.compute(c1, c2, Operator(Parser.Type.DRILL)))
    }

    @Test
    fun testCardvariance() {
        check(Cardvariance.compute(c2, c1, Operator(Parser.Type.ROLLUP)))
        check(Cardvariance.compute(c1, c2, Operator(Parser.Type.DRILL)))
    }

    @Test
    fun testUnivariance() {
        check(Univariance.compute(c1, c2, Operator(Parser.Type.DRILL)))
    }

    @Test
    fun testSADincrease() {
        val c = Config.getCube("sales")
        val ci = GPSJ(c, setOf("store_type", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf())
        val cj = GPSJ(c, setOf("store_type", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf(Triple.of("product_subcategory", "=", "'Beer'")))
        check(SADIncrease.compute(ci, cj, null))
        // check(SADIncrease.compute(cj, ci, null))
    }
}