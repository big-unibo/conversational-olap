package it.unibo.vocalization.modules

import com.google.common.collect.Sets
import it.unibo.conversational.database.QueryGenerator
import it.unibo.conversational.datatypes.DependencyGraph
import krangl.*
import org.slf4j.LoggerFactory
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt

object Peculiarity {

    fun getCost(s: String): Int {
        return s.split(" ").filter { it.isNotEmpty() }.size
    }

    fun Double.format(digits: Int) = "%.${digits}f".format(this)

    fun tuple2string(cube: IGPSJ, r: DataFrameRow): String {
        return cube.attributes.map { r[it].toString() }.reduce { a, b -> "$a, $b" }
    }

    fun Double.round(decimals: Int = 2): Double {
        val mult: Double = 10.0.pow(decimals)
        return (this * mult).roundToInt() / mult
    }

    private val L = LoggerFactory.getLogger(Peculiarity::class.java)

    /**
     * Extend the cube with the proxy cells.
     *
     * @param d the current intention
     * @param cube the current cube
     */
    fun extendCubeWithProxy(cube2: IGPSJ, cube1: IGPSJ, returnAllColumns: Boolean = false, other: MutableMap<String, Any> = mutableMapOf()): IGPSJ {
        val prevGc = cube1.attributes // get the previous coordinate
        var prevCube = cube1.df.addColumns(*cube1.measureNames().map { m -> "zscore_$m" to { (it[m] - cube1.df[m].mean()!!) / cube1.df[m].sd()!! } }.toTypedArray())
        // get the previous data
        val coordinate = cube2.attributes // get the current coordinate
        var cube = cube2.df.addColumns(*cube2.measureNames().map { m -> "zscore_$m" to { (it[m] - cube2.df[m].mean()!!) / cube2.df[m].sd()!! } }.toTypedArray()) // get the current data
        val gencoord: MutableSet<String> = Sets.newLinkedHashSet()

        coordinate.forEach { currA ->
            if (!prevGc.contains(currA)) { // the previous cube contains the same attribute
                val prev: String? = prevGc.find {
                    DependencyGraph.lca(cube2.cube, currA, it).isPresent
                } // find an attribute with a rollup/drilldown relationship from the previous coordinate
                if (!prev.isNullOrEmpty() && !(DependencyGraph.lca(cube2.cube, currA, prev).get() != currA.toLowerCase() && DependencyGraph.lca(cube2.cube, currA, prev).get() != prev.toLowerCase())) {
                    val specific: String = DependencyGraph.lca(cube2.cube, currA, prev).get().toUpperCase() // get the most specific attribute
                    val spec2gen: Map<String, String> = QueryGenerator.getFunctionalDependency(cube2.cube, specific, if (specific.equals(currA, ignoreCase = true)) prev else currA)
                    if (prevGc.contains(specific)) { // if it is a rollup
                        cube = cube.addColumn(currA) { it[currA].map<Any> { it.toString() } }
                        prevCube = prevCube.addColumn(currA) {
                            it[specific.toUpperCase()].map<Any> { toFind -> spec2gen[toFind.toString()] }
                        }
                        gencoord.add(currA)
                    } else { // if it is a drill down
                        prevCube = prevCube.addColumn(prev) { it[prev].map<Any> { it.toString() } }
                        cube = cube.addColumn(prev) {
                            it[specific.toUpperCase()].map<Any> { toFind -> spec2gen[toFind.toString()] }
                        }
                        gencoord.add(prev)
                    }
                } else {
                    prevCube = prevCube.addColumn("all_$currA") { prevCube.cols[0].map<Any> { "all" } }
                    cube = cube.addColumn("all_$currA") { cube[currA].map<Any> { "all" } }
                    gencoord.add("all_$currA")
                }
            } else {
                gencoord.add(currA)
            }
        }

        val stats = cube // estimate the z-score of the previous cube
            .leftJoin(right = prevCube, by = gencoord, suffices = "" to "_bc") // and join them base on the proxy
            .groupBy(*cube2.attributes.toTypedArray()) // ... group by coordinate
            .summarize(
                *(cube2.measureNames().map { m -> // ... average the z-score from the previous cube (i.e., proxy cells)
                    ("avg_zscore_$m") to {
                        if (cube1.measureNames().contains(m)) {
                            val avg: Double = it["zscore_${m}_bc"].mean(true)!!
                            if (avg.isNaN()) prevCube["zscore_${m}"].mean(false) else avg
                        } else {
                            0
                        }
                    }
                }.toTypedArray())
            )
            .sortedBy(*cube2.attributes.toTypedArray()) // it is essential that this tuples are sorted as the ones from the original cube
        cube = cube
            .sortedBy(*cube2.attributes.toTypedArray())
            .addColumns( // compute the difference between the zscores
                *(cube2.measureNames()
                    .filter { m -> prevCube.names.contains(m) }
                    .map { m -> ("zscore_$m") `=` { (it["zscore_$m"] - stats["avg_zscore_$m"]).map<Double> { (abs(it) * 1000).roundToInt() / 1000.0 } } }
                    .toTypedArray())
            ).remove(gencoord.filter { !returnAllColumns && !cube2.attributes.contains(it) })
        L.warn("Proxy completed")

        // get the peculiarity
        var enh = cube.addColumn("peculiarity") { myMax(*(cube2.measureNames().map { m -> it["zscore_$m"] }.toTypedArray())) }
        enh = enh.addColumn("peculiarity") { enh["peculiarity"] / (enh["peculiarity"].max() as Double) }
        other["gencoord"] = gencoord
        other["prevcube"] = prevCube
        return GPSJ(enh, cube2.attributes, cube2.measures, cube2.selection)
    }

    /**
     * Get maximum value among many columns
     */
    fun myMax(vararg cols: DataCol): Array<Double?> {
        if (cols.isEmpty()) {
            throw IllegalArgumentException("Empty columns")
        }
        if (cols.size == 1) {
            return cols[0].toDoubles()
        }
        return cols
            .map { it.toDoubles().toList() }
            .reduce { a1, a2 -> a1.zip(a2).map { (it.first!!).coerceAtLeast(it.second!!) } }
            .toTypedArray()
    }

    fun argMax(vararg cols: DataCol): Array<Any?> {
        if (cols.isEmpty()) {
            throw IllegalArgumentException("Empty columns")
        }
        if (cols.size == 1) {
            return cols[0].map<Double> { cols[0].name.replace("diff_", "") }.toTypedArray()
        }
        return cols
            .map {
                val a = it.toStrings().map { it!! }.toMutableList()
                a.add(0, it.name.replace("diff_", ""))
                a
            }
            .reduce { a1, a2 ->
                val t1 = a1.removeAt(0).replace("diff_", "")
                val t2 = a2.removeAt(0).replace("diff_", "")
                a1.map { it.toDouble() }.zip(a2.map { it.toDouble() }).map { if (it.first > it.second) { t1 } else {t2}} .toMutableList()
            }
            .toTypedArray()
    }
}