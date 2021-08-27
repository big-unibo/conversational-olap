package test.kotlin

import it.unibo.conversational.Validator
import it.unibo.conversational.database.Config
import it.unibo.vocalization.AssessmentModule
import it.unibo.vocalization.GPSJ
import it.unibo.vocalization.IGPSJ
import it.unibo.vocalization.PeculiarityModule
import org.apache.commons.lang3.tuple.Triple
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestModule {

    @Test
    @Throws(Exception::class)
    fun test01() {
        val c = Config.getCube("sales")
        val cube1 = GPSJ(c, setOf("product_category", "gender"), setOf("unit_sales"), setOf())
        val cube2 = GPSJ(c, setOf("product_subcategory", "gender"), setOf("unit_sales"), setOf())
        AssessmentModule.compute(cube1, cube2)
        PeculiarityModule.compute(cube1, cube2)
    }
}