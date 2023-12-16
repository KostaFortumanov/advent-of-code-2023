package day16

import println
import readFileCharMatrix

fun main() {

    part1().println()
    part2().println()
}

private fun part1(): Int =
    traverse(listOf(Point.points[Position(0, 0)]!!.copy(beamDirection = BeamDirection.RIGHT)))

private fun part2(): Int {
    val downStart = List(Point.columns) { Position(0, it) }
        .mapNotNull { Point.points[it]?.copy(beamDirection = BeamDirection.DOWN) }
    val upStart = List(Point.columns) { Position(Point.rows, it) }
        .mapNotNull { Point.points[it]?.copy(beamDirection = BeamDirection.UP) }
    val leftStart = List(Point.rows) { Position(it, 0) }
        .mapNotNull { Point.points[it]?.copy(beamDirection = BeamDirection.RIGHT) }
    val rightStart = List(Point.rows) { Position(it, Point.columns) }
        .mapNotNull { Point.points[it]?.copy(beamDirection = BeamDirection.LEFT) }

    return traverse(downStart + upStart + leftStart + rightStart)
}

private fun traverse(starts: List<Point>): Int =
    starts.maxOf { start ->
        val visited = mutableSetOf(start)
        val queue = mutableListOf(start)
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            current.next()
                .filter { it !in visited }
                .forEach {
                    queue.add(it)
                    visited.add(it)
                }
        }

        visited.distinctBy { it.position }.size
    }

private data class Point(val type: Char, val position: Position, val beamDirection: BeamDirection? = null) {

    fun next(): List<Point> = when {
        type == '/' && beamDirection == BeamDirection.UP ->
            listOfNotNull(points[position.right()]?.copy(beamDirection = BeamDirection.RIGHT))

        type == '/' && beamDirection == BeamDirection.DOWN ->
            listOfNotNull(points[position.left()]?.copy(beamDirection = BeamDirection.LEFT))

        type == '/' && beamDirection == BeamDirection.LEFT ->
            listOfNotNull(points[position.down()]?.copy(beamDirection = BeamDirection.DOWN))

        type == '/' && beamDirection == BeamDirection.RIGHT ->
            listOfNotNull(points[position.up()]?.copy(beamDirection = BeamDirection.UP))

        type == '\\' && beamDirection == BeamDirection.UP ->
            listOfNotNull(points[position.left()]?.copy(beamDirection = BeamDirection.LEFT))

        type == '\\' && beamDirection == BeamDirection.DOWN ->
            listOfNotNull(points[position.right()]?.copy(beamDirection = BeamDirection.RIGHT))

        type == '\\' && beamDirection == BeamDirection.LEFT ->
            listOfNotNull(points[position.up()]?.copy(beamDirection = BeamDirection.UP))

        type == '\\' && beamDirection == BeamDirection.RIGHT ->
            listOfNotNull(points[position.down()]?.copy(beamDirection = BeamDirection.DOWN))

        type == '-' && beamDirection in listOf(BeamDirection.UP, BeamDirection.DOWN) ->
            listOfNotNull(
                points[position.left()]?.copy(beamDirection = BeamDirection.LEFT),
                points[position.right()]?.copy(beamDirection = BeamDirection.RIGHT),
            )

        type == '|' && beamDirection in listOf(BeamDirection.LEFT, BeamDirection.RIGHT) ->
            listOfNotNull(
                points[position.up()]?.copy(beamDirection = BeamDirection.UP),
                points[position.down()]?.copy(beamDirection = BeamDirection.DOWN),
            )

        beamDirection == BeamDirection.UP ->
            listOfNotNull(points[position.up()]?.copy(beamDirection = beamDirection))

        beamDirection == BeamDirection.DOWN ->
            listOfNotNull(points[position.down()]?.copy(beamDirection = beamDirection))

        beamDirection == BeamDirection.LEFT ->
            listOfNotNull(points[position.left()]?.copy(beamDirection = beamDirection))

        beamDirection == BeamDirection.RIGHT ->
            listOfNotNull(points[position.right()]?.copy(beamDirection = beamDirection))

        else -> listOf()
    }

    companion object {
        val points = extractPoints()
        private val input = readFileCharMatrix(16)
        val rows = input.size
        val columns = input.first().size
    }
}

private data class Position(val x: Int, val y: Int) {
    fun up() = Position(x - 1, y)
    fun down() = Position(x + 1, y)
    fun left() = Position(x, y - 1)
    fun right() = Position(x, y + 1)
}

private enum class BeamDirection {
    UP,
    DOWN,
    LEFT,
    RIGHT,
}

private fun extractPoints(): Map<Position, Point> =
    readFileCharMatrix(16).flatMapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, it -> Point(it, Position(rowIndex, colIndex)) }
    }.associateBy { it.position }
