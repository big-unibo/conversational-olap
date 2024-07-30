package it.unibo.vocalization.selection.model

class NoGroupsProblem : Problem {
    constructor() : super() {}
    constructor(w: Int, profit: IntArray, weight: IntArray, group: IntArray) : super(w, profit, weight, group) {}

    override fun getSolution(option1: Int, option2: Int, option3: Int): Boolean {
        return option2 > option1 || option2 > option3
    }

    override fun checkIfInSameGroup(n: Int, lastTakenGroup: Int, group: IntArray?): Boolean {
        return false
    }

    override fun getMax(group: Int, row: IntArray?, groups: IntArray?, n: Int): Int {
        var max = 0
        for (i in 1 until n) {
            if (row!![i] > max) max = row[i]
        }
        return max
    }

    override fun calculateIsMax(n: Int, w: Int, groups: IntArray?, matrix: Array<IntArray?>?, N: Int): Boolean {
        var max = 0
        for (i in 1..N) {
            if (matrix!![w]!![i] > max) max = matrix[w]!![i]
        }
        return matrix!![w]!![n] == max
    }
}