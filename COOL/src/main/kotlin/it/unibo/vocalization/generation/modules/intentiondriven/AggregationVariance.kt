package it.unibo.vocalization.generation.modules.intentiondriven

import it.unibo.conversational.algorithms.Parser
import it.unibo.conversational.database.Config
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.IGPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.VocalizationPattern
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity.tuple2string
import krangl.DataFrame
import krangl.gt
import krangl.readCSV
import krangl.writeCSV
import java.io.File
import java.util.*

/**
 * Describe intention in action.
 */
object AggregationVariance : VocalizationModule {
    override val moduleName: String
        get() = "AggregationVariance"

    override fun compute(c1: IGPSJ?, c2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val cube1 = if (operator!!.type == Parser.Type.DRILL) c1!! else c2
        val cube2 = if (operator.type == Parser.Type.DRILL) c2 else c1!!
        val cube: IGPSJ = Peculiarity.extendCubeWithProxy(cube2, cube1, returnAllColumns = true)
        val attributes = if (cube1.attributes.size == cube2.attributes.size) cube1.attributes - cube2.attributes else cube2.attributes.intersect(cube1.attributes)
        val path = "generated/"
        val fileName = "${UUID.randomUUID()}.csv"
        cube.df.writeCSV(File("$path$fileName"))
        computePython(Config.getPython(), path, "modules.py", fileName, attributes, cube.measureNames())
        val df = DataFrame.readCSV(File("$path$fileName")).filter { it[moduleName] gt 0.2 }.sortedByDescending(moduleName)

        if (df.nrow == 0) {
            return listOf()
        }

        return (1..df.nrow.coerceAtMost(4)).map { // get the topk
            var text = "" // starting sentence
            var csum = 0.0
            var cov = 0.0
            if (it == 1) {
                val r = df.row(it - 1)
                csum += r[moduleName] as Double
                cov += r["cov"] as Double
                text += "The group with highest value variability of ${
                    cube.measureNames().first()
                } is ${tuple2string(attributes, r)}"
            } else {
                val tuples: String = (0 until it).map {
                    val r = df.row(it)
                    csum += r[moduleName] as Double
                    cov += r["cov"] as Double
                    tuple2string(attributes, r)
                }.reduce { a, b -> "$a, $b" }
                text += "The groups with highest value variability of ${cube.measureNames().first()} are $tuples"
            }
            VocalizationPattern(text, csum, cov, moduleName)
        }.toList()
    }

    override fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean {
        return cube1 != null && setOf(Parser.Type.DRILL, Parser.Type.ROLLUP).contains(operator!!.type)
    }
}