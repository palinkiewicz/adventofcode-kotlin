package adventofcode2023

import java.io.File
import kotlin.math.abs

object Day11 {
    private const val EMPTY_CHAR = '.'
    private const val GALAXY_CHAR = '#'

    private val inputs = File("resources/adventofcode2023/Day11.txt").readLines()

    private fun getExpandedUniverse(universe: List<String>): List<String> {
        val expandedUniverse = universe.map { it.toMutableList() }.toMutableList()

        // Add horizontal lines
        var i = 0
        while (i < expandedUniverse.size) {
            if (expandedUniverse[i].count {it == EMPTY_CHAR} == expandedUniverse[i].size) {
                expandedUniverse.add(i, MutableList(expandedUniverse[i].size) { EMPTY_CHAR })
                i++
            }
            i++
        }

        // Add vertical lines
        i = 0
        while (i < expandedUniverse[0].size) {
            if (expandedUniverse.count { it[i] == EMPTY_CHAR } == expandedUniverse.count()) {
                expandedUniverse.forEach { it.add(i, EMPTY_CHAR) }
                i++
            }
            i++
        }

        return expandedUniverse.map { it.joinToString("") }
    }

    private fun getGalaxiesPositions(universe: List<String>): List<Pair<Int, Int>> {
        val positions: MutableList<Pair<Int, Int>> = mutableListOf()
        for (y in universe.indices)
            for (x in universe[y].indices)
                if (universe[y][x] == GALAXY_CHAR) positions.add(Pair(x, y))
        return positions
    }

    private fun getDistance(positionA: Pair<Int, Int>, positionB: Pair<Int, Int>): Int =
        abs(positionA.first - positionB.first) + abs(positionA.second - positionB.second)

    fun part1() {
        val galaxiesPositions = getGalaxiesPositions(getExpandedUniverse(inputs))
        var distancesSum = 0

        for (i in galaxiesPositions.indices)
            for (j in i + 1..<galaxiesPositions.size)
                distancesSum += getDistance(galaxiesPositions[i], galaxiesPositions[j])

        println(distancesSum)
    }
}

fun main() {
    Day11.part1()
}