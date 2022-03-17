package it.unibo.vocalization.generation.modules.querydriven

import it.unibo.conversational.database.Config
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.PATH
import it.unibo.vocalization.generation.modules.IGPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.VocalizationPattern
import krangl.DataFrame
import krangl.readCSV
import krangl.writeCSV
import java.io.File
import java.util.*
import kotlin.math.abs

/**
 * Describe intention in action.
 */
object Correlation : VocalizationModule {
    override val moduleName: String
        get() = "Correlation"

    fun correlation(x: Double): String {
        return if (x > 0.6) {
            "strong"
        } else if (x > 0.3) {
            "weak"
        } else if (x > -0.3) {
            "no"
        } else if (x > -0.6) {
            "weak inverse"
        } else {
            "strong inverse"
        }
    }


    override fun compute(c1: IGPSJ?, c2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val cube = c2

        val fileName = "${UUID.randomUUID()}.csv"
        cube.df.writeCSV(File("$PATH$fileName"))
        computePython(Config.getPython(), PATH, "modules.py", fileName, setOf(), cube.measureNames())
        val df = DataFrame.readCSV(File("$PATH$fileName")).sortedByDescending(moduleName)

        return (1..df.nrow).map { // can have multiple correlations between pairs of measures
            var text = "" // starting sentence
            var csum = 0.0
            if (it == 1) {
                val r = df.row(0)
                csum += abs(r[moduleName] as Double)
                text += "${r["m1"]} and ${r["m2"]} show ${correlation(r[moduleName] as Double)} correlation"
            } else {
                val tuples: String = (0 until it)
                    .map { df.row(it) }
                    .map { r ->
                        csum += Math.abs(r[moduleName] as Double)
                        "${r["m1"]} and ${r["m2"]} show ${correlation(r[moduleName] as Double)} correlation"
                    }.reduce { a, b -> "$a, $b" }
                text += tuples
            }
            VocalizationPattern(text, csum, 1.0, moduleName)
        }.toList()
    }

    override fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean {
        // return cube1 != null && setOf(Parser.Type.ADD, Parser.Type.DROP).contains(operator!!.type)
        return cube2.measureNames().size > 1
    }
}