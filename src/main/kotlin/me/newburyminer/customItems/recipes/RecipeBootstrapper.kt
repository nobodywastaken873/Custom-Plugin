package me.newburyminer.customItems.recipes

import me.newburyminer.customItems.items.CustomItem
import org.bukkit.Material

interface RecipeBootstrapper {
    fun bootstrap()
    fun recipe(applyRecipe: RecipeBuilder.() -> Unit) {
        val builder = RecipeBuilder()
        builder.applyRecipe()
        val recipe = builder.build()
        RecipeRegistry.addRecipe(recipe)
    }
    fun item(material: Material, amount: Int = 1): RecipeItem {
        return RecipeItem(material, amount)
    }
    fun custom(customItem: CustomItem, amount: Int = 1): RecipeCustom {
        return RecipeCustom(customItem, amount)
    }
}