package it.unibo.vocalization.generation.modules.intentiondriven

import it.unibo.conversational.algorithms.Parser
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.IGPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.VocalizationPattern
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity.argMax
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity.extendCubeWithProxy
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity.myMax
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity.tuple2string
import krangl.*

/**
 * Assess intention in action.
 */
object Assess : VocalizationModule {
    override val moduleName: String
        get() = "Assess"

    private fun label(c: Double): String {
        return if (c <= 0.9) {
            "worse than"
        } else if (c > 0.9 && c <= 1.11) {
            "tantamount to"
        } else {
            "better than"
        }
    }

    override fun compute(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val other: MutableMap<String, Any> = mutableMapOf()
        val cube = extendCubeWithProxy(cube2, cube1!!, returnAllColumns = true, other = other) // extend the cube with the proxy cells
        val df = cube.df
        val gencoord = other["gencoord"] as Set<String>
        var prevCube = (other["prevcube"] as DataFrame).sortedBy(*gencoord.toTypedArray())

        val normCube = df.sortedBy(*gencoord.toTypedArray()).groupBy(*gencoord.toTypedArray()).summarize("count" to { nrow })
        prevCube = prevCube.innerJoin(normCube, by = gencoord)
        prevCube = prevCube.addColumns(*cube2.measureNames().map { m -> "norm_$m" to { prevCube[m].div(prevCube["count"]) } }.toTypedArray())

        var enhcube = df.leftJoin(right = prevCube, by = gencoord, suffices = "" to "_bc") // and join them base on the proxy
        enhcube = enhcube
                .addColumns(*cube2.measureNames().map { m -> "diff_$m" to { enhcube[m].div(enhcube["norm_$m"]) } }.toTypedArray())
                .addColumn("score") { myMax(*(cube2.measureNames().map { m -> it["diff_$m"] }.toTypedArray())) }
                .addColumn("score_mea") { argMax(*(cube2.measureNames().map { m -> it["diff_$m"] }.toTypedArray())) }
                .sortedByDescending("score")

        val maxpec: Double = enhcube["score"].max()!!
        var text = "When compared to the previous query"
        val patterns =
            (0..2)
                .map { enhcube.row(it) }
                .map { r ->
                    text += ", the ${r["score_mea"]} of ${tuple2string(cube2, r)} is ${r[r["score_mea"]]}, " +
                            "${label(r["score"] as Double)} the average ${r["score_mea"]} of ${tuple2string(cube1, r)} that is ${r["norm_" + r["score_mea"]]}"
                    VocalizationPattern(
                        text,
                        r["score"] as Double / maxpec,
                        1.0 / df.nrow,
                        moduleName
                    ) // TODO must fix coverage
                }.toList()
        return patterns
    }

    override fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean {
        return cube1 != null && setOf(Parser.Type.DRILL).contains(operator!!.type)
    }
}