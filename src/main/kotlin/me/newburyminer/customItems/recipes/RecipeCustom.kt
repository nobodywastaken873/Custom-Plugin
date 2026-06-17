package me.newburyminer.customItems.recipes

import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.ItemRegistry
import org.bukkit.inventory.ItemStack

class RecipeCustom(private val custom: CustomItem, override val amount: Int = 1): RecipeItemBase {

    override fun itemMatches(other: ItemStack?): Boolean {
        val otherCustom = other?.getCustom() ?: return false
        return otherCustom == custom
    }

    override fun getItem(): ItemStack {
        val newItem = ItemRegistry.get(custom)
        newItem.amount = amount
        return newItem
    }

    override fun equals(other: Any?): Boolean {
        if (other !is RecipeCustom) return false
        return custom == other.custom
    }

    override fun hashCode(): Int {
        var result = amount
        result = 31 * result + custom.hashCode()
        return result
    }

}