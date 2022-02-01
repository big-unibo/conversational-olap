package it.unibo.vocalization.generation.modules

import it.unibo.conversational.Utils
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.Peculiarity.argMax
import it.unibo.vocalization.generation.modules.Peculiarity.extendCubeWithProxy
import it.unibo.vocalization.generation.modules.Peculiarity.myMax
import it.unibo.vocalization.generation.modules.Peculiarity.tuple2string
import krangl.DataFrame
import krangl.leftJoin
import krangl.max
import krangl.to

/**
 * Assess intention in action.
 */
object Assess : VocalizationModule {
    override val moduleName: String
        get() = "assess"

    private fun label(a: Double, b: Double): String {
        val c = a / (b + 1)
        if (c <= 0.9) {
            return "worse than"
        } else if (c > 0.9 && c <= 1.11) {
            return "as good as"
        } else {
            return "better than"
        }
    }

    private fun label(c: Double): String {
        return if (c <= 0.9) {
            "worse than"
        } else if (c > 0.9 && c <= 1.11) {
            "as good as"
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
        prevCube = prevCube.addColumn("count") { normCube["count"] }
        prevCube = prevCube.addColumns(*cube2.measureNames().map { m -> "norm_$m" to { prevCube[m].div(prevCube["count"]) } }.toTypedArray())

        var enhcube = df.leftJoin(right = prevCube, by = gencoord, suffices = "" to "_bc") // and join them base on the proxy
        enhcube = enhcube
                .addColumns(*cube2.measureNames().map { m -> "diff_$m" to { enhcube[m].div(enhcube["norm_$m"]) } }.toTypedArray())
                .addColumn("score") { myMax(*(cube2.measureNames().map { m -> it["diff_$m"] }.toTypedArray())) }
                .addColumn("score_mea") { argMax(*(cube2.measureNames().map { m -> it["diff_$m"] }.toTypedArray())) }
                .sortedByDescending("score")

        val maxpec: Double = enhcube["score"].max()!!
        var text = "As to assessments"
        val patterns =
                (0..2).map {
                    val r = enhcube.row(it)
                    text += ", the ${r["score_mea"]} of ${tuple2string(cube2, r)} is ${label(r["score"] as Double)} ${tuple2string(cube1, r)}"
                            // + "sold ${cube2.measureNames().map { r[it].toString() + " " + it }.reduce { a, b -> "$a, $b" }} " +
                    VocalizationPattern(text, r["score"] as Double / maxpec, 1.0 / df.nrow, moduleName) // TODO must fix coverage
                }.toList()
        return patterns
    }

    override fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean {
        return cube1 != null && setOf(Utils.Type.DRILL, Utils.Type.SAD).contains(operator!!.type)
    }
}