package it.unibo.vocalization.modules

import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.modules.Peculiarity.round
import it.unibo.vocalization.modules.Peculiarity.tuple2string
import krangl.sum

/**
 * Describe intention in action.
 */
object TopK : VocalizationModule {
    override val moduleName: String
        get() = "Top-K"

    override fun compute(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val cube: IGPSJ = if (cube1 != null) { Peculiarity.extendCubeWithProxy(cube2, cube1) } else { cube2 }
        val mea = cube.measures.first().right // get the current measure
        val sum: Double = cube.df[mea].sum()!!.toDouble() // get the sum of the measure
        val df = cube.df.sortedByDescending(mea) // sort by descending value
        val patterns =
            (1..4).map { // get the topk
                var text = "" // starting sentence
                var csum = 0.0
                if (it == 1) {
                    val r = df.row(it - 1)
                    text += "The fact with highest $mea is ${tuple2string(cube, r)} with ${(r[mea] as Double).round()} "
                    csum += if (cube1 == null) { r[mea] as Double } else { r[mea] as Double * r["peculiarity"] as Double }
                } else {
                    val tuples: String = (0 until it).map {
                        val r = df.row(it)
                        val s = tuple2string(cube, r)
                        csum += if (cube1 == null) { r[mea] as Double } else { r[mea] as Double * r["peculiarity"] as Double }
                        s + " with " + (r[mea] as Double).round()
                    }.reduce { a, b -> "$a, $b" }
                    text += "The $it facts with highest $mea are $tuples"
                }
                VocalizationPattern(text, csum / sum, 1.0 * it / df.nrow, moduleName)
            }.toList()
        return patterns
    }

    override fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean {
        return cube2.measures.size == 1 && setOf("max", "sum", "avg").contains(cube2.measures.first().left.toLowerCase())
    }
}