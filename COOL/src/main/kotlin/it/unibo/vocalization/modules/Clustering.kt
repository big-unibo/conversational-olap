package it.unibo.vocalization.modules

import it.unibo.conversational.database.Config
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.modules.Peculiarity.round
import krangl.*
import java.io.File
import java.util.*

/**
 * Describe intention in action.
 */
object Clustering : VocalizationModule {
    override val moduleName: String
        get() = "clustering"

    val fileName = "${UUID.randomUUID()}.csv"
    override fun compute(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val cube: IGPSJ = cube2
        val path = "generated/"

        cube.df.writeCSV(File("$path$fileName"))
        computePython(Config.getPython(), path, "modules.py", cube.measureNames())

        cube.df = DataFrame.readCSV(File("$path$fileName"))


        val df = cube.df.groupBy("cluster_label")
            .summarize(
                "count" to { it.nrow },
                "cluster_sil" to { it["cluster_sil"].mean() },
                *cube.measureNames().map { m -> m to { it[m].mean()!!.round() } }.toTypedArray()
            )
        var text = "Among ${df.nrow} clusters of facts"

        var i = 0
        return df
            .sortedByDescending("count")
            .rows
            .map {
                val card = it["count"].toString().toInt()
                val n = listOf("largest", "second", "third", "fourth", "fifth")[i++] // it["cluster_label"] as Int - 1
                text += ", the $n one includes ${it["count"]} facts and has ${cube.measureNames().map { m -> "${it[m]} as $m"  }.reduce{ a, b -> "$a, $b"}}"
                VocalizationPattern(text, it["cluster_sil"] as Double, 1.0 * card / cube2.df.nrow, moduleName)
            }
    }

    override fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean {
        return cube2.measures.size > 1
    }

    override fun toPythonCommand(commandPath: String, path: String, measures: Collection<String>): String {
        val fullCommand = (commandPath.replace("/", File.separator) //
                + " --path " + (if (path.contains(" ")) "\"" else "") + path.replace("\\", "/") + (if (path.contains(" ")) "\"" else "") //
                + " --file $fileName" //
                + " --module $moduleName"
                + " --measures ${measures.reduce{ a, b -> "$a,$b" }}")
        return fullCommand
    }
}