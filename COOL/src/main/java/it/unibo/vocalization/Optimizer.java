package it.unibo.vocalization;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
    public static Set<IVocalizationPattern> getDummyPatterns(Collection<IVocalizationPattern> patterns) {
        return new HashSet<>(patterns);
    }

    /**
     * @param patterns patterns among which the most interesting ones are selected
     * @return the most interesting patterns
     */
    public static Set<IVocalizationPattern> getPatterns(Collection<IVocalizationPattern> patterns) {
        throw new IllegalArgumentException("This is not implemented yet");
    }
}
