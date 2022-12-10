package ru.glukhov.aoc

import java.io.BufferedReader

fun main() {
    Problem.forDay("day4").use { solveFirst(it) }.let { println("Result of the first problem is $it") }
    Problem.forDay("day4").use { solveSecond(it) }.let { println("Result of the first problem is $it") }
}

private fun solveFirst(reader: BufferedReader): Int = reader.lines()
    .filter() { it.hasFullIntersection() }
    .count()
    .toInt()

private fun solveSecond(reader: BufferedReader): Int = reader.lines()
    .filter() { it.hasOverlap() }
    .count()
    .toInt()

private fun String.pairs(): Pair<Pair<Int, Int>, Pair<Int, Int>> = this.split(",")
    .let { Pair(it[0].asRange(), it[1].asRange()) }

private fun String.asRange(): Pair<Int, Int> = this.split("-")
    .let { Pair(Integer.valueOf(it[0]), Integer.valueOf(it[1])) }

private fun String.hasFullIntersection(): Boolean = pairs().let {
    (it.first.first <= it.second.first && it.first.second >= it.second.second)
        .or(it.second.first <= it.first.first && it.second.second >= it.first.second)
}

private fun String.hasOverlap(): Boolean = if (hasFullIntersection()) true else
    pairs().let { (it.first.second >= it.second.first).and(it.first.first <= it.second.second) }