package it.unibo.vocalization.generation

import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.*

fun generatePatterns(prevQuery: GPSJ?, curQuery: GPSJ, operator: Operator?): List<List<IVocalizationPattern>> {
    return listOf(Preamble, TopK, BottomK, Skyline, OutlierDetection, Assess, Clustering) // list of modules
        .filter {
            // check conditions for applying the modules
            it.applyCondition(prevQuery, curQuery, operator)
        }.map {
            // compute the module
            it.compute(prevQuery, curQuery)
        }
}