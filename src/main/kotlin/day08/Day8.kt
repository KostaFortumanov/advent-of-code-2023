package day08

import println
import readFileLines
import kotlin.math.max

fun main() {
    part1().println()
    part2().println()
}

private fun part1() = Node.steps(start = { it.name == "AAA" }, end = { it.name == "ZZZ" })

private fun part2() = Node.steps(start = { it.name.endsWith('A') }, end = { it.name.endsWith('Z') })

private data class Node(val name: String, val left: String, val right: String) {
    fun next(step: Int): Node =
        when (instructions[step % instructions.length]) {
            'L' -> nodes[left]
            else -> nodes[right]
        }!!

    companion object {
        val instructions = readInstructions()
        val nodes = readNodes()

        fun steps(start: (Node) -> Boolean, end: (Node) -> Boolean): Long =
            nodes.filter { start(it.value) }.map { (_, startNode) ->
                generateSequence(State(startNode, 0L)) { it.next() }
                    .firstNotNullOf { (node, step) -> step.takeIf { end(node) } }
            }.lcm()
    }
}

private data class State(val node: Node, val step: Long) {
    fun next() = State(node.next(step.toInt()), step + 1)
}

private fun List<Long>.lcm() = fold(1L) { acc, it -> findLcm(acc, it) }

private fun findLcm(a: Long, b: Long): Long {
    val max = max(a, b)
    val maxLcm = a * b
    return (max..maxLcm step max).first {
        it % a == 0L && it % b == 0L
    }
}

private fun readInstructions(): String = readFileLines(8).first()

private fun readNodes(): Map<String, Node> =
    readFileLines(8).drop(2).mapNotNull {
        Regex("([\\w\\s]+) = \\(([\\w\\s]+), ([\\w\\s]+)\\)").find(it)?.groupValues
    }.associate { gv ->
        gv[1] to Node(gv[1], gv[2], gv[3])
    }
