package it.unibo.vocalization;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implement the strategies to return the most interesting patterns.
 */
public final class Optimizer {
    /**
     * Dummy method. Return all the input patterns.
     *
     * @param patterns patterns among which the most interesting ones are selected
     * @return the most interesting patterns
     */
    public static Set<IVocalizationPattern> getDummyPatterns(Collection<Collection<IVocalizationPattern>> patterns) {
        final Set<IVocalizationPattern> p = patterns.stream().flatMap(Collection::stream).collect(Collectors.toSet());
        return p;
    }

    /**
     * @param patterns patterns among which the most interesting ones are selected
     * @return the most interesting patterns
     */
    public static Set<IVocalizationPattern> getPatterns(Collection<Collection<IVocalizationPattern>> patterns) {
        throw new IllegalArgumentException("This is not implemented yet");
    }
}
