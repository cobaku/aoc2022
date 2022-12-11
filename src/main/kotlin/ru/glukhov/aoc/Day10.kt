package ru.glukhov.aoc

import java.io.BufferedReader

fun main() {
    val microcode = Problem.forDay("day10").use { parseMicrocode(it) }
    println("Result of the first problem: ${first(microcode)}")
    println("Result of the first problem:\n ${second(microcode)}")
}

private fun first(microcode: List<Pair<Operation, Int>>): Int {
    val powers = mutableListOf<Int>()
    execute(microcode) { register, cycle ->
        if (cycle % 20 == 0) {
            powers.add(cycle * register)
        }
    }
    return powers[0] + powers[2] + powers[4] + powers[6] + powers[8] + powers[10]
}

private fun second(microcode: List<Pair<Operation, Int>>): String {
    val builder = StringBuilder()
    execute(microcode) { register, cycle ->
        if (cycle % 40 == 0) {
            builder.append("\n")
        }
        if (isPixelInSprite(cycle - 1, register)) {
            builder.append("#")
        } else {
            builder.append(".")
        }
    }
    return builder.toString()
}

private fun execute(microcode: List<Pair<Operation, Int>>, state: (register: Int, cycle: Int) -> Unit) {
    var cycle = 1
    var operationCycle = 1
    var register = 1
    var index = 0
    var operation = microcode[index]
    while (microcode.size - 1 > index) {
        state(register, cycle)
        if (operation.first == Operation.NOOP) {
            operation = microcode[++index]
            operationCycle = 1
        } else {
            if (operationCycle == 2) {
                register += operation.second
                operation = microcode[++index]
                operationCycle = 1
            } else {
                operationCycle++
            }
        }
        cycle++
    }
}

private fun isPixelInSprite(pixel: Int, spriteMiddle: Int): Boolean {
    val normalizedPixel = pixel % 40
    return normalizedPixel >= spriteMiddle - 1 && normalizedPixel <= spriteMiddle + 1
}

private fun parseMicrocode(reader: BufferedReader): List<Pair<Operation, Int>> = reader.lines()
        .map {
            if (it == "noop") {
                return@map Pair(Operation.NOOP, 0)
            } else {
                return@map Pair(Operation.ADDX, it.split(" ").let { s -> Integer.valueOf(s[1]) })
            }
        }
        .toList()

private enum class Operation {
    NOOP,
    ADDX
}