package it.unibo.vocalization.modules

import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.modules.Peculiarity.round
import krangl.mean

/**
 * Preamble module
 */
object Preamble : VocalizationModule {
    override val moduleName: String
        get() = "Preamble"

    override fun compute(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val mea = cube2.measures.first().right // get the current measure
        val mean: Double = cube2.df[mea].mean()!!.round() // get the mean of the measure
        val text = "Grouped by ${cube2.attributes.reduce { a, b -> "$a, $b" }}, the average sale is $mean"
        return listOf(VocalizationPattern(text, 1.0, 1.0, moduleName))
    }
}