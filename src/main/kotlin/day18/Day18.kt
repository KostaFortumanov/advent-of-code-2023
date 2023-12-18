package day18

import println
import readFileLines
import kotlin.math.abs
import kotlin.math.absoluteValue

fun main() {
    val input = readFileLines(18)

    part1(input).println()
    part2(input).println()
}

private fun part1(input: List<String>): Long {
    val points = pointsFromDigInstructions(extractDigInstructions(input))
    return polygonArea(points) + polygonPerimeter(points) / 2 + 1
}

private fun part2(input: List<String>): Long {
    val points = pointsFromDigInstructions(extractHexDigInstructions(input))
    return polygonArea(points) + polygonPerimeter(points) / 2 + 1
}

private fun polygonArea(points: List<Point>): Long =
    points.windowed(2)
        .fold(0L) { acc, (first, second) ->
            acc + ((first.x + second.x) * (first.y - second.y))
        }.absoluteValue / 2

private fun polygonPerimeter(points: List<Point>): Long = points.windowed(2)
    .fold(0L) { acc, (first, second) ->
        acc + abs(first.x - second.x) + abs(first.y - second.y)
    }.absoluteValue

private fun pointsFromDigInstructions(digInstructions: List<DigInstruction>) =
    digInstructions.fold(listOf(Point(0, 0))) { acc, instruction ->
        val prevPoint = acc.last()
        acc + when (instruction.direction) {
            'U' -> Point(prevPoint.x - instruction.amount, prevPoint.y)
            'D' -> Point(prevPoint.x + instruction.amount, prevPoint.y)
            'L' -> Point(prevPoint.x, prevPoint.y - instruction.amount)
            else -> Point(prevPoint.x, prevPoint.y + instruction.amount)
        }
    }

private fun extractDigInstructions(input: List<String>) =
    input.map {
        val (direction, amount, _) = it.split(" ")
        DigInstruction(direction.first(), amount.toLong())
    }

private fun extractHexDigInstructions(input: List<String>) =
    input.map {
        val (_, _, hex) = it.split(" ")
        val instruction = when (hex[hex.lastIndex - 1]) {
            '0' -> 'R'
            '1' -> 'D'
            '2' -> 'L'
            else -> 'U'
        }
        DigInstruction(instruction, hex.removePrefix("(#").dropLast(2).toLong(16))
    }

private data class DigInstruction(val direction: Char, val amount: Long)

private data class Point(val x: Long, val y: Long)
