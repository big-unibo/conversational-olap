package it.unibo.vocalization.selection.helper

object Printer {
    fun printBagTable(W: Int, N: Int, matrix: Array<IntArray>) {
        for (w in 0..W) {
            for (n in 0..N) {
                System.out.format("%5d", matrix[w][n])
            }
            println()
        }
    }

    fun printChosenItems(N: Int, profit: IntArray, weight: IntArray, group: IntArray, take: BooleanArray) {
        println("item" + "\t" + "profit" + "\t" + "weight" + "\t" + "group" + "\t" + "take")
        for (n in 1..N) {
            println(n.toString() + "\t\t" + profit[n] + "\t\t" + weight[n] + "\t\t" + group[n] + "\t\t" + take[n])
        }
    }

    fun printSolutionTable(W: Int, N: Int, solution: Array<BooleanArray>) {
        for (w in 1..W) {
            for (n in 1..N) {
                System.out.format("%7s", solution[w][n])
            }
            println()
        }
    }

    fun printResult(N: Int, profit: IntArray, weight: IntArray, group: IntArray?, take: BooleanArray) {
        val result = StringBuilder("Items picked: ")
        var totalProfit = 0
        var totalWeight = 0
        for (n in 1..N) {
            if (take[n]) {
                result.append(n).append(" ")
                totalProfit += profit[n]
                totalWeight += weight[n]
            }
        }
        result.append(String.format("with total profit of %s and weight %s", totalProfit, totalWeight))
        // println(result)
    }
}