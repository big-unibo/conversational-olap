package test;

import com.google.common.collect.Sets;
import it.unibo.vocalization.IVocalizationPattern;
import it.unibo.vocalization.Optimizer;
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
        final VocalizationPattern a = new VocalizationPattern("foo", 0.8, 3);
        final VocalizationPattern b = new VocalizationPattern("bar", 0.7, 3);
        final VocalizationPattern c = new VocalizationPattern("foobar", 0.6, 6);
        final Set<IVocalizationPattern> p1 = Sets.newHashSet(a, b);
        final Set<IVocalizationPattern> p2 = Sets.newHashSet(c);
        assertEquals(Sets.newHashSet(a, b, c), Optimizer.getDummyPatterns(Sets.newHashSet(p1, p2)));
    }
}
