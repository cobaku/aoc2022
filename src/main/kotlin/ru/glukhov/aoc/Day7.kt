package ru.glukhov.aoc

fun main() {
    val shell = Problem.forDay("day7").use {
        val shell = Shell()
        it.forEachLine {
            val cmd = it.parseCommand()
            if (cmd != null) {
                shell.execute(cmd)
            } else {
                shell.appendOutput(it)
            }
        }
        return@use shell
    }
    println("Result of the first problem is ${shell.getFirstProblemOutput()}")
    println("Result of the second problem is ${shell.getSecondProblemOutput()}")
}

private fun String.parseCommand(): ShellCommand? {
    if (!this.startsWith("$")) {
        return null
    }
    val plain = this.substring(1).trim()
    return if (plain.startsWith("cd")) {
        ShellCommand(plain.replace("cd", "").trim(), ShellCommand.Type.CD)
    } else {
        ShellCommand("", ShellCommand.Type.LS)
    }
}

private fun String.parseFile(): Pair<String, Int> = this.split(" ").let { Pair(it[1].trim(), it[0].toInt()) }

private class Shell {

    private var currentItem = Folder("/", 0, null, mutableListOf())

    fun execute(cmd: ShellCommand) {
        if (cmd.cmd == ShellCommand.Type.CD) {
            currentItem = if (cmd.path == "/") {
                currentItem.toRoot()
            } else {
                currentItem.cd(cmd.path.trim())
            }
        }
    }

    fun appendOutput(line: String) {
        if (line.startsWith("dir")) {
            val folderName = line.replace("dir", "")
            currentItem.addFolder(folderName.trim())
        }
        val first = line.first()
        if (first.isDigit()) {
            val (name, size) = line.parseFile()
            currentItem.addFile(name, size)
        }
    }

    fun getFirstProblemOutput(): Int {
        return count(currentItem.toRoot())
    }

    fun getSecondProblemOutput(): Int {
        val toRoot = currentItem.toRoot()
        val rootSize = toRoot.items.stream().mapToInt { it.size }.sum()
        val free = 70000000 - rootSize
        val required = 30000000 - free
        val target = mutableListOf<Folder>()
        findLessThan(required, toRoot, target)
        target.sortBy { it.size }
        return target.first().size
    }

    private fun count(root: Folder): Int {
        val selfSize = if (root.size > 100_000) 0 else root.size
        val entrySize = root.items.stream().filter { it.type == Item.Type.FOLDER }
            .mapToInt { count(it as Folder) }
            .sum()
        return selfSize + entrySize
    }

    private fun findLessThan(required: Int, root: Folder, target: MutableList<Folder>) {
        if (root.size >= required) {
            target.add(root)
        }
        root.items.stream()
            .filter { it.type == Item.Type.FOLDER }
            .map { it as Folder }
            .forEach { findLessThan(required, it, target) }
    }
}

private class ShellCommand(val path: String, val cmd: Type) {

    enum class Type {
        CD, LS;
    }
}

private open class Item(val name: String, val type: Type, var size: Int) {

    enum class Type {
        FOLDER, FILE
    }
}

private class Folder(name: String, size: Int, val root: Folder?, val items: MutableList<Item>) :
    Item(name, Type.FOLDER, size) {

    fun addFolder(name: String): Folder {
        val folder = Folder(name, 0, this, mutableListOf())
        items.add(folder)
        return folder
    }

    fun addFile(name: String, size: Int) {
        this.size += size
        items.add(File(name, size))
        incrementRootFolderSize(size)
    }

    fun cd(name: String): Folder {
        if (name == "..") {
            return this.root!!
        }
        return (items.find { it.name == name && it.type == Type.FOLDER } ?: addFolder(name)) as Folder
    }

    fun toRoot(): Folder {
        if (root == null) {
            return this
        }
        var fold = this
        while (fold.root != null) {
            fold = fold.root!!
        }
        return fold
    }

    fun incrementRootFolderSize(size: Int) {
        if (root == null) {
            return
        }
        var r = root
        while (r!!.root != null) {
            r.size += size
            r = r.root
        }
    }

    override fun toString(): String {
        return "${super.name} dir ($size)"
    }
}

private class File(name: String, size: Int) : Item(name, Type.FILE, size) {
    override fun toString(): String {
        return "${super.name} (file) $size"
    }
}
