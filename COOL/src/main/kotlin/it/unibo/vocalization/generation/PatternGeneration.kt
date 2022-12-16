package it.unibo.vocalization.generation

import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.GPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.intentiondriven.*
import it.unibo.vocalization.generation.modules.querydriven.*

fun generatePatterns(prevQuery: GPSJ?, curQuery: GPSJ, operator: Operator?, options: MutableMap<String, Any> = mutableMapOf()): List<List<IVocalizationPattern>> {
    return generatePatterns(
        prevQuery,
        curQuery,
        operator,
        listOf(
            Preamble,
            Statistics,
            TopK,
            BottomK,
            Skyline,
            OutlierDetection,
            Assess,
            Clustering,
            DomainVariance,
            AggregationVariance,
            UniformAggregationVariance,
            Correlation,
            SlicingVariance
        ),
        options
    )
}

fun generatePatterns(
    prevQuery: GPSJ?,
    curQuery: GPSJ,
    operator: Operator?,
    l: List<VocalizationModule>,
    options: MutableMap<String, Any> = mutableMapOf()
): List<List<IVocalizationPattern>> {
    options["cube"] = curQuery.cube?.factTable?: "foo"
    options["card"] = curQuery.df.nrow
    return l // list of modules
        // .parallelStream()
        .filter {
            // check conditions for applying the modules
            it.applyCondition(prevQuery, curQuery, operator)
        }.map {
            // compute the module
            val m: MutableMap<String, Any> = mutableMapOf()
            m["module"] = it.moduleName
            val startTime = System.currentTimeMillis()
            val r = it.compute(prevQuery, curQuery, operator)
            m["time"] = System.currentTimeMillis() - startTime
            m["npatterns"] = r.size
            m["selected"] = false
            m["selected_cost"] = -1
            m["selected_int"] = -1
            m["length"] = r.map { it.cost }.average()
            options.compute("acc") { _, v ->
                if (v == null) mutableListOf(m) else (v as MutableList<*>) + mutableListOf(m)
            }
            println(m)
            r
        }.toList()
}