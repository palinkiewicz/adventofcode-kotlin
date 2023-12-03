package adventofcode2023

import java.io.File

object Day02 {
    fun part1(inputs: List<String>) {
        val maxCubes = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14
        )

        println(inputs.sumOf { line ->
            var index = line.split(": ")[0].split(" ")[1].toInt()

            for (cubesSet in line.split(": ")[1].split("; ")) {
                for (cubesOfType in cubesSet.split(", ")) {
                    val numberType = cubesOfType.split(" ")

                    if (maxCubes[numberType[1]]!! < numberType[0].toInt()) {
                        index = 0
                        break
                    }
                }
            }

            index
        })
    }
}

fun main() {
    val inputs = File("resources/adventofcode2023/Day02.txt").readLines()
    Day02.part1(inputs)
}