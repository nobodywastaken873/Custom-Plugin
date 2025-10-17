package me.newburyminer.customItems.recipes

import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.ItemRegistry
import org.bukkit.inventory.ItemStack

class RecipeBuilder {

    private lateinit var recipeGrid: List<List<RecipeItemBase?>>
    private lateinit var resultItem: ItemStack

    fun grid(applyGrid: GridBuilder.() -> Unit) {
        val gridBuilder = GridBuilder()
        gridBuilder.applyGrid()
        recipeGrid = gridBuilder.build()
    }

    fun result(item: ItemStack) {
        resultItem = item.clone()
    }

    fun result(custom: CustomItem) {
        resultItem = ItemRegistry.get(custom).clone()
    }

    fun build(): Recipe {
        return Recipe(recipeGrid, resultItem.clone())
    }

}