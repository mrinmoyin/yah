package com.gitlab.notscripter.yah

import com.github.ajalt.clikt.command.SuspendingCliktCommand
import com.github.ajalt.clikt.command.main
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.mordant.animation.coroutines.animateInCoroutine
import com.github.ajalt.mordant.animation.progress.advance
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextColors.Companion.rgb
import com.github.ajalt.mordant.rendering.TextStyles.*
import com.github.ajalt.mordant.table.horizontalLayout
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.widgets.Spinner
import com.github.ajalt.mordant.widgets.progress.completed
import com.github.ajalt.mordant.widgets.progress.marquee
import com.github.ajalt.mordant.widgets.progress.percentage
import com.github.ajalt.mordant.widgets.progress.progressBar
import com.github.ajalt.mordant.widgets.progress.progressBarLayout
import com.github.ajalt.mordant.widgets.progress.speed
import com.github.ajalt.mordant.widgets.progress.timeRemaining
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HelloCli : SuspendingCliktCommand() {
    private val name: String? by option(help = "Your name").prompt("Enter your name")

    val t = Terminal()

    override suspend fun run() = coroutineScope {
        println("Hello, $name!")
        t.println(brightRed("You can use any of the standard ANSI colors"))

        val style = (bold + black + strikethrough)
        t.println(cyan("You ${(green on white)("can ${style("nest")} styles")} arbitrarily"))

        t.println(rgb("#b4eeb4")("You can also use true color and color spaces like HSL"))

        horizontalLayout {
            cell("Spinner:")
            cell(Spinner.Dots(initial = 2))
        }

        val progress =
            progressBarLayout {
                    marquee(t.theme.warning("my-file-download.bin"), width = 15)
                    percentage()
                    progressBar()
                    completed(style = t.theme.success)
                    speed("B/s", style = t.theme.info)
                    timeRemaining(style = magenta)
                }
                .animateInCoroutine(t)

        launch { progress.execute() }

        // Update the progress as the download progresses
        progress.update { total = 3_000_000_000 }
        while (!progress.finished) {
            progress.advance(15_000_000)
            Thread.sleep(100)
        }
    }
}

suspend fun main(args: Array<String>) = HelloCli().main(args)
