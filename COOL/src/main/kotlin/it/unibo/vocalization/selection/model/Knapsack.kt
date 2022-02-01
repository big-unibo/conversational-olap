package it.unibo.vocalization.selection.model

import it.unibo.vocalization.selection.helper.Printer
import it.unibo.vocalization.selection.model.Problem
import it.unibo.vocalization.selection.model.Solution

class Knapsack(private val problem: Problem) {
    private var maximumProfit = 0

    fun solve(): Solution {
        val bagSize: Int = problem.bagSize
        val profit: IntArray = problem.profit
        val weight: IntArray = problem.weight
        val group: IntArray = problem.group
        val N = profit.size - 1
        val matrix = Array<IntArray?>(bagSize + 1) { IntArray(N + 1) }
        val solution = Array(bagSize + 1) {
            BooleanArray(
                N + 1
            )
        }

        //just to make print pretty :D
        for (i in 0..bagSize) {
            matrix[i]!![0] = i
        }
        for (w in 1..bagSize) {
            for (n in 1..N) {
                if (group[n] == 0) {
                    if (weight[n] <= w) {
                        val option2 = profit[n] + problem.getMax(group[n] - 1, matrix[w - weight[n]], group, n)
                        matrix[w]!![n] = Math.max(profit[n], option2)
                        updateMaximumProfit(matrix[w]!![n])
                        solution[w][n] = true
                    }
                } else {
                    val option1 = problem.getMax(group[n] - 1, matrix[w], group, n)
                    var option2 = Int.MIN_VALUE
                    val option3 = problem.getMax(group[n], matrix[w], group, n)
                    if (weight[n] <= w) {
                        option2 = profit[n] + problem.getMax(group[n] - 1, matrix[w - weight[n]], group, n)
                    }
                    matrix[w]!![n] = Math.max(option1, option2)
                    updateMaximumProfit(matrix[w]!![n])
                    solution[w][n] = problem.getSolution(option1, option2, option3)
                }
            }
        }

//        Printer.printBagTable(bagSize, N, matrix);
//        Printer.printSolutionTable(bagSize, N, solution);
        val take = getSolution(N, bagSize, solution, group, matrix, weight)
        //        Printer.printChosenItems(N, profit, weight, group, take);
        Printer.printResult(N, profit, weight, group, take)
        return Solution(take)
    }

    private fun updateMaximumProfit(profit: Int) {
        if (profit > maximumProfit) {
            maximumProfit = profit
        }
    }

    private fun getSolution(
        N: Int,
        W: Int,
        sol: Array<BooleanArray>,
        group: IntArray,
        matrix: Array<IntArray?>,
        weight: IntArray
    ): BooleanArray {
        val solution = BooleanArray(N + 1)
        var lastTakenGroup = -1
        var n = N
        var w = W
        while (n > 0) {
            if (sol[w][n] && problem.calculateIsMax(n, w, group, matrix, N)) {
                if (problem.checkIfInSameGroup(n, lastTakenGroup, group)) {
                    n--
                    continue
                }
                solution[n] = true
                w = w - weight[n]
                lastTakenGroup = group[n]
            } else {
                solution[n] = false
            }
            n--
        }
        return solution
    }
}