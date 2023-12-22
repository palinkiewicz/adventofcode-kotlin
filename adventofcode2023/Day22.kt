package adventofcode2023

import java.io.File

object Day22 {
    private const val EMPTY = -1
    private const val FLOOR = 0

//    private val bricks = ("1,0,1~1,2,1\n" +
//            "0,0,2~2,0,2\n" +
//            "0,2,3~2,2,3\n" +
//            "0,0,4~0,2,4\n" +
//            "2,0,5~2,2,5\n" +
//            "0,1,6~2,1,6\n" +
//            "1,1,8~1,1,9").split("\n")

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

        fun supports(): List<Brick> =
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
            brick.supports().all { it.numberOfSupporting() > 1 }
        }

    fun part1() {
        initializeBricksMap()
        makeBricksFall()
        println(numberOfSafeToDisintegrate())
    }
}

fun main() {
    Day22.part1()
}