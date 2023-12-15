package adventofcode2023

import java.io.File

object Day13 {
    private val inputs = File("resources/adventofcode2023/Day13.txt")
        .readText()
        .split("\n\n")
        .map { it.split("\n") }

    private fun verticalLine(list: List<String>, index: Int): String =
        list.joinToString(separator = "") { it[index].toString() }

    private fun countLinesDifference(list: List<String>, a: Int, b: Int, vertical: Boolean): Int {
        val lineA = if (vertical) verticalLine(list, a) else list[a]
        val lineB = if (vertical) verticalLine(list, b) else list[b]
        return lineA.withIndex().count { it.value != lineB[it.index] }
    }

    private fun findReflectionLine(
        list: List<String>,
        different: Int = 0,
        vertical: Boolean = false
    ): Int? {
        val maxIndex = if (vertical) list[0].length else list.size

        for (i in 0..maxIndex - 2) {
            var j = 1
            var differentCount = countLinesDifference(list, i, i + 1, vertical)

            while (differentCount <= different && i - j >= 0 && i + j + 1 < maxIndex) {
                differentCount += countLinesDifference(list, i - j, i + j + 1, vertical)
                j++
            }

            if (differentCount == different) return i + 1
        }

        return null
    }

    fun part1() = println(inputs.sumOf {
        (findReflectionLine(it, vertical = true) ?: findReflectionLine(it)?.times(100))!!.toInt()
    })

    fun part2() = println(inputs.sumOf {
        (findReflectionLine(it, 1, vertical = true) ?: findReflectionLine(it, 1)?.times(100))!!.toInt()
    })
}

fun main() {
    Day13.part1()
    Day13.part2()
}