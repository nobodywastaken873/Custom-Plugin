package me.newburyminer.customItems.recipes

import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.ItemRegistry
import org.bukkit.inventory.ItemStack

class RecipeCustom(private val custom: CustomItem, private val amount: Int = 1): RecipeItemBase {

    override fun matches(other: ItemStack?): Boolean {
        val otherCustom = other?.getCustom() ?: return false
        if (otherCustom != custom) return false
        if (other.amount != amount) return false
        return true
    }

    override fun getItem(): ItemStack {
        return ItemRegistry.get(custom)
    }

}