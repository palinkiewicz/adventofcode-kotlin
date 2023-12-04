package adventofcode2023

import java.io.File

object Day03 {
    fun part1(inputs: List<String>) {
        val firstDigitChecks = setOf(
            Pair(-1, 0),
            Pair(-1, -1),
            Pair(0, -1),
            Pair(-1, 1),
            Pair(0, 1)
        )
        val centralDigitChecks = setOf(
            Pair(0, 1),
            Pair(0, -1)
        )
        val lastDigitChecks = setOf(
            Pair(1, 0),
            Pair(1, 1),
            Pair(0, 1),
            Pair(1, -1),
            Pair(0, -1)
        )

        fun checkPositions(x: Int, y: Int, checks: Set<Pair<Int, Int>>): Boolean {
            return checks.any {
                val checkX = x + it.first
                val checkY = y + it.second

                if (checkX < 0 || checkY < 0
                    || checkX >= inputs[0].length || checkY >= inputs.size
                ) {
                    false
                } else {
                    inputs[checkY][checkX] != '.'
                }
            }
        }

        fun checkPartNumberDigit(x: Int, y: Int): Boolean =
            (x > 0 && !inputs[y][x - 1].isDigit() && checkPositions(x, y, firstDigitChecks)
                    || x < inputs[0].length - 1 && !inputs[y][x + 1].isDigit() && checkPositions(x, y, lastDigitChecks)
                    || checkPositions(x, y, centralDigitChecks))

        var sum = 0

        for (y in inputs.indices) {
            var x = 0
            while (x < inputs[0].length) {
                if (inputs[y][x].isDigit()) {
                    var isPartNumber = checkPartNumberDigit(x, y)
                    var number = inputs[y][x].toString()

                    while (x < inputs[0].length - 1 && inputs[y][x + 1].isDigit()) {
                        x++
                        number += inputs[y][x]
                        if (!isPartNumber && checkPartNumberDigit(x, y)) isPartNumber = true
                    }

                    if (isPartNumber) sum += number.toInt()
                }
                x++
            }
        }

        println(sum)
    }

    fun part2(inputs: List<String>) {
        fun getAsteriskPositions(): List<Pair<Int, Int>> {
            val asteriskPositions = mutableListOf<Pair<Int, Int>>()
            for (y in inputs.indices) {
                for (x in inputs[0].indices) {
                    if (inputs[y][x] == '*') {
                        asteriskPositions.add(Pair(x, y))
                    }
                }
            }
            return asteriskPositions
        }

        fun getPartNumber(x: Int, y: Int): Pair<Int, Int>? {
            if (inputs[y][x].isDigit()) {
                var number = inputs[y][x].toString()
                var checkLeft = x - 1
                var checkRight = x + 1

                while (checkLeft >= 0 && inputs[y][checkLeft].isDigit()) {
                    number = inputs[y][checkLeft] + number
                    checkLeft--
                }
                while (checkRight < inputs[0].length && inputs[y][checkRight].isDigit()) {
                    number += inputs[y][checkRight]
                    checkRight++
                }

                return Pair(checkLeft * y, number.toInt())
            }
            return null
        }

        println(getAsteriskPositions().sumOf { // first: x, second: y
            val partNumbers = mutableListOf<Pair<Int, Int>>()

            for (i in -1..1) {
                for (j in -1..1) {
                    val number = getPartNumber(it.first + j, it.second + i)

                    if (number != null && !partNumbers.contains(number)) {
                        partNumbers.add(number)
                    }
                }
            }

            if (partNumbers.size == 2) partNumbers[0].second * partNumbers[1].second else 0
        })
    }

}

fun main() {
    val inputs = File("resources/adventofcode2023/Day03.txt").readLines()
    Day03.part1(inputs)
    Day03.part2(inputs)
}