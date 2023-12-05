package day04

import integers
import println
import readFileLines
import kotlin.math.pow

fun main() {
    val scratchCards = readFileLines(4).toScratchCards()

    part1(scratchCards).println()
    part2(scratchCards).println()
}

private fun part1(scratchCards: List<ScratchCard>): Int = scratchCards.sumOf { it.points }

private fun part2(scratchCards: List<ScratchCard>): Int =
    MutableList(scratchCards.size) { 1 }.also { copies ->
        scratchCards.forEachIndexed { index, scratchCard ->
            (1..scratchCard.winningSize).forEach { copies[index + it] += copies[index] }
        }
    }.sum()

private fun List<String>.toScratchCards() =
    map { line ->
        val (winning, numbers) = line.split("|")
        ScratchCard(winning.integers().drop(1), numbers.integers())
    }

private data class ScratchCard(val winningNumbers: List<Int>, val numbers: List<Int>) {
    val winningSize = numbers.count { it in winningNumbers }
    val points = 2.0.pow((winningSize - 1).toDouble()).toInt()
}
