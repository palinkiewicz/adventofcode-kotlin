package adventofcode2023

import java.io.File
import java.util.LinkedList
import kotlin.math.max

object Day23 {
    private var inputs = File("resources/adventofcode2023/Day23.txt").readLines()

    private const val FOREST = '#'
    private const val EMPTY = '.'

    private val slopeDirections = mapOf(
        '^' to Vector2(0, -1),
        'v' to Vector2(0, 1),
        '>' to Vector2(1, 0),
        '<' to Vector2(-1, 0)
    )

    private val startPoint = Vector2(1, 0)
    private val endPoint = Vector2(inputs[0].length - 2, inputs.size - 1)

    private data class Vector2(val x: Int, val y: Int) {
        fun add(other: Vector2) = Vector2(x + other.x, y + other.y)

        fun inBounds(): Boolean = (x >= 0 && y >= 0 && x < inputs.size && y < inputs[0].length)

        fun char(): Char = inputs[y][x]

        fun neighbors(slipperySlopes: Boolean = true): Set<Vector2> {
            val neighbors = mutableSetOf<Vector2>()

            if (!slipperySlopes || char() == EMPTY) {
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

    private fun searchCrossings(crossingLocation: Vector2, slipperySlopes: Boolean = true): List<Pair<Vector2, Int>> {
        val crossings = mutableListOf<Pair<Vector2, Int>>()

        crossingLocation.neighbors().forEach { start ->
            var current = start
            var currentNeighbors = current.neighbors(slipperySlopes).filter { it != crossingLocation }
            var distance = 1

            while (currentNeighbors.size == 1) {
                val next = currentNeighbors.single()

                if (current == endPoint || current == startPoint) break

                currentNeighbors = next.neighbors(slipperySlopes).filter { it != current }
                current = next
                distance++
            }

            if (current == endPoint || current == startPoint || currentNeighbors.isNotEmpty())
                crossings.add(Pair(current, distance))
        }

        return crossings
    }

    private fun crossingGraph(slipperySlopes: Boolean = true): Map<Vector2, List<Pair<Vector2, Int>>> {
        val graph = mutableMapOf<Vector2, List<Pair<Vector2, Int>>>()
        graph[startPoint] = searchCrossings(startPoint, slipperySlopes)

        for (y in inputs.indices) {
            for (x in inputs[y].indices) {
                if (inputs[y][x] != FOREST) {
                    val location = Vector2(x, y)

                    if (location.neighbors().size > 2) {
                        graph[location] = searchCrossings(location, slipperySlopes)
                    }
                }
            }
        }

        return graph
    }

    private fun longestHike(graph: Map<Vector2, List<Pair<Vector2, Int>>>): Int {
        val histories = LinkedList(listOf(Pair(0, listOf(Vector2(1, 0)))))
        var longest = 0

        while (histories.isNotEmpty()) {
            val current = histories.removeFirst()
            val lastStep = current.second.last()

            if (lastStep == endPoint) {
                longest = max(longest, current.first)
                continue
            }

            graph[lastStep]!!.filterNot { current.second.contains(it.first) }.forEach {
                histories.add(Pair(current.first + it.second, current.second + listOf(it.first)))
            }
        }

        return longest
    }

    fun part1() = println(longestHike(crossingGraph()))

    fun part2() = println(longestHike(crossingGraph(false)))
}

fun main() {
    Day23.part1()
    Day23.part2()
}