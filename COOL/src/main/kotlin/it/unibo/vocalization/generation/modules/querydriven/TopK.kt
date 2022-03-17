package it.unibo.vocalization.generation.modules.querydriven

import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.K
import it.unibo.vocalization.generation.modules.IGPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.VocalizationPattern
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity.round
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity.tuple2string
import krangl.slice
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

    fun topKpatterns(moduleName: String, cube: IGPSJ, mea: String, isTopK: Boolean = true): List<VocalizationPattern> {
        if (cube.df.nrow <= 1) {
            return listOf()
        }

        var df = if (isTopK) { cube.df.sortedByDescending(mea) } else { cube.df.sortedBy(mea) }
        val superlative = if (isTopK) { "highest" } else { "lowest" }
        val maxLen = df.nrow.coerceAtMost(K + 1)
        df = df.addColumn(mea) { df[mea].minus(df.row(maxLen - 1)[mea] as Double) }
        val sum: Double = df.slice(0 until maxLen)[mea].sum()!!.toDouble()

        return (1 until maxLen).map { // get the topk
                var text = "" // starting sentence
                var csum = 0.0
                if (it == 1) {
                    val r = df.row(0)
                    text += "The fact with $superlative $mea is ${tuple2string(cube, r)} with $mea ${(r[mea] as Double).round()} "
                    csum += r[mea] as Double * (if (!r.contains("peculiarity")) 1.0 else r["peculiarity"] as Double)
                } else {
                    val tuples: String = (0 until it)
                        .map { df.row(it) }
                        .map { r ->
                            csum += r[mea] as Double * (if (!r.contains("peculiarity")) 1.0 else r["peculiarity"] as Double)
                            tuple2string(cube, r) + " with " + (r[mea] as Double).round()
                        }.reduce { a, b -> "$a, $b" }
                    text += "The $it facts with $superlative $mea are $tuples"
                }
                VocalizationPattern(text, csum / sum, 1.0 * it / df.nrow, moduleName)
            }.toList()
    }

    override fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean {
        return cube2.measures.size == 1 && setOf("max", "sum", "avg").contains(cube2.measures.first().left.toLowerCase())
    }
}