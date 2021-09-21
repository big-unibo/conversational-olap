package it.unibo.vocalization;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import knapsack.Knapsack;
import knapsack.model.OneOrNoneFromGroupProblem;
import knapsack.model.Problem;
import knapsack.model.Solution;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implement the strategies to return the most interesting patterns.
 */
public final class Optimizer {

    /**
     * Dummy method. Return at most three input patterns.
     *
     * @param patterns patterns among which the most interesting ones are selected
     * @param maxCost  the size of the knapsack
     * @return the most interesting patterns
     */
    public static Set<IVocalizationPattern> getDummyPatterns(Collection<Collection<IVocalizationPattern>> patterns, int maxCost) {
        final Set<IVocalizationPattern> p = patterns.stream() // iterate over the patterns
                .flatMap(Collection::stream) // flatten the patterns
                .filter(i -> i.getState().equals(PatternState.AVAILABLE)) // select the available ones (NB: no check is done on the originating module)
                .limit(3) // select the first three patterns
                .collect(Collectors.toSet());
        // set the previously taken patterns to TAKEN
        patterns.stream().flatMap(Collection::stream).forEach(i -> i.setState(i.getState().equals(PatternState.CURRENTLYTAKEN) ? PatternState.TAKEN : i.getState()));
        // set the currently taken patterns to CURRENTLYTAKEN (i.e., they are no more available for selection)
        p.forEach(i -> i.setState(PatternState.CURRENTLYTAKEN));
        // return the patterns
        return p;
    }

    /**
     * @param patterns patterns among which the most interesting ones are selected
     * @param maxCost  the size of the knapsack
     * @return the most interesting patterns
     */
    public static Set<IVocalizationPattern> getPatterns(Collection<Collection<IVocalizationPattern>> patterns, int maxCost) {

        // Initialize bagSize = total number of patterns
        int nPatterns = 1; // MCKP library requires an additional slot
        for (Collection<IVocalizationPattern> module : patterns) {
            for (IVocalizationPattern pattern : module) {
                if (pattern.getState().equals(PatternState.AVAILABLE)) {
                    nPatterns++;
                }
            }
        }

        if (nPatterns == 1) {
            return Sets.newHashSet();
        }

        // Initialize other MCKP structures
        int[] profit = new int[nPatterns];
        int[] weight = new int[nPatterns];
        int[] group = new int[nPatterns];
        int[] patternIx = new int[nPatterns]; // Used to store each pattern's index with the module

        profit[0] = 0;
        weight[0] = 0;
        group[0] = -1;

        int currentGroup = 0, currentPattern = 1;
        for (Collection<IVocalizationPattern> module : patterns) {
            int i = 0;
            for (IVocalizationPattern pattern : module) {
                if (pattern.getState().equals(PatternState.AVAILABLE)) {
                    Double interestingness = pattern.getInterestingness().doubleValue() * 100;
                    profit[currentPattern] = interestingness.intValue();
                    weight[currentPattern] = pattern.getCost();
                    group[currentPattern] = currentGroup;
                    patternIx[currentPattern] = i;
                    currentPattern++;
                }
                i++;
            }
            currentGroup++;
        }

        // Initialize and run MCKP problem
        Problem problem = new OneOrNoneFromGroupProblem(maxCost, profit, weight, group);
        Knapsack knapsack = new Knapsack(problem);
        Solution solution = knapsack.solve();

        // Set selected patterns as taken and return them
        Set<IVocalizationPattern> selectedPatterns = new HashSet<>();
        int j = 0;
        for (boolean jthPatternIsSelected : solution.getSolution()) {
            if (jthPatternIsSelected) {
                int i = patternIx[j];
                int moduleIx = group[j];
                Collection<IVocalizationPattern> module = Iterables.get(patterns, moduleIx);
                IVocalizationPattern pattern = Iterables.get(module, i);
                pattern.setState(PatternState.CURRENTLYTAKEN);
                selectedPatterns.add(pattern);
            }
            j++;
        }

        return selectedPatterns;
    }
}
