import java.io.File

fun readFileLines(day: Int): List<String> = File({}.javaClass.getResource("input_$day.txt")!!.path).readLines()

private fun readFileMatrix(day: Int): List<List<String>> =
    readFileLines(day).map { line -> line.split("").filter { it.isNotEmpty() } }

fun readFileCharMatrix(day: Int): List<List<Char>> =
    readFileMatrix(day).map { line -> line.map { it.first() } }

fun String.integers() = Regex("-?\\d+").findAll(this).map { it.value.toInt() }.toList()

fun String.longs() = Regex("-?\\d+").findAll(this).map { it.value.toLong() }.toList()

fun String?.toIntOrZero() = this?.toIntOrNull() ?: 0

fun <T> List<T>.cartesianProduct(other: List<T> = this) = flatMap { s -> List(size) { s }.zip(other) }

fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> =
    fold(listOf(emptyList())) { acc, element ->
        if (predicate(element)) {
            acc + listOf(emptyList())
        } else {
            acc.dropLast(1) + listOf(acc.last() + element)
        }
    }

fun Any?.println() = println(this)
