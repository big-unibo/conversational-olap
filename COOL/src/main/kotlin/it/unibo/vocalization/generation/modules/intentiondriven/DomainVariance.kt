package it.unibo.vocalization.generation.modules.intentiondriven

import it.unibo.conversational.algorithms.Parser
import it.unibo.conversational.database.Config
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.PATH
import it.unibo.vocalization.generation.modules.IGPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.VocalizationPattern
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity
import krangl.DataFrame
import krangl.max
import krangl.readCSV
import krangl.writeCSV
import java.io.File
import java.util.*

/**
 * Describe intention in action.
 */
object DomainVariance : VocalizationModule {
    override val moduleName: String
        get() = "DomainVariance"

    override fun compute(c1: IGPSJ?, c2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val cube1 = if (operator!!.type == Parser.Type.DRILL) c1!! else c2
        val cube2 = if (operator.type == Parser.Type.DRILL) c2 else c1!!
        val cube: IGPSJ = Peculiarity.extendCubeWithProxy(cube2, cube1, returnAllColumns = true)
        val attributes = if (cube1.attributes.size == cube2.attributes.size) cube1.attributes - cube2.attributes else cube2.attributes.intersect(cube1.attributes)
        val attribute: String = (c2.attributes - c1!!.attributes).first()
        val prevAttributes = if (cube1.attributes.size == cube2.attributes.size) cube2.attributes - cube1.attributes else cube2.attributes - cube2.attributes.intersect(cube1.attributes)
        val fileName = "${UUID.randomUUID()}.csv"
        cube.df.writeCSV(File("$PATH$fileName"))
        computePython(Config.getPython(), PATH, "modules.py", fileName, attributes, cube.measureNames())
        val df = DataFrame.readCSV(File("$PATH$fileName"))
        val variation = df[moduleName].max()!!
        val level = if (variation > 0.7) "high" else if (variation > 0.3) "low" else "no"
        return listOf(VocalizationPattern("There is a $level degree of variation in the number of members of ${prevAttributes.reduce{a, b -> "$a, $b"}} for each $attribute", df[moduleName].max()!!, 1.0, moduleName))
    }

    override fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean {
        return cube1 != null && setOf(Parser.Type.DRILL, Parser.Type.ROLLUP).contains(operator!!.type)
    }
}