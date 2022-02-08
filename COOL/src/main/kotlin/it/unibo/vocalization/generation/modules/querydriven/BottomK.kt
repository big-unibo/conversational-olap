package it.unibo.vocalization.generation.modules.querydriven

import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.IGPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.querydriven.TopK.topKpatterns

/**
 * Describe intention in action.
 */
object BottomK : VocalizationModule {
    override val moduleName: String
        get() = "bottom-K"

    override fun compute(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        val cube: IGPSJ = if (cube1 != null) {
            Peculiarity.extendCubeWithProxy(cube2, cube1)
        } else { cube2 }
        val mea = cube.measures.first().right // get the current measure
        return topKpatterns(moduleName, cube, mea, isTopK = false)
    }

    override fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean {
        return cube2.measures.size == 1 && setOf("max", "sum", "avg").contains(cube2.measures.first().left.toLowerCase())
    }
}