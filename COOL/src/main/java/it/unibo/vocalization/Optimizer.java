package it.unibo.vocalization;

import com.google.common.collect.Sets;

import java.util.Collection;
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
        return getDummyPatterns(Sets.newHashSet(), patterns);
    }

    /**
     * Dummy method. Return at most three input patterns.
     *
     * @param prevPatterns patterns that have been already returned
     * @param patterns     patterns among which the most interesting ones are selected
     * @return the most interesting patterns
     */
    public static Set<IVocalizationPattern> getDummyPatterns(Collection<IVocalizationPattern> prevPatterns, Collection<Collection<IVocalizationPattern>> patterns) {
        return patterns.stream() // iterate over the patterns
                .flatMap(Collection::stream) // flatten the patterns
                .filter(i -> !prevPatterns.contains(i)) // pick those that are not part of the previous patterns
                .limit(3) // select the first three patterns
                .collect(Collectors.toSet());
    }

    /**
     * @param patterns patterns among which the most interesting ones are selected
     * @return the most interesting patterns
     */
    public static Set<IVocalizationPattern> getPatterns(Collection<Collection<IVocalizationPattern>> patterns) {
        return getPatterns(Sets.newHashSet(), patterns);
    }


    /**
     * @param prevPatterns patterns that have been already returned
     * @param patterns     patterns among which the most interesting ones are selected
     * @return the most interesting patterns
     */
    public static Set<IVocalizationPattern> getPatterns(Collection<IVocalizationPattern> prevPatterns, Collection<Collection<IVocalizationPattern>> patterns) {
        throw new IllegalArgumentException("This is not implemented yet");
    }
}
