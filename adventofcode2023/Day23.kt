package adventofcode2023

import java.io.File
import java.util.LinkedList

object Day23 {
    private val inputs = File("resources/adventofcode2023/Day23.txt").readLines()

    private const val FOREST = '#'
    private const val EMPTY = '.'

    private val slopeDirections = mapOf(
        '^' to Vector2(0, -1),
        'v' to Vector2(0, 1),
        '>' to Vector2(1, 0),
        '<' to Vector2(-1, 0)
    )

    private data class Vector2(val x: Int, val y: Int) {
        fun add(other: Vector2) = Vector2(x + other.x, y + other.y)

        fun inBounds(): Boolean = (x >= 0 && y >= 0 && x < inputs.size && y < inputs[0].length)

        fun char(): Char = inputs[y][x]

        fun neighbors(): Set<Vector2> {
            val neighbors = mutableSetOf<Vector2>()

            if (char() == EMPTY) {
                slopeDirections.values.forEach {
                    val next = add(it)
                    if (next.inBounds() && next.char() != FOREST) {
                        neighbors.add(next)
                    }
                }
            } else {
                val next = add(slopeDirections[char()]!!)
                if (next.inBounds() && next.char() != FOREST) neighbors.add(next)
            }

            return neighbors
        }
    }

    private fun longestHike(): Int {
        val histories = LinkedList<List<Vector2>>(listOf(listOf(Vector2(1, 0))))
        var longest = 0

        while (histories.isNotEmpty()) {
            val current = histories.removeFirst()
            val lastStep = current.last()

            if (lastStep.y == inputs.size - 1 && lastStep.x == inputs[0].length - 2) {
                if (current.size > longest) longest = current.size
                continue
            }

            lastStep.neighbors().filterNot { current.contains(it) }.forEach {
                histories.add(current + listOf(it))
            }
        }

        return longest - 1
    }

    fun part1() = println(longestHike())
}

fun main() {
    Day23.part1()
}