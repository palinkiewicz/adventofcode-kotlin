package adventofcode2023

import java.io.File
import java.util.LinkedList

object Day20 {
    private val inputs = File("resources/adventofcode2023/Day20.txt")
        .readLines()
        .map { line ->
            val split = line.split(" -> ")
            Pair(split[0], split[1].split(", "))
        }

    private val modules: Map<String, Module> = inputs.associate { line ->
        val name = line.first.removePrefix("%").removePrefix("&")
        val module =
            when (line.first.first()) {
                '%' -> FlipFlopModule(line.second)
                '&' -> ConjunctionModule(line.second, inputs.filter { it.second.contains(name) }.map {
                    it.first.removePrefix("%").removePrefix("&")
                })
                else -> BroadcastModule(line.second)
            }
        name to module
    }

    private sealed class Module(val outputs: List<String>) {
        abstract fun send(pulse: Boolean, input: String): Map<String, Boolean>

        open fun reset() {}
    }

    private class BroadcastModule(outputs: List<String>) : Module(outputs) {
        override fun send(pulse: Boolean, input: String): Map<String, Boolean> =
            outputs.associateWith { pulse }
    }

    private class FlipFlopModule(outputs: List<String>) : Module(outputs) {
        var enabled = false

        override fun send(pulse: Boolean, input: String): Map<String, Boolean> =
            if (pulse) emptyMap()
            else {
                enabled = !enabled
                outputs.associateWith { enabled }
            }

        override fun reset() { enabled = false }
    }

    private class ConjunctionModule(outputs: List<String>, val inputs: List<String>) : Module(outputs) {
        var lastPulses = inputs.associateWith { false }.toMutableMap()

        override fun send(pulse: Boolean, input: String): Map<String, Boolean> {
            lastPulses[input] = pulse
            return outputs.associateWith { !lastPulses.values.all { it } }
        }

        override fun reset() { lastPulses = inputs.associateWith { false }.toMutableMap() }
    }

    private fun pushButton(): Map<Boolean, Long> {
        val queue = LinkedList<Triple<String, Boolean, String>>()
        val pulseCount = mutableMapOf(true to 0L, false to 0L)

        queue.add(Triple("broadcaster", false, ""))

        while (queue.isNotEmpty()) {
            val (destination, pulse, from) = queue.removeFirst()
            val module = modules[destination]

            pulseCount[pulse] = pulseCount[pulse]!!.plus(1)

            module?.send(pulse, from)?.forEach {
                queue.add(Triple(it.key, it.value, destination))
            }
        }

        return pulseCount
    }

    private fun multiplePushButton(times: Int): Map<Boolean, Long> {
        val pulseCount = mutableMapOf(true to 0L, false to 0L)

        for (i in 1..times)
            pushButton().forEach { pulseCount[it.key] = pulseCount[it.key]!!.plus(it.value) }

        return pulseCount
    }

    fun part1() = println(multiplePushButton(1000).values.reduce { acc, i -> acc * i })
}

fun main() {
    Day20.part1()
}