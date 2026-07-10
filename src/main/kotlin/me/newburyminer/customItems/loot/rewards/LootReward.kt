package me.newburyminer.customItems.loot.rewards

import org.bukkit.inventory.ItemStack

interface LootReward {
    fun evaluate(scaler: Double): List<ItemStack>
}