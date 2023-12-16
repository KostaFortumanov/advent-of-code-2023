package day14

import println
import readFileCharMatrix
import split
import transpose


fun main() {
    val platform = Platform(readFileCharMatrix(14))

    part1(platform).println()
    part2(platform).println()
}

private fun part1(platform: Platform) = platform.tiltUp().load()

private fun part2(platform: Platform) = platform.afterCycles(1_000_000_000).load()

private data class Platform(val platform: List<List<Char>>, val cycleNum: Int = 0) {
    fun afterCycles(numCycles: Int): Platform {
        val platformMap: MutableMap<List<List<Char>>, Platform> = mutableMapOf()
        generateSequence(this) { it.cycle() }
            .takeWhile { it.platform !in platformMap && it.cycleNum < numCycles }
            .onEach { platformMap[it.platform] = it }.toList()

        val last = platformMap.values.last().cycle()
        return platformMap[last.platform]?.let { repeatedPlatform ->
            val finalCycle =
                (numCycles - repeatedPlatform.cycleNum) % (platformMap.size - repeatedPlatform.cycleNum) + repeatedPlatform.cycleNum
            platformMap.values.first { it.cycleNum == finalCycle }
        } ?: last
    }

    private fun cycle() = tiltUp().tiltLeft().tiltDown().tiltRight().copy(cycleNum = cycleNum + 1)

    fun load() = platform
        .mapIndexed { index, row -> row.count { it == Rock.ROUNDED.value } * (platform.size - index) }
        .sum()

    fun tiltUp() = tiltVertical()

    private fun tiltDown() = tiltVertical { reversed() }

    private fun tiltLeft() = tiltHorizontal()

    private fun tiltRight() = tiltHorizontal { reversed() }

    private fun tiltHorizontal(row: List<Char>.() -> List<Char> = { this }) =
        Platform(platform.map { tiltRowLeft(it.row()).row() })

    private fun tiltVertical(row: List<Char>.() -> List<Char> = { this }) =
        Platform(platform.transpose().map { tiltRowLeft(it.row()).row() }.transpose())

    private fun tiltRowLeft(row: List<Char>) =
        row.split { it == Rock.CUBE_SHAPED.value }
            .flatMap {
                val (rounded, empty) = it.partition { char -> char == Rock.ROUNDED.value }
                rounded + empty + Rock.CUBE_SHAPED.value
            }.dropLast(1)
}

private enum class Rock(val value: Char) {
    ROUNDED('O'),
    CUBE_SHAPED('#'),
}
