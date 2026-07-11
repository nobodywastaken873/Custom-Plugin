package me.newburyminer.customItems.loot

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object LootFactory {

    fun evaluate(table: LootTable, scaler: Double, player: Player): List<ItemStack> {
        val results = table.evaluate(scaler, player)
        PlayerPityManager.increasePity(player, table, scaler)
        return results
    }

}