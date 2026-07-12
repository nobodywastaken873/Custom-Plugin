package me.newburyminer.customItems.helpers

import me.newburyminer.customItems.loot.PlayerPityManager
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.util.UUID

abstract class FileDatabase: BukkitRunnable() {

    abstract val fileName: String

    fun writeToFile(text: String, backup: Boolean = false) {
        val file = loadFile(backup)
        file.writeText(text)
    }

    fun readFromFile(): String {
        val file = loadFile()
        return file.readText()
    }

    private fun loadFile(backup: Boolean = false): File {
        val dirPath =
            if (backup) "/plugins/customItems/backups/"
            else "/plugins/customItems/"

        val folderPath = System.getProperty("user.dir") + dirPath
        val directory = File(folderPath)
        if (!directory.exists()) { directory.mkdir() }

        val realFileName =
            if (backup) insertHash()
            else fileName

        val fullPath = folderPath + realFileName
        val file = File(fullPath)
        if (!file.exists()) { file.createNewFile() }

        return file
    }

    private fun insertHash(): String {
        val uuid = UUID.randomUUID()
        return fileName.substring(0..<fileName.lastIndexOf('.')) + uuid.toString() + ".txt"
    }

    abstract fun pushToFile(backup: Boolean = false)
    abstract fun initialize()
    fun backup() { pushToFile(true) }
    fun startPushing(plugin: Plugin) {
        this.runTaskTimerAsynchronously(plugin, 20L, 1200L)
    }

    override fun run() {
        pushToFile()
    }

}