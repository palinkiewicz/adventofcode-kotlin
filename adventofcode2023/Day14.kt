package adventofcode2023

import java.io.File

object Day14 {
    private const val ROUNDED_ROCK = 'O'
    private const val EMPTY = '.'

    private val inputs = File("resources/adventofcode2023/Day14.txt").readLines()

    private fun rollAll(rocks: List<String>, direction: Pair<Int, Int>): List<String> {
        val rolled = rocks.map { it.toMutableList() }

        val yIndices = if (direction.second < 0) rolled.indices else rolled.indices.reversed()
        val xIndices = if (direction.first < 0) rolled[0].indices else rolled[0].indices.reversed()

        for (y in yIndices) {
            for (x in xIndices) {
                if (rolled[y][x] == ROUNDED_ROCK) {
                    var nextX = x + direction.first
                    var nextY = y + direction.second

                    while (
                        nextY >= 0 && nextX >= 0 && nextY < rolled.size && nextX < rolled[nextY].size &&
                        rolled[nextY][nextX] == EMPTY
                    ) {
                        rolled[nextY - direction.second][nextX - direction.first] = EMPTY
                        rolled[nextY][nextX] = ROUNDED_ROCK

                        nextX += direction.first
                        nextY += direction.second
                    }
                }
            }
        }

        return rolled.map { it.joinToString(separator = "") }
    }

    private fun spinCycle(rocks: List<String>): List<String> =
        rollAll(rollAll(rollAll(rollAll(rocks, Pair(0, -1)), Pair(-1, 0)), Pair(0, 1)), Pair(1, 0))

    private fun runSpinCycles(rocks: List<String>, cycles: Int): List<String> {
        var current = rocks
        val history = mutableListOf<List<String>>()
        var cycle = 0

        while (cycle < cycles) {
            current = spinCycle(current)

            val lastAppearedIndex = history.indexOf(current)
            if (lastAppearedIndex > -1) {
                val toAdd = cycle - lastAppearedIndex
                cycle += ((cycles - cycle) / toAdd) * toAdd
            }

            history.add(current)
            cycle++
        }

        return current
    }

    private fun calculateNorthLoad(rocks: List<String>): Int =
        rocks.withIndex().sumOf { line ->
            line.value.sumOf {
                if (it == ROUNDED_ROCK) rocks.size - line.index else 0
            }
        }

    fun part1() = println(
        calculateNorthLoad(rollAll(inputs, Pair(0, -1)))
    )

    fun part2() = println(
        calculateNorthLoad(runSpinCycles(inputs, 1000000000))
    )
}

fun main() {
    Day14.part1()
    Day14.part2()
}