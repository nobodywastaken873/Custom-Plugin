package me.newburyminer.customItems.loot

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.math.roundToInt

class WeightedTable(
    vararg val entries: LootEntry,
    val rolls: IntRange
): LootTable {

    override fun evaluate(scaler: Double, player: Player): List<ItemStack> {
        val roll = (rolls.first + (rolls.last - rolls.first) * scaler).roundToInt()
        val allItems = mutableListOf<ItemStack>()
        repeat(roll) {
            allItems.addAll(randomEntry().evaluate(scaler, player))
        }
        return allItems
    }

    private fun randomEntry(): LootEntry {
        val totalWeight = entries.sumOf { it.weight }
        var roll = totalWeight * Math.random()
        for (entry in entries) {
            if (entry.weight > roll) return entry
            roll -= entry.weight
        }
        return entries.last()
    }

}