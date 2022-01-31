package test.kotlin

import it.unibo.conversational.Validator
import it.unibo.conversational.algorithms.Parser
import it.unibo.conversational.database.Config
import it.unibo.conversational.datatypes.Mapping
import it.unibo.vocalization.modules.*
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
        Assess.compute(cube1, cube2)
        Clustering.compute(cube1, cube2)
        TopK.compute(cube1, cube1)
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
            "Beer", 35.0,
            "Wine", 32.0,
            "Cola", 30.0,
            "Pizza", 6.0,
            "Bread", 5.0
        )
        val c = GPSJ(df, setOf("product"), setOf(Pair.of("sum", "quantity")), setOf())
        val p = Preamble.compute(null, c)
        p.forEach { println(it) }
        val t = TopK.compute(null, c)
        t.forEach { println(it) }
        val cl = Clustering.compute(null, c)
        cl.forEach { println(it) }
    }

    @Test
    fun testClustering() {
        var t = Clustering.compute(null, c2)
        t.forEach { println(it) }
        assertTrue(t.all { p -> p.int.toDouble() > 0 })
        t = Clustering.compute(c1, c2)
        t.forEach { println(it) }
        assertTrue(t.all { p -> p.int.toDouble() > 0 })
    }

    @Test
    fun testAssess() {
        val t = Assess.compute(c1, c2)
        t.forEach { println(it) }
        assertTrue(t.all { p -> p.int.toDouble() > 0 })
    }

    @Test
    fun testSkyline() {
        var t = Skyline.compute(null, c2)
        t.forEach { println(it) }
        assertTrue(t.all { p -> p.int.toDouble() > 0 })
        t = Skyline.compute(c1, c2)
        t.forEach { println(it) }
        assertTrue(t.all { p -> p.int.toDouble() > 0 })
    }

    @Test
    fun testOutlierDetection() {
        var t = OutlierDetection.compute(null, c2)
        t.forEach { println(it) }
        assertTrue(t.all { p -> p.int.toDouble() > 0 })
        t = OutlierDetection.compute(c1, c2)
        t.forEach { println(it) }
        assertTrue(t.all { p -> p.int.toDouble() > 0 })
    }

    @Test
    fun testTopK() {
        var t = TopK.compute(null, c2)
        t.forEach { println(it) }
        assertTrue(t.all { p -> p.int.toDouble() > 0 })
        t = TopK.compute(c1, c2)
        t.forEach { println(it) }
        // assertTrue(t.all { p -> p.int.toDouble() > 0 })
    }

    @Test
    fun testBottomK() {
        var t = BottomK.compute(null, c2)
        t.forEach { println(it) }
        assertTrue(t.all { p -> p.int.toDouble() > 0 })
        t = BottomK.compute(c1, c2)
        t.forEach { println(it) }
        assertTrue(t.all { p -> p.int.toDouble() > 0 })
    }
}