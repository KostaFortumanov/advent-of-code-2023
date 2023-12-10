package day10

import println
import readFileCharMatrix
import kotlin.math.absoluteValue

fun main() {
    val input = readFileCharMatrix(10)

    part1(input).println()
    part2(input).println()
}

private fun part1(input: List<List<Char>>): Int = pipesInLoop(input).size / 2

private fun part2(input: List<List<Char>>): Int {
    val pipesInLoop = pipesInLoop(input)
    val area = polygonArea(pipesInLoop.filter { it.type !in listOf('-', '|') })
    return area - pipesInLoop.size / 2 + 1
}

private fun polygonArea(points: List<Pipe>): Int =
    (points + points.first()).windowed(2)
        .fold(0) { acc, (first, second) ->
            acc + ((first.row + second.row) * (first.col - second.col))
        }.absoluteValue / 2

private fun pipesInLoop(input: List<List<Char>>): List<Pipe> {
    val positions = extractPositions(input)
    val startPipe = positions.firstNotNullOf { row -> row.firstOrNull { it?.type == 'S' } }
    return generateSequence(startPipe) {
        it.apply { visited = true }.next(positions)
    }.toList()
}

private data class Pipe(val type: Char, val row: Int, val col: Int, var visited: Boolean = false) {
    fun next(positions: List<List<Pipe?>>): Pipe? = Direction.entries
        .filter { type in it.canMoveFrom || type == 'S' }
        .firstNotNullOfOrNull { direction ->
            val (dx, dy) = direction.step
            positions.getOrNull(row + dx)?.getOrNull(col + dy)
                ?.takeIf { !it.visited && it.type in direction.canMoveTo }
        }
}

private enum class Direction(val canMoveFrom: List<Char>, val canMoveTo: List<Char>, val step: Pair<Int, Int>) {
    NORTH(listOf('|', 'J', 'L'), listOf('|', 'F', '7'), -1 to 0),
    SOUTH(NORTH.canMoveTo, NORTH.canMoveFrom, 1 to 0),
    WEST(listOf('-', 'J', '7'), listOf('-', 'F', 'L'), 0 to -1),
    EAST(WEST.canMoveTo, WEST.canMoveFrom, 0 to 1),
}

private fun extractPositions(input: List<List<Char>>) =
    input.mapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, value ->
            Pipe(value, rowIndex, colIndex).takeIf { value != '.' }
        }
    }
