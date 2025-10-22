package com.gitlab.notscripter.yah

import com.github.ajalt.clikt.command.SuspendingCliktCommand
import com.github.ajalt.clikt.command.main
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*
import com.gitlab.notscripter.yah.utils.sh
import com.gitlab.notscripter.yah.utils.t
import java.io.File

class Compose : SuspendingCliktCommand(name = "yah") {
    override fun help(context: Context) = "Yet another aur helper."

    private val sync by option("-S", "--sync").help("Sync")
    private val dbpath by option("-b", "--dbpath").help("set an alternate database location")
    private val clean by
        option("-c", "--clean").help("remove old packages from cache directory (-cc for all)")
    private val nodeps by
        option("-d", "--nodeps").help("skip dependency version checks (-dd to skip all checks)")
    private val groups by
        option("-g", "--groups")
            .help("view all members of a package group (-gg to view all groups and members)")
    private val info by
        option("-i", "--info").help("view package information (-ii for extended information)")
    private val list by option("-l", "--list").help("view a list of packages in a repo")
    private val print by
        option("-p", "--print").help("print the targets instead of performing the operation")
    private val quiet by option("-q", "--quiet").help("show less information for query and search")
    private val root by option("-r", "--root").help("set an alternate installation root")
    private val search by
        option("-s", "--search").help("search remote repositories for matching strings")
    private val sysupgrade by
        option("-u", "--sysupgrade").help("upgrade installed packages (-uu enables downgrades)")
    private val verbose by option("-v", "--verbose").help("be verbose")
    private val downloadonly by
        option("-w", "--downloadonly").help("download packages but do not install/upgrade anything")
    private val refresh by
        option("-y", "--refresh")
            .flag()
            .help(
                "download fresh package databases from the server (-yy to force a refresh even if up to date)"
            )

    private val arch by option("--arch").help("set an alternate architecture")
    private val aasdeps by option("--asdeps").help("install packages as non-explicitly installed")
    private val asexplicit by
        option("--asexplicit").help("install packages as explicitly installed")
    private val aassume_installed by
        option("--aassume-installed").help("add a virtual package to satisfy dependencies")
    private val cachedir by option("--cachedir").help("set an alternate package cache location")
    private val color by option("--color").help("colorize the output")
    private val config by option("--config").help("set an alternate configuration file")
    private val confirm by option("--confirm").help("always ask for confirmation")
    private val dbonly by option("--dbonly").help("only modify database entries, not package files")
    private val debug by option("--debug").help("display debug messages")
    private val disable_download_timeout by
        option("--disable-download-timeout").help("use relaxed timeouts for download")
    private val disable_sandbox by
        option("--disable-sandbox").help("disable the sandbox used for the downloader process")
    private val gpgdir by option("--gpgdir").help("set an alternate home directory for GnuPG")
    private val hookdir by option("--hookdir").help("set an alternate hook location")
    private val ignore by
        option("--ignore").help("ignore a package upgrade (can be used more than once)")
    private val ignoregroup by
        option("--ignoregroup").help("ignore a group upgrade (can be used more than once)")
    private val logfile by option("--logfile").help("set an alternate log file")
    private val needed by option("--needed").help("do not reinstall up to date packages")
    private val noconfirm by option("--noconfirm").help("do not ask for any confirmation")
    private val noprogressbar by
        option("--noprogressbar").help("do not show a progress bar when downloading files")
    private val noscriptlet by
        option("--noscriptlet").help("do not execute the install scriptlet if one exists")
    private val overwrite by
        option("--overwrite").help("overwrite conflicting files (can be used more than once)")
    private val print_formant by
        option("--print-formant").help("specify how the targets should be printed")
    private val sysroot by option("--sysroot").help("operate on a mounted guest system (root-only)")

    override suspend fun run() {
        val home = sh("echo \$HOME")
        val yahDir = File("${home}/.yah")
        yahDir.mkdir()

        if (sync != null) {
            val repoUrl = "https://aur.archlinux.org/${sync}.git"
            val remoteRepo = sh("git ls-remote ${repoUrl}")
            if (remoteRepo.isNullOrEmpty()) {
                t.println(red("repository not found"))
                return
            }
            sh("git clone https://aur.archlinux.org/${sync}.git -q ${yahDir}/${sync}")
        }
    }
}

suspend fun main(args: Array<String>) = Compose().main(args)
