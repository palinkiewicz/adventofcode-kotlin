package adventofcode2023

import java.io.File

object Day07 {
    fun part1(inputs: List<String>) {
        val labelValues = mapOf(
            'A' to 'E',
            'K' to 'D',
            'Q' to 'C',
            'J' to 'B',
            'T' to 'A'
        )

        fun getTypeScore(hand: String): Int {
            val labelCounts = hand.toSet().map { char -> char to hand.count { it == char } }
            val maxCount = labelCounts.maxOf { it.second }

            if (labelCounts.map { it.second }.reduce { acc, x -> acc * x } == 6) {
                return 5
            } else if (labelCounts.count {it.second == 2} == 2) {
                return 3
            }

            return when (maxCount) {
                1, 2 -> maxCount
                3 -> maxCount + 1
                4, 5 -> maxCount + 2
                else -> throw Exception()
            }
        }

        println(
            inputs
                .map { line ->
                    val (hand, bid) = line.split(" ")
                    Triple(
                        getTypeScore(hand),
                        labelValues.entries.fold(hand) { acc, (key, value) -> acc.replace(key, value) },
                        bid.toInt()
                    )
                }
                .sortedWith(compareBy({ it.first }, { it.second }))
                .map { it.third }
                .reduceIndexed { index, acc, bid -> acc + bid * (index + 1) }
        )
    }
}

fun main() {
    val inputs = File("resources/adventofcode2023/Day07.txt").readLines()
    Day07.part1(inputs)
}