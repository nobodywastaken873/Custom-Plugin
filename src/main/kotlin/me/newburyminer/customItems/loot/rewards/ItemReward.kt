package me.newburyminer.customItems.loot.rewards

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ItemReward(
    val item: (Double) -> ItemStack,
    val count: (Double) -> Int = {1}
): LootReward {

    constructor(
        item: (Double) -> ItemStack,
        count: Int
    ) : this(item, { count })

    override fun evaluate(scaler: Double, player: Player): List<ItemStack> {
        val itemStack = item(scaler)
        itemStack.amount = count(scaler)
        return listOf(itemStack)
    }

}