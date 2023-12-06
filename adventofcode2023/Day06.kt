package adventofcode2023

import java.io.File

object Day06 {
    fun part1(inputs: List<String>) {
        val (times, distances) = inputs.map { line ->
            line.replace("\\s+".toRegex(), " ").split(" ").drop(1).map { it.toInt() }
        }

        println(times.indices.map {
            var chargeTime = 1
            while (chargeTime * (times[it] - chargeTime) < distances[it]) chargeTime++
            (times[it] + 1) - 2 * chargeTime
        }.reduce { acc, x -> acc * x })
    }
}

fun main() {
    val inputs = File("resources/adventofcode2023/Day06.txt").readLines()
    Day06.part1(inputs)
}