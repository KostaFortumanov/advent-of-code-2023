package day01

import println
import readFileLines
import toIntOrZero

val wordMap =
    mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9",
    )

fun main() {
    val input = readFileLines(1)

    part1(input).println()
    part2(input).println()
}

private fun part1(input: List<String>) =
    input.map { it.findFirstAndLastFromList(wordMap.values) }
        .sumOf { (first, last) -> first.toIntOrZero() * 10 + last.toIntOrZero() }

private fun part2(input: List<String>) =
    input.map { it.findFirstAndLastFromList(wordMap.keys + wordMap.values) }
        .sumOf { (first, last) ->
            (first?.toIntOrNull() ?: wordMap[first].toIntOrZero()) * 10 +
                    (last?.toIntOrNull() ?: wordMap[last].toIntOrZero())
        }

private fun String.findFirstAndLastFromList(list: Collection<String>): Pair<String?, String?> =
    this.findAnyOf(list)?.second to this.findLastAnyOf(list)?.second
