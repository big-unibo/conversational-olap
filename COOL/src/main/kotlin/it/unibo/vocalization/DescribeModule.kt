package it.unibo.vocalization

import it.unibo.conversational.olap.Operator
import krangl.DataFrameRow
import krangl.mean
import krangl.sum
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Describe intention in action.
 */
object DescribeModule : VocalizationModule {
    override val moduleName: String
        get() = "Describe"

    fun tuple2string(cube: IGPSJ, r: DataFrameRow): String {
        return cube.attributes.map { r[it].toString() }.reduce { a, b -> "$a, $b" }
    }

    fun Double.round(decimals: Int = 2): Double {
        val mult: Double = 10.0.pow(decimals)
        return (this * mult).roundToInt() / mult
    }

    override fun compute(cube1: IGPSJ, cube2: IGPSJ, operator: Operator?): Set<IVocalizationPattern> {
        val mea = cube1.measures.first().right // get the current measure
        val mean: Double = cube1.df[mea].mean()!!.round() // get the mean of the measure
        val sum: Double = cube1.df[mea].sum()!!.toDouble() // get the max of the measure
        val enhcube = cube1.df.sortedByDescending(mea) // sort by descending value
        val patterns =
                (1..4).map { // get the topk
                    var text = "The average sale is $mean. " // starting sentence
                    var csum = 0.0
                    if (it == 1) {
                        val r = enhcube.row(it)
                        text += "The tuple with highest sales is ${tuple2string(cube2, r)} "
                        csum += r[mea] as Double
                    } else {
                        val tuples: String = (1..it).map {
                            val r = enhcube.row(it)
                            val s = tuple2string(cube2, r)
                            csum += r[mea] as Double
                            s
                        }.reduce { a, b -> "$a and $b" }
                        text += "The $it tuples with highest sales are $tuples"
                    }
                    VocalizationPattern(text, csum / sum, text.length, AssessmentModule.moduleName)
                }.toSet()
        return patterns
    }
}