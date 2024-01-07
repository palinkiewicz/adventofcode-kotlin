package adventofcode2023

import java.io.File
import java.util.LinkedList

object Day25 {
    private val inputs = File("resources/adventofcode2023/Day25.txt").readLines()

    private fun wires(): Map<String, List<String>> {
        val wires = mutableMapOf<String, MutableList<String>>()

        inputs.forEach { line ->
            val (location, otherLocations) = line.split(": ")

            otherLocations.split(" ").forEach {
                wires.getOrPut(location) { mutableListOf() }
                wires.getOrPut(it) { mutableListOf() }

                wires[location]!!.add(it)
                wires[it]!!.add(location)
            }
        }

        return wires
    }

    private fun groupSizesMultipliedIfCorrectNumber(wires: Map<String, List<String>>, number: Int): Int? {
        val checked = mutableListOf<String>()
        val groupCounts = mutableListOf<Int>()
        var i = 0

        while (i < number && wires.size != checked.size) {
            val toCheck = LinkedList<String>()
            toCheck.add(wires.keys.first { !checked.contains(it) })

            while (toCheck.isNotEmpty()) {
                val current = toCheck.removeFirst()

                checked.add(current)
                wires[current]!!
                    .filter { !checked.contains(it) && !toCheck.contains(it) && wires.contains(it) }
                    .forEach { toCheck.add(it) }
            }

            groupCounts.add(checked.size - groupCounts.sum())
            i++
        }

        return if(wires.size == checked.size && i == number) groupCounts.reduce { acc, c -> acc * c }
        else null
    }

    private fun groupSizesMultiplied(groupNumber: Int): Int {
        val wires = wires().toList()

        for (i in wires.indices) {
            for (j in i + 1..<wires.size) {
                for (k in j + 1..<wires.size) {
                    val currentWires = wires.toMap().toMutableMap()

                    currentWires.remove(wires[i].first)
                    currentWires.remove(wires[j].first)
                    currentWires.remove(wires[k].first)

                    val multiplied = groupSizesMultipliedIfCorrectNumber(currentWires, 2)
                    if (multiplied != null) return multiplied
                }
            }
        }

        throw Exception("Couldn't find wires to disconnect to make $groupNumber groups.")
    }

    fun part1() = println(groupSizesMultiplied(2))
}

fun main() {
    Day25.part1()
}