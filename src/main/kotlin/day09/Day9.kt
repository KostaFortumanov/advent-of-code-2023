package day09

import integers
import println
import readFileLines

fun main() {
    val input = readFileLines(9).map { it.integers() }

    part1(input).println()
    part2(input).println()
}

private fun part1(input: List<List<Int>>) = sumSequences(input) { sumOf { it.last() } }

private fun part2(input: List<List<Int>>) =
    sumSequences(input) { fold(0) { acc, sequence -> sequence.first() - acc } }

private fun sumSequences(
    input: List<List<Int>>,
    mapSequence: List<List<Int>>.() -> Int,
): Int = input.sumOf { sequences(it).mapSequence() }

private tailrec fun sequences(numbers: List<Int>, acc: List<List<Int>> = listOf(numbers)): List<List<Int>> {
    val nextSequence = numbers.windowed(2).map { (a, b) -> b - a }
    return if (nextSequence.all { it == 0 }) {
        acc.reversed()
    } else {
        sequences(nextSequence, acc + listOf(nextSequence))
    }
}
