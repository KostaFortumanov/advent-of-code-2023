package day03

import cartesianProduct
import println
import readFileLines

val directions = listOf(-1, 0, 1).cartesianProduct()

fun main() {
    val input = readFileLines(3)

    val schematicNumbers = extractSchematicNumbers(input)
    val schematicStarPositions = extractSchematicStarPositions(input)

    part1(input, schematicNumbers).println()
    part2(schematicNumbers, schematicStarPositions).println()
}

private fun part1(input: List<String>, schematicNumbers: List<SchematicNumber>): Long =
    schematicNumbers.filter {
        it.digitPositions.any { position ->
            position.isAdjacentToSymbol(input)
        }
    }.sumOf { it.number.toLong() }

private fun part2(schematicNumbers: List<SchematicNumber>, schematicStarPositions: List<Pair<Int, Int>>): Long =
    schematicStarPositions.map { symbolPos ->
        schematicNumbers.filter { number -> number.digitPositions.any { it.isAdjacentTo(symbolPos) } }
    }.filter { it.size == 2 }.sumOf { (first, second) -> first.number.toLong() * second.number.toLong() }

private fun Pair<Int, Int>.isAdjacentTo(other: Pair<Int, Int>): Boolean =
    directions.any { (dx, dy) ->
        val x = first + dx
        val y = second + dy
        (x to y) == other
    }

private fun Pair<Int, Int>.isAdjacentToSymbol(input: List<String>): Boolean {
    val isValid = { x: Int, y: Int ->
        x >= 0 && x < input.size && y >= 0 && y < input[x].length
    }
    return directions.any { (dx, dy) ->
        val x = first + dx
        val y = second + dy
        if (isValid(x, y)) {
            !input[x][y].isDigit() && input[x][y] != '.'
        } else {
            false
        }
    }
}

private fun extractSchematicStarPositions(input: List<String>) =
    input.flatMapIndexed { row, line ->
        line.mapIndexedNotNull { col, it ->
            if (it == '*') {
                row to col
            } else {
                null
            }
        }
    }

private fun extractSchematicNumbers(input: List<String>) =
    input.flatMapIndexed { row, line ->
        line.runningFold("") { acc, it ->
            if (it.isDigit()) {
                "$acc$it"
            } else {
                ""
            }
        }.drop(1).mapIndexedNotNull { col, it ->
            val shouldSaveNumber =
                it.isNotEmpty() && (line.getOrNull(col + 1)?.isDigit() == false || line.length == col + 1)
            if (shouldSaveNumber) {
                val colRange = (col - it.length + 1)..col
                val digitPositions = colRange.toList().cartesianProduct(listOf(row)).map { (a, b) -> b to a }
                SchematicNumber(it, digitPositions)
            } else {
                null
            }
        }
    }

private data class SchematicNumber(val number: String, val digitPositions: List<Pair<Int, Int>>)
