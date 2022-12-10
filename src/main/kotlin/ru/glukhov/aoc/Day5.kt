package ru.glukhov.aoc

import java.io.BufferedReader
import java.util.stream.Collectors

private data class Command(val count: Int, val source: Int, val destination: Int)

private class Cargo(private val space: List<ArrayDeque<String>>) {

    fun execute(cmd: Command) {
        val source = space[cmd.source]
        val target = space[cmd.destination]
        for (i in 1..cmd.count) {
            val head = source.removeFirst()
            target.addFirst(head)
        }
    }

    fun execute2(cmd: Command) {
        val source = space[cmd.source]
        val target = space[cmd.destination]
        var buf = ""
        for (i in 1..cmd.count) {
            val head = source.removeFirst()
            buf += head
        }
        buf.reversed().forEach { target.addFirst(it.toString()) }
    }

    fun heads(): String = space.stream().map { it.first() }.collect(Collectors.joining())
}

fun main() {
    Problem.forDay("day5").use { solveFirst(it) }.let { println("Result of the first problem is $it") }
    Problem.forDay("day5").use { solveSecond(it) }.let { println("Result of the first problem is $it") }
}

private fun solveFirst(reader: BufferedReader): String {
    val (cargo, commands) = getCargoAndCommands(reader)
    commands.forEach { cargo.execute(it) }
    return cargo.heads()
}

private fun solveSecond(reader: BufferedReader): String {
    val (cargo, commands) = getCargoAndCommands(reader)
    commands.forEach { cargo.execute2(it) }
    return cargo.heads()
}

private fun getCargoAndCommands(reader: BufferedReader): Pair<Cargo, MutableList<Command>> {
    var cargoSpace: MutableList<ArrayDeque<String>>? = null
    val commands: MutableList<Command> = mutableListOf()
    var parsingCommands = false
    while (reader.ready()) {
        val line = reader.readLine()
        if (line.isEmpty()) {
            parsingCommands = true
            continue
        }
        if (parsingCommands) {
            commands.add(parseCommand(line))
        } else {
            if (cargoSpace == null) {
                cargoSpace = parseCargoMeta(line)
            }
            if (line.contains("[")) {
                addCargoState(line, cargoSpace)
            }
        }
    }
    val cargo = Cargo(cargoSpace ?: throw IllegalArgumentException("There is no cargo space!"))
    return Pair(cargo, commands)
}

private fun parseCargoMeta(line: String): MutableList<ArrayDeque<String>> {
    val count = normalize(line).trim().split("[").filter { it.isNotBlank() }.size
    val target = mutableListOf<ArrayDeque<String>>()
    for (i in 0 until count) {
        target.add(ArrayDeque())
    }
    return target
}

private fun addCargoState(line: String, cargoSpace: MutableList<ArrayDeque<String>>) {
    val normalized = normalize(line)
    val chars = normalized.replace("[", "").replace("]", "").replace(" ", "")
    chars.forEachIndexed { index, c ->
        if (c != '_') {
            cargoSpace[index].addLast(c.toString())
        }
    }
}

private fun normalize(line: String): String {
    var result = line
    var old = line
    while (true) {
        result = result.replaceFirst("     ", " [_] ")
        if (result == old) {
            return result
        }
        old = result
    }
}

private fun parseCommand(line: String): Command =
    line.split(" ").let { Command(it[1].toInt(), it[3].toInt() - 1, it[5].toInt() - 1) }