package adventofcode2023

import java.io.File
import kotlin.math.abs

object Day18 {
    private val inputs = File("resources/adventofcode2023/Day18.txt")
        .readLines()
        .map { line ->
            val split = line.split(' ')
            Step(split[0][0], split[1].toInt())
        }

    private data class Step(val direction: Char, val times: Int)

    private data class Vector2(val x: Long, val y: Long) {
        fun add(other: Vector2) = Vector2(x + other.x, y + other.y)
    }

    private const val UPWARDS = 'U'
    private const val DOWNWARDS = 'D'
    private const val LEFTWARDS = 'L'
    private const val RIGHTWARDS = 'R'
    private const val EMPTY = '.'

    private val digitsToDirections = mapOf(
        '0' to RIGHTWARDS,
        '1' to DOWNWARDS,
        '2' to LEFTWARDS,
        '3' to UPWARDS
    )

    private val inputsFromColors = File("resources/adventofcode2023/Day18.txt")
        .readLines()
        .map { line ->
            val color = line.split(' ')[2].removeSurrounding("(#", ")")
            val direction = digitsToDirections[color.last()]!!
            val times = color.dropLast(1).toLong(radix = 16)

            when (direction) {
                UPWARDS -> Vector2(0, -times)
                DOWNWARDS -> Vector2(0, times)
                LEFTWARDS -> Vector2(-times, 0)
                RIGHTWARDS -> Vector2(times, 0)
                else -> throw Exception("Unknown direction")
            }
        }

    private fun getCornerPositions(digPlan: List<Vector2>): List<Vector2> {
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

    private fun getBorders(): List<List<Char>> {
        val map = mutableListOf<MutableList<Char>>(mutableListOf())
        var currentX = 0
        var currentY = 0

        fun dig(direction: Char) {
            if (currentX < 0) {
                currentX = 0
                map.add(0, MutableList(map[0].size) { EMPTY })
            } else if (currentX >= map.size) {
                map.add(MutableList(map[0].size) { EMPTY })
            }

            if (currentY < 0) {
                currentY = 0
                map.forEach { it.add(0, EMPTY) }
            } else if (currentY >= map[0].size) {
                map.forEach { it.add(EMPTY) }
            }

            map[currentX][currentY] = direction
        }

        inputs.forEach { step ->
            if (step.direction == LEFTWARDS || step.direction == RIGHTWARDS) dig(step.direction)

            for (i in 1..step.times) {
                when (step.direction) {
                    UPWARDS -> currentY--
                    DOWNWARDS -> currentY++
                    LEFTWARDS -> currentX--
                    RIGHTWARDS -> currentX++
                }

                dig(step.direction)
            }
        }

        return map
    }

    private fun getArea(borders: List<List<Char>>): Int {
        var area = 0

        borders.forEach { line ->
            var shouldCount = false

            line.withIndex().forEach {
                when (it.value) {
                    UPWARDS, DOWNWARDS, RIGHTWARDS, LEFTWARDS -> {
                        area++
                        shouldCount = it.value == RIGHTWARDS
                    }
                    EMPTY -> if (shouldCount) area++
                }
            }
        }

        return area
    }

    fun part1() = println(getArea(getBorders()))

    fun part2() {
        println(integerPointsFromPickTheorem(shoelaceFormula(getCornerPositions(inputsFromColors)), perimeter(inputsFromColors)))
    }
}

fun main() {
    Day18.part1()
    Day18.part2()
}