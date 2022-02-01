package it.unibo.vocalization.selection.model

import java.util.*

class Solution(val solution: BooleanArray) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val solution1 = o as Solution
        return Arrays.equals(solution, solution1.solution)
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(solution)
    }

    override fun toString(): String {
        return "Solution{solution=" + Arrays.toString(solution) + '}'
    }
}