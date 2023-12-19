package adventofcode2023

import java.io.File
import java.util.LinkedList

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
            checkValue(checked[variable]!!)

        fun checkRange(checked: Map<Char, Pair<Int, Int>>): Int =
            (if (checkValue(checked[variable]!!.first)) RANGE_LOWER else 0) +
            (if (checkValue(checked[variable]!!.second)) RANGE_HIGHER else 0)

        private fun checkValue(checkedValue: Int): Boolean =
            when (checker) {
                '>' -> checkedValue > value
                '<' -> checkedValue < value
                else -> throw Exception("Unknown checker")
            }

        companion object {
            const val RANGE_NONE = 0
            const val RANGE_LOWER = 1
            const val RANGE_HIGHER = 2
            const val RANGE_BOTH = 3
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

    private fun acceptedRanges(): List<Map<Char, Pair<Int, Int>>> {
        // Map of values (x, m, a, s), Workflow name, Workflow step index
        val ranges = LinkedList<Triple<Map<Char, Pair<Int, Int>>, String, Int>>()

        val accepted = mutableListOf<Map<Char, Pair<Int, Int>>>()

        ranges.add(Triple(mapOf(
            'x' to Pair(1, 4000),
            'm' to Pair(1, 4000),
            'a' to Pair(1, 4000),
            's' to Pair(1, 4000)
        ), STARTING_WORKFLOW, 0))

        while (ranges.isNotEmpty()) {
            val range = ranges.removeFirst()
            val (valueRanges, workflowName, stepIndex) = range

            when (workflowName) {
                ACCEPT -> {
                    accepted.add(valueRanges)
                    continue
                }
                REJECT -> continue
            }

            val step = workflows[workflowName]!![stepIndex]

            if (step.first != null) {
                val condition = step.first!!
                val (variable, _, value) = condition

                fun createTriple(
                    changeWorkflow: Boolean, firstValue: Int = -1, secondValue: Int = -1
                ): Triple<Map<Char, Pair<Int, Int>>, String, Int> {
                    val newRangeMap = valueRanges.toMutableMap().apply {
                        if (firstValue >= 0) this[variable] = this[variable]!!.copy(first = firstValue)
                        if (secondValue >= 0) this[variable] = this[variable]!!.copy(second = secondValue)
                    }

                    return Triple(newRangeMap, if (changeWorkflow) step.second else workflowName, if (changeWorkflow) 0 else stepIndex + 1)
                }

                when (condition.checkRange(valueRanges)) {
                    Condition.RANGE_NONE -> {
                        ranges.add(Triple(valueRanges, workflowName, stepIndex + 1))
                    }
                    Condition.RANGE_LOWER -> {
                        ranges.add(createTriple(true, secondValue = value - 1))
                        ranges.add(createTriple(false, firstValue = value))
                    }
                    Condition.RANGE_HIGHER -> {
                        ranges.add(createTriple(true, firstValue = value + 1))
                        ranges.add(createTriple(false, secondValue = value))
                    }
                    Condition.RANGE_BOTH -> {
                        ranges.add(Triple(valueRanges, step.second, 0))
                    }
                }
            } else {
                ranges.add(Triple(valueRanges, step.second, 0))
            }
        }

        return accepted
    }

    fun part1() = println(parts.filter { checkPart(it) }.sumOf { it.values.sum() })

    fun part2() = println(acceptedRanges().sumOf { ranges ->
        ranges.values.fold(1L) { acc, pair -> acc * (pair.second - pair.first + 1) }
    })
}

fun main() {
    Day19.part1()
    Day19.part2()
}