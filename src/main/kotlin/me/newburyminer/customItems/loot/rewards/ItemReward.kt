package me.newburyminer.customItems.loot.rewards

import me.newburyminer.customItems.loot.rewards.MaterialReward
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemReward(
    val item: (Double) -> ItemStack,
    val count: (Double) -> Int = {1}
): LootReward {

    constructor(
        item: (Double) -> ItemStack,
        count: Int
    ) : this(item, { count })

    override fun evaluate(scaler: Double): List<ItemStack> {
        val itemStack = item(scaler)
        itemStack.amount = count(scaler)
        return listOf(itemStack)
    }

}