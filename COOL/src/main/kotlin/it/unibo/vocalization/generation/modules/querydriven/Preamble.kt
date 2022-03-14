package it.unibo.vocalization.generation.modules.querydriven

import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.IGPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.VocalizationPattern

/**
 * Preamble module
 */
object Preamble : VocalizationModule {
    override val moduleName: String
        get() = "Preamble"

    override fun compute(cube1: IGPSJ?, curQuery: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val mea = "The query returns the ${curQuery.measures.map { "${it.left} of ${it.right}" }.reduce { a, b -> "$a, $b" }}"
        val groupby = " grouped by ${curQuery.attributes.reduce { a, b -> "$a, $b" }}"
        val where = if (curQuery.selection.isNotEmpty()) " and filtered by ${ curQuery.selection.map { it.right }.reduce { a, b -> "$a, $b" } }," else ""
        return listOf(VocalizationPattern(mea + groupby + where, 1.001, 1.001, moduleName))
    }
}