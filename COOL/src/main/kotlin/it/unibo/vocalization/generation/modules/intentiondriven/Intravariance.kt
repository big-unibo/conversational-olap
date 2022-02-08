package it.unibo.vocalization.generation.modules.intentiondriven

import it.unibo.conversational.algorithms.Parser
import it.unibo.conversational.database.Config
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.IGPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.VocalizationPattern
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity
import krangl.DataFrame
import krangl.readCSV
import krangl.writeCSV
import java.io.File
import java.util.*

/**
 * Describe intention in action.
 */
object Intravariance : VocalizationModule {
    override val moduleName: String
        get() = "intravariance"

    override fun compute(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val cube: IGPSJ = cube2
        val attributes =
            if (operator!!.type == Parser.Type.DRILL) {
                cube1!!.attributes - cube2.attributes
            } else if (operator.type == Parser.Type.ROLLUP) {
                cube2.attributes - cube1!!.attributes
            } else {
                cube2.attributes
            }
        val path = "generated/"
        val fileName = "${UUID.randomUUID()}.csv"
        cube.df.writeCSV(File("$path$fileName"))
        computePython(Config.getPython(), path, "modules.py", fileName, attributes, cube.measureNames())
        cube.df = DataFrame.readCSV(File("$path$fileName"))
        val df = cube.df.sortedByDescending(moduleName)

        return (1..df.nrow).map { // get the topk
            var text = "" // starting sentence
            var csum = 0.0
            var cov = 0.0
            if (it == 1) {
                val r = df.row(it - 1)
                csum += r[moduleName] as Double
                cov += r["cov"] as Double
                text += "The group with highest deviation of ${
                    cube.measureNames().first()
                } is ${Peculiarity.tuple2string(attributes, r)}} "
            } else {
                val tuples: String = (0 until it).map {
                    val r = df.row(it)
                    csum += r[moduleName] as Double
                    cov += r["cov"] as Double
                    Peculiarity.tuple2string(attributes, r)
                }.reduce { a, b -> "$a, $b" }
                text += "The groups with highest deviation of ${cube.measureNames().first()} are $tuples"
            }
            VocalizationPattern(text, csum / it, cov, moduleName)
        }.toList()
    }

    override fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean {
        return cube1 != null
    }
}