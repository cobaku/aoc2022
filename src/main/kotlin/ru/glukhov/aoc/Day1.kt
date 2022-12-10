package ru.glukhov.aoc

import java.io.BufferedReader

fun main() {
    Problem.forDay("day1").use { solveFirst(it) }.let { println("Result of the first problem is $it") }
    Problem.forDay("day1").use { solveSecond(it) }.let { println("Result of the second problem is $it") }
}

private fun solveFirst(input: BufferedReader): Int = input.use {
    var max = 0
    var currentElf = 0
    while (it.ready()) {
        val line = it.readLine()
        if (line.isEmpty()) {
            if (currentElf > max) {
                max = currentElf
            }
            currentElf = 0
            continue
        }
        currentElf += Integer.parseInt(line)
    }
    return max
}

private fun solveSecond(input: BufferedReader): Int = input.use {
    var currentElf = 0
    val elves = mutableListOf<Int>()
    while (it.ready()) {
        val line = it.readLine()
        if (line.isEmpty()) {
            elves.add(currentElf)
            currentElf = 0
            continue
        }
        currentElf += Integer.parseInt(line)
    }
    return elves.sortedDescending().run { get(0) + get(1) + get(2) }
}
