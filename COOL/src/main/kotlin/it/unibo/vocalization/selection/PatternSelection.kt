package it.unibo.vocalization

import com.google.common.collect.Iterables
import com.google.common.collect.Sets
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.PatternState
import it.unibo.vocalization.selection.model.Knapsack
import it.unibo.vocalization.selection.model.OneOrNoneFromGroupProblem
import it.unibo.vocalization.selection.model.Problem
import it.unibo.vocalization.selection.model.Solution
import java.util.function.Consumer
import java.util.stream.Collectors

/**
 * Implement the strategies to return the most interesting patterns.
 */
object Optimizer {
    /**
     * Dummy method. Return at most three input patterns.
     *
     * @param patterns patterns among which the most interesting ones are selected
     * @param maxCost  the size of the knapsack
     * @return the most interesting patterns
     */
    fun getDummyPatterns(
        patterns: Collection<Collection<IVocalizationPattern>>,
        maxCost: Int
    ): Set<IVocalizationPattern> {
        val p = patterns.stream() // iterate over the patterns
            .flatMap { obj: Collection<IVocalizationPattern> -> obj.stream() } // flatten the patterns
            .filter { i: IVocalizationPattern -> i.state == PatternState.AVAILABLE } // select the available ones (NB: no check is done on the originating module)
            .limit(3) // select the first three patterns
            .collect(Collectors.toSet())
        // set the previously taken patterns to TAKEN
        patterns.stream().flatMap { obj: Collection<IVocalizationPattern> -> obj.stream() }
            .forEach { i: IVocalizationPattern ->
                i.state = if (i.state == PatternState.CURRENTLYTAKEN) PatternState.TAKEN else i.state
            }
        // set the currently taken patterns to CURRENTLYTAKEN (i.e., they are no more available for selection)
        p.forEach(Consumer { i: IVocalizationPattern ->
            i.state = PatternState.CURRENTLYTAKEN
        })
        // return the patterns
        return p
    }

    /**
     * @param patterns patterns among which the most interesting ones are selected
     * @param maxCost  the size of the knapsack
     * @return the most interesting patterns
     */
    fun getPatterns(patterns: Collection<List<IVocalizationPattern>>, maxCost: Int): Set<IVocalizationPattern> {

        // Initialize bagSize = total number of patterns
        var nPatterns = 1 // MCKP library requires an additional slot
        for (module in patterns) {
            for (pattern in module) {
                if (pattern.state == PatternState.AVAILABLE) {
                    nPatterns++
                }
            }
        }
        if (nPatterns == 1) {
            return Sets.newHashSet()
        }

        // Initialize other MCKP structures
        val profit = IntArray(nPatterns)
        val weight = IntArray(nPatterns)
        val group = IntArray(nPatterns)
        val patternIx = IntArray(nPatterns) // Used to store each pattern's index with the module
        profit[0] = 0
        weight[0] = 0
        group[0] = -1
        var currentGroup = 0
        var currentPattern = 1
        for (module in patterns) {
            var i = 0
            for (pattern in module) {
                if (pattern.state == PatternState.AVAILABLE) {
                    val interestingness = pattern.int.toDouble() * 100
                    profit[currentPattern] = interestingness.toInt()
                    weight[currentPattern] = pattern.cost
                    group[currentPattern] = currentGroup
                    patternIx[currentPattern] = i
                    currentPattern++
                }
                i++
            }
            currentGroup++
        }

        // Initialize and run MCKP problem
        val problem: Problem = OneOrNoneFromGroupProblem(maxCost, profit, weight, group)
        val knapsack = Knapsack(problem)
        val solution: Solution = knapsack.solve()

        // Set selected patterns as taken and return them
        val selectedPatterns: MutableSet<IVocalizationPattern> = HashSet()
        var j = 0
        for (jthPatternIsSelected in solution.solution) {
            if (jthPatternIsSelected) {
                val i = patternIx[j]
                val moduleIx = group[j]
                val module = Iterables.get(patterns, moduleIx)
                val pattern = Iterables.get(module, i)
                pattern.state = PatternState.CURRENTLYTAKEN
                selectedPatterns.add(pattern)
                // Set all previous patterns in the same module (i.e., those coarser than the selected pattern) as taken
                for (k in i - 1 downTo 0) {
                    val prevPattern = Iterables.get(module, k)
                    prevPattern.state = PatternState.TAKEN
                }
            }
            j++
        }
        return selectedPatterns
    }
}