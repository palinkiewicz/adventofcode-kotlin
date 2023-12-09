package adventofcode2023

import java.io.File

object Day09 {
    private fun getPrediction(values: List<Int>, end: Boolean = true): Int {
        return if (values.all { it == 0 }) 0
        else if (end) values.last() + getPrediction(values.zipWithNext { a, b -> b - a })
        else values.first() - getPrediction(values.zipWithNext { a, b -> b - a }, end = false)
    }

    fun part1(inputs: List<List<Int>>) = println(inputs.sumOf { getPrediction(it) })

    fun part2(inputs: List<List<Int>>) = println(inputs.sumOf { getPrediction(it, end = false) })
}

fun main() {
    val inputs = File("resources/adventofcode2023/Day09.txt")
        .readLines()
        .map { line ->
            line.split(" ").map { it.toInt() }
        }

    Day09.part1(inputs)
    Day09.part2(inputs)
}