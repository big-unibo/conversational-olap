package it.unibo.vocalization.generation

import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.GPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.intentiondriven.*
import it.unibo.vocalization.generation.modules.querydriven.*

fun generatePatterns(prevQuery: GPSJ?, curQuery: GPSJ, operator: Operator?): List<List<IVocalizationPattern>> {
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
            Correlation
        )
    )
}

fun generatePatterns(
    prevQuery: GPSJ?,
    curQuery: GPSJ,
    operator: Operator?,
    l: List<VocalizationModule>
): List<List<IVocalizationPattern>> {
    return l // list of modules
        .filter {
            // check conditions for applying the modules
            it.applyCondition(prevQuery, curQuery, operator)
        }.map {
            // compute the module
            it.compute(prevQuery, curQuery, operator)
        }
}