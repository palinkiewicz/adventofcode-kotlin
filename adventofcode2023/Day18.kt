package adventofcode2023

import java.io.File
import kotlin.math.abs

object Day18 {
    private val inputs = File("resources/adventofcode2023/Day18.txt")
        .readLines()
        .map { line ->
            val split = line.split(' ')
            Triple(split[0][0], split[1].toLong(), split[2].removeSurrounding("(#", ")"))
        }

    private data class Vector2(val x: Long, val y: Long) {
        fun add(other: Vector2) = Vector2(x + other.x, y + other.y)

        fun multiply(multiplier: Long) = Vector2(x * multiplier, y * multiplier)
    }

    private val normalDigPlan = inputs.map { triple -> shiftDirection(triple.first).multiply(triple.second) }

    private val colorsDigPlan = inputs.map { triple ->
        shiftDirection(triple.third.last()).multiply(triple.third.dropLast(1).toLong(radix = 16))
    }

    private fun shiftDirection(direction: Char) =
        when (direction) {
            '0', 'R' -> Vector2(1, 0)
            '1', 'D' -> Vector2(0, 1)
            '2', 'L' -> Vector2(-1, 0)
            '3', 'U' -> Vector2(0, -1)
            else -> throw Exception("Unknown direction")
        }

    private fun cornerPositions(digPlan: List<Vector2>): List<Vector2> {
        val positions = mutableListOf(Vector2(0, 0))

        digPlan.forEach { shiftVector ->
            positions.add(positions.last().add(shiftVector))
        }

        return positions
    }

    private fun perimeter(digPlan: List<Vector2>): Long =
        digPlan.sumOf { abs(it.x) + abs(it.y) }

    private fun shoelaceFormula(corners: List<Vector2>): Long =
        (corners + listOf(corners.first())).zipWithNext { a, b ->
            a.x * b.y - a.y * b.x
        }.sum() / 2

    private fun integerPointsFromPickTheorem(area: Long, perimeter: Long): Long =
        area + perimeter / 2L + 1L

    private fun lavaCapacity(digPlan: List<Vector2>): Long
        = integerPointsFromPickTheorem(shoelaceFormula(cornerPositions(digPlan)), perimeter(digPlan))

    fun part1() = println(lavaCapacity(normalDigPlan))

    fun part2() = println(lavaCapacity(colorsDigPlan))
}

fun main() {
    Day18.part1()
    Day18.part2()
}