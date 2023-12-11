package adventofcode2023

import java.io.File
import kotlin.math.abs

object Day11 {
    private const val EMPTY_CHAR = '.'
    private const val GALAXY_CHAR = '#'

    private val inputs = File("resources/adventofcode2023/Day11.txt").readLines()

    private fun getEmptyRowsAndColumns(): Pair<List<Int>, List<Int>> {
        val rows = mutableListOf<Int>()
        val columns = mutableListOf<Int>()

        for (i in inputs.indices)
            if (inputs[i].count {it == EMPTY_CHAR} == inputs[i].length)
                rows.add(i)
        for (i in inputs[0].indices)
            if (inputs.count { it[i] == EMPTY_CHAR } == inputs.size)
                columns.add(i)

        return Pair(rows, columns)
    }

    private fun getGalaxiesPositions(additionalEmpty: Int = 0): List<Pair<Int, Int>> {
        val (emptyRows, emptyColumns) = getEmptyRowsAndColumns()
        val positions: MutableList<Pair<Int, Int>> = mutableListOf()

        for (y in inputs.indices)
            for (x in inputs[y].indices)
                if (inputs[y][x] == GALAXY_CHAR)
                    positions.add(Pair(
                        x + emptyColumns.count {it < x} * additionalEmpty,
                        y + emptyRows.count {it < y} * additionalEmpty
                    ))

        return positions
    }

    private fun getDistance(positionA: Pair<Int, Int>, positionB: Pair<Int, Int>): Int =
        abs(positionA.first - positionB.first) + abs(positionA.second - positionB.second)

    private fun getDistancesSum(additionalEmpty: Int): Long {
        val galaxiesPositions = getGalaxiesPositions(additionalEmpty)
        var distancesSum = 0L

        for (i in galaxiesPositions.indices)
            for (j in i + 1..<galaxiesPositions.size)
                distancesSum += getDistance(galaxiesPositions[i], galaxiesPositions[j])

        return distancesSum
    }

    fun part1() = println(getDistancesSum(1))

    fun part2() = println(getDistancesSum(999999))
}

fun main() {
    Day11.part1()
    Day11.part2()
}