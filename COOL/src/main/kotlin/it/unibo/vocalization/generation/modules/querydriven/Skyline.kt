package it.unibo.vocalization.generation.modules.querydriven

import it.unibo.conversational.database.Config
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.K
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

/**
 * Describe intention in action.
 */
object Skyline : VocalizationModule {
    override val moduleName: String
        get() = "Skyline"

    override fun compute(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val cube: IGPSJ = cube2
        val fileName = "${UUID.randomUUID()}.csv"
        cube.df.writeCSV(File("$PATH$fileName"))
        computePython(Config.getPython(), PATH, "modules.py", fileName, cube.attributes, cube.measureNames())
        val df = DataFrame.readCSV(File("$PATH$fileName"))

        val patterns =
            (1..df.nrow.coerceAtMost(K)).map { // get the topk
                var text = "" // starting sentence
                var csum = 0.0
                if (it == 1) {
                    val r = df.row(it - 1)
                    if (df.nrow == 1) {
                        text += "The fact whose ${cube2.measureNames().reduce{a, b -> "$a, $b"}} are both higher than those of all other facts is ${Peculiarity.tuple2string(cube, r)}}"
                    } else {
                        text += "Among the facts whose ${cube2.measureNames().reduce{a, b -> "$a, $b"}} are both higher than those of all other facts, the most relevant one is ${Peculiarity.tuple2string(cube, r)}}"
                    }
                    csum += r["dominance"] as Double * (if (!r.contains("peculiarity")) 1.0 else r["peculiarity"] as Double)
                } else {
                    val tuples: String = (0 until it).map { df.row(it) }.map { r ->
                            val s = Peculiarity.tuple2string(cube, r)
                            csum += r["dominance"] as Double * (if (!r.contains("peculiarity")) 1.0 else r["peculiarity"] as Double)
                            s
                        }.reduce { a, b -> "$a, $b" }
                    if (df.nrow == it) {
                        text += "The facts whose ${
                            cube2.measureNames().reduce { a, b -> "$a, $b" }
                        } are both higher than those of all other facts are " + tuples
                    } else {
                        text += "Among the facts whose ${
                            cube2.measureNames().reduce { a, b -> "$a, $b" }
                        } are both higher than those of all other facts, the most relevant ones are " + tuples
                    }
                }
                VocalizationPattern(text, csum, 1.0 * it / df.nrow, moduleName)
            }.toList()
        return patterns.filter { it.int > 0 }
    }

    override fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean {
        return cube2.measures.size > 1
    }
}