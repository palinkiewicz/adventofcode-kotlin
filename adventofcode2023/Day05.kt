package adventofcode2023

import java.io.File
import kotlin.math.max
import kotlin.math.min

object Day05 {
    fun part1(inputs: List<String>) {
        println(inputs[0].split(" ").drop(1).minOf { seed ->
            var number = seed.toLong()
            var i = 1

            while (i < inputs.size) {
                if (inputs[i].split(" ").size == 3) {
                    val line = inputs[i].split(" ").map { it.toLong() }

                    if (line[1] <= number && line[1] + line[2] > number) {
                        number = line[0] + number - line[1]
                        while (i < inputs.size && inputs[i] != "") i++
                    }
                }
                i++
            }
            number
        })
    }

    fun part2(inputs: List<String>) {
        fun getRangesMaps(): List<List<LongArray>> = inputs
            .joinToString("\n")
            .split("\n\n")
            .drop(1)
            .map { map ->
                map.split("\n").drop(1).map { line ->
                    line.split(" ").map { it.toLong() }.toLongArray()
                }
            }

        fun getSeedRanges(): List<Pair<Long, Long>> = inputs[0]
            .split(" ")
            .drop(1)
            .windowed(2, 2)
            .map {
                Pair(it[0].toLong(), it[0].toLong() + it[1].toLong())
            }


        val rangesMaps = getRangesMaps()
        var ranges = getSeedRanges().toMutableList()

        for (rangesMap in rangesMaps) {
            val newRanges = mutableListOf<Pair<Long, Long>>()
            for (filterRange in rangesMap) {
                var i = 0
                while (i < ranges.size) {
                    val range = ranges[i]
                    val offset = filterRange[0] - filterRange[1]
                    val overlapStart = max(filterRange[1], range.first)
                    val overlapEnd = min(filterRange[1] + filterRange[2], range.second)

                    if (overlapStart < overlapEnd) {
                        newRanges.add(Pair(offset + overlapStart, offset + overlapEnd))
                        ranges.removeAt(i)

                        if (range.second > overlapEnd) {
                            ranges.add(Pair(overlapEnd, range.second))
                        }
                        if (range.first < overlapStart) {
                            ranges.add(Pair(range.first, overlapStart))
                        }
                    } else {
                        i++
                    }
                }
            }
            ranges = (newRanges + ranges).toMutableList()
        }

        println(ranges.minOf { it.first })
    }
}

fun main() {
    val inputs = File("resources/adventofcode2023/Day05.txt").readLines()
    Day05.part1(inputs)
    Day05.part2(inputs)
}