package adventofcode2023

import java.io.File
import java.util.PriorityQueue

object Day22 {
    private const val EMPTY = -1
    private const val FLOOR = 0

    private val bricks = File("resources/adventofcode2023/Day22.txt")
        .readLines()
        .withIndex()
        .map { brick ->
            val startEnd = brick.value.split('~').map { positions ->
                Vector3(positions.split(',').map { it.toInt() })
            }

            Brick(brick.index + 1, startEnd[0], startEnd[1])
        }

    private val map: Array<Array<Array<Int>>> =
        Array(bricks.maxOf { it.end.x } + 1) {
            Array(bricks.maxOf { it.end.y } + 1) {
                Array(bricks.maxOf { it.end.z } + 1) {
                    if (it == 0) FLOOR else EMPTY
                }
            }
        }

    private fun initializeBricksMap() =
        bricks.forEach { it.updateMap(true) }

    private data class Vector3(val x: Int, val y: Int, val z: Int) {
        fun lower() = Vector3(x, y, z - 1)
    }

    private fun Vector3(positions: List<Int>) = Vector3(positions[0], positions[1], positions[2])

    private data class Brick(val id: Int, var start: Vector3, var end: Vector3) {
        private fun iterateThroughAllCubes(fixedZ: Int = 0, onEveryCube: (Int, Int, Int) -> Unit) {
            for (x in start.x..end.x)
                for (y in start.y..end.y)
                    if (fixedZ > 0)
                        onEveryCube(x, y, fixedZ)
                    else for (z in start.z..end.z)
                        onEveryCube(x, y, z)
        }

        fun verticalNeighborsIds(higher: Boolean): Set<Int> {
            val supportingIds = mutableSetOf<Int>()

            iterateThroughAllCubes(if (higher) end.z + 1 else start.z - 1) { x, y, z ->
                val currentId = map[x][y][z]
                if (currentId > EMPTY && !supportingIds.contains(currentId)) supportingIds.add(currentId)
            }

            return supportingIds
        }

        private fun lower() {
            start = start.lower()
            end = end.lower()
        }

        fun updateMap(fill: Boolean) {
            iterateThroughAllCubes { x, y, z ->
                map[x][y][z] = if (fill) id else -1
            }
        }

        fun numberOfSupporting(): Int =
            verticalNeighborsIds(false).size

        fun supportedBricks(): List<Brick> =
            verticalNeighborsIds(true).map { bricks[it - 1] }

        fun fall(): Boolean {
            return if (numberOfSupporting() == 0) {
                updateMap(false)
                lower()
                updateMap(true)
                true
            } else {
                false
            }
        }
    }

    private fun makeBricksFall() {
        var anyFell = true

        while (anyFell) {
            anyFell = false

            bricks.forEach {
                if (it.fall()) anyFell = true
            }
        }
    }

    private fun numberOfSafeToDisintegrate(): Int =
        bricks.count { brick ->
            brick.supportedBricks().all { it.numberOfSupporting() > 1 }
        }

    private fun numberOfBrickThatWouldFall(brick: Brick): Int {
        val currentBricks = PriorityQueue<Brick> { brick1, brick2 -> brick1.end.z - brick2.end.z }
        val fallenIds = mutableListOf(brick.id)
        var fallenCount = 0

        currentBricks.addAll(brick.supportedBricks())

        while (currentBricks.isNotEmpty()) {
            val current = currentBricks.remove()

            if (current.verticalNeighborsIds(false).all { fallenIds.contains(it) }) {
                if (!fallenIds.contains(current.id)) fallenIds.add(current.id)
                currentBricks.addAll(current.supportedBricks().filterNot { currentBricks.contains(it) })
                fallenCount++
            }
        }

        println("${brick.id}: $fallenCount")
        return fallenCount
    }

    fun initialize() {
        initializeBricksMap()
        makeBricksFall()
    }

    fun part1() = println(numberOfSafeToDisintegrate())

    fun part2() = println(bricks.sumOf { numberOfBrickThatWouldFall(it) })
}

fun main() {
    Day22.initialize()
    Day22.part1()
    Day22.part2()
}