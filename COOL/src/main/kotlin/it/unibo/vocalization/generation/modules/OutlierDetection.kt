package it.unibo.vocalization.generation.modules

import it.unibo.conversational.database.Config
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.TopK.topKpatterns
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

    val fileName = "${UUID.randomUUID()}.csv"
    override fun compute(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val cube: IGPSJ = cube2
        val path = "generated/"

        cube.df.writeCSV(File("$path$fileName"))
        computePython(Config.getPython(), path, "modules.py", cube.measureNames())

        cube.df = DataFrame.readCSV(File("$path$fileName"))
        return topKpatterns(Skyline.moduleName, cube, "anomaly")
    }

    override fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean {
        return cube2.measures.size == 1 && setOf("max", "sum", "avg").contains(cube2.measures.first().left.toLowerCase())
    }
}