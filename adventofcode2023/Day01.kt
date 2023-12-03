package adventofcode2023

import java.io.File

fun part1(inputs: List<String>) {
    println(inputs.sumOf { line ->
        val digits = line.filter { char -> char.isDigit() }
        digits.first().digitToInt() * 10 + digits.last().digitToInt()
    })
}

fun part2(inputs: List<String>) {
    val digits = setOf(
        Pair("one", '1'),
        Pair("two", '2'),
        Pair("three", '3'),
        Pair("four", '4'),
        Pair("five", '5'),
        Pair("six", '6'),
        Pair("seven", '7'),
        Pair("eight", '8'),
        Pair("nine", '9')
    )

    fun findDigit(line: String, indices: IntProgression = line.indices): Int {
        for (i in indices) {
            for (digit in digits) {
                if ((line.length >= i + digit.first.length
                            && line.substring(i, i + digit.first.length) == digit.first)
                    || line[i] == digit.second
                ) {
                    return digit.second.digitToInt()
                }
            }
        }

        return 0
    }

    println(inputs.sumOf { line ->
        findDigit(line) * 10 + findDigit(line, line.indices.reversed())
    })
}

fun main() {
    val inputs = File("resources/adventofcode2023/Day01.txt").readLines()
    part1(inputs)
    part2(inputs)
}