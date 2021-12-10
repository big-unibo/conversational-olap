package test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unibo.vocalization.modules.IVocalizationPattern;
import it.unibo.vocalization.Optimizer;
import it.unibo.vocalization.modules.VocalizationPattern;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test the validation accuracy.
 */
public class TestVocalization {
    @Test
    public void testDummy() {
        final VocalizationPattern a = new VocalizationPattern("foo", 0.6, 3, "foo");
        final VocalizationPattern b = new VocalizationPattern("bar", 0.7, 3, "foo");
        final VocalizationPattern c = new VocalizationPattern("foobar", 0.6, 6, "foo");
        final List<IVocalizationPattern> p1 = Lists.newArrayList(a, b);
        final List<IVocalizationPattern> p2 = Lists.newArrayList(c);
        assertEquals(Sets.newHashSet(a, b, c), Optimizer.getDummyPatterns(Sets.newHashSet(p1, p2), 20));
    }

    /**
     * Only take one item for each group; A is taken instead of B
     */
    @Test
    public void test1() {
        final VocalizationPattern a = new VocalizationPattern("foo", 0.6, 3, "foo");
        final VocalizationPattern b = new VocalizationPattern("bar", 0.7, 3, "foo");
        final VocalizationPattern c = new VocalizationPattern("foobar", 0.6, 6, "foo");
        final List<IVocalizationPattern> p1 = Lists.newArrayList(a, b);
        final List<IVocalizationPattern> p2 = Lists.newArrayList(c);
        assertEquals(Sets.newHashSet(b, c), Optimizer.getPatterns(Sets.newHashSet(p1, p2), 20));
    }

    /**
     * Do not exceed bag size; only A is taken
     */
    @Test
    public void test2() {
        final VocalizationPattern a = new VocalizationPattern("foo", 0.6, "m1");
        final VocalizationPattern b = new VocalizationPattern("bar", 0.7, "m1");
        final VocalizationPattern c = new VocalizationPattern("foobar", 0.6, "m2");
        final List<IVocalizationPattern> p1 = Lists.newArrayList(a, b);
        final List<IVocalizationPattern> p2 = Lists.newArrayList(c);
        assertEquals(Sets.newHashSet(b, c), Optimizer.getPatterns(Sets.newHashSet(p1, p2), 2));
        assertEquals(Sets.newHashSet(), Optimizer.getPatterns(Sets.newHashSet(p1, p2), 0));
    }

    @Test
    public void test5() {
        final VocalizationPattern a = new VocalizationPattern("foo", 0.6, "foo");
        final List<IVocalizationPattern> p1 = Lists.newArrayList(a);
        final Collection<Collection<IVocalizationPattern>> p = Sets.newHashSet();
        p.add(p1);

        assertEquals(Sets.newHashSet(a), Optimizer.getDummyPatterns(p, 5));
        assertEquals(Sets.newHashSet(), Optimizer.getDummyPatterns(p, 5));
    }

    @Test
    public void test6() {
        final VocalizationPattern a = new VocalizationPattern("foo", 0.6, "foo");
        final VocalizationPattern b = new VocalizationPattern("bar", 0.7, "foo");
        final List<IVocalizationPattern> p1 = Lists.newArrayList(a, b);
        final Collection<List<IVocalizationPattern>> p = Sets.newHashSet();
        p.add(p1);

        assertEquals(Sets.newHashSet(b), Optimizer.getPatterns(p, 5));
        assertEquals(Sets.newHashSet(), Optimizer.getPatterns(p, 5));
    }

    @Test
    public void test7() {
        final VocalizationPattern a = new VocalizationPattern("foo", 0.6, "foo");
        final VocalizationPattern b = new VocalizationPattern("bar", 0.7, "foo");
        final VocalizationPattern c = new VocalizationPattern("foobar", 0.8, "foo");
        final List<IVocalizationPattern> p1 = Lists.newArrayList(a, b, c);
        final Collection<List<IVocalizationPattern>> p = Sets.newHashSet();
        p.add(p1);
        assertEquals(Sets.newHashSet(c), Optimizer.getPatterns(p, 1));
    }

    @Test
    public void test8() {
        final VocalizationPattern p1 = new VocalizationPattern("Grouped by Product, the average Quantity is 21.6", 1.0, "Preamble");
        assertEquals(p1.getCost(), 8);
        final VocalizationPattern p2 = new VocalizationPattern("The fact with highest Quantity is Beer with 35", 0.32, 0.2, "Top-K");
        assertEquals(p2.getCost(), 9);
        final VocalizationPattern p3 = new VocalizationPattern("The facts with highest Quantity are Beer with 35, Wine with 32, and Cola with 30", 0.9, 0.6, "Top-K");
        assertEquals(p3.getCost(), 16);
        final VocalizationPattern p4 = new VocalizationPattern("Among 2 clusters, the largest one includes 3 facts and has average Quantity 32.3", 0.6, 0.6, "Clustering");
        assertEquals(p4.getCost(), 14);
        final VocalizationPattern p5 = new VocalizationPattern("Among 2 clusters, the largest one includes 3 facts and has average Quantity 32.3, the second one includes 2 facts and has average Quantity 5.5", 1, 1, "Clustering");
        assertEquals(p5.getCost(), 25);
        final VocalizationPattern p6 = new VocalizationPattern("The Quantity of Pizza is good as compared to the Quantity of Food", 0.55, 0.2, "Assess");
        assertEquals(p6.getCost(), 13);
        // final List<IVocalizationPattern> preamble = Lists.newArrayList(p1);
        final List<IVocalizationPattern> topk = Lists.newArrayList(p2, p3);
        final List<IVocalizationPattern> clustering = Lists.newArrayList(p4, p5);
        final List<IVocalizationPattern> assess = Lists.newArrayList(p6);
        final Collection<List<IVocalizationPattern>> p = Sets.newHashSet();
        // p.add(preamble);
        p.add(topk);
        p.add(clustering);
        p.add(assess);
        final Set<IVocalizationPattern> ret = Optimizer.getPatterns(p, 60 - p1.getCost());
        ret.add(p1);
        final double cost = ret.stream().mapToDouble(IVocalizationPattern::getCost).sum();
        assertEquals(51.0, cost, 0.01);
        ret.stream().sorted((i, j) -> -Double.compare(i.getCov(), j.getCov())).forEach(System.out::println);
    }
}