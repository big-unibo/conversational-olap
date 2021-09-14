package test.kotlin

import it.unibo.conversational.Validator
import it.unibo.conversational.algorithms.Parser
import it.unibo.conversational.database.Config
import it.unibo.conversational.datatypes.Mapping
import it.unibo.vocalization.AssessmentModule
import it.unibo.vocalization.DescribeModule
import it.unibo.vocalization.GPSJ
import it.unibo.vocalization.PeculiarityModule
import it.unibo.vocalization.web.MainServlet
import org.apache.commons.lang3.tuple.Pair
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TestModule {

    @Test
    fun test01() {
        val c = Config.getCube("sales")
        val cube1 = GPSJ(c, setOf("product_category", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf())
        val cube2 = GPSJ(c, setOf("product_subcategory", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf())
        AssessmentModule.compute(cube1, cube2)
        PeculiarityModule.compute(cube1, cube2)
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
        val c = Config.getCube("sales")
        val cube1 = GPSJ(c, setOf("product_category", "gender"), setOf(Pair.of("sum", "unit_sales")), setOf())
        DescribeModule.compute(cube1, cube1)
    }
}