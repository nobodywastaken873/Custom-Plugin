package me.newburyminer.customItems.loot.rewards

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class MaterialReward(
    val material: Material,
    val count: (Double) -> Int
): LootReward {

    constructor(
        material: Material,
        count: Int
    ) : this(material, { count })

    override fun evaluate(scaler: Double, player: Player): List<ItemStack> {
        var total = count(scaler)
        val items = mutableListOf<ItemStack>()
        while (total > 0) {
            val toSubtract = total.coerceAtMost(64)
            items += ItemStack(material, total)
            total -= toSubtract
        }
        return items
    }

}