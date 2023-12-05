package adventofcode2023

import java.io.File

object Day05 {
    fun part1(inputs: List<String>) {
        println(inputs[0].split(" ").drop(1).minOf { seed ->
            var number = seed.toLong()
            var i = 1

            while (i < inputs.size) {
                if (inputs[i].split(" ").size == 3) {
                    val line = inputs[i].split(" ").map { it.toLong() }

                    if (line[1] <= number && line[1] + line[2] > number) {
                        number = line[0] + number - line[1]
                        while (i < inputs.size && inputs[i] != "") i++
                    }
                }
                i++
            }
            number
        })
    }
}

fun main() {
    val inputs = File("resources/adventofcode2023/Day05.txt").readLines()
    Day05.part1(inputs)
}