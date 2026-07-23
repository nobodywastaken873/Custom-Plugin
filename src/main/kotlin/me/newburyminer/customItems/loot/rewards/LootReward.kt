package me.newburyminer.customItems.loot.rewards

import me.newburyminer.customItems.loot.TableCreation
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface LootReward: TableCreation {
    fun evaluate(scaler: Double, player: Player): List<ItemStack>
}