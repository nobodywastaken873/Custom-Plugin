package me.newburyminer.customItems.loot

import org.bukkit.inventory.ItemStack

class LootTable(
    vararg val entries: LootEntry,
    val rolls: IntRange
) {

    fun evaluate(scaler: Double): List<ItemStack> {
        return emptyList()
    }

}