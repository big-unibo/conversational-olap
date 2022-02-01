package it.unibo.vocalization.generation.modules

import it.unibo.conversational.database.Config
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.Peculiarity.round
import krangl.*
import java.io.File
import java.util.*

/**
 * Describe intention in action.
 */
object Clustering : VocalizationModule {
    override val moduleName: String
        get() = "clustering"

    override fun compute(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val cube: IGPSJ = cube2
        val path = "generated/"
        val fileName = "${UUID.randomUUID()}.csv"
        cube.df.writeCSV(File("$path$fileName"))
        computePython(Config.getPython(), path, "modules.py", fileName, cube.measureNames())
        cube.df = DataFrame.readCSV(File("$path$fileName"))
        val df = cube.df.groupBy("cluster_label")
            .summarize(
                "count" to { it.nrow },
                "cluster_sil" to { it["cluster_sil"].mean() },
                *cube.measureNames().map { m -> m to { it[m].mean()!!.round() } }.toTypedArray()
            )
        var text = "Among ${df.nrow} clusters"
        var i = 0
        var cumSil = 0.0
        return df
            .sortedByDescending("count")
            .rows
            .map {
                val card = it["count"].toString().toInt()
                val n = listOf("largest", "second", "third", "fourth", "fifth")[i++] // it["cluster_label"] as Int - 1
                text += ", the $n one includes ${it["count"]} facts and has ${cube.measureNames().map { m -> "${it[m]} as $m"  }.reduce{ a, b -> "$a, $b"}}"
                cumSil += it["cluster_sil"] as Double
                VocalizationPattern(text, cumSil / i, 1.0 * card / cube2.df.nrow, moduleName)
            }
    }
}