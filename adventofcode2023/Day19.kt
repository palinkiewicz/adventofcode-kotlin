package adventofcode2023

import java.io.File

object Day19 {
    private const val ACCEPT = "A"
    private const val REJECT = "R"
    private const val STARTING_WORKFLOW = "in"

    private val inputs = File("resources/adventofcode2023/Day19.txt")
        .readText()
        .split("\n\n")
        .map { it.split("\n") }

    private val workflows = inputs.first().associate { line ->
        val split = line.split('{')
        val steps = split.last().removeSuffix("}").split(',').map { step ->
            val stepSplit = step.split(':')
            if (stepSplit.size == 1)
                Pair(null, stepSplit.first())
            else
                Pair(
                    Condition(
                        stepSplit.first().first(),
                        stepSplit.first().drop(1).first(),
                        stepSplit.first().drop(2).toInt()
                    ),
                    stepSplit.last()
                )
        }
        split[0] to steps
    }

    private val parts = inputs.last().map { line ->
        val split = line.removeSurrounding("{", "}").split(',')
        split.associate {
            val rating = it.split('=')
            rating.first().first() to rating.last().toInt()
        }
    }

    private data class Condition(val variable: Char, val checker: Char, val value: Int) {
        fun check(checked: Map<Char, Int>): Boolean =
            when (checker) {
                '>' -> checked[variable]!! > value
                '<' -> checked[variable]!! < value
                else -> throw Exception("Unknown checker")
            }
    }

    private fun checkPart(part: Map<Char, Int>): Boolean {
        var currentWorkflow = workflows[STARTING_WORKFLOW]!!
        var stepIndex = 0

        while (stepIndex < currentWorkflow.size) {
            val step = currentWorkflow[stepIndex]
            stepIndex++

            if (step.first != null && !step.first!!.check(part)) continue

            when (step.second) {
                ACCEPT -> return true
                REJECT -> return false
                else -> {
                    currentWorkflow = workflows[step.second]!!
                    stepIndex = 0
                }
            }
        }

        throw Exception("Couldn't check part")
    }

    fun part1() = println(parts.filter { checkPart(it) }.sumOf { it.values.sum() })
}

fun main() {
    Day19.part1()
}