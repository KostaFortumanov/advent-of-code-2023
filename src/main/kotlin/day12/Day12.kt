package day12

import integers
import println
import readFileLines

fun main() {
    val input = readFileLines(12)

    part1(input).println()
    part2(input).println()
}

private fun part1(input: List<String>) = sumPossibleArrangements(input, 1)

private fun part2(input: List<String>) = sumPossibleArrangements(input, 5)

private fun sumPossibleArrangements(input: List<String>, patternRepeat: Int) =
    input.map { it.split(" ") }
        .sumOf { (pattern, numbers) ->
            possibleArrangements(
                List(patternRepeat) { pattern }.joinToString("?"),
                List(patternRepeat) { numbers }.joinToString(",").integers()
            )
        }

private fun possibleArrangements(pattern: String, numbers: List<Int>): Long {
    val cache = mutableMapOf<Pair<String, List<Int>>, Long>()

    fun possibleArrangementsInner(pattern: String, numbers: List<Int>): Long =
        if (notValid(pattern, numbers)) {
            0L
        } else if (pattern.isEmpty()) {
            1L
        } else {
            val damagedCase = if (pattern.first() in listOf('?', '#')) {
                cache.getOrPut(pattern to numbers) {
                    numbers.firstOrNull()?.let { number ->
                        if (canPlaceNumber(pattern, number)) {
                            possibleArrangementsInner(pattern.drop(number + 1), numbers.drop(1))
                        } else {
                            null
                        }
                    } ?: 0L
                }
            } else {
                0L
            }

            val operationalCase = if (pattern.first() in listOf('?', '.')) {
                possibleArrangementsInner(pattern.drop(1), numbers)
            } else {
                0L
            }

            damagedCase + operationalCase
        }

    return possibleArrangementsInner(pattern, numbers)
}

private fun notValid(pattern: String, numbers: List<Int>): Boolean =
    pattern.length < numbers.sum() + numbers.size - 1

private fun canPlaceNumber(pattern: String, number: Int): Boolean =
    pattern.length >= number && pattern.take(number).none { it == '.' } && pattern.getOrNull(number) != '#'
