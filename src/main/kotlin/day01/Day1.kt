package day01

import digits
import println
import readFileLines

fun main() {
    val input = readFileLines(1)

    part1(input).println()
    part2(input).println()
}

private fun part1(input: List<String>) =
    input.map { it.digits() }
        .sumOf { it.first() * 10 + it.last() }

private fun part2(input: List<String>): Int {
    val digitMap = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
    )
    return input.map { line ->
        digitMap.entries.flatMap { (k, v) ->
            listOf(
                line.windowed(k.length).indexOfFirst { it == k } to digitMap[k],
                line.windowed(k.length).indexOfLast { it == k } to digitMap[k],
                line.indexOfFirst { it == v.digitToChar() } to v,
                line.indexOfLast { it == v.digitToChar() } to v,
            )
        }.filter { (index, _) -> index != -1 }
            .sortedBy { (index, _) -> index }
            .mapNotNull { (_, value) -> value }
    }.sumOf { it.first() * 10 + it.last() }
}
