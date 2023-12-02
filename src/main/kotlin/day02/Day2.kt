package day02

import println
import readFileLines

const val RED_CUBES = 12
const val GREEN_CUBES = 13
const val BLUE_CUBES = 14

fun main() {
    val input = readFileLines(2)

    part1(input.map { it.toGame() }).println()
    part2(input.map { it.toGame() }).println()
}

private fun part1(games: List<Game>): Int =
    games.filterNot { it.maxRed > RED_CUBES || it.maxGreen > GREEN_CUBES || it.maxBlue > BLUE_CUBES }
        .sumOf { it.id }

private fun part2(games: List<Game>): Int = games.sumOf { it.maxRed * it.maxGreen * it.maxBlue }

private data class Game(val id: Int, val maxRed: Int, val maxGreen: Int, val maxBlue: Int)

private fun String.toGame(): Game {
    val gameId = Regex("Game (\\d+)").findMaxCaptureGroup(this)
    val maxRed = Regex("(\\d+) red").findMaxCaptureGroup(this)
    val maxGreen = Regex("(\\d+) green").findMaxCaptureGroup(this)
    val maxBlue = Regex("(\\d+) blue").findMaxCaptureGroup(this)

    return Game(gameId, maxRed, maxGreen, maxBlue)
}

private fun Regex.findMaxCaptureGroup(input: String): Int =
    findAll(input).maxOfOrNull { it.groupValues.last().toInt() } ?: 0
