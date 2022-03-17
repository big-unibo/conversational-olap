package it.unibo.vocalization.generation.modules.intentiondriven

import it.unibo.conversational.database.Config
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.PATH
import it.unibo.vocalization.generation.modules.IGPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.VocalizationPattern
import it.unibo.vocalization.generation.modules.querydriven.Correlation
import krangl.DataFrame
import krangl.innerJoin
import krangl.readCSV
import krangl.writeCSV
import java.io.File
import java.lang.Math.abs
import java.util.*

/**
 * Describe intention in action.
 */
object SlicingVariance : VocalizationModule {
    override val moduleName: String
        get() = "SlicingVariance"

    override fun compute(c1: IGPSJ?, c2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        var df = c1!!.df.innerJoin(c2.df, by = c2.attributes)
        val mea = c2.firstMeasure()
        val fileName = "${UUID.randomUUID()}.csv"
        df.writeCSV(File("$PATH$fileName"))
        computePython(Config.getPython(), PATH, "modules.py", fileName, c2.attributes, setOf("$mea.x", "$mea.y"))
        df = DataFrame.readCSV(File("$PATH$fileName"))
        val r = df.row(0)
        val text = "After slicing, values of $mea show ${Correlation.correlation(r[Correlation.moduleName] as Double)} correlation"
        return listOf(VocalizationPattern(text, abs(r["Correlation"] as Double), 1.0, moduleName))
    }

    override fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean {
        return cube1 != null && (cube1.selection != cube2.selection) && cube2.measureNames().size == 1
    }
}