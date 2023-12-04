package adventofcode2023

import java.io.File
import kotlin.math.pow

object Day04 {
    fun part1(inputs: List<String>) {
        println(inputs.sumOf { line ->
            val numbers = line.split(": ")[1].split(" | ")
            val winningNumbers = numbers[0].split(" ").filter { it != "" }
            val actualNumbers = numbers[1].split(" ").filter { it != "" }
            val correctNumbersCount = actualNumbers.count { winningNumbers.contains(it) }

            if (correctNumbersCount == 0) 0
            else 2.0.pow(correctNumbersCount - 1).toInt()
        })
    }
}

fun main() {
    val inputs = File("resources/adventofcode2023/Day04.txt").readLines()
    Day04.part1(inputs)
}