package ru.glukhov.aoc

import java.io.BufferedReader
import java.util.*

fun main() {
    Problem.forDay("day3").use { solveFirst(it) }.let { println("Result of the first problem is $it") }
    Problem.forDay("day3").use { solveSecond(it) }.let { println("Result of the second problem is $it") }
}

private fun solveFirst(input: BufferedReader): Int {
    var result = 0
    val checker = BitSet(127)
    input.lines().forEach {
        checker.clear()
        val (first, second) = it.splitByHalf()
        first.chars().forEach { char -> checker.set(char) }
        for (char in second.chars()) {
            if (checker[char]) {
                result += char.weight()
                break
            }
        }
    }
    return result
}

private fun solveSecond(input: BufferedReader): Int {
    val checker = MutableList(127) { 0 }
    var groupSize = 0
    var result = 0
    for (line in input.lines()) {
        line.toSet().forEach { checker[it.code]++ }
        groupSize++
        if (groupSize == 3) {
            result += checker.indexOf(3).weight()
            checker.fill(0)
            groupSize = 0
        }
    }
    return result
}

private fun String.splitByHalf(): Pair<String, String> = Pair(substring(0, length / 2), substring(length / 2, length))

private fun Int.weight(): Int {
    if (this in 65..90) {
        return this - 38
    }
    if (this in 97..122) {
        return this - 96
    }
    throw IllegalArgumentException("Get back! We a fooled!")
}
