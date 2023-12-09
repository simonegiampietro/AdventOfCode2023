import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

fun part1(file: String) {
    val reader = BufferedReader(InputStreamReader(FileInputStream(file)))
    val lines = reader.readLines()
        .filter { it.isNotBlank() }
        .map {
            it.split(" ")
                .filter { s -> s.isNotBlank() }
                .drop(1)
                .map { s -> s.toLong() }
        }

    var multiply = 1L
    for ((i, max) in lines[1].withIndex()) {
        val millis = countMillis(millis = lines[0][i], distance = max)
        println("[$i] ${millis}")
        multiply *= millis
    }

    println("Result: $multiply")
}

fun part2(file: String) {
    val reader = BufferedReader(InputStreamReader(FileInputStream(file)))
    val lines = reader.readLines()
        .filter { it.isNotBlank() }
        .map {
            it.split(" ")
                .filter { s -> s.isNotBlank() }
                .drop(1)
                .fold("") { acc, curr -> acc + curr }
                .toLong()
        }


    val result = countMillis(millis = lines[0], distance = lines[1])
    println("[0] $result")

    println("Result: $result")
}

fun countMillis(millis: Long, distance: Long): Long {
    var count = 0L
    for (j in 1L..(millis / 2L)) {
        if ((millis - j) * j > distance) {
            count++
        }
    }
    return count * 2 + (millis % 2 - 1)
}

fun main() {
//    val resource = object {}.javaClass.getResource("test-input.txt")
    val resource = object {}.javaClass.getResource("input.txt")
    part1(resource.path)
    part2(resource.path)
}
