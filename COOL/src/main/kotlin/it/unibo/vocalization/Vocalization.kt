package it.unibo.vocalization

import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.generatePatterns
import it.unibo.vocalization.generation.modules.GPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.querydriven.Preamble

fun vocalize(prevQuery: GPSJ?, curQuery: GPSJ, operator: Operator?, budget: Int, options: MutableMap<String, Any> = mutableMapOf()): List<IVocalizationPattern> {
    val allPatterns = generatePatterns(prevQuery, curQuery, operator, options)
    return vocalize(allPatterns, budget, options)
}

fun vocalize(allPatterns: List<List<IVocalizationPattern>>, budget: Int, options: MutableMap<String, Any> = mutableMapOf()): List<IVocalizationPattern> {
    val preamble: List<IVocalizationPattern> = allPatterns.first { it.any { it.moduleName == Preamble.moduleName } }
    val startTime = System.currentTimeMillis()
    val r = Optimizer.getPatterns(allPatterns - listOf(preamble), budget)
    val m: MutableMap<String, Any> = mutableMapOf()
    m["npatterns"] = r.size
    m["module"] = "Mckp"
    m["length"] = -1
    m["time"] = System.currentTimeMillis() - startTime
    options.compute("acc", { k, v -> if (v == null) mutableListOf(m) else (v as MutableList<MutableMap<String, Any>>) + mutableListOf(m) })
    return listOf(preamble).first() + r.sortedWith(compareBy({ -it.cov }, { -it.int.toDouble() }))
}