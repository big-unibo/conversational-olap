package it.unibo.vocalization.generation.modules.intentiondriven

import it.unibo.conversational.algorithms.Parser
import it.unibo.conversational.database.Config
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.IGPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.VocalizationPattern
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity
import krangl.*
import java.io.File
import java.util.*

/**
 * Describe intention in action.
 */
object Univariance : VocalizationModule {
    override val moduleName: String
        get() = "univariance"

    override fun compute(c1: IGPSJ?, c2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val cube1 = if (operator!!.type == Parser.Type.DRILL) c1!! else c2
        val cube2 = if (operator.type == Parser.Type.DRILL) c2 else c1!!
        val cube: IGPSJ = Peculiarity.extendCubeWithProxy(cube2, cube1, returnAllColumns = true)
        val attributes = if (cube1.attributes.size == cube2.attributes.size) cube1.attributes - cube2.attributes else cube2.attributes.intersect(cube1.attributes)
        val attribute = if (cube1.attributes.size == cube2.attributes.size) (cube1.attributes - cube2.attributes).first() else (cube2.attributes - cube1.attributes).first()

        val path = "generated/"
        val fileName = "${UUID.randomUUID()}.csv"
        cube.df.writeCSV(File("$path$fileName"))
        computePython(Config.getPython(), path, "modules.py", fileName, attributes, cube.measureNames())
        val df = DataFrame.readCSV(File("$path$fileName")).filter { it[moduleName] gt 0.2 }.sortedByDescending(moduleName)

        if (df.nrow == 0) {
            return listOf()
        }

        val min = df[moduleName].min()!!
        if (min >= 0.8) {
            return listOf(
                VocalizationPattern(
                    "All $attribute have similar ${cube.measureNames().first()} values",
                    df[moduleName].mean()!!,
                    1.0,
                    moduleName
                )
            )
        }

        val sum = df[moduleName].sum()!!.toDouble()
        return (1..df.nrow).map { // get the topk
            var text = "" // starting sentence
            var csum = 0.0
            var cov = 0.0
            if (it == 1) {
                val r = df.row(it - 1)
                csum += r[moduleName] as Double
                cov += r["cov"] as Double
                text += "All $attribute in ${
                    Peculiarity.tuple2string(attributes, r)
                } have similar ${cube.measureNames().first()} values"
            } else {
                val tuples: String = (0 until it).map {
                    val r = df.row(it)
                    csum += r[moduleName] as Double
                    cov += r["cov"] as Double
                    Peculiarity.tuple2string(attributes, r)
                }.reduce { a, b -> "$a, $b" }
                text += "All $attribute in $tuples have similar ${cube.measureNames().first()} values"
            }
            VocalizationPattern(text, csum / sum, cov, moduleName)
        }.toList()
    }

    override fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean {
        return cube1 != null && setOf(Parser.Type.DRILL).contains(operator!!.type)
    }
}