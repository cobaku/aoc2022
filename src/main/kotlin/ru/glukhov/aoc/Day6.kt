package ru.glukhov.aoc

import java.io.BufferedReader

fun main() {
    Problem.forDay("day6").use { solveFirst(it) }.let { println("Result of the first problem is $it") }
    Problem.forDay("day6").use { solveSecond(it) }.let { println("Result of the second problem is $it") }
}

private fun solveFirst(reader: BufferedReader): Int {
    val line = reader.readLine()
    return findStartMarker(line)
}

private fun solveSecond(reader: BufferedReader): Int {
    val line = reader.readLine()
    return findMessageMarker(line)
}

private fun findStartMarker(line: String): Int {
    val deduplicator = mutableSetOf<Char>()
    var start = 0
    while (true) {
        deduplicator.clear()
        val sub = line.substring(start, start + 4)
        var allSame = true
        for (character in sub) {
            if (!deduplicator.add(character)) {
                allSame = false
            }
        }
        if (allSame) {
            return start + 4
        } else {
            start++
        }
    }
}

private fun findMessageMarker(line: String): Int {
    val deduplicator = mutableSetOf<Char>()
    var start = findStartMarker(line)
    while (true) {
        deduplicator.clear()
        val sub = line.substring(start, start + 14)
        var allSame = true
        for (character in sub) {
            if (!deduplicator.add(character)) {
                allSame = false
            }
        }
        if (allSame) {
            return start + 14
        } else {
            start++
        }
    }
}