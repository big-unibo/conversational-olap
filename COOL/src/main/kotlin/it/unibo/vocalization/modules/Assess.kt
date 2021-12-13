package it.unibo.vocalization.modules

import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.modules.Peculiarity.extendCubeWithProxy
import it.unibo.vocalization.modules.Peculiarity.myMax
import krangl.DataFrame
import krangl.leftJoin
import krangl.max
import krangl.to

/**
 * Assess intention in action.
 */
object Assess : VocalizationModule {
    override val moduleName: String
        get() = "Assess"

    override fun compute(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val other: MutableMap<String, Any> = mutableMapOf()
        val cube = extendCubeWithProxy(cube2, cube1!!, other) // extend the cube with the proxy cells
        val df = cube.df
        val gencoord = other["gencoord"] as Set<String>
        var prevCube = other["prevcube"] as DataFrame

        val normCube = df.groupBy(*gencoord.toTypedArray()).summarize("count" to { nrow })
        prevCube = prevCube.addColumn("count") { normCube["count"] }
        prevCube = prevCube.addColumns(*cube2.measureNames().map { m -> "norm_$m" to { prevCube[m].div(prevCube["count"]) } }.toTypedArray())

        var enhcube = df.leftJoin(right = prevCube, by = gencoord, suffices = "" to "_bc") // and join them base on the proxy
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
                    VocalizationPattern(text, r["score"] as Double / maxpec, 1.0, text.length, moduleName) // TODO must fix coverage
                }.toList()
        return patterns
    }
}