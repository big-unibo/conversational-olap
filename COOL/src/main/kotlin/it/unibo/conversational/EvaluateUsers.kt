package it.unibo.conversational

import com.google.common.collect.Maps
import com.opencsv.CSVWriter
import it.unibo.conversational.Validator.getBest
import it.unibo.conversational.database.Config
import it.unibo.conversational.database.QueryGenerator.getSessionStatistics
import it.unibo.conversational.datatypes.Mapping
import java.util.*

private val cube = Config.getCube("sales_fact_1997")
private val lookup_fullqry: MutableMap<String, List<Mapping>> = Maps.newHashMap()
private val lookup_session: MutableMap<String, List<Mapping>> = Maps.newHashMap()

fun main() {
    lookup_fullqry["q1"] = listOf(getBest(cube, "avg unit sales where year = 1997"))
    lookup_session["q1"] = listOf(getBest(cube, "avg unit sales where year = 1997 by category"))
    lookup_fullqry["q2"] = listOf(getBest(cube, "avg unit sales for product category = beer and wine"))
    lookup_session["q2"] = listOf(getBest(cube, "max store sales"))
    lookup_fullqry["q3"] = listOf(getBest(cube, "sum store sales by month for product category = beer and wine"))
    lookup_session["q3"] = listOf(getBest(cube, "sum store sales max store sales by year for product category = beer and wine"))
    lookup_fullqry["q4"] = listOf(getBest(cube, "average store sales where country = Canada"))
    lookup_session["q4"] = lookup_fullqry["q4"]!!
    lookup_fullqry["q5"] = listOf(
        getBest(cube, "sum unit sales by product category month"),
        getBest(cube, "sum unit sales by month product category")
    )
    lookup_session["q5"] = lookup_fullqry["q5"]!!
    lookup_fullqry["q6"] = listOf(
            getBest(cube, "sum unit sales by product family where year = 1997"),
            getBest(cube, "sum store sales by product family where year = 1997"),
            // getBest(cube, "max unit sales by product family where year = 1997")
    )
    lookup_session["q6"] = lookup_fullqry["q6"]!!
    lookup_fullqry["q7"] = listOf(
        getBest(cube, "sum unit sales by gender"),
        getBest(cube, "max unit sales by gender"),
    )
    lookup_session["q7"] = lookup_fullqry["q7"]!!

    println("Scanning session ids...")
    val ids = mutableListOf<List<String>>()
    Scanner(java.io.File("resources/test/results_IS/sessionids.txt")).use { s ->
        while (s.hasNextLine()) {
            ids.add(s.nextLine().split(","))
        }
    }
    // ids.add(listOf("nicolo.didomenico@studio.unibo.it", "5")) // Uncomment this to check a single user

    java.nio.file.Files.newBufferedWriter(java.nio.file.Paths.get("resources/test/results_IS/sessionsim.csv")).use { buffWrit ->
        CSVWriter(buffWrit).use { writer ->
            val columns = arrayOf(
                    "user", "session", "DFM",
                    "fullquery_sim", "fullquery_time", "fullquery_iterations", "fullquery_ambiguities",
                    "session_sim", "operator_time", "operator_iterations", "operator_ambiguities",
                    "session_time", "session_iterations", "session_ambiguities",
                    "fullquery", "fullquery_gt", "session", "session_gt")
            writer.writeNext(columns)
            ids.forEach { tuple ->
                val user = tuple[0]
                val dfm = tuple[1]
                for (q in lookup_fullqry.keys) {
                    if (
                            (user.contains("asia.lucchi@studio.unibo.it") || user.contains("michele.mongardi2@studio.unibo.it")) && Integer.parseInt(q.split("q")[1]) > 3
                            || (user.contains("j.giovanelli@unibo.it")) && Integer.parseInt(q.split("q")[1]) > 6
                    ) {
                        continue;
                    }
                    try {
                        val statistics = getSessionStatistics(cube, user + "_" + q, lookup_fullqry[q], lookup_session[q])
                        val toWrite = arrayOfNulls<String>(columns.size)
                        toWrite[0] = user
                        toWrite[1] = q
                        toWrite[2] = dfm
                        for (i in 3 until columns.size) {
                            toWrite[i] = "" + statistics[columns[i]]
                        }
                        writer.writeNext(toWrite)
                        println(listOf(*toWrite))
                    } catch (e: IllegalArgumentException) {
                        System.err.println(e.message)
                    }
                }
            }
        }
    }
}