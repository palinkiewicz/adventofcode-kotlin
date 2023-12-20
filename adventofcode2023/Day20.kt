package adventofcode2023

import java.io.File
import java.util.LinkedList

object Day20 {
    private const val BROADCASTER = "broadcaster"
    private const val RX_OUTPUT = "rx"

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

    private fun pushButton(onEveryPulse: (String, Boolean, String) -> Unit) {
        val queue = LinkedList(listOf(Triple(BROADCASTER, false, "")))

        while (queue.isNotEmpty()) {
            val (destination, pulse, from) = queue.removeFirst()
            val module = modules[destination]

            onEveryPulse(destination, pulse, from)

            module?.send(pulse, from)?.forEach {
                queue.add(Triple(it.key, it.value, destination))
            }
        }
    }

    private fun highLowPulsesCounts(times: Int): Map<Boolean, Long> {
        val pulseCount = mutableMapOf(true to 0L, false to 0L)

        for (i in 1..times) {
            pushButton { _, pulse, _ ->
                pulseCount[pulse] = pulseCount[pulse]!!.plus(1)
            }
        }

        return pulseCount
    }

    private fun pushesToGetRx(): Long {
        val beforeRxName = modules.filter { it.value.outputs.contains(RX_OUTPUT) }.map { it.key }.single()
        val beforeRxInputsCounts = modules.filter { it.value.outputs.contains(beforeRxName) }.mapValues { 0L }.toMutableMap()
        var pushCount = 0L

        while (beforeRxInputsCounts.any { it.value == 0L }) {
            pushCount++

            pushButton { destination, pulse, from ->
                if (destination == beforeRxName && pulse) {
                    beforeRxInputsCounts[from] = pushCount
                }
            }
        }

        return beforeRxInputsCounts.values.reduce { acc, i -> lcm(acc, i) }
    }

    private fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

    private fun lcm(a: Long, b: Long): Long = a * b / gcd(a, b)

    fun resetModules() = modules.forEach { it.value.reset() }

    fun part1() = println(highLowPulsesCounts(1000).values.reduce { acc, i -> acc * i })

    fun part2() = println(pushesToGetRx())
}

fun main() {
    Day20.part1()
    Day20.resetModules()
    Day20.part2()
}