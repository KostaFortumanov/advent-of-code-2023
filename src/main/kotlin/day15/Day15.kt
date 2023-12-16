package day15

import println
import readFileLines

fun main() {
    val input = readFileLines(15).first().split(",")

    part1(input).println()
    part2(input).println()
}

private fun part1(input: List<String>) = input.sumOf { hash(it) }

private fun part2(input: List<String>) = input.toInstructions()
    .fold(mapOf<Int, List<AddInstruction>>()) { boxes, currentInstruction ->
        val currentBox = currentInstruction.box()
        when (currentInstruction) {
            is AddInstruction ->
                boxes + (currentBox to boxes[currentBox].orEmpty().addLens(currentInstruction))

            is RemoveInstruction ->
                boxes + (currentBox to boxes[currentBox].orEmpty().filter { it.label != currentInstruction.label })
        }
    }.values.sumOf {
        it.mapIndexed { index, lens -> (lens.box() + 1) * (index + 1) * lens.focalLength }.sum()
    }

private fun hash(line: String) = line.fold(0) { acc, it -> (acc + it.code) * 17 % 256 }

private fun List<AddInstruction>.addLens(newElement: AddInstruction) =
    if (any { it.label == newElement.label }) {
        replaceLens(newElement)
    } else {
        this + newElement
    }

private fun List<AddInstruction>.replaceLens(newElement: AddInstruction) = map {
    if (it.label == newElement.label) {
        newElement
    } else {
        it
    }
}

private sealed interface Instruction {
    val label: String
    fun box() = hash(label)
}

private data class AddInstruction(override val label: String, val focalLength: Int) : Instruction

private data class RemoveInstruction(override val label: String) : Instruction

private fun List<String>.toInstructions() = map {
    when {
        it.contains("=") -> {
            val (label, focalLength) = it.split("=")
            AddInstruction(label, focalLength.toInt())
        }

        else -> {
            val (label, _) = it.split("-")
            RemoveInstruction(label)
        }
    }
}
