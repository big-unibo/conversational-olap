@file:JvmName("IInterfacesVocalization")

package it.unibo.vocalization.generation.modules

import it.unibo.conversational.algorithms.Parser
import it.unibo.conversational.database.Cube
import it.unibo.conversational.database.DBmanager
import it.unibo.conversational.olap.Operator
import it.unibo.vocalization.generation.modules.Peculiarity.format
import it.unibo.vocalization.generation.modules.Peculiarity.getCost
import krangl.ArrayUtils.handleListErasure
import krangl.DataFrame
import krangl.dataFrameOf
import krangl.readCSV
import krangl.rename
import org.apache.commons.lang3.tuple.Pair
import org.apache.commons.lang3.tuple.Triple
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.sql.ResultSet
import java.time.LocalDate
import java.time.LocalTime


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
    fun toPythonCommand(commandPath: String, path: String, fileName: String, measures: Collection<String>): String {
        val fullCommand = (commandPath.replace("/", File.separator) //
                + " --path " + (if (path.contains(" ")) "\"" else "") + path.replace("\\", "/") + (if (path.contains(" ")) "\"" else "") //
                + " --file $fileName" //
                + " --module $moduleName"
                + " --measures ${measures.reduce{ a, b -> "$a,$b" }}")
        return fullCommand
    }

    /**
     * Compute python algorithms
     *
     * @param pythonPath   outputPath to python installation (e.g., virtual env with configured libraries)
     * @param outputPath   output path
     * @param pythonModule module to execute
     */
    @Throws(IOException::class, InterruptedException::class)
    fun computePython(pythonPath: String, outputPath: String, pythonModule: String, fileName: String, measures: Collection<String>): Long {
        val commandPath: String
        commandPath = if (File(pythonPath + "venv/Scripts").exists()) {
            pythonPath + "venv/Scripts/python.exe " + pythonPath + pythonModule + " " //.replace("/", File.separator);
        } else if (File(pythonPath + "venv/bin").exists()) {
            pythonPath + "venv/bin/python " + pythonPath + pythonModule + " "
        } else {
            "python3 $pythonPath$pythonModule "
        }
        var startTime = System.currentTimeMillis()
        val proc = Runtime.getRuntime().exec(toPythonCommand(commandPath, outputPath, fileName, measures))
        val ret = proc.waitFor()
        startTime = System.currentTimeMillis() - startTime
        if (ret != 0) {
            val stdInput = BufferedReader(InputStreamReader(proc.inputStream))
            var s: String? = ""
            var error = ""
            while (s != null) {
                s = stdInput.readLine()

                error += """
                ${s ?: ""}
                
                """.trimIndent()
            }
            val stdError = BufferedReader(InputStreamReader(proc.errorStream))
            while (stdError.readLine().also { s = it } != null) {
                error += """
                $s
                
                """.trimIndent()
            }
            throw java.lang.IllegalArgumentException(error)
        }
        return startTime
    }
}

interface IGPSJ {
    val cube: Cube?
    val fileName: String?
    var df: DataFrame
    val attributes: Set<String>
    val measures: Set<Pair<String, String>>
    fun measureNames(): Set<String> = measures.map { it.right }.toSet()
    val selection: Set<Triple<String, String, String>>
}

fun fromResultSet(rs: ResultSet): DataFrame {

    val numColumns = rs.metaData.columnCount
    val colNames = (1..numColumns).map { rs.metaData.getColumnName(it) }

    // see http://www.h2database.com/html/datatypes.html
    val colData = listOf<MutableList<Any?>>().toMutableList()

    val colTypes = (1..numColumns).map { rs.metaData.getColumnTypeName(it) }

    //    http://www.cs.toronto.edu/~nn/csc309/guide/pointbase/docs/html/htmlfiles/dev_datatypesandconversionsFIN.html
    colTypes.map {
        when (it) {
            "INTEGER", "INT", "SMALLINT" -> listOf<Int>()
            "REAL", "FLOAT", "NUMERIC", "DECIMAL", "NUM", "DOUBLE" -> listOf<Double?>()
            "BOOLEAN" -> listOf<Boolean?>()
            "DATE" -> listOf<LocalDate?>()
            "TIME" -> listOf<LocalTime?>()
            "CHAR", "CHARACTER", "VARCHAR", "TEXT" -> listOf<String>()
            else -> throw IllegalArgumentException("Column type ${it} is not yet supported by {krangl}.")
        }.toMutableList().let { colData.add(it as MutableList<Any?>) }
    }

    // see https://stackoverflow.com/questions/21956042/mapping-a-jdbc-resultset-to-an-object
    while (rs.next()) {
        for (colIndex in 1..numColumns) {
            val any: Any? = when (colTypes[colIndex - 1]) {
                "INTEGER", "INT", "SMALLINT" -> rs.getInt(colIndex)
                "REAL", "FLOAT", "NUMERIC", "DECIMAL", "NUM", "DOUBLE" -> rs.getDouble(colIndex)
                "BOOLEAN" -> rs.getBoolean(colIndex)
                "DATE" -> rs.getDate(colIndex).toLocalDate()
                "TIME" -> rs.getTime(colIndex).toLocalTime()
                "CHAR", "CHARACTER", "VARCHAR", "TEXT" -> rs.getString(colIndex)
                else -> throw IllegalArgumentException("Column type ${colTypes[colIndex - 1]} is not yet supported by {krangl}. ")
            }
            colData[colIndex - 1].add(any)
        }
    }

    val cols = colNames.zip(colData).map { (name, data) -> handleListErasure(name, data) }
    return dataFrameOf(*cols.toTypedArray())
}

class GPSJ(
    override val cube: Cube?,
    override val fileName: String?,
    var curdf: DataFrame?,
    override val attributes: Set<String>,
    override val measures: Set<Pair<String, String>>,
    override val selection: Set<Triple<String, String, String>>
) : IGPSJ {
    override var df: DataFrame
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
                    curdf = fromResultSet(it) // DataFrame.fromResultSet(it)
                }
                curdf = curdf!!.rename(*curdf!!.names.map { Pair(it, it.toUpperCase()) }.toTypedArray())
                return curdf!!
            } else {
                throw IllegalArgumentException("Cannot get the data, both filename and cube are null")
            }
        }
        set(value) { curdf = value }

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