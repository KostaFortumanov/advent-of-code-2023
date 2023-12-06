package day06

import integers
import println
import readFileLines
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    val (times, distances) = readFileLines(6).map { it.integers() }

    part1(times, distances).println()
    part2(times, distances).println()
}

private fun part1(times: List<Int>, distances: List<Int>) =
    times.zip(distances).map { (time, distance) ->
        possibleSolutions(time.toDouble(), distance.toDouble())
    }.reduce { acc, it -> acc * it }

private fun part2(times: List<Int>, distances: List<Int>) =
    possibleSolutions(times.joinToString("").toDouble(), distances.joinToString("").toDouble())

private fun possibleSolutions(time: Double, distance: Double): Int {
    val discriminant = time.pow(2) - 4 * distance
    val (minTime, maxTime) =
        listOf((-time + sqrt(discriminant)) / -2, (-time - sqrt(discriminant)) / -2).sorted()
    return (ceil(maxTime) - floor(minTime) - 1).toInt()
}
