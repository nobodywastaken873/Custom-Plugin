package me.newburyminer.customItems.systems.materials

import me.newburyminer.customItems.CustomItems
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ShapelessRecipe

/*class RecipeConverterBootstrapper {
    fun registerAll() {

        val recipes = listOf(
            Material.REDSTONE, Material.REDSTONE_TORCH
        )

    }

    fun getMaterials(material: Material): MaterialCollection? {
        // base case
        val result = MaterialConverterRegistry.convert(ItemStack(material))
        if (result != null) return result

        // find all possible recipes for material
        val allRecipes = Bukkit.recipeIterator()
        val possibleRecipes = mutableListOf<Recipe>()
        allRecipes.forEach {
            if (it.result.type == material && (it is ShapelessRecipe || it is ShapedRecipe)) possibleRecipes.add(it)
        }

        // if there are no possible recipes, and we cannot convert this base item to MaterialType, the recipe is not possible
        if (possibleRecipes.isEmpty()) return null

        for (recipe in possibleRecipes) {
            val choiceList =
                if (recipe is ShapedRecipe)
                    recipe.choiceMap.values.toList()
                else if (recipe is ShapelessRecipe)
                    recipe.choiceList
                else {CustomItems.plugin.logger.severe("RecipeConverterBootstrapper is not working."); continue }

        }

    }
}*/