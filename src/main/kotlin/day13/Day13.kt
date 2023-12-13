package day13

import println
import readFileLines
import split
import transposeStringList

fun main() {
    val input = readFileLines(13).split { it.isEmpty() }

    part1(input).println()
    part2(input).println()
}

private fun part1(input: List<List<String>>) = sumMirrors(input, 0)

private fun part2(input: List<List<String>>) = sumMirrors(input, 1)

private fun sumMirrors(input: List<List<String>>, mirrorDiff: Int) = input.sumOf {
    findRowMirror(it, mirrorDiff) ?: findColumnMirror(it, mirrorDiff) ?: 0
}

private fun findRowMirror(input: List<String>, mirrorDiff: Int): Int? = findMirror(input, mirrorDiff)?.let { it * 100 }

private fun findColumnMirror(input: List<String>, mirrorDiff: Int): Int? =
    findMirror(input.transposeStringList(), mirrorDiff)

private fun findMirror(input: List<String>, mirrorDiff: Int): Int? = input.drop(1).indices
    .asSequence()
    .mapNotNull { index ->
        (index + 1).takeIf {
            mirrorRange(index, input.lastIndex).sumOf { (first, second) ->
                countDifferences(input[first], input[second])
            } == mirrorDiff
        }
    }.firstOrNull()

private fun mirrorRange(start: Int, end: Int): List<Pair<Int, Int>> =
    (start + 1..end).zip(start downTo 0)

private fun countDifferences(pattern1: String, pattern2: String): Int =
    pattern1.zip(pattern2).count { (first, second) -> first != second }
