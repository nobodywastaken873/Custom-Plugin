package me.newburyminer.customItems.loot

import me.newburyminer.customItems.CustomItems
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.util.UUID

object PlayerLootManager: BukkitRunnable() {

    private val loots = mutableMapOf<UUID, MutableMap<LootContext, Int>>()

    fun initialize() {

        val folderPath = System.getProperty("user.dir") + "/plugins/customItems/"
        val directory = File(folderPath)
        if (!directory.exists()) { directory.mkdir() }

        val fileName = folderPath + "playerLootData.txt"
        val file = File(fileName)
        if (!file.exists()) { file.createNewFile() }

        val text = file.readText()
        if (text.isEmpty()) return

        val entries = text.split("\n")
        entries.forEach {entry ->
            val splitIndex = entry.indexOf(":")
            val uuid = UUID.fromString(entry.substring(0, splitIndex))
            val loot = entry.substring(splitIndex + 1).split(",").map {
                val split = it.split("=")
                LootContext.fromStringData(split[0]) to split[1].toInt()
            }
            loots[uuid] = loot.toMap().toMutableMap()
        }

    }

    fun pushToFile() {
        val folderPath = System.getProperty("user.dir") + "/plugins/customItems/"
        val directory = File(folderPath)
        if (!directory.exists()) { directory.mkdir() }

        val fileName = folderPath + "playerLootData.txt"
        val file = File(fileName)
        if (!file.exists()) { file.createNewFile() }

        val text = loots.map { (key, value) ->
            "$key:" + value.map { (loot, amount) ->
                "${loot.toStringData()}=$amount"
            }.joinToString(",")
        }.joinToString("\n")

        file.writeText(
            text
        )
    }

    fun addLoot(loot: LootContext, player: Player, count: Int = 1) {
        val map = loots.getOrPut(player.uniqueId) {mutableMapOf()}
        map[loot] = map[loot]?.plus(count) ?: count
    }

    fun removeLoot(loot: LootContext, player: Player, count: Int) {
        val map = loots.getOrPut(player.uniqueId) {mutableMapOf()}
        val current = map[loot]
        if (current == null || current < count) {
            CustomItems.plugin.logger.warning("${player.name} does not have loot $loot but attempted to remove it.")
            return
        }
        map[loot] = current - count
        map.toList().forEach {
            if (it.second == 0) map.remove(it.first)
        }
    }

    fun getAllLoot(player: Player): Map<LootContext, Int> {
        return loots[player.uniqueId]?.toMap() ?: mutableMapOf()
    }

    override fun run() {
        pushToFile()
    }

}