package adventofcode2023

import java.io.File

fun Pair<Int, Int>.add(other: Pair<Int, Int>): Pair<Int, Int> =
    Pair(this.first + other.first, this.second + other.second)

object Day10 {
    private val inputs = File("resources/adventofcode2023/Day10.txt").readLines()

    private val pipes = mapOf(
        '|' to listOf(Pair(0, -1), Pair(0, 1)),
        '-' to listOf(Pair(-1, 0), Pair(1, 0)),
        'L' to listOf(Pair(0, -1), Pair(1, 0)),
        'J' to listOf(Pair(0, -1), Pair(-1, 0)),
        '7' to listOf(Pair(-1, 0), Pair(0, 1)),
        'F' to listOf(Pair(1, 0), Pair(0, 1)),
        'S' to listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1)),
        '.' to emptyList()
    )

    private fun getCharAt(position: Pair<Int, Int>): Char =
        inputs[position.second][position.first]

    private fun getStartPosition(): Pair<Int, Int> {
        for (y in inputs.indices) {
            val x = inputs[y].indexOf('S')
            if (x != -1) return Pair(x, y)
        }
        throw NoSuchElementException("Start position not found")
    }

    private fun getFirstPipe(startPosition: Pair<Int, Int>): Pair<Int, Int> {
        pipes['S']?.forEach { direction ->
            val position = startPosition.add(direction)
            if (pipes[getCharAt(position)]!!.any { getCharAt(position.add(it)) == 'S' })
                return position
        }
        throw NoSuchElementException("First pipe not found")
    }

    private fun getLoopLength(): Int {
        var last = getStartPosition()
        var current = getFirstPipe(last)
        var counter = 0

        while (getCharAt(current) != 'S') {
            val newCurrent = current.add(pipes[getCharAt(current)]!!.first { current.add(it) != last })
            last = current
            current = newCurrent
            counter++
        }

        return counter
    }

    fun part1() = println((getLoopLength() + 1) / 2)
}

fun main() {
    Day10.part1()
}