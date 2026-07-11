package me.newburyminer.customItems.loot

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class RoundTable(
    vararg val entries: LootEntry
): LootTable {

    override fun evaluate(scaler: Double, player: Player): List<ItemStack> {
        val allItems = mutableListOf<ItemStack>()
        entries.forEach { entry ->
            allItems.addAll(entry.evaluate(scaler, player))
        }
        return allItems
    }

}