package adventofcode2023

import java.io.File

object Day14 {
    private const val ROUNDED_ROCK = 'O'
    private const val EMPTY = '.'

    private val inputs = File("resources/adventofcode2023/Day14.txt").readLines()

    private fun rollAllNorth(): List<String> {
        val rolledInputs = inputs.map { it.toMutableList() }

        for (y in rolledInputs.indices) {
            for (x in rolledInputs[y].indices) {
                if (rolledInputs[y][x] == ROUNDED_ROCK) {
                    var currentY = y

                    while (currentY > 0 && rolledInputs[currentY - 1][x] == EMPTY) {
                        rolledInputs[currentY][x] = EMPTY
                        currentY--
                        rolledInputs[currentY][x] = ROUNDED_ROCK
                    }
                }
            }
        }

        return rolledInputs.map { it.joinToString(separator = "") }
    }

    fun part1() =
        println(rollAllNorth().withIndex().sumOf { line ->
            line.value.sumOf {
                if (it == ROUNDED_ROCK) inputs.size - line.index else 0
            }
        })
}

fun main() {
    Day14.part1()
}