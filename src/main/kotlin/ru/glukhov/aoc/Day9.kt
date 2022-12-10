package ru.glukhov.aoc

import java.io.BufferedReader
import kotlin.math.abs

private class Field {

    val head: Position = Position(0, 0)
    val tail: Position = Position(0, 0)

    private var oldDirection: Boolean? = true

    fun apply(input: String) {
        val (changeX, value) = input.parse()

        val increment = if (value < 0) -1 else 1
        for (counter in 0 until abs(value)) {
            val headPrev = Pair(head.x, head.y)
            if (changeX) {
                head.moveX(increment)
            } else {
                head.moveY(increment)
            }

            if (changeX != oldDirection && counter == 0) {
                tail.set(headPrev)
            }

            if (counter != 0 && counter != abs(value) - 1) {
                if (changeX) {
                    tail.moveX(increment)
                } else {
                    tail.moveY(increment)
                }
            }
        }
        oldDirection = changeX
    }

    private fun String.parse(): Pair<Boolean, Int> {
        this.split(" ").let {
            val direction = it[0].trim()
            var vertical = false
            var multiplier = 1
            when (direction) {
                "U" -> {
                    vertical = true
                }

                "D" -> {
                    vertical = true
                    multiplier = -1
                }

                "L" -> {
                    multiplier = -1
                }
            }
            return Pair(vertical, Integer.parseInt(it[1]) * multiplier)
        }
    }
}

private data class Position(var x: Int, var y: Int) {

    val history: MutableSet<Pair<Int, Int>> = mutableSetOf()

    init {
        history.add(Pair(x, y))
    }

    fun set(position: Pair<Int, Int>) {
        this.x = position.first
        this.y = position.second
        history.add(position)
    }

    fun moveX(x: Int) {
        this.x += x
        history.add(Pair(this.x, y))
    }

    fun moveY(y: Int) {
        this.y += y
        history.add(Pair(x, this.y))
    }
}

fun main() {
    Problem.forDay("debug").use { solvePartOne(it) }.let {
        println("Debug of the first problem is $it")
    }
    //Problem.forDay("day9").use { solvePartOne(it) }.let {
    //    println("Result of the first problem is $it")
    //}
}

private fun solvePartOne(reader: BufferedReader): Int {
    val field = Field()
    reader.forEachLine {
        field.apply(it)
    }
    return field.tail.history.size
}