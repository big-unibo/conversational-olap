package it.unibo.vocalization

import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.PeculiarityModule.extendCubeWithProxy
import it.unibo.vocalization.PeculiarityModule.myMax
import krangl.leftJoin
import krangl.max
import krangl.to

/**
 * Describe intention in action.
 */
object AssessmentModule : VocalizationModule {
    override val moduleName: String
        get() = "Assessment"

    override fun compute(cube1: IGPSJ, cube2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val p = extendCubeWithProxy(cube2, cube1) // extend the cube with the proxy cells
        val cube = p.first.sortedBy(*cube2.attributes.toTypedArray()) // get the extended cube
        var prevCube = p.second // get the previous cube
        val gencoord = p.third // get the generalized coordinates (i.e., the ones on which the cubes are joining)

        val normCube = cube.groupBy(*gencoord.toTypedArray()).summarize("count" to { nrow })
        prevCube = prevCube.addColumn("count") { normCube["count"] }
        prevCube = prevCube.addColumns(*cube2.measureNames().map { m -> "norm_$m" to { prevCube[m].div(prevCube["count"]) } }.toTypedArray())

        var enhcube = cube.leftJoin(right = prevCube, by = gencoord, suffices = "" to "_bc") // and join them base on the proxy
        enhcube = enhcube
                .addColumns(*cube2.measureNames().map { m -> "diff_$m" to { enhcube[m].minus(enhcube["norm_$m"]) } }.toTypedArray())
                .addColumn("score") { myMax(*(cube2.measureNames().map { m -> it["diff_$m"] }.toTypedArray())) }
                .sortedByDescending("score")

        val maxpec: Double = enhcube["score"].max()!!
        var text = "As to assessment, "
        val patterns =
                (0..2).map {
                    val r = enhcube.row(it)
                    text += "the tuple ${cube2.attributes.map { r[it].toString() }.reduce { a, b -> "$a, $b" }} " +
                            "sold ${cube2.measureNames().map { r[it].toString() + " " + it }.reduce { a, b -> "$a, $b" }} " +
                            "which accounts for ${cube2.measureNames().map { (r[it] as Double / r["${it}_bc"] as Double * 100).toInt().toString() + "% of the $it of its parent ${cube1.attributes.map { r[it] }.reduce { a, b -> "$a, $b" }}" }.reduce { a, b -> "$a, $b" }}; "
                    VocalizationPattern(text, r["score"] as Double / maxpec, text.length, moduleName)
                }.toList()
        return patterns
    }
}