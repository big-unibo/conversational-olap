package it.unibo.vocalization.generation.modules.intentiondriven

import it.unibo.conversational.database.Config
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.IGPSJ
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationModule
import it.unibo.vocalization.generation.modules.VocalizationPattern
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity.round
import it.unibo.vocalization.generation.modules.querydriven.Peculiarity.tuple2string
import krangl.*
import java.io.File
import java.util.*

/**
 * Describe intention in action.
 */
object SADIncrease : VocalizationModule {
    override val moduleName: String
        get() = "sadincrease"


    fun percent(d: Double): String {
        return "${(d * 100).round(0)}%"
    }

    override fun compute(c1: IGPSJ?, c2: IGPSJ, operator: Operator?): List<IVocalizationPattern> {
        var time = System.currentTimeMillis()
        var df = c1!!.df.outerJoin(c2.df, by = c2.attributes)
        println("${moduleName} proxy done " + (System.currentTimeMillis() - time))
        time = System.currentTimeMillis()

        val mea = c2.measureNames().first()

        val path = "generated/"
        val fileName = "${UUID.randomUUID()}.csv"
        df.writeCSV(File("$path$fileName"))
        computePython(Config.getPython(), path, "modules.py", fileName, c2.attributes, c2.measureNames())
        df = DataFrame.readCSV(File("$path$fileName"))
        println("${moduleName} python done " + (System.currentTimeMillis() - time))


        val avg = df[mea].mean()!!
        df = df.filter { it[mea] gt 0.1 }.sortedByDescending("${mea}_kpi")

        val superlative = if (c2.selection.size > c1.selection.size) "decrease" else "increase"
        val sum = df["${mea}_kpi"].sum()!!.toDouble()
        return (1..df.nrow.coerceAtMost(7)).map { // get the topk
            var text = "The average $superlative in ${c2.measureNames().map { "$it is ${percent(avg)}" }.reduce { a, b -> "$a,$b" }}" // starting sentence
            var csum = 0.0
            if (it == 1) {
                val r = df.row(it - 1)
                csum += r["${mea}_kpi"] as Double
                text += ", the fact with highest variance is ${tuple2string(c2, r)} with ${percent(r[mea] as Double)}"
            } else {
                val tuples: String = (0 until it).map {
                    val r = df.row(it)
                    csum += r["${mea}_kpi"] as Double
                    tuple2string(c2, r) + " with " + percent(r[mea] as Double)
                }.reduce { a, b -> "$a, $b" }
                text += ", the facts with highest $superlative are $tuples"
            }
            VocalizationPattern(text, csum / sum, 1.0 * it / df.nrow, moduleName)
        }.toList()
    }

    override fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean {
        return cube1 != null && (cube1.selection.size > cube2.selection.size || cube1.selection.size < cube2.selection.size) && cube2.measureNames().size == 1
    }
}