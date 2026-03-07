package me.newburyminer.customItems.items

import me.newburyminer.customItems.items.behaviors.ItemPredicate
import org.bukkit.inventory.ItemStack

interface CustomItemDefinition: CustomItemBehavior, ItemPredicate {
    val item: ItemStack
    val custom: CustomItem
}