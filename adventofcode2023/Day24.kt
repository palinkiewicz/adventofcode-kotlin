package adventofcode2023

import java.io.File

object Day24 {
    private val inputs = File("resources/adventofcode2023/Day24.txt")
        .readLines()
        .map { line ->
            val (position, velocity) = line.split(" @ ")
            val (posX, posY, posZ) = position.split(", ").map { it.toLong() }
            val (velX, velY, velZ) = velocity.split(", ").map { it.toLong() }
            Hailstone(Vector3(posX, posY, posZ), Vector3(velX, velY, velZ))
        }

    private data class Vector2(val x: Double, val y: Double)

    private data class Vector3(val x: Long, val y: Long, val z: Long)

    private data class Hailstone(val position: Vector3, val velocity: Vector3)

    private fun intersection2d(first: Hailstone, second: Hailstone): Vector2? {
        val pos1 = first.position
        val pos2 = second.position
        val slope1 = first.velocity.y.toDouble() / first.velocity.x
        val slope2 = second.velocity.y.toDouble() / second.velocity.x

        if (slope1 == slope2) return null

        val xInterjection = (pos2.y - pos1.y + slope1 * pos1.x - slope2 * pos2.x) / (slope1 - slope2)
        val yInterjection = slope1 * (xInterjection - pos1.x) + pos1.y

        return Vector2(xInterjection, yInterjection)
    }

    private fun inputsPairPermutations(): List<Pair<Hailstone, Hailstone>> =
        inputs.flatMapIndexed { i, element1 ->
            inputs.subList(i + 1, inputs.size).mapNotNull { element2 ->
                element1.takeUnless { it === element2 }?.let { it to element2 }
            }
        }

    fun part1(min: Long = 200000000000000, max: Long = 400000000000000) = println(
        inputsPairPermutations().count {
            val intersection = intersection2d(it.first, it.second)

            if (intersection == null) false
            else {
                val t1 = (intersection.x - it.first.position.x) / it.first.velocity.x
                val t2 = (intersection.x - it.second.position.x) / it.second.velocity.x
                if (t1 < 0 || t2 < 0) false
                else intersection.x >= min && intersection.x <= max && intersection.y >= min && intersection.y <= max
            }
        }
    )
}

fun main() {
    Day24.part1()
}