package day11

import println
import readFileCharMatrix
import transpose
import kotlin.math.abs

fun main() {
    val input = readFileCharMatrix(11)

    part1(input).println()
    part2(input).println()
}

private fun part1(input: List<List<Char>>) = sumDistances(input, 2L)

private fun part2(input: List<List<Char>>) = sumDistances(input, 1_000_000L)

private fun sumDistances(input: List<List<Char>>, emptyCost: Long): Long {
    val emptyRows = emptyRows(input)
    val emptyColumns = emptyRows(input.transpose())
    return pointCombinations(extractPoints(input)).sumOf { (first, second) ->
        first.distanceTo(second, emptyRows, emptyColumns, emptyCost)
    }
}

private fun pointCombinations(points: List<Point>): List<Pair<Point, Point>> =
    points.flatMapIndexed { index: Int, point: Point ->
        points.drop(index + 1).map { other -> point to other }
    }

private fun extractPoints(input: List<List<Char>>): List<Point> =
    input.flatMapIndexed { rowIndex, row ->
        row.mapIndexedNotNull { colIndex, value ->
            Point(rowIndex, colIndex).takeIf { value != '.' }
        }
    }

private fun emptyRows(input: List<List<Char>>): List<Int> =
    input.mapIndexedNotNull { index, row -> index.takeIf { row.all { it == '.' } } }

private data class Point(val x: Int, val y: Int) {
    fun distanceTo(other: Point, emptyRows: List<Int>, emptyColumns: List<Int>, emptyCost: Long): Long =
        abs(x - other.x) + abs((y - other.y)) +
                emptyDistance(x, other.x, emptyRows, emptyCost) + emptyDistance(y, other.y, emptyColumns, emptyCost)

    private fun emptyDistance(first: Int, second: Int, emptyIndices: List<Int>, emptyCost: Long): Long {
        val (rangeStart, rangeEnd) = listOf(first, second).sorted()
        return emptyIndices.count { it in (rangeStart..<rangeEnd) } * (emptyCost - 1)
    }
}
