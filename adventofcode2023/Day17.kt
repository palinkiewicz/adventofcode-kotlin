package adventofcode2023

import java.io.File
import java.util.PriorityQueue

object Day17 {
    private val inputs = File("resources/adventofcode2023/Day17.txt")
        .readLines()
        .map { line ->
            line.toList().map { it.digitToInt() }
        }

    private data class Position(val x: Int, val y: Int) {
        fun addPair(other: Pair<Int, Int>) =
            Position(x + other.first, y + other.second)

        fun inBounds() =
            x >= 0 && y >= 0 && x < inputs[0].size && y < inputs.size
    }

    private data class Location(
        val position: Position,
        val distance: Int,
        val direction: Char,
        val consecutiveDirection: Int
    )

    private const val UPWARDS = '^'
    private const val DOWNWARDS = 'v'
    private const val LEFTWARDS = '<'
    private const val RIGHTWARDS = '>'

    private val directionVectors = mapOf(
        UPWARDS to Pair(0, -1),
        DOWNWARDS to Pair(0, 1),
        LEFTWARDS to Pair(-1, 0),
        RIGHTWARDS to Pair(1, 0),
    )

    private val possibleDirections = mapOf(
        UPWARDS to setOf(UPWARDS, LEFTWARDS, RIGHTWARDS),
        DOWNWARDS to setOf(DOWNWARDS, LEFTWARDS, RIGHTWARDS),
        LEFTWARDS to setOf(LEFTWARDS, DOWNWARDS, UPWARDS),
        RIGHTWARDS to setOf(RIGHTWARDS, DOWNWARDS, UPWARDS),
    )

    private fun getNeighbors(location: Location, maxConsecutive: Int): Set<Location> {
        val neighbors = mutableSetOf<Location>()

        possibleDirections[location.direction]?.forEach { nextDirection ->
            val nextPosition = location.position.addPair(directionVectors[nextDirection]!!)
            val nextConsecutiveDirection = if (location.direction == nextDirection) location.consecutiveDirection + 1 else 1

            if (nextPosition.inBounds() && nextConsecutiveDirection <= maxConsecutive) {
                val nextDistance = location.distance + inputs[nextPosition.y][nextPosition.x]
                neighbors.add(Location(nextPosition, nextDistance, nextDirection, nextConsecutiveDirection))
            }
        }

        return neighbors
    }

    private fun dijkstra(destination: Position): Int {
        val frontier = PriorityQueue<Location> { loc1, loc2 -> loc1.distance - loc2.distance }
        frontier.add(Location(Position(0, 0), 0, RIGHTWARDS, 0))

        val minDistances = mutableMapOf<Position, MutableMap<Char, MutableMap<Int, Int>>>()
        minDistances
            .getOrPut(Position(0, 0)) { mutableMapOf() }
            .getOrPut(RIGHTWARDS) { mutableMapOf() }[0] = 0

        while (frontier.isNotEmpty()) {
            val current = frontier.remove()

            if (current.position == destination) return current.distance

            getNeighbors(current, 3).forEach { neighbor ->
                val currentNeighborMinDistance = minDistances[neighbor.position]?.get(neighbor.direction)?.get(neighbor.consecutiveDirection)

                if (currentNeighborMinDistance == null || currentNeighborMinDistance > neighbor.distance) {
                    minDistances
                        .getOrPut(neighbor.position) { mutableMapOf() }
                        .getOrPut(neighbor.direction) { mutableMapOf() }

                    minDistances[neighbor.position]!![neighbor.direction]!![neighbor.consecutiveDirection] = neighbor.distance
                    frontier.add(neighbor)
                }
            }
        }

        throw Exception("Location not found")
    }

    fun part1() = println(dijkstra(Position(inputs[0].size - 1, inputs.size - 1)))
}

fun main() {
    Day17.part1()
}