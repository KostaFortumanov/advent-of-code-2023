package day05

import longs
import println
import readFileLines
import split

fun main() {
    val input = readFileLines(5).split { it.isEmpty() }
    val seeds = input[0].first().longs()
    val seedRanges = seeds.chunked(2).map { (start, length) -> start..<start + length }
    val elfMaps = input.drop(1).map { it.toElfMap() }.reversed()

    part1(seeds, elfMaps).println()
    part2(seedRanges, elfMaps).println()
}

private fun part1(seeds: List<Long>, elfMaps: List<ElfMap>) =
    elfMaps.findLocation { it in seeds }

private fun part2(seedRanges: List<LongRange>, elfMaps: List<ElfMap>) =
    elfMaps.findLocation { seedRanges.any { range -> it in range } }

private fun List<ElfMap>.findLocation(seedPredicate: (Long) -> Boolean): Long =
    generateSequence(0L) { it + 1 }.first { location ->
        val seed = fold(location) { acc, elfMap -> elfMap[acc] }
        seedPredicate(seed)
    }

private fun List<String>.toElfMap(): ElfMap =
    drop(1)
        .joinToString()
        .longs()
        .chunked(3)
        .fold(ElfMap()) { acc, (destinationStart, sourceStart, length) ->
            ElfMap(
                sourceRanges = acc.sourceRanges + listOf(sourceStart..<sourceStart + length),
                destinationRanges = acc.destinationRanges + listOf(destinationStart..<destinationStart + length)
            )
        }

private data class ElfMap(
    val sourceRanges: List<LongRange> = listOf(),
    val destinationRanges: List<LongRange> = listOf(),
) {
    operator fun get(destinationValue: Long): Long = destinationRanges
        .indexOfFirst { range -> destinationValue in range }
        .takeIf { it != -1 }
        ?.let { index ->
            val sourceValue = destinationValue + sourceRanges[index].first - destinationRanges[index].first
            sourceValue.takeIf { it in sourceRanges[index] }
        } ?: destinationValue
}
