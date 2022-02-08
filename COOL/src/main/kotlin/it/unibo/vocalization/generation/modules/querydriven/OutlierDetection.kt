package it.unibo.vocalization.generation.modules.querydriven

import it.unibo.conversational.database.Config
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.IGPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.VocalizationPattern
import it.unibo.vocalization.generation.modules.querydriven.TopK.topKpatterns
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
        get() = "outlierdetection"

    override fun compute(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val cube: IGPSJ = cube2
        val path = "generated/"
        val fileName = "${UUID.randomUUID()}.csv"
        cube.df.writeCSV(File("$path$fileName"))
        computePython(Config.getPython(), path, "modules.py", fileName, cube.attributes, cube.measureNames())
        cube.df = DataFrame.readCSV(File("$path$fileName"))
        return topKpatterns(moduleName, cube, "anomaly", kpi = cube.measureNames().first()).map {
            VocalizationPattern(
                it.text.replace("highest anomaly", "anomalous ${cube.measureNames().first()}"),
                it.int,
                it.cov,
                moduleName
            )
        }
    }

    override fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean {
        return cube2.measures.size == 1 && setOf(
            "max",
            "sum",
            "avg"
        ).contains(cube2.measures.first().left.toLowerCase())
    }
}