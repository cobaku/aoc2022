package ru.glukhov.aoc

import java.io.BufferedReader

enum class Element(val value: String, val weight: Int) {

    ROCK("A", 1), PAPER("B", 2), SCISSORS("C", 3);

    companion object {

        fun rockPaperScissors(row: String): Int = row.split(" ")
            .let { Pair(Element.parse(it[0]), Element.parse(it[1])) }
            .let { Element.play(it.second, it.first).weight + it.second.weight }

        fun normalize(row: String): String = row.replace("X", "A").replace("Y", "B").replace("Z", "C")

        fun parse(char: String): Element = Element.values().find { element -> element.value == char }
            ?: throw IllegalArgumentException("Element for $char not found")

        private fun play(a: Element, b: Element): Result = if (a == b) Result.DRAW
        else when (a) {
            ROCK -> if (b == PAPER) Result.LOSE else Result.WIN
            PAPER -> if (b == SCISSORS) Result.LOSE else Result.WIN
            SCISSORS -> if (b == ROCK) Result.LOSE else Result.WIN
        }
    }
}

enum class Result(val code: String, val weight: Int) {

    LOSE("X", 0), DRAW("Y", 3), WIN("Z", 6);

    companion object {

        private fun parse(char: String): Result = Result.values().find { result -> result.code == char }
            ?: throw IllegalArgumentException("Element for $char not found")

        private fun guessMyElement(element: Element, result: Result): Element = if (result == DRAW) element
        else when (element) {
            Element.ROCK -> if (result == LOSE) Element.SCISSORS else Element.PAPER
            Element.PAPER -> if (result == LOSE) Element.ROCK else Element.SCISSORS
            Element.SCISSORS -> if (result == LOSE) Element.PAPER else Element.ROCK
        }

        fun rockPaperScissors(row: String): Int = row.split(" ").let { Pair(Element.parse(it[0]), Result.parse(it[1])) }
            .let { Result.guessMyElement(it.first, it.second).weight + it.second.weight }
    }
}

fun main() {
    Problem.forDay("day2").use { solveFirst(it) }.let { println("Result of the first problem is $it") }
    Problem.forDay("day2").use { solveSecond(it) }.let { println("Result of the second problem is $it") }
}

private fun solveFirst(input: BufferedReader): Int =
    input.lines().mapToInt { Element.rockPaperScissors(Element.normalize(it)) }.sum()

private fun solveSecond(input: BufferedReader): Int = input.lines().mapToInt { Result.rockPaperScissors(it) }.sum()