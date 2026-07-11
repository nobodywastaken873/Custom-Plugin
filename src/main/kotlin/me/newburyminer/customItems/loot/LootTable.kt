package me.newburyminer.customItems.loot

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface LootTable {

    fun evaluate(scaler: Double, player: Player): List<ItemStack>

}