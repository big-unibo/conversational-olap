package it.unibo.vocalization.generation

import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.GPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.intentiondriven.*
import it.unibo.vocalization.generation.modules.querydriven.Clustering
import it.unibo.vocalization.generation.modules.querydriven.OutlierDetection
import it.unibo.vocalization.generation.modules.querydriven.Preamble
import it.unibo.vocalization.generation.modules.querydriven.Skyline
import kotlin.streams.toList

fun generatePatterns(prevQuery: GPSJ?, curQuery: GPSJ, operator: Operator?, options: MutableMap<String, Any> = mutableMapOf()): List<List<IVocalizationPattern>> {
    return generatePatterns(
        prevQuery,
        curQuery,
        operator,
        listOf(
            Preamble,
            // TopK,
            // BottomK,
            Skyline,
            OutlierDetection,
            Assess,
            Clustering,
            Cardvariance,
            Intravariance,
            Univariance,
            Correlation,
            SADIncrease
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
    options["cube"] = curQuery.cube!!.factTable
    options["card"] = curQuery.df.nrow
    return l // list of modules
        .parallelStream()
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
            options.compute(
                "acc",
                { k, v -> if (v == null) mutableListOf(m) else (v as MutableList<MutableMap<String, Any>>) + mutableListOf(m) }
            )
            println(m)
            r
        }.toList()
}