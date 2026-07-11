package me.newburyminer.customItems.loot.rewards

import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.loot.LootTable
import me.newburyminer.customItems.loot.rewards.CustomReward
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class LootTableReward(
    val table: LootTable,
    val count: (Double) -> Int = {1}
): LootReward {

    constructor(
        table: LootTable,
        count: Int
    ) : this(table, { count })

    override fun evaluate(scaler: Double, player: Player): List<ItemStack> {
        val allItems = mutableListOf<ItemStack>()
        repeat(count(scaler)) {allItems.addAll(table.evaluate(scaler, player))}
        return allItems
    }

}