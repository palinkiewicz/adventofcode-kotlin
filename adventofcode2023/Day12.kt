package adventofcode2023

import java.io.File

object Day12 {
    private const val OPERATIONAL_CHAR = '.'
    private const val DAMAGED_CHAR = '#'
    private const val UNKNOWN_CHAR = '?'

    private val inputs = File("resources/adventofcode2023/Day12.txt")
        .readLines()
        .map { line ->
            val split = line.split(" ")
            Pair(split[0], split[1].split(",").map { it.toInt() })
        }

    private fun isRecordValid(record: String, groupSizes: List<Int>): Boolean =
        if (record.contains(UNKNOWN_CHAR)) false
        else record.split(OPERATIONAL_CHAR).map { it.length }.filter { it > 0 } == groupSizes

    private fun possibleRecordArrangements(record: String, groupSizes: List<Int>): Int {
        if (!record.contains(UNKNOWN_CHAR)) return if (isRecordValid(record, groupSizes)) 1 else 0

        return possibleRecordArrangements(record.replaceFirst(UNKNOWN_CHAR, DAMAGED_CHAR), groupSizes) +
                possibleRecordArrangements(record.replaceFirst(UNKNOWN_CHAR, OPERATIONAL_CHAR), groupSizes)
    }

    fun part1() = println(inputs.sumOf { possibleRecordArrangements(it.first, it.second) })
}

fun main() {
    Day12.part1()
}