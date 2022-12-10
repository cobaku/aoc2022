package ru.glukhov.aoc

import java.io.BufferedReader
import java.nio.charset.Charset

object Problem {

    fun forDay(day: String): BufferedReader =
        Problem::class.java.getResourceAsStream("/$day.txt")?.bufferedReader(Charset.defaultCharset())
            ?: throw IllegalArgumentException("Day $day not found in the resources folder")
}