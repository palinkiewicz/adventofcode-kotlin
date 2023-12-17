package adventofcode2023

import java.io.File

object Day16 {
    private const val UPWARDS = '^'
    private const val DOWNWARDS = 'v'
    private const val LEFTWARDS = '<'
    private const val RIGHTWARDS = '>'

    private val beamVectors = mapOf(
        UPWARDS to Pair(0, -1),
        DOWNWARDS to Pair(0, 1),
        LEFTWARDS to Pair(-1, 0),
        RIGHTWARDS to Pair(1, 0)
    )

    private val beamModifiers = mapOf(
        '\\' to mapOf(
            RIGHTWARDS to setOf(DOWNWARDS),
            LEFTWARDS to setOf(UPWARDS),
            UPWARDS to setOf(LEFTWARDS),
            DOWNWARDS to setOf(RIGHTWARDS)
        ),
        '/' to mapOf(
            RIGHTWARDS to setOf(UPWARDS),
            LEFTWARDS to setOf(DOWNWARDS),
            UPWARDS to setOf(RIGHTWARDS),
            DOWNWARDS to setOf(LEFTWARDS)
        ),
        '-' to mapOf(
            RIGHTWARDS to setOf(RIGHTWARDS),
            LEFTWARDS to setOf(LEFTWARDS),
            UPWARDS to setOf(LEFTWARDS, RIGHTWARDS),
            DOWNWARDS to setOf(LEFTWARDS, RIGHTWARDS)
        ),
        '|' to mapOf(
            RIGHTWARDS to setOf(UPWARDS, DOWNWARDS),
            LEFTWARDS to setOf(UPWARDS, DOWNWARDS),
            UPWARDS to setOf(UPWARDS),
            DOWNWARDS to setOf(DOWNWARDS)
        )
    )

    private val inputs = File("resources/adventofcode2023/Day16.txt").readLines()

    private fun isLocationInBounds(x: Int, y: Int): Boolean =
        (x >= 0 && y >= 0 && x < inputs[0].length && y < inputs.size)

    private fun getBeamLocations(
        startingX: Int,
        startingY: Int,
        startingDirection: Char
    ): List<List<List<Char>>> {
        val beamLocations = List(inputs.size) { List(inputs[0].length) { mutableListOf<Char>() } }

        fun sendBeam(initX: Int, initY: Int, direction: Char) {
            var x = initX
            var y = initY
            val shiftVector = beamVectors[direction]!!

            while (isLocationInBounds(x, y)) {
                if (beamLocations[y][x].contains(direction)) return

                beamLocations[y][x].add(direction)

                if (beamModifiers.keys.contains(inputs[y][x])) {
                    beamModifiers[inputs[y][x]]?.get(direction)?.forEach {
                        sendBeam(x + beamVectors[it]!!.first, y + beamVectors[it]!!.second, it)
                    }
                    return
                }

                x += shiftVector.first
                y += shiftVector.second
            }
        }

        sendBeam(startingX, startingY, startingDirection)
        return beamLocations
    }

    private fun countEnergizedTiles(beamLocations: List<List<List<Char>>>): Int =
        beamLocations.sumOf { line -> line.count { it.isNotEmpty() } }

    fun part1() = println(countEnergizedTiles(getBeamLocations(0, 0, RIGHTWARDS)))
}

fun main() {
    Day16.part1()
}