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
import java.io.File

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
        check(TopK.compute(cube1, cube2))
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
    fun testUserSession01() {
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
    fun testUserSession02() {
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
            "Beer", 80.0, "Wine", 70.0, "Cola", 30.0, "Bagel", 8.0, "Pizza", 6.0, "Bread", 5.0
        )
        val c = GPSJ(df, setOf("product"), setOf(Pair.of("sum", "quantity")), setOf())
        // check(Statistics.compute(null, c))
        // check(TopK.compute(null, c))
        // check(Clustering.compute(null, c))
        var p = generatePatterns(null, c, null).flatten()
        check(p)
        // p = vocalize(null, c, null, BUDGET)
        // check(p)
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
        t.forEach { println("${it}.") }
        if (t.isNotEmpty()) {
            assertTrue(t.first().int.toDouble() in 0.0..1.001)
        }
        assertTrue(t.all { p -> p.int.toDouble() >= 0 }, t.filter { p -> p.int.toDouble() < 0 }.toString())
        assertTrue(t.all { p -> p.cov in 0.0..1.001 }, t.filter { p -> p.cov < 0 || p.cov > 1 }.toString())
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
        check(AggregationVariance.compute(c2, c1, Operator(Parser.Type.ROLLUP)))
        check(AggregationVariance.compute(c1, c2, Operator(Parser.Type.DRILL)))
    }

    @Test
    fun testCardvariance() {
        check(DomainVariance.compute(c2, c1, Operator(Parser.Type.ROLLUP)))
        check(DomainVariance.compute(c1, c2, Operator(Parser.Type.DRILL)))
    }

    @Test
    fun testUnivariance() {
        check(UniformAggregationVariance.compute(c1, c2, Operator(Parser.Type.DRILL)))
    }

    @Test
    fun testSADincrease() {
        val c = Config.getCube("sales")
        val ci = GPSJ(c, setOf("store_type", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf())
        val cj = GPSJ(c, setOf("store_type", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf(Triple.of("product_subcategory", "=", "'Beer'")))
        check(SlicingVariance.compute(ci, cj, null))
        check(SlicingVariance.compute(cj, ci, null))
    }

    @Test
    fun testScalability() {
        val c = Config.getCube("sales")

        val fileName = "resources/vool_stats.csv"
        val myFile = File(fileName)
        if (myFile.exists()) myFile.delete()
        var first = true
        var x = 0

        (0..2).forEach { seed: Int ->
            listOf(100, 1000, 10000).forEach { limit ->
                val l =
                    listOf(
                        listOf(
                            Pair(null, GPSJ(c, setOf("product_subcategory"), setOf(Pair.of("sum", "store_sales")), setOf(), limit)),
                            Pair(Operator(Parser.Type.ROLLUP), GPSJ(c, setOf("product_category"), setOf(Pair.of("sum", "store_sales")), setOf(), limit)),
                            Pair(Operator(Parser.Type.ADD), GPSJ(c, setOf("product_category"), setOf(Pair.of("sum", "store_sales"), Pair.of("sum", "store_cost")), setOf(), limit))
                        ),
                        listOf(
                            Pair(null, GPSJ(c, setOf("product_id"), setOf(Pair.of("sum", "store_sales")), setOf(), limit)),
                            Pair(Operator(Parser.Type.ROLLUP), GPSJ(c, setOf("product_subcategory"), setOf(Pair.of("sum", "store_sales")), setOf(), limit)),
                            Pair(Operator(Parser.Type.ADD), GPSJ(c, setOf("product_subcategory"), setOf(Pair.of("sum", "store_sales"), Pair.of("sum", "store_cost")), setOf(), limit))
                        ),
                        listOf(
                            Pair(null, GPSJ(c, setOf("product_name", "the_month"), setOf(Pair.of("sum", "unit_sales")), setOf(), limit)),
                            Pair(Operator(Parser.Type.DRILL), GPSJ(c, setOf("product_name", "the_month", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf(), limit)),
                            Pair(Operator(Parser.Type.SAD), GPSJ(c, setOf("product_name", "the_month", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf(Triple.of("occupation", "=", "'Professional'")), limit)),
                            Pair(Operator(Parser.Type.ADD), GPSJ(c, setOf("product_name", "the_month", "gender"), setOf(Pair.of("sum", "store_sales"), Pair.of("sum", "store_cost")), setOf(Triple.of("occupation", "=", "'Professional'")), limit))
                        ),
                        listOf(
                            Pair(null, GPSJ(c, setOf("product_name"), setOf(Pair.of("sum", "unit_sales")), setOf(), limit)),
                            Pair(Operator(Parser.Type.DRILL), GPSJ(c, setOf("product_name", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf(), limit)),
                            Pair(Operator(Parser.Type.SAD), GPSJ(c, setOf("product_name", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf(Triple.of("occupation", "=", "'Professional'")), limit)),
                        ),
                        listOf(
                            Pair(null, GPSJ(c, setOf("product_subcategory"), setOf(Pair.of("sum", "unit_sales")), setOf(), limit)),
                            Pair(Operator(Parser.Type.DRILL), GPSJ(c, setOf("product_subcategory", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf(), limit)),
                            Pair(Operator(Parser.Type.SAD), GPSJ(c, setOf("product_subcategory", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf(Triple.of("occupation", "=", "'Professional'")), limit))
                        ),
                        listOf(
                            Pair(null, GPSJ(c, setOf("product_category"), setOf(Pair.of("sum", "unit_sales")), setOf(), limit)),
                            Pair(Operator(Parser.Type.DRILL), GPSJ(c, setOf("product_category", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf(), limit)),
                            Pair(Operator(Parser.Type.SAD), GPSJ(c, setOf("product_category", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf(Triple.of("occupation", "=", "'Professional'")), limit))
                        ),
                    )

                l.forEachIndexed { j, l ->
                    l.forEachIndexed { i , _ ->
                        val options = mutableMapOf<String, Any>()
                        options["limit"] = limit
                        options["seed"] = seed
                        options["uid"] = x++
                        options["sessionid"] = j
                        val ci = if (i == 0) { null } else { l[i - 1].second }
                        val cj = l[i].second
                        cj.df // compute the cube out of the patterns, so that query time is not counted
                        vocalize(ci, cj, l[i].first, 120, options)
                        val m: List<MutableMap<String, Any>> = options.remove("acc")!! as List<MutableMap<String, Any>>
                        options.forEach { k, v -> m.forEach { it[k] = v } }
                        if (first) {
                            val header: String = m[0].entries.sortedBy { it.key }.map { it.key }.reduce { a, b -> "$a,$b" }
                            File(fileName).appendText(header + "\n")
                            first = false
                        }
                        m.forEach { options ->
                            val data: String = options.entries.sortedBy { it.key }.map { it.value.toString() }.reduce { a, b -> "$a,$b" }
                            File(fileName).appendText(data + "\n")
                        }
                    }
                }
            }
        }
    }
}