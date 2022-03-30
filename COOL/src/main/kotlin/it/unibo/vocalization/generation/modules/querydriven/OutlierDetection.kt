package it.unibo.vocalization.generation.modules.querydriven

import it.unibo.conversational.database.Config
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.K
import it.unibo.vocalization.PATH
import it.unibo.vocalization.generation.modules.IGPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.VocalizationPattern
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity.round
import krangl.DataFrame
import krangl.readCSV
import krangl.writeCSV
import java.io.File
import java.util.*

/**
 * Describe intention in action.
 */
object OutlierDetection : VocalizationModule {
    override val moduleName: String
        get() = "OutlierDetection"

    override fun compute(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val cube: IGPSJ = cube2
        val fileName = "${UUID.randomUUID()}.csv"
        cube.df.writeCSV(File("$PATH$fileName"))
        computePython(Config.getPython(), PATH, "modules.py", fileName, cube.attributes, cube.measureNames())
        val df = DataFrame.readCSV(File("$PATH$fileName")).sortedByDescending("anomaly")
        val mea = cube.measureNames().first()

        return (1 until df.nrow.coerceAtMost(K)).map {
            var text = "" // starting sentence
            var csum = 0.0
            if (it == 1) {
                val r = df.row(0)
                csum += r["anomaly"] as Double * (if (!r.contains("peculiarity")) 1.0 else r["peculiarity"] as Double)
                text += "${Peculiarity.tuple2string(cube, r)} with ${(r[mea] as Double).round()} is the most anomalous fact"
            } else {
                val tuples: String = (0 until it)
                    .map { df.row(it) }
                    .map { r ->
                        csum += r["anomaly"] as Double * (if (!r.contains("peculiarity")) 1.0 else r["peculiarity"] as Double)
                        Peculiarity.tuple2string(cube, r) + " with " + (r[mea] as Double).round()
                    }.reduce { a, b -> "$a, $b" }
                text += "$tuples are the most anomalous facts"
            }
            VocalizationPattern(text, csum, 1.0 * it / df.nrow, moduleName)
        }.toList().filter { it.int > 0 }
    }

    override fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean {
        return cube2.measures.size == 1 && setOf("max", "sum", "avg").contains(cube2.measures.first().left.toLowerCase())
    }
}