package it.unibo.vocalization

import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.generatePatterns
import it.unibo.vocalization.generation.modules.GPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.querydriven.Preamble

fun vocalize(prevQuery: GPSJ?, curQuery: GPSJ, operator: Operator?, budget: Int): List<IVocalizationPattern> {
    val allPatterns = generatePatterns(prevQuery, curQuery, operator)
    return vocalize(allPatterns, budget)
}

fun vocalize(allPatterns: List<List<IVocalizationPattern>>, budget: Int): List<IVocalizationPattern> {
    val preamble: IVocalizationPattern = allPatterns.flatten().first { it.moduleName == Preamble.moduleName }
    val budget1 = budget - preamble.cost
    return Optimizer.getPatterns(allPatterns, budget1).sortedWith(compareBy({ -it.cov }, { -it.int.toDouble() }))
}