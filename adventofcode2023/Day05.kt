package adventofcode2023

import java.io.File

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
        fun getRangesMaps(): Array<MutableList<LongArray>> {
            val rangesMaps = Array<MutableList<LongArray>>(7) { mutableListOf() }
            var mapIndex = 0

            for (i in inputs.indices.drop(2)) {
                if (inputs[i] == "")
                    mapIndex++
                else if (inputs[i].split(" ").size == 3)
                    rangesMaps[mapIndex].add(inputs[i].split(" ").map { it.toLong() }.toLongArray())
            }

            return rangesMaps
        }

        fun getSeedRanges(): List<Pair<Long, Long>> {
            val rangesLine = inputs[0].split(" ").drop(1)
            val ranges: MutableList<Pair<Long, Long>> = mutableListOf()

            for (i in rangesLine.indices) {
                if (i % 2 == 0) ranges.add(
                    Pair(
                        rangesLine[i].toLong(),
                        rangesLine[i].toLong() + rangesLine[i + 1].toLong()
                    )
                )
            }

            return ranges
        }

        val rangesMaps = getRangesMaps()
        val seedRanges = getSeedRanges()

        fun getSeedFromLocation(location: Long): Long {
            var number = location
            var mapIndex = rangesMaps.size - 1

            while (mapIndex >= 0) {
                for (line in rangesMaps[mapIndex]) {
                    if (line[0] <= number && line[0] + line[2] > number) {
                        number = line[1] + number - line[0]
                        break
                    }
                }
                mapIndex--
            }

            return number
        }

        for (location in 0L..Long.MAX_VALUE) {
            val number = getSeedFromLocation(location)
            if (seedRanges.any { number >= it.first && number < it.second }) {
                println(location)
                break
            }
        }
    }
}

fun main() {
    val inputs = File("resources/adventofcode2023/Day05.txt").readLines()
    Day05.part1(inputs)
    Day05.part2(inputs)
}