package it.unibo.vocalization.modules

import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.modules.Peculiarity.round
import org.nield.kotlinstatistics.kMeansCluster

/**
 * Describe intention in action.
 */
object Clustering : VocalizationModule {
    override val moduleName: String
        get() = "Clustering"

    override fun compute(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val m1 = cube2.measureNames().sorted()[0]
        val m2 = if (cube2.measureNames().size == 2) { cube2.measureNames().sorted()[1] } else { "foo" }
        val k = 2
        val clusters =
            cube2.df
                .select(cube2.measureNames()).rows.toList()
                .kMeansCluster(
                    k = k,
                    maxIterations = 10000,
                    xSelector = { it[m1] as Double },
                    ySelector = { if (m2 == "foo") { 1.0 } else { it[m2] as Double }
                })
        var text = "Among $k clusters of facts"
        return clusters
            .toList()
            .sortedBy { -it.points.size }
            .mapIndexed { index, cluster ->
                val n = listOf("largest", "second", "third", "fourth", "fifth")[index]
                val hasSecondMeasure = if (m2 == "foo") "" else " and ${cluster.center.y.round(2)} as $m2"
                text += ", the $n one includes ${cluster.points.size} facts and has ${cluster.center.x.round()} as $m1$hasSecondMeasure"
                VocalizationPattern(text, 1.0 * cluster.points.size / cube2.df.nrow, 1.0 * cluster.points.size / cube2.df.nrow, moduleName)
            }
    }
}