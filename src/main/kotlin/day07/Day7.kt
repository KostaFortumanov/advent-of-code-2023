package day07

import println
import readFileLines

fun main() {
    val input = readFileLines(7).map { it.split(" ") }

    part1(input).println()
    part2(input).println()
}

fun part1(input: List<List<String>>) = totalWinnings(input) { sortableHand(it) }

fun part2(input: List<List<String>>) = totalWinnings(input) {
    sortableHand(it, '0') { hand ->
        val maxOccurringCard = hand.filter { char -> char != 'J' }
            .groupingBy { char -> char }
            .eachCount()
            .maxByOrNull { (_, value) -> value }?.key ?: 'J'

        hand.replace('J', maxOccurringCard)
    }
}

fun totalWinnings(input: List<List<String>>, sortBy: (String) -> String) =
    input.sortedBy { sortBy(it.first()) }
        .foldIndexed(0) { index, acc, (_, bet) ->
            acc + bet.toInt() * (index + 1)
        }

fun sortableHand(hand: String, jValue: Char = 'B', mapHand: (String) -> String = { hand }): String {
    val letterValues = mapOf('A' to 'E', 'K' to 'D', 'Q' to 'C', 'J' to jValue, 'T' to 'A')
    val newHand = hand.fold("") { acc, card -> "$acc${letterValues.getOrDefault(card, card)}" }
    return "${handStrength(mapHand(hand))}$newHand"
}

fun handStrength(hand: String): Int =
    hand.groupingBy { it }.eachCount().values.let {
        when {
            it.size == 1 -> 7
            it.size == 2 && it.any { cardCount -> cardCount == 4 } -> 6
            it.size == 2 -> 5
            it.size == 3 && it.any { cardCount -> cardCount == 3 } -> 4
            it.size == 3 -> 3
            it.size == 4 -> 2
            else -> 1
        }
    }
