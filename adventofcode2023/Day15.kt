package adventofcode2023

import java.io.File

object Day15 {
    private val inputs = File("resources/adventofcode2023/Day15.txt").readText().split(',')

    private fun hash(word: String): Int =
        word.fold(0) { acc, c ->
            (acc + c.code) * 17 % 256
        }

    private fun hashmap(steps: List<String>): List<List<Pair<String, Int>>> {
        val boxes = List<MutableList<Pair<String, Int>>>(256) { mutableListOf() }

        steps.forEach { step ->
            if (step.contains('=')) {
                val split = step.split('=')
                val labelHash = hash(split[0])
                val index = boxes[labelHash].indexOfFirst { it.first == split[0] }

                if (index > -1) {
                    boxes[labelHash][index] = boxes[labelHash][index].copy(second = split[1].toInt())
                } else {
                    boxes[labelHash].add(Pair(split[0], split[1].toInt()))
                }
            } else {
                boxes[hash(step.removeSuffix("-"))].removeIf { it.first == step.removeSuffix("-") }
            }
        }

        return boxes.map { it.toList() }
    }

    private fun calculateFocusingPower(hashmap: List<List<Pair<String, Int>>>): Int =
        hashmap.withIndex().sumOf { box ->
            box.value.withIndex().fold(0) { acc, lens ->
                acc + (box.index + 1) * (lens.index + 1) * lens.value.second
            }.toInt()
        }

    fun part1() = println(inputs.sumOf { hash(it) })

    fun part2() = println(calculateFocusingPower(hashmap(inputs)))
}

fun main() {
    Day15.part1()
    Day15.part2()
}