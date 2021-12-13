package it.unibo.vocalization.web

import com.google.common.base.Optional
import it.unibo.conversational.algorithms.Parser
import it.unibo.conversational.database.Cube
import it.unibo.conversational.datatypes.Mapping
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.*
import it.unibo.vocalization.modules.*
import org.json.JSONObject
import java.util.*

/**
 * A step of an OLAP session
 */
class Session(val cube: Cube, uuid: String? = null, val mapping: Mapping? = null, val prevMapping: Mapping? = null, val operator: Operator? = null) {
    private val uuid: String = uuid ?: if (Parser.TEST) "s0" else UUID.randomUUID().toString()
    val state: Mapping.State
        get() = mapping!!.state
    val ret = JSONObject()
    val patterns: MutableCollection<List<IVocalizationPattern>> = mutableSetOf()

    @Throws(Exception::class)
    fun toJSON(value: String?, limit: String?): JSONObject {
        if (mapping != null) {
            val vocalization = JSONObject()
            vocalization.put("error", 0)
            vocalization.put("quality", 1)
            if (value!!.toLowerCase().replace(" ", "") != "tellmemore") {
                val json = mapping.JSONobj(cube, value, if (limit == null) Optional.absent() else Optional.of(limit.toLong()))
                val curQueryClauses = Parser.getClauses(cube, mapping.bestNgram) // get the clauses of the current query
                val curQuery = GPSJ(cube, curQueryClauses.left, curQueryClauses.middle, curQueryClauses.right) // build the current query
                if (prevMapping != null) { // OLAP operator
                    val prevQueryClauses = Parser.getClauses(cube, prevMapping.bestNgram) // get the clauses from the previous query
                    val prevQuery = GPSJ(cube, prevQueryClauses.left, prevQueryClauses.middle, prevQueryClauses.right) // build the previous query
                    listOf(Preamble, TopK, Assess).forEach { // for each module
                        patterns.addAll(listOf(it.compute(prevQuery, curQuery))) // add the resulting patterns
                    }
                } else { // full query
                    listOf(TopK).forEach { // for each module
                        patterns.addAll(setOf(it.compute(curQuery, curQuery))) // add the resulting patterns
                    }
                }
                ret.put("parseforest", json)
                ret.put("operatorforest", if (operator == null || operator.countAnnotatedNodesInTree() == 0) JSONObject() else operator.toJSON(cube))
                ret.put("state", state)
                ret.put("sessionid", uuid)
            }
            val curPatterns = Optimizer.getPatterns(patterns,400)
            if (curPatterns.isEmpty()) {
                vocalization.put("description", "All patterns have been vocalized.")
            } else {
                vocalization.put("description", curPatterns.map { p -> p.text }.reduce { a, b -> "$a. $b" })
            }
            ret.put("vocalization", vocalization)
        }
        return ret
    }
}