package it.unibo.vocalization.selection.model

abstract class Problem : IProblem {
    var bagSize: Int
        private set
    var profit: IntArray
        private set
    var weight: IntArray
        private set
    var group: IntArray
        private set

    internal constructor() {
        bagSize = 15
        profit = intArrayOf(0, 15, 11, 5, 8, 12, 18, 20, 14, 8, 9, 6, 10, 50, 7, 2, 3, 6, 5)
        weight = intArrayOf(0, 8, 4, 4, 3, 5, 14, 11, 5, 4, 6, 3, 5, 1, 5, 3, 2, 9, 7)
        group = intArrayOf(-1, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 4, 4, 4, 4)
    }

    internal constructor(bagSize: Int, profit: IntArray, weight: IntArray, group: IntArray) {
        this.bagSize = bagSize
        this.profit = profit
        this.weight = weight
        this.group = group
    }
}