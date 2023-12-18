package adventofcode2023

import java.io.File

object Day18 {
    private val inputs = File("resources/adventofcode2023/Day18.txt")
        .readLines()
        .map { line ->
            val split = line.split(' ')
            Step(split[0][0], split[1].toInt(), split[2].removeSurrounding("(#", ")"))
        }

    private data class Step(val direction: Char, val times: Int, val color: String)

    private const val UPWARDS = 'U'
    private const val DOWNWARDS = 'D'
    private const val LEFTWARDS = 'L'
    private const val RIGHTWARDS = 'R'
    private const val EMPTY = '.'

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
}

fun main() {
    Day18.part1()
}