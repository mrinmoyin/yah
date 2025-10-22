package com.gitlab.notscripter.yah.utils

import com.github.ajalt.mordant.animation.textAnimation
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*
import com.github.ajalt.mordant.terminal.Terminal

val t: Terminal = Terminal()

fun sh(command: String, label: String? = null, printOutput: Boolean = false): String? {
    var process = ProcessBuilder("sh", "-c", command).redirectErrorStream(true).start()
    t.cursor.hide(showOnExit = true)

    /*
    Runtime.getRuntime().addShutdownHook(Thread { if (process.isAlive) process.destroyForcibly() })
    */

    if (!label.isNullOrEmpty()) {
        val spinnerFrames = listOf("⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏")

        val animation =
            t.textAnimation<Int> { frame ->
                val spinner = spinnerFrames[frame % spinnerFrames.size]
                green("$spinner $label...")
            }

        var frame = 0
        while (process.isAlive) {
            animation.update(frame++)
            Thread.sleep(100)
        }

        if (process.exitValue() != 0) {
            animation.clear()
            t.println(red("❌$label..."))
            t.println(red("❌Failed to execute command: ${command}"))
            return null
        }

        animation.clear()
        t.println(green("✔️ $label..."))

        animation.stop()
    } else if (printOutput == true) {
        process.inputStream.bufferedReader(Charsets.UTF_8).use { reader ->
            while (true) {
                val line = reader.readLine() ?: break
                when {
                    line.contains(" D ") -> t.println(blue(line))
                    line.contains(" I ") -> t.println(green(line))
                    line.contains(" W ") -> t.println(yellow(line))
                    line.contains(" E ") -> t.println(red(line))
                    line.contains(" F ") -> t.println(red(line))
                }
            }
        }
        process.waitFor()
    }

    val output = process.inputStream.bufferedReader().readText().trim()
    process.destroy()
    t.cursor.show()
    return output
}
