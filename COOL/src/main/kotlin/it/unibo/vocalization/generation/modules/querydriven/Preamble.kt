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
        val groupby = "Grouped by ${cube2.attributes.reduce { a, b -> "$a, $b" }}"
        val where = if (cube2.selection.isNotEmpty()) " and filtered by ${cube2.selection.map { it.right }.reduce { a, b -> "$a, $b" }}," else ""
        val mea = " ${cube2.measureNames().map { "the average $it is ${cube2.df[it].mean()!!.round()}" }.reduce { a, b -> "$a, $b"}}"
        val text = groupby + where + mea
        return listOf(VocalizationPattern(text, 1.0001, 1.0, moduleName))
    }
}