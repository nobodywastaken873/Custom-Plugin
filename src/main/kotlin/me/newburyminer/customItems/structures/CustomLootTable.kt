package me.newburyminer.customItems.structures

import me.newburyminer.customItems.helpers.RandomSelector
import org.bukkit.inventory.ItemStack

class CustomLootTable(private val table: RandomSelector<*>, val rolls: IntRange) {
    fun roll(mult: Double = 1.0): List<ItemStack> {
        val items = mutableListOf<ItemStack>()
        for (i in 0..<(rolls.random()*mult).toInt()) {
            var result = table.next()
            while (result!!::class.java != ItemStack::class.java) {
                result = (result as RandomSelector<*>).next()
            }
            result = (result as ItemStack)
            items.add(result)
        }
        return items
    }
}