package me.newburyminer.customItems.recipes

import org.bukkit.inventory.ItemStack

interface RecipeItemBase {
    fun matches(other: ItemStack?): Boolean
}