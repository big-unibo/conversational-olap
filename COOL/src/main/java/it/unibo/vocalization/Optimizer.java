package it.unibo.vocalization;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implement the strategies to return the most interesting patterns.
 */
public final class Optimizer {

    /**
     * Dummy method. Return at most three input patterns.
     *
     * @param prevPatterns patterns that have been already returned
     * @param patterns     patterns among which the most interesting ones are selected
     * @return the most interesting patterns
     */
    public static Set<IVocalizationPattern> getDummyPatterns(Collection<Collection<IVocalizationPattern>> patterns) {
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
     * @param prevPatterns patterns that have been already returned
     * @param patterns     patterns among which the most interesting ones are selected
     * @return the most interesting patterns
     */
    public static Set<IVocalizationPattern> getPatterns(Collection<Collection<IVocalizationPattern>> patterns) {
        throw new IllegalArgumentException("This is not implemented yet");
    }
}
