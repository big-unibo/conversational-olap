package test.kotlin

import com.google.common.collect.Lists
import com.google.common.collect.Sets
import it.unibo.vocalization.Optimizer.getDummyPatterns
import it.unibo.vocalization.Optimizer.getPatterns
import it.unibo.vocalization.generation.modules.IVocalizationPattern
import it.unibo.vocalization.generation.modules.VocalizationPattern
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestVocalization {

    @Test
    fun testDummy() {
        val a = VocalizationPattern("foo", 0.6, 3.0, "foo")
        val b = VocalizationPattern("bar", 0.7, 3.0, "foo")
        val c = VocalizationPattern("foobar", 0.6, 6.0, "foo")
        val p1: List<IVocalizationPattern> = Lists.newArrayList<IVocalizationPattern>(a, b)
        val p2: List<IVocalizationPattern> = Lists.newArrayList<IVocalizationPattern>(c)
        Assertions.assertEquals(Sets.newHashSet(a, b, c), getDummyPatterns(Sets.newHashSet(p1, p2), 20))
    }

    /**
     * Only take one item for each group; A is taken instead of B
     */
    @Test
    fun test1() {
        val a = VocalizationPattern("foo", 0.6, 3.0, "foo")
        val b = VocalizationPattern("bar", 0.7, 3.0, "foo")
        val c = VocalizationPattern("foobar", 0.6, 6.0, "foo")
        val p1: List<IVocalizationPattern> = Lists.newArrayList<IVocalizationPattern>(a, b)
        val p2: List<IVocalizationPattern> = Lists.newArrayList<IVocalizationPattern>(c)
        Assertions.assertEquals(Sets.newHashSet(b, c), getPatterns(Sets.newHashSet(p1, p2), 20))
    }

    /**
     * Do not exceed bag size; only A is taken
     */
    @Test
    fun test2() {
        val a = VocalizationPattern("foo", 0.6, "m1")
        val b = VocalizationPattern("bar", 0.7, "m1")
        val c = VocalizationPattern("foobar", 0.6, "m2")
        val p1: List<IVocalizationPattern> = Lists.newArrayList<IVocalizationPattern>(a, b)
        val p2: List<IVocalizationPattern> = Lists.newArrayList<IVocalizationPattern>(c)
        Assertions.assertEquals(Sets.newHashSet(b, c), getPatterns(Sets.newHashSet(p1, p2), 2))
        Assertions.assertEquals(Sets.newHashSet<Any>(), getPatterns(Sets.newHashSet(p1, p2), 0))
    }

    @Test
    fun test5() {
        val a = VocalizationPattern("foo", 0.6, "foo")
        val p1: List<IVocalizationPattern> = Lists.newArrayList<IVocalizationPattern>(a)
        val p: MutableCollection<Collection<IVocalizationPattern>> = Sets.newHashSet()
        p.add(p1)
        Assertions.assertEquals(Sets.newHashSet(a), getDummyPatterns(p, 5))
        Assertions.assertEquals(Sets.newHashSet<Any>(), getDummyPatterns(p, 5))
    }

    @Test
    fun test6() {
        val a = VocalizationPattern("foo", 0.6, "foo")
        val b = VocalizationPattern("bar", 0.7, "foo")
        val p1: List<IVocalizationPattern> = Lists.newArrayList<IVocalizationPattern>(a, b)
        val p: MutableCollection<List<IVocalizationPattern>> = Sets.newHashSet()
        p.add(p1)
        Assertions.assertEquals(Sets.newHashSet(b), getPatterns(p, 5))
        Assertions.assertEquals(Sets.newHashSet<Any>(), getPatterns(p, 5))
    }

    @Test
    fun test7() {
        val a = VocalizationPattern("foo", 0.6, "foo")
        val b = VocalizationPattern("bar", 0.7, "foo")
        val c = VocalizationPattern("foobar", 0.8, "foo")
        val p1: List<IVocalizationPattern> = Lists.newArrayList<IVocalizationPattern>(a, b, c)
        val p: MutableCollection<List<IVocalizationPattern>> = Sets.newHashSet()
        p.add(p1)
        Assertions.assertEquals(Sets.newHashSet(c), getPatterns(p, 1))
    }

    @Test
    fun test8() {
        val p1 = VocalizationPattern("Grouped by Product, the average Quantity is 21.6", 1.0, "Preamble")
        Assertions.assertEquals(p1.cost, 8)
        val p2 = VocalizationPattern("The fact with highest Quantity is Beer with 35", 0.32, 0.2, "Top-K")
        Assertions.assertEquals(p2.cost, 9)
        val p3 = VocalizationPattern("The facts with highest Quantity are Beer with 35, Wine with 32, and Cola with 30", 0.9, 0.6, "Top-K")
        Assertions.assertEquals(p3.cost, 16)
        val p4 = VocalizationPattern("Among 2 clusters, the largest one includes 3 facts and has average Quantity 32.3", 0.6, 0.6, "Clustering")
        Assertions.assertEquals(p4.cost, 14)
        val p5 = VocalizationPattern("Among 2 clusters, the largest one includes 3 facts and has average Quantity 32.3, the second one includes 2 facts and has average Quantity 5.5", 1.0, 1.0, "Clustering")
        Assertions.assertEquals(p5.cost, 25)
        val p6 = VocalizationPattern("The Quantity of Pizza is good as compared to the Quantity of Food", 0.55, 0.2, "Assess")
        Assertions.assertEquals(p6.cost, 13)

        val topk: List<IVocalizationPattern> = Lists.newArrayList(p2, p3)
        val clustering: List<IVocalizationPattern> = Lists.newArrayList(p4, p5)
        val assess: List<IVocalizationPattern> = Lists.newArrayList(p6)
        val p: Collection<List<IVocalizationPattern>> = mutableSetOf(topk, clustering, assess)
        val ret = getPatterns(p, 60 - p1.cost) + setOf(p1)
        val cost: Double = ret.map { it.cost }.sum().toDouble()
        Assertions.assertEquals(51.0, cost, 0.001)
        ret.sortedBy { -it.cov }.forEach { println(it) }
    }
}

