package it.unibo.vocalization.selection.model

internal interface IProblem {
    fun getSolution(option1: Int, option2: Int, option3: Int): Boolean
    fun checkIfInSameGroup(n: Int, lastTakenGroup: Int, group: IntArray?): Boolean
    fun getMax(group: Int, row: IntArray?, groups: IntArray?, n: Int): Int
    fun calculateIsMax(n: Int, w: Int, groups: IntArray?, matrix: Array<IntArray?>?, N: Int): Boolean
}