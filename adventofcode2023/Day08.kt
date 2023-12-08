package adventofcode2023

import java.io.File

object Day08 {
    fun part1(inputs: List<String>) {
        val instructions = inputs[0]
        val nodes = inputs.drop(2).map {
            val node = it.replace(Regex("[^A-Z ]"), "").replace(Regex("\\s+"), " ").split(" ")
            node[0] to Pair(node[1], node[2])
        }.toMap()

        var steps = 0
        var i = 0
        var location = "AAA"

        while (location != "ZZZ") {
            location = if (instructions[i] == 'L') nodes[location]!!.first else nodes[location]!!.second
            if (i + 1 == instructions.length) i = 0 else  i++
            steps++
        }

        println(steps)
    }
}

fun main() {
    val inputs = File("resources/adventofcode2023/Day08.txt").readLines()
    Day08.part1(inputs)
}