package adventofcode2023

import java.io.File
import kotlin.math.pow

object Day04 {
    private fun countCorrectNumbersOnCard(line: String): Int {
        val numbers = line.split(": ")[1].split(" | ")
        val winningNumbers = numbers[0].split(" ").filter { it != "" }
        val actualNumbers = numbers[1].split(" ").filter { it != "" }
        return actualNumbers.count { winningNumbers.contains(it) }
    }

    fun part1(inputs: List<String>) {
        println(inputs.sumOf { line ->
            val correctNumbersCount = countCorrectNumbersOnCard(line)
            if (correctNumbersCount == 0) 0 else 2.0.pow(correctNumbersCount - 1).toInt()
        })
    }

    fun part2(inputs: List<String>) {
        val scratchcardsCounters = IntArray(inputs.size) { 1 }

        for (i in inputs.indices) {
            val correctNumbersCount = countCorrectNumbersOnCard(inputs[i])
            for (j in i + 1..i + correctNumbersCount) {
                scratchcardsCounters[j] += scratchcardsCounters[i]
            }
        }

        println(scratchcardsCounters.sum())
    }
}

fun main() {
    val inputs = File("resources/adventofcode2023/Day04.txt").readLines()
    Day04.part1(inputs)
    Day04.part2(inputs)
}