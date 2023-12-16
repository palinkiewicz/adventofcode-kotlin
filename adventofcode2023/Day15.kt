package adventofcode2023

import java.io.File

object Day15 {
    private val inputs = File("resources/adventofcode2023/Day15.txt").readText().split(',')

    private fun hash(word: String): Int =
        word.fold(0) { acc, c ->
            (acc + c.code) * 17 % 256
        }

    fun part1() = println(inputs.sumOf { hash(it) })
}

fun main() {
    Day15.part1()
}