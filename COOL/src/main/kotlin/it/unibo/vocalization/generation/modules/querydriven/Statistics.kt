package it.unibo.vocalization.generation.modules.querydriven

import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.IGPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.VocalizationPattern
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity.round
import krangl.max
import krangl.mean
import krangl.min
import krangl.sd

/**
 * Statistics module
 */
object Statistics : VocalizationModule {
    override val moduleName: String
        get() = "Statistics"

    override fun compute(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val dict = mutableMapOf<String, Double>()
        cube2.measureNames().forEach{
            dict["${it}_mean"] = cube2.df[it].mean()!!.round().toDouble()
            dict["${it}_std"] = cube2.df[it].sd()!!.round().toDouble()
            dict["${it}_min"] = cube2.df[it].min()!!.round().toDouble()
            dict["${it}_max"] = cube2.df[it].max()!!.round().toDouble()
        }
        val mea = cube2.measureNames().map { "the average $it is ${dict["${it}_mean"]}" }.reduce { a, b -> "$a, $b"}
        val mea2 = cube2.measureNames().map { "the average $it is ${dict["${it}_mean"]} and ranges between ${dict["${it}_min"]} and ${dict["${it}_max"]}" }.reduce { a, b -> "$a, $b"}
        var int: Double = 1 - cube2.measureNames().map { dict["${it}_std"]!! / dict["${it}_mean"]!! }.average().round().toDouble()
        int = if (int < 0) 0.0 else int

        return listOf(
            VocalizationPattern(mea[0].toUpperCase() + mea.substring(1), int, 1.001, moduleName),
            VocalizationPattern(mea2[0].toUpperCase() + mea2.substring(1), int, 1.001, moduleName)
        )
    }
}