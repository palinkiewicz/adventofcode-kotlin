package adventofcode2023

import java.io.File

object Day07 {
    private fun getBidsMultiplication(
        inputs: List<String>,
        labelValues: Map<Char, Char>,
        getTypeScore: (String) -> Int)
    {
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

        getBidsMultiplication(inputs, labelValues) { getTypeScore(it) }
    }

    fun part2(inputs: List<String>) {
        val labelValues = mapOf(
            'A' to 'D',
            'K' to 'C',
            'Q' to 'B',
            'T' to 'A',
            'J' to '1'
        )

        fun isFull(labelCounts: List<Pair<Char, Int>>, jokerCount: Int): Boolean =
            labelCounts.map { it.second }.reduce { acc, x -> acc * x } == 6 ||
            labelCounts.size == 2 &&
                    ((labelCounts[0].second + jokerCount) * labelCounts[1].second == 6 ||
                    (labelCounts[1].second + jokerCount) * labelCounts[0].second == 6)

        fun isTwoPair(labelCounts: List<Pair<Char, Int>>, jokerCount: Int): Boolean =
            labelCounts.count {it.second == 2} == 2 ||
            labelCounts.count {it.second == 2} == 1 && jokerCount > 0


        fun getTypeScore(hand: String): Int {
            val labelCounts = hand.toSet().map { char -> char to hand.count { it == char } }.filter { it.first != 'J' }
            val jokerCount = hand.count { it == 'J' }
            val maxCount = if (jokerCount == 5) 5 else labelCounts.maxOf { it.second + jokerCount }

            return if (maxCount == 5 || maxCount == 4) {
                maxCount + 2
            } else if (isFull(labelCounts, jokerCount)) {
                5
            } else if (maxCount == 3) {
                maxCount + 1
            } else if (isTwoPair(labelCounts, jokerCount)) {
                3
            } else {
                maxCount
            }
        }

        getBidsMultiplication(inputs, labelValues) { getTypeScore(it) }
    }
}

fun main() {
    val inputs = File("resources/adventofcode2023/Day07.txt").readLines()
    Day07.part1(inputs)
    Day07.part2(inputs)
}