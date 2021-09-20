package test;

import com.google.common.collect.Sets;
import it.unibo.vocalization.IVocalizationPattern;
import it.unibo.vocalization.Optimizer;
import it.unibo.vocalization.PatternState;
import it.unibo.vocalization.VocalizationPattern;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test the validation accuracy.
 */
public class TestVocalization {
    @Test
    public void testDummy() {
        final VocalizationPattern a = new VocalizationPattern("foo", 0.8, 3, "foo", PatternState.AVAILABLE);
        final VocalizationPattern b = new VocalizationPattern("bar", 0.7, 3, "foo", PatternState.AVAILABLE);
        final VocalizationPattern c = new VocalizationPattern("foobar", 0.6, 6, "foo", PatternState.AVAILABLE);
        final Set<IVocalizationPattern> p1 = Sets.newHashSet(a, b);
        final Set<IVocalizationPattern> p2 = Sets.newHashSet(c);
        assertEquals(Sets.newHashSet(a, c), Optimizer.getDummyPatterns(Sets.newHashSet(p1, p2),20));
    }

    /**
     * Only take one item for each group; A is taken instead of B
     */
    @Test
    public void test1() {
        final VocalizationPattern a = new VocalizationPattern("foo", 0.8, 3, "foo", PatternState.AVAILABLE);
        final VocalizationPattern b = new VocalizationPattern("bar", 0.7, 3, "foo", PatternState.AVAILABLE);
        final VocalizationPattern c = new VocalizationPattern("foobar", 0.6, 6, "foo", PatternState.AVAILABLE);
        final Set<IVocalizationPattern> p1 = Sets.newHashSet(a, b);
        final Set<IVocalizationPattern> p2 = Sets.newHashSet(c);
        assertEquals(Sets.newHashSet(a, c), Optimizer.getPatterns(Sets.newHashSet(p1, p2), 20));
    }

    /**
     * Do not exceed bag size; only A is taken
     */
    @Test
    public void test2() {
        final VocalizationPattern a = new VocalizationPattern("foo", 0.8, 3, "foo", PatternState.AVAILABLE);
        final VocalizationPattern b = new VocalizationPattern("bar", 0.7, 3, "foo", PatternState.AVAILABLE);
        final VocalizationPattern c = new VocalizationPattern("foobar", 0.6, 6, "foo", PatternState.AVAILABLE);
        final Set<IVocalizationPattern> p1 = Sets.newHashSet(a, b);
        final Set<IVocalizationPattern> p2 = Sets.newHashSet(c);
        assertEquals(Sets.newHashSet(a), Optimizer.getPatterns(Sets.newHashSet(p1, p2), 4));
    }

    /**
     * No item is taken
     */
    @Test
    public void test3() {
        final VocalizationPattern a = new VocalizationPattern("foo", 0.8, 3, "foo", PatternState.AVAILABLE);
        final VocalizationPattern b = new VocalizationPattern("bar", 0.7, 3, "foo", PatternState.AVAILABLE);
        final VocalizationPattern c = new VocalizationPattern("foobar", 0.6, 6, "foo", PatternState.AVAILABLE);
        final Set<IVocalizationPattern> p1 = Sets.newHashSet(a, b);
        final Set<IVocalizationPattern> p2 = Sets.newHashSet(c);
        assertEquals(Sets.newHashSet(), Optimizer.getPatterns(Sets.newHashSet(p1, p2), 2));
    }

    /**
     * Determinism; return the first in case of tie
     */
    @Test
    public void test4() {
        final VocalizationPattern a = new VocalizationPattern("foo", 0.8, 3, "foo", PatternState.AVAILABLE);
        final VocalizationPattern b = new VocalizationPattern("bar", 0.8, 3, "foo", PatternState.AVAILABLE);
        final Set<IVocalizationPattern> p1 = Sets.newHashSet(a);
        final Set<IVocalizationPattern> p2 = Sets.newHashSet(b);
        assertEquals(Sets.newHashSet(a), Optimizer.getPatterns(Sets.newHashSet(p1, p2), 5));
    }
}
