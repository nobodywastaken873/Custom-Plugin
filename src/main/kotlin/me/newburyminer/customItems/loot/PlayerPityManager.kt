package me.newburyminer.customItems.loot

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.helpers.FileDatabase
import me.newburyminer.customItems.loot.rewards.LootTableReward
import me.newburyminer.customItems.loot.rewards.VanillaTableReward
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.util.UUID

object PlayerPityManager: FileDatabase() {

    private val pities = mutableMapOf<UUID, MutableMap<String, PityProgress>>()

    override val fileName: String = "playerPityData.txt"

    override fun initialize() {

        val text = readFromFile()
        if (text.isEmpty()) return

        val entries = text.split("\n")
        entries.forEach {entry ->
            val splitIndex = entry.indexOf(":")
            val uuid = UUID.fromString(entry.substring(0, splitIndex))
            val loot = entry.substring(splitIndex + 1).split(",").map {
                val split = it.split("=")
                split[0] to PityProgress(split[1].toDouble(), Material.valueOf(split[2]))
            }
            pities[uuid] = loot.toMap().toMutableMap()
        }

    }

    override fun pushToFile(backup: Boolean) {
        val text = pities.toList().map { (key, value) ->
            "$key:" + value.map { (loot, progress) ->
                "$loot=${progress.progress}=${progress.material.name}"
            }.joinToString(",")
        }.joinToString("\n")

        writeToFile(text, backup)
    }

    fun increasePity(player: Player, table: LootTable, scaler: Double) {
        val entries = findPityEntries(table)
        val pityMap = pities.getOrPut(player.uniqueId) { mutableMapOf() }
        for (entry in entries) {
            val pity = entry.pity ?: continue
            if (pity.id in pityMap.keys) {
                val progress = pityMap[pity.id] ?: continue
                progress.progress += 1.0 / (pity.threshold)
                if (progress.progress >= 1.0) {
                    player.sendMessage(Utils.text("Pity threshold reached and reset for ${pity.name}. You have received the drop.", arrayOf(237, 223, 24)))
                    progress.progress = 0.0
                    entry.evaluate(scaler, player).forEach { player.addItemorDrop(it) }
                }
            }
            else {
                val mat = when (entry.reward) {
                    is LootTableReward -> Material.ENDER_CHEST
                    is VanillaTableReward -> Material.CHEST
                    else -> entry.reward.evaluate(scaler, player).first().type
                }
                val newProgress = PityProgress(1.0 / pity.threshold, mat)
                pityMap[pity.id] = newProgress
            }
        }
    }

    private fun findPityEntries(table: LootTable): List<LootEntry> {
        val entries = mutableListOf<LootEntry>()
        when (table) {
            is WeightedTable -> {
                table.entries.forEach {
                    if (it.pity != null)
                        entries.add(it)
                    if (it.pity == null && it.reward is LootTableReward) {
                        entries.addAll(findPityEntries(it.reward.table))
                    }
                }
            }
            is RoundTable -> {
                table.entries.forEach {
                    if (it.pity == null && it.reward is LootTableReward) {
                        entries.addAll(findPityEntries(it.reward.table))
                    }
                }
            }
        }
        return entries
    }

    fun resetPity(id: String, threshold: Int, player: Player) {
        val pityMap = pities.getOrPut(player.uniqueId) { mutableMapOf() }
        val currentProgress = pityMap[id] ?: return
        currentProgress.progress = -1.0 / threshold
    }

    fun getAllPity(player: Player): Map<String, PityProgress> {
        return pities[player.uniqueId]?.toMap() ?: mutableMapOf()
    }

}