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
        return topKpatterns(moduleName, cube, mea)
    }

    fun topKpatterns(moduleName: String, cube: IGPSJ, mea: String, isTopK: Boolean = true): List<VocalizationPattern> {
        val sum: Double = cube.df[mea].sum()!!.toDouble() // get the sum of the measure
        val count: Int = cube.df[mea].length // get the count of elements
        val df = if (isTopK) { cube.df.sortedByDescending(mea) } else { cube.df.sortedBy(mea) } // sort by descending value
        val superlative = if (isTopK) { "highest" } else { "lowest" }
        val patterns =
            (1..4).map { // get the topk
                var text = "" // starting sentence
                var csum = 0.0
                if (it == 1) {
                    val r = df.row(it - 1)
                    text += "The fact with $superlative $mea is ${tuple2string(cube, r)} with ${(r[mea] as Double).round()} "
                    csum += if (!r.contains("peculiarity")) { r[mea] as Double } else { r[mea]  as Double * r["peculiarity"] as Double }
                } else {
                    val tuples: String = (0 until it).map {
                        val r = df.row(it)
                        val s = tuple2string(cube, r)
                        csum += if (!r.contains("peculiarity")) { r[mea] as Double } else { r[mea] as Double * r["peculiarity"] as Double }
                        s + " with " + (r[mea] as Double).round()
                    }.reduce { a, b -> "$a, $b" }
                    text += "The $it facts with $superlative $mea are $tuples"
                }
                VocalizationPattern(text, 1 - ((sum - csum) / (count - it)) / (csum / it), 1.0 * it / df.nrow, moduleName)
            }.toList()
        return patterns
    }

    override fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean {
        return cube2.measures.size == 1 && setOf("max", "sum", "avg").contains(cube2.measures.first().left.toLowerCase())
    }
}