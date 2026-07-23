package me.newburyminer.customItems.loot

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.helpers.FileDatabase
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.util.UUID

object PlayerLootManager: FileDatabase() {

    private val loots = mutableMapOf<UUID, MutableMap<LootContext, Int>>()

    override val fileName: String = "playerLootData.txt"

    override fun initialize() {
        val text = readFromFile()
        if (text.isEmpty()) return

        val entries = text.split("\n")
        entries.forEach {entry ->
            val splitIndex = entry.indexOf(":")
            val uuid = UUID.fromString(entry.substring(0, splitIndex))
            val loot = entry.substring(splitIndex + 1).split(",").filter { it.isNotEmpty() }.map {
                val split = it.split("=")
                LootContext.fromStringData(split[0]) to split[1].toInt()
            }
            loots[uuid] = loot.toMap().toMutableMap()
        }
    }

    override fun pushToFile(backup: Boolean) {
        val text = loots.toList().map { (key, value) ->
            "$key:" + value.map { (loot, amount) ->
                "${loot.toStringData()}=$amount"
            }.joinToString(",")
        }.joinToString("\n")

        writeToFile(text, backup)
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

}