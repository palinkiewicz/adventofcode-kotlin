package adventofcode2023

import java.io.File

fun Pair<Int, Int>.add(other: Pair<Int, Int>): Pair<Int, Int> =
    Pair(this.first + other.first, this.second + other.second)

object Day10 {
    private const val STARTING_CHAR = 'S'
    // Constants for marking flow direction in the second part
    private const val UPWARDS_DIRECTION = 'U'
    private const val DOWNWARDS_DIRECTION = 'D'
    private const val OTHER_DIRECTION = 'O'
    private const val NOT_LOOP = '.'

    private val inputs = File("resources/adventofcode2023/Day10.txt").readLines()

    // Vertical pipes' first element is the direction that flow is going from so that the pipe is going upwards, the second is downwards
    private val pipes = mapOf(
        '|' to listOf(Pair(0, 1), Pair(0, -1)),
        'L' to listOf(Pair(1, 0), Pair(0, -1)),
        'J' to listOf(Pair(-1, 0), Pair(0, -1)),
        '7' to listOf(Pair(0, 1), Pair(-1, 0)),
        'F' to listOf(Pair(0, 1), Pair(1, 0)),
        '-' to listOf(Pair(-1, 0), Pair(1, 0)),
        '.' to emptyList(),
        STARTING_CHAR to listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))
    )

    private val verticalPipes = setOf('|', 'L', 'J', '7', 'F')

    private fun getCharAt(position: Pair<Int, Int>): Char =
        inputs[position.second][position.first]

    private fun getStartPosition(): Pair<Int, Int> {
        for (y in inputs.indices) {
            val x = inputs[y].indexOf(STARTING_CHAR)
            if (x != -1) return Pair(x, y)
        }
        throw NoSuchElementException("Start position not found")
    }

    private fun getFirstPipe(startPosition: Pair<Int, Int>): Pair<Int, Int> {
        pipes[STARTING_CHAR]?.forEach { direction ->
            val position = startPosition.add(direction)
            if (pipes[getCharAt(position)]!!.any { getCharAt(position.add(it)) == STARTING_CHAR })
                return position
        }
        throw NoSuchElementException("First pipe not found")
    }

    private fun getLoopLength(): Int {
        var last = getStartPosition()
        var current = getFirstPipe(last)
        var counter = 0

        while (getCharAt(current) != STARTING_CHAR) {
            val newCurrent = current.add(pipes[getCharAt(current)]!!.first { current.add(it) != last })
            last = current
            current = newCurrent
            counter++
        }

        return counter
    }

    // Returns a map with only the loop consisting of upwards/downwards/other markings
    private fun getLoopDirectionsMap(): List<List<Char>> {
        val directionsMap = List(inputs.size) { MutableList(inputs[0].length) { NOT_LOOP } }

        fun updateDirectionsMap(position: Pair<Int, Int>, char: Char) {
            directionsMap[position.second][position.first] = char
        }

        var last = getStartPosition()
        var current = getFirstPipe(last)
        var currentChar = getCharAt(current)

        while (currentChar != STARTING_CHAR) {
            updateDirectionsMap(
                current,
                if (verticalPipes.contains(currentChar)) {
                    if (last == current.add(pipes[currentChar]!![0])) UPWARDS_DIRECTION else DOWNWARDS_DIRECTION
                } else OTHER_DIRECTION
            )

            val newCurrent = current.add(pipes[currentChar]!!.first { current.add(it) != last })
            last = current
            current = newCurrent
            currentChar = getCharAt(current)
        }

        when (last) {
            current.add(pipes[STARTING_CHAR]!![0]) -> updateDirectionsMap(current, UPWARDS_DIRECTION)
            current.add(pipes[STARTING_CHAR]!![1]) -> updateDirectionsMap(current, DOWNWARDS_DIRECTION)
            else -> updateDirectionsMap(current, OTHER_DIRECTION)
        }

        return directionsMap
    }

    private fun getStartEndCountCharacters(directionsMap: List<List<Char>>): List<Char> {
        directionsMap.forEach { line ->
            line.forEach {
                if (it != NOT_LOOP)
                    return listOf( it, if (it == UPWARDS_DIRECTION) DOWNWARDS_DIRECTION else UPWARDS_DIRECTION )
            }
        }
        throw NoSuchElementException("Start and end count characters not found")
    }

    private fun countTilesEnclosedByLoop(): Int {
        val directionsMap = getLoopDirectionsMap()
        val (startChar, endChar) = getStartEndCountCharacters(directionsMap)
        var counter = 0

        directionsMap.forEach { line ->
            var shouldCount = false

            line.forEach { character ->
                if (character == startChar) shouldCount = true
                else if (character == endChar) shouldCount = false
                else if (shouldCount && character == NOT_LOOP) counter++
            }
        }

        return counter
    }

    fun part1() = println((getLoopLength() + 1) / 2)

    fun part2() = println(countTilesEnclosedByLoop())
}

fun main() {
    Day10.part1()
    Day10.part2()
}