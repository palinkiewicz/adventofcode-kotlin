package adventofcode2023

import java.io.File

object Day08 {
    fun part1(inputs: List<String>) {
        val instructions = inputs[0]
        val nodes = inputs.drop(2).associate {
            val node = it.replace(Regex("[^A-Z ]"), "").replace(Regex("\\s+"), " ").split(" ")
            node[0] to Pair(node[1], node[2])
        }

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

    fun part2(inputs: List<String>) {
        val instructions = inputs[0]
        val nodes = inputs.drop(2).associate {
            val node = it.replace(Regex("[^A-Z ]"), "").replace(Regex("\\s+"), " ").split(" ")
            node[0] to Pair(node[1], node[2])
        }

        fun lcm(a: Long, b: Long): Long {
            var ma = a
            var mb = b
            var remainder: Long

            while (mb != 0L) {
                remainder = ma % mb
                ma = mb
                mb = remainder
            }

            return a * b / ma
        }

        println(
            nodes.filter { it.key.endsWith('A') }.map {
                var steps = 0
                var i = 0
                var location = it.key

                while (!location.endsWith('Z')) {
                    location = if (instructions[i] == 'L') nodes[location]!!.first else nodes[location]!!.second
                    if (i + 1 == instructions.length) i = 0 else  i++
                    steps++
                }

                steps.toLong()
            }.reduce { acc, i -> lcm(acc, i) }
        )
    }
}

fun main() {
    val inputs = File("resources/adventofcode2023/Day08.txt").readLines()
    Day08.part1(inputs)
    Day08.part2(inputs)
}