package me.newburyminer.customItems.items

import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.Utils.Companion.setTag
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.UUID

class ItemRegistry {
    companion object {

        private val items = mutableMapOf<CustomItem, ItemStack>()

        fun register(customItem: CustomItem, itemStack: ItemStack) {
            val item = ItemStack(itemStack)
            items[customItem] = item
        }

        fun get(customItem: CustomItem): ItemStack {
            val item = items[customItem] ?: ItemStack(Material.BARRIER)
            if (item.getCustom()?.stackable == false) {
                item.setTag("uniquesalt", UUID.randomUUID().toString())
            }
            return ItemStack(item)
        }
    }
}