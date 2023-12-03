package adventofcode2023

import java.io.File

fun part1(file: File) {
    println(file.readLines().sumOf { line ->
        val digits = line.filter { char -> char.isDigit() }
        digits.first().digitToInt() * 10 + digits.last().digitToInt()
    })
}

fun main(args: Array<String>) {
    val inputs = File("resources/adventofcode2023/Day01.txt")
    part1(inputs)
}