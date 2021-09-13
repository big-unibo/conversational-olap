package it.unibo.vocalization.web

import com.google.common.base.Optional
import it.unibo.conversational.algorithms.Parser
import it.unibo.conversational.database.Cube
import it.unibo.conversational.datatypes.Mapping
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.*
import org.json.JSONObject
import java.util.*

class Session(val cube: Cube, uuid: String? = null, val mapping: Mapping? = null, val prevMapping: Mapping? = null, val operator: Operator? = null) {
    val state: Mapping.State
        get() = mapping!!.state

    @Throws(Exception::class)
    fun toJSON(value: String?, limit: String?): JSONObject {
        val ret = JSONObject()
        if (mapping != null) {
            val curQueryClauses = Parser.getClauses(cube, mapping.bestNgram) // get the clauses of the current query
            val curQuery = GPSJ(cube, curQueryClauses.left, curQueryClauses.middle, curQueryClauses.right) // build the current query
            val patterns: MutableSet<IVocalizationPattern> = mutableSetOf()
            val vocalization: JSONObject = JSONObject()
            vocalization.put("description", "This is not implemented yet.")
            vocalization.put("error", 0)
            vocalization.put("quality", 1)
            if (prevMapping != null) { // OLAP operator
                val prevQueryClauses = Parser.getClauses(cube, prevMapping.bestNgram) // get the clauses from the previous query
                val prevQuery = GPSJ(cube, prevQueryClauses.left, prevQueryClauses.middle, prevQueryClauses.right) // build the previous query
                patterns.addAll(setOf(PeculiarityModule.compute(prevQuery, curQuery).toList()[1])) // compute the vocalization patterns
                patterns.addAll(setOf(AssessmentModule.compute(prevQuery, curQuery).toList()[1])) // compute the vocalization patterns
                vocalization.put("description", Optimizer.getDummyPatterns(patterns).map { p -> p.text }.reduce { a, b -> "$a. $b" })
            } else { // full query
                patterns.addAll(setOf(DescribeModule.compute(curQuery, curQuery).toList()[2])) // compute the vocalization patterns
                vocalization.put("description", Optimizer.getDummyPatterns(patterns).map { p -> p.text }.reduce { a, b -> "$a. $b" })
            }
            val json = mapping.JSONobj(cube, value, if (limit == null) Optional.absent() else Optional.of(limit.toLong()))
            ret.put("parseforest", json)
            ret.put("vocalization", vocalization)
            ret.put("operatorforest", if (operator == null || operator.countAnnotatedNodesInTree() == 0) JSONObject() else operator.toJSON(cube))
            ret.put("state", state)
            ret.put("sessionid", uuid)
        }
        return ret
    }

    private val uuid: String = uuid ?: if (Parser.TEST) "s0" else UUID.randomUUID().toString()
}