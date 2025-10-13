package me.newburyminer.customItems.items

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

// todo: refactor back to ItemRegistry
class ItemRegistry {
    companion object {

        private val items = mutableMapOf<CustomItem, ItemStack>()

        fun register(customItem: CustomItem, itemStack: ItemStack) {
            val item = ItemStack(itemStack)
            items[customItem] = item
        }

        fun get(customItem: CustomItem): ItemStack {
            val item = items[customItem] ?: ItemStack(Material.BARRIER)
            return ItemStack(item)
        }
    }
}