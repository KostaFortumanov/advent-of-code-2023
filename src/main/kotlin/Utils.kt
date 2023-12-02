import java.io.File

fun readFileLines(day: Int): List<String> = File({}.javaClass.getResource("input_$day.txt")!!.path).readLines()

fun String.digits() = mapNotNull { it.digitToIntOrNull() }

fun String?.toIntOrZero() = this?.toIntOrNull() ?: 0

fun Any?.println() = println(this)
