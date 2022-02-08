package it.unibo.vocalization.generation.modules.querydriven

import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.IGPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.VocalizationPattern
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity.round
import krangl.mean

/**
 * Preamble module
 */
object Preamble : VocalizationModule {
    override val moduleName: String
        get() = "preamble"

    override fun compute(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val text = "Grouped by ${cube2.attributes.reduce { a, b -> "$a, $b" }}, ${cube2.measureNames().map { "the average $it is ${cube2.df[it].mean()!!.round()}" }.reduce { a, b -> "$a, $b"}}"
        return listOf(VocalizationPattern(text, 1.0, 1.0, moduleName))
    }
}