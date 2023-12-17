package adventofcode2023

import java.io.File
import java.util.PriorityQueue

object Day17 {
    private val inputs = File("resources/adventofcode2023/Day17.txt")
        .readLines()
        .map { line ->
            line.toList().map { it.digitToInt() }
        }

    private fun dijkstra(destinationX: Int, destinationY: Int): Int {
        data class Location(val x: Int, val y: Int, val distance: Int)

        fun getNeighbors(x: Int, y: Int, distance: Int): Set<Location> {
            val neighbors = mutableSetOf<Location>()

            if (x > 0) neighbors.add(Location(x - 1, y, distance + inputs[y][x - 1]))
            if (y > 0) neighbors.add(Location(x, y - 1, distance + inputs[y - 1][x]))
            if (x < inputs[0].size - 1) neighbors.add(Location(x + 1, y, distance + inputs[y][x + 1]))
            if (y < inputs.size - 1) neighbors.add(Location(x, y + 1, distance + inputs[y + 1][x]))

            return neighbors
        }

        val frontier = PriorityQueue<Location> { loc1, loc2 -> loc1.distance - loc2.distance }
        frontier.add(Location(0, 0, 0))
        val minDistances = Array(inputs[0].size) {Array<Int?>(inputs.size) { null } }
        minDistances[0][0] = 0

        while (frontier.isNotEmpty()) {
            val current = frontier.remove()

            if (current.x == destinationX && current.y == destinationY) return current.distance

            getNeighbors(current.x, current.y, current.distance).forEach { neighbor ->
                if (minDistances[neighbor.x][neighbor.y] == null || minDistances[neighbor.x][neighbor.y]!! > neighbor.distance) {
                    frontier.add(neighbor)
                    minDistances[neighbor.x][neighbor.y] = neighbor.distance
                }
            }
        }

        throw Exception("Location not found")
    }

    fun part1() = println(dijkstra(inputs[0].size - 1, inputs.size - 1))
}

fun main() {
    Day17.part1()
}