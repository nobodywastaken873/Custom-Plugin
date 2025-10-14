package me.newburyminer.customItems.items

import org.bukkit.inventory.ItemStack

interface CustomItemDefinition: CustomItemBehavior {
    val item: ItemStack
    val custom: CustomItem
    val period: Int get() = 20
}