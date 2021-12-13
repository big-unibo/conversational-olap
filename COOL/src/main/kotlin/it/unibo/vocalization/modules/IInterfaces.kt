@file:JvmName("IInterfacesVocalization")

package it.unibo.vocalization.modules

import it.unibo.conversational.algorithms.Parser
import it.unibo.conversational.database.Cube
import it.unibo.conversational.database.DBmanager
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.modules.Peculiarity.format
import it.unibo.vocalization.modules.Peculiarity.getCost
import krangl.DataFrame
import krangl.fromResultSet
import krangl.readCSV
import krangl.rename
import org.apache.commons.lang3.tuple.Pair
import org.apache.commons.lang3.tuple.Triple

/**
 * Describe the state of a certain pattern
 */
enum class PatternState {
    /**
     * The pattern is returned now
     */
    CURRENTLYTAKEN,

    /**
     * The pattern has been already returned
     */
    TAKEN,

    /**
     * The pattern has not been yet taken
     */
    AVAILABLE
}

/**
 * A vocalization pattern (i.e., what is returned by a vocalization module)
 */
interface IVocalizationPattern {
    /**
     * Textual description of the pattern
     */
    val text: String

    /**
     * Interestingness
     */
    val int: Number

    /**
     * Vocalization cost (e.g., the number of characters in the text
     */
    val cost: Int

    /**
     * Coverage of the current pattern
     */
    val cov: Double

    /**
     * Name of the originating module
     */
    val moduleName: String

    /**
     * Whether the pattern has been returned or not
     */
    var state: PatternState
}

class VocalizationPattern(
    override val text: String,
    override val int: Double,
    override val cov: Double,
    override val cost: Int,
    override val moduleName: String,
    override var state: PatternState = PatternState.AVAILABLE
) : IVocalizationPattern {
    override fun toString(): String = "<$moduleName, ${int.format(2)}, ${cov.format(2)}, $cost, $state, ${text.trim()}>"

    constructor(text: String, int: Double, moduleName: String) : this(
        text,
        int,
        1.0,
        text.split(" ").size,
        moduleName,
        PatternState.AVAILABLE
    )

    constructor(text: String, int: Double, cov: Double, moduleName: String) : this(
        text.trim(),
        int,
        cov,
        getCost(text),
        moduleName,
        PatternState.AVAILABLE
    )
}

interface VocalizationModule {
    val moduleName: String
    fun compute(cube1: IGPSJ?, cube2: IGPSJ): List<IVocalizationPattern> = compute(cube1, cube2, null)
    fun compute(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): List<IVocalizationPattern>
    fun applyCondition(cube1: IGPSJ?, cube2: IGPSJ, operator: Operator?): Boolean = true
}

interface IGPSJ {
    val cube: Cube?
    val fileName: String?
    val df: DataFrame
    val attributes: Set<String>
    val measures: Set<Pair<String, String>>
    fun measureNames(): Set<String> = measures.map { it.right }.toSet()
    val selection: Set<Triple<String, String, String>>
}

class GPSJ(
    override val cube: Cube?,
    override val fileName: String?,
    var curdf: DataFrame?,
    override val attributes: Set<String>,
    override val measures: Set<Pair<String, String>>,
    override val selection: Set<Triple<String, String, String>>
) : IGPSJ {
    override val df: DataFrame
        get() {
            if (curdf != null) {
                return curdf!!
            } else if (!fileName.isNullOrBlank()) {
                curdf = DataFrame.readCSV(fileName)
                curdf = curdf!!.rename(*curdf!!.names.map { Pair(it, it.toUpperCase()) }.toTypedArray())
                return curdf!!
            } else if (cube != null) {
                val sql = Parser.createQuery(cube, attributes, measures, selection)
                DBmanager.executeDataQuery(cube, sql) {
                    curdf = DataFrame.fromResultSet(it)
                }
                curdf = curdf!!.rename(*curdf!!.names.map { Pair(it, it.toUpperCase()) }.toTypedArray())
                return curdf!!
            } else {
                throw IllegalArgumentException("Cannot get the data, both filename and cube are null")
            }
        }

    constructor(attributes: Set<String>, measures: Set<String>, selection: Set<Triple<String, String, String>>) : this(
        null,
        null,
        null,
        attributes.map { it.toUpperCase() }.toSet(),
        measures.map { Pair.of("sum", it.toUpperCase()) }.toSet(),
        selection.map { Triple.of(it.left.toUpperCase(), it.middle, it.right) }.toSet()
    )

    constructor(
        cube: Cube,
        attributes: Set<String>,
        measures: Set<Pair<String, String>>,
        selection: Set<Triple<String, String, String>>
    ) : this(
        cube,
        null,
        null,
        attributes.map { it.toUpperCase() }.toSet(),
        measures.map { Pair.of(it.left, it.right.toUpperCase()) }.toSet(),
        selection.map { Triple.of(it.left.toUpperCase(), it.middle, it.right) }.toSet()
    )

    constructor(
        df: DataFrame,
        attributes: Set<String>,
        measures: Set<Pair<String, String>>,
        selection: Set<Triple<String, String, String>>
    ) : this(
        null,
        null,
        df,
        attributes.map { it.toUpperCase() }.toSet(),
        measures.map { Pair.of(it.left, it.right.toUpperCase()) }.toSet(),
        selection.map { Triple.of(it.left.toUpperCase(), it.middle, it.right) }.toSet()
    )

    constructor(
        fileName: String,
        attributes: Set<String>,
        measures: Set<String>,
        selection: Set<Triple<String, String, String>>
    ) : this(
        null,
        fileName,
        null,
        attributes.map { it.toUpperCase() }.toSet(),
        measures.map { Pair.of("sum", it.toUpperCase()) }.toSet(),
        selection.map { Triple.of(it.left.toUpperCase(), it.middle, it.right) }.toSet()
    )
}