package me.newburyminer.customItems.loot

import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.loot.rewards.CustomReward
import me.newburyminer.customItems.loot.rewards.ItemReward
import me.newburyminer.customItems.loot.rewards.LootReward
import me.newburyminer.customItems.loot.rewards.LootTableReward
import me.newburyminer.customItems.loot.rewards.MaterialReward
import me.newburyminer.customItems.loot.rewards.VanillaTableReward
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootTables

interface LootProvider: TableCreation {

    val id: String
        get() = this::class.simpleName!!
    val name: String
        get() = id
            .replace(Regex("([a-z0-9])([A-Z])"), "$1 $2") // Space between lower/digit and upper
            .replace(Regex("([A-Z])([A-Z][a-z])"), "$1 $2") // Space between consecutive capitals (e.g., XMLParser -> XML Parser)

    fun getMarker(amount: Int, context: LootContext): ItemStack
    fun getLoot(table: String, scaler: Int, player: Player, count: Int = 1): List<ItemStack>

}