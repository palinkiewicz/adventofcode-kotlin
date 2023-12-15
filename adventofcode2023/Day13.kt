package adventofcode2023

import java.io.File

object Day13 {
    private val inputs = File("resources/adventofcode2023/Day13.txt")
        .readText()
        .split("\n\n")
        .map { it.split("\n") }

    private fun verticalLine(list: List<String>, index: Int): String =
        list.joinToString(separator = "") { it[index].toString() }

    private fun checkIfSameLines(list: List<String>, a: Int, b: Int, vertical: Boolean): Boolean
        = (vertical && verticalLine(list, a) == verticalLine(list, b)) || (!vertical && list[a] == list[b])

    private fun findReflectionLine(list: List<String>, vertical: Boolean = false): Int? {
        val maxIndex = if (vertical) list[0].length else list.size
        for (i in 0..maxIndex - 2) {
            if (checkIfSameLines(list, i, i + 1, vertical)) {
                var j = 1
                var isReflection = true

                while (i - j >= 0 && i + j + 1 < maxIndex) {
                    if (!checkIfSameLines(list, i - j, i + j + 1, vertical)) {
                        isReflection = false
                        break
                    }
                    j++
                }

                if (isReflection) return i + 1
            }
        }

        return null
    }

    fun part1() = println(inputs.sumOf {
        val ind = (findReflectionLine(it, true) ?: findReflectionLine(it)?.times(100))
        ind!!.toInt()
    })
}

fun main() {
    Day13.part1()
}