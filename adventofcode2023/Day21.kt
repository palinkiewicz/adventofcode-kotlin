package adventofcode2023

import java.io.File
import java.util.LinkedList

object Day21 {
    private const val START = 'S'
    private const val ROCK = '#'

    private val directions = setOf(
        Vector2(0, -1),
        Vector2(0, 1),
        Vector2(1, 0),
        Vector2(-1, 0)
    )

    private val inputs = File("resources/adventofcode2023/Day21.txt")
        .readLines()
        .map { it.toList() }

    private data class Vector2(val x: Int, val y: Int) {
        fun add(other: Vector2) = Vector2(x + other.x, y + other.y)

        fun inBounds(): Boolean = (x >= 0 && y >= 0 && x < inputs.size && y < inputs[0].size)
    }

    private data class Step(val position: Vector2, val stepCount: Int) {
        fun next(offset: Vector2) = Step(position.add(offset), stepCount + 1)

        fun inputsChar(): Char = inputs[position.x][position.y]
    }

    private fun startingStep(): Step {
        val startingX = inputs.indexOfFirst { it.contains(START) }
        val startingY = inputs[startingX].indexOf(START)
        return Step(Vector2(startingX, startingY), 0)
    }

    private fun neighbors(step: Step): List<Step> =
        directions.map { step.next(it) }.filter { it.position.inBounds() && it.inputsChar() != ROCK }

    private fun stepPossibilities(numberOfSteps: Int): Int {
        val steps = LinkedList(listOf(startingStep()))
        val possible = mutableListOf<Vector2>()

        while (steps.isNotEmpty()) {
            val current = steps.removeFirst()

            if (current.stepCount > numberOfSteps) break
            if ((current.stepCount + numberOfSteps) % 2 == 0) possible.add(current.position)

            steps.addAll(neighbors(current).filterNot { steps.contains(it) || possible.contains(it.position) })
        }

        return possible.size
    }

    fun part1() = println(stepPossibilities(64))
}

fun main() {
    Day21.part1()
}