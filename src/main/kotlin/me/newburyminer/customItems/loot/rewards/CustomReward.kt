package me.newburyminer.customItems.loot.rewards

import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.ItemRegistry
import me.newburyminer.customItems.loot.rewards.MaterialReward
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class CustomReward(
    val custom: CustomItem,
    val count: (Double) -> Int = {1}
): LootReward {

    constructor(
        custom: CustomItem,
        count: Int
    ) : this(custom, { count })

    override fun evaluate(scaler: Double, player: Player): List<ItemStack> {
        val item = ItemRegistry.get(custom)
        item.amount = count(scaler)
        return listOf(item)
    }

}