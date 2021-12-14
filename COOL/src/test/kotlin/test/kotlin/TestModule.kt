package test.kotlin

import edu.stanford.nlp.coref.hybrid.HybridCorefPrinter.df
import it.unibo.conversational.Validator
import it.unibo.conversational.algorithms.Parser
import it.unibo.conversational.database.Config
import it.unibo.conversational.datatypes.Mapping
import it.unibo.vocalization.modules.*
import krangl.dataFrameOf
import org.apache.commons.lang3.compare.ComparableUtils.ge
import org.apache.commons.lang3.tuple.Pair
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.security.enterprise.credential.Credential

class TestModule {

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
    fun test04() {
        val c1 = GPSJ(Config.getCube("SSBORA_TEST"), setOf("category"), setOf(Pair.of("sum", "quantity")), setOf())
        val c2 = GPSJ(Config.getCube("SSBORA_TEST"), setOf("product"), setOf(Pair.of("sum", "quantity")), setOf())
        val t = TopK.compute(c1, c2)
        t.forEach { println(it) }
        val a = Assess.compute(c1, c2)
        a.forEach { println(it) }
    }
}