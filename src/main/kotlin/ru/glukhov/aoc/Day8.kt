package ru.glukhov.aoc

import java.io.Reader

fun main() {
    val field = Problem.forDay("day8").use { parseTree(it) }
    println("Result of the first problem is ${solvePartOne(field)}")
    println("Result of the second problem is ${solvePartTwo(field)}")
}

private fun parseTree(reader: Reader): Array<IntArray> {
    val buffer = mutableListOf<IntArray>()
    reader.forEachLine {
        val array = IntArray(it.length)
        it.forEachIndexed { index, num ->
            array[index] = num.digitToInt()
        }
        buffer.add(array)
    }
    return buffer.toTypedArray()
}

private fun walk(array: Array<IntArray>, action: (x: Int, y: Int) -> Int): Int {
    var target = 0
    for (vertical in array.indices) {
        for (horizontal in array.indices) {
            target += action(horizontal, vertical)
        }
    }
    return target
}

private fun solvePartOne(field: Array<IntArray>): Int = walk(field) { x, y ->
    return@walk if (isBorder(x, y, field) || isVisibleLtr(x, y, field) || isVisibleRtl(x, y, field) || isVisibleTtb(
            x, y, field
        ) || isVisibleBtt(x, y, field)
    ) 1 else 0
}

private fun solvePartTwo(field: Array<IntArray>): Int {
    val target = mutableListOf<Int>()
    for (vertical in field.indices) {
        for (horizontal in field.indices) {
            if (isBorder(horizontal, vertical, field)) {
                continue
            }
            val step = powerLtr(horizontal, vertical, field) * powerRtl(horizontal, vertical, field) * powerTtb(
                horizontal, vertical, field
            ) * powerBtt(horizontal, vertical, field)
            target.add(step)
        }
    }
    return target.sortedDescending()[0]
}

private fun isBorder(x: Int, y: Int, field: Array<IntArray>): Boolean {
    if (y == 0 || y == field.size - 1) {
        return true
    }
    if (x == 0 || x == field[y].size - 1) {
        return true
    }
    return false
}

private fun isVisibleLtr(x: Int, y: Int, field: Array<IntArray>): Boolean {
    val row = field[y]
    val value = row[x]
    for (i in x + 1 until row.size) {
        if (value <= row[i]) {
            return false
        }
    }
    return true
}

private fun isVisibleRtl(x: Int, y: Int, field: Array<IntArray>): Boolean {
    val row = field[y]
    val value = row[x]
    for (i in 0 until x) {
        if (value <= row[i]) {
            return false
        }
    }
    return true
}

private fun isVisibleTtb(x: Int, y: Int, field: Array<IntArray>): Boolean {
    val value = field[y][x]
    for (i in y + 1 until field.size) {
        if (value <= field[i][x]) {
            return false
        }
    }
    return true
}

private fun isVisibleBtt(x: Int, y: Int, field: Array<IntArray>): Boolean {
    val value = field[y][x]
    for (i in 0 until y) {
        if (value <= field[i][x]) {
            return false
        }
    }
    return true
}

private fun powerLtr(x: Int, y: Int, field: Array<IntArray>): Int {
    var power = 0
    val row = field[y]
    val value = row[x]
    for (i in x + 1 until row.size) {
        if (value > row[i]) {
            power++
        } else {
            return power + 1
        }
    }
    return power
}

private fun powerRtl(x: Int, y: Int, field: Array<IntArray>): Int {
    var power = 0
    val row = field[y]
    val value = row[x]
    for (i in x - 1 downTo 0) {
        if (value > row[i]) {
            power++
        } else {
            return power + 1
        }
    }
    return power
}

private fun powerTtb(x: Int, y: Int, field: Array<IntArray>): Int {
    var power = 0
    val value = field[y][x]
    for (i in y + 1 until field.size) {
        if (value > field[i][x]) {
            power++
        } else {
            return power + 1
        }
    }
    return power
}

private fun powerBtt(x: Int, y: Int, field: Array<IntArray>): Int {
    var power = 0
    val value = field[y][x]
    for (i in y - 1 downTo 0) {
        if (value > field[i][x]) {
            power++
        } else {
            return power + 1
        }
    }
    return power
}