import java.io.File

fun readFileLines(day: Int): List<String> = File({}.javaClass.getResource("input_$day.txt")!!.path).readLines()

fun String?.toIntOrZero() = this?.toIntOrNull() ?: 0

fun <T> List<T>.cartesianProduct(other: List<T> = this) = this.flatMap { s -> List(this.size) { s }.zip(other) }

fun Any?.println() = println(this)
