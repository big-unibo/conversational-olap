package it.unibo.vocalization.generation.modules.querydriven

import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.IGPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.VocalizationPattern
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity.round
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity.tuple2string
import krangl.gt
import krangl.sum

/**
 * Describe intention in action.
 */
object TopK : VocalizationModule {
    override val moduleName: String
        get() = "Top-K"

    override fun compute(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val cube: IGPSJ = if (cube1 != null) {
            Peculiarity.extendCubeWithProxy(cube2, cube1)
        } else { cube2 }
        val mea = cube.measures.first().right // get the current measure
        return topKpatterns(moduleName, cube, mea)
    }

    fun topKpatterns(moduleName: String, cube: IGPSJ, mea: String, isTopK: Boolean = true, kpi: String? = null, normalizeValue: Boolean = true): List<VocalizationPattern> {
        if (cube.df.nrow == 0) {
            return listOf()
        }

        val kpi = if (kpi == null) mea else kpi
        val sum: Double = cube.df[mea].sum()!!.toDouble() // get the sum of the measure
        val df = if (isTopK) { cube.df.sortedByDescending(mea) } else { cube.df.sortedBy(mea) }.filter { it[mea] gt 0.1 } // sort by descending value
        val superlative = if (isTopK) { "highest" } else { "lowest" }
        val patterns =
            (1..df.nrow.coerceAtMost(4)).map { // get the topk
                var text = "" // starting sentence
                var csum = 0.0
                if (it == 1) {
                    val r = df.row(it - 1)
                    text += "The fact with $superlative $mea is ${tuple2string(cube, r)} with $kpi ${(r[kpi] as Double).round(0)} "
                    csum += if (!r.contains("peculiarity")) { r[mea] as Double } else { r[mea] as Double * r["peculiarity"] as Double }
                } else {
                    val tuples: String = (0 until it).map {
                        val r = df.row(it)
                        val s = tuple2string(cube, r)
                        csum += if (!r.contains("peculiarity")) { r[mea] as Double } else { r[mea] as Double * r["peculiarity"] as Double }
                        s + " with " + (r[kpi] as Double).round(0)
                    }.reduce { a, b -> "$a, $b" }
                    text += "The $it facts with $superlative $mea are $tuples"
                }
                val v = if (normalizeValue) csum / sum else csum
                val int = if (isTopK) {
                    // 1 - average of "not top-k value" / average of "top-k values"
                    // 1 - ((sum - csum) / (count - it)) / (csum / it)
                    v
                } else {
                    // 1 - average of "bottom-k values" / average of "not bottom-k value"
                    // 1 - (csum / it) / ((sum - csum) / (count - it))
                    1 - v
                }
                VocalizationPattern(text, int, 1.0 * it / df.nrow, moduleName)
            }.toList()
        return patterns.filter { it.int > 0 }
    }

    override fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean {
        return cube2.measures.size == 1 && setOf("max", "sum", "avg").contains(cube2.measures.first().left.toLowerCase())
    }
}