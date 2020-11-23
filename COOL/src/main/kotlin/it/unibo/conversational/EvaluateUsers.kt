package it.unibo.conversational

import com.google.common.collect.Maps
import com.opencsv.CSVWriter
import it.unibo.conversational.Validator.parseAndTranslate
import it.unibo.conversational.database.Config
import it.unibo.conversational.database.QueryGenerator.getSessionStatistics
import it.unibo.conversational.datatypes.Mapping
import java.util.*

private val cube = Config.getCube("sales_fact_1997")
private val lookup_fullqry: MutableMap<String, Mapping> = Maps.newHashMap()
private val lookup_session: MutableMap<String, Mapping> = Maps.newHashMap()

fun main() {
    lookup_fullqry["q1"] = parseAndTranslate(cube, "avg unit sales where year = 1997")
    lookup_session["q1"] = parseAndTranslate(cube, "avg unit sales where year = 1997 by category")

    lookup_fullqry["q2"] = parseAndTranslate(cube, "avg unit sales for category = beer and wine")
    lookup_session["q2"] = parseAndTranslate(cube, "max store sales")

    lookup_fullqry["q3"] = parseAndTranslate(cube, "sum store sales by month for category = beer and wine")
    lookup_session["q3"] = parseAndTranslate(cube, "sum store sales max store sales by year for category = beer and wine")

    println("Scanning session ids...")
    Scanner(java.io.File("resources/test/results_IS/sessionids.txt")).use { s ->
        java.nio.file.Files.newBufferedWriter(java.nio.file.Paths.get("resources/test/results_IS/sessionsim.csv")).use { buffWrit ->
            CSVWriter(buffWrit).use { writer ->
                val columns = arrayOf(
                        "user", "session",
                        "fullquery_sim", "fullquery_time", "fullquery_iterations", "fullquery_ambiguities",
                        "session_sim", "operator_time", "operator_iterations", "operator_ambiguities",
                        "session_time", "session_iterations", "session_ambiguities",
                        "fullquery", "fullquery_gt", "session", "session_gt")
                writer.writeNext(columns)
                while (s.hasNextLine()) {
                    val user: String = s.nextLine()
                    for (q in lookup_fullqry.keys) {
                        try {
                            val statistics = getSessionStatistics(cube, user + "_" + q, lookup_fullqry[q], lookup_session[q])
                            val toWrite = arrayOfNulls<String>(columns.size)
                            toWrite[0] = user
                            toWrite[1] = q
                            for (i in 2 until columns.size) {
                                toWrite[i] = "" + statistics[columns[i]]
                            }
                            writer.writeNext(toWrite)
                            println(Arrays.asList(*toWrite))
                        } catch (e: IllegalArgumentException) {
                            System.err.println(e.message)
                        }
                    }
                }
            }
        }
    }
}