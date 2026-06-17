package me.newburyminer.customItems.recipes

import org.bukkit.inventory.ItemStack

interface RecipeItemBase {
    val amount: Int
    fun matches(other: ItemStack?): Boolean {
        return (other?.amount ?: 0) >= amount && itemMatches(other)
    }
    fun itemMatches(other: ItemStack?): Boolean
    fun getItem(): ItemStack
    override fun equals(other: Any?): Boolean
}