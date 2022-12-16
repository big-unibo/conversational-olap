package it.unibo.vocalization.generation.modules.querydriven

import it.unibo.conversational.database.Config
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.IGPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.VocalizationPattern
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity.round
import krangl.*
import java.io.File
import java.util.*

/**
 * Describe intention in action.
 */
object Clustering : VocalizationModule {
    override val moduleName: String
        get() = "Clustering"

    override fun compute(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val cube: IGPSJ = cube2
        val path = "generated/"
        val fileName = "${UUID.randomUUID()}.csv"
        cube.df.writeCSV(File("$path$fileName"))
        computePython(Config.getPython(), path, "modules.py", fileName, cube.attributes, cube.measureNames())
        val df = DataFrame.readCSV(File("$path$fileName"))
            .groupBy("cluster_label")
            .summarize(
                "count" to { it.nrow },
                "cluster_sil" to { it["cluster_sil"].mean() },
                *cube.measureNames().map { m -> m to { it[m].mean()!!.round() } }.toTypedArray()
            )
        var text = "Facts can be grouped into ${df.nrow} clusters"
        var cumSil = 0.0
        var cumCard = 0.0
        return df
            .sortedByDescending("count")
            .rows
            .filterIndexed { i, _ -> i < 3 }
            .mapIndexed { i, r ->
                cumCard += r["count"].toString().toInt()
                val n = listOf("largest", "second", "third", "fourth", "fifth")[i] // it["cluster_label"] as Int - 1
                text += ", the $n has ${r["count"]} facts and ${cube.measureNames().map { m -> "${r[m]} as average $m" }.reduce { a, b -> "$a, $b" }}"
                cumSil += r["cluster_sil"] as Double
                VocalizationPattern(text, cumSil, 1.0 * cumCard / cube2.df.nrow, moduleName)
            }
    }
}