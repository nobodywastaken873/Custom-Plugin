package me.newburyminer.customItems.recipes

import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.recipes.registrars.ArmorRecipeBootstrapper
import me.newburyminer.customItems.recipes.registrars.MaterialRecipeBootstrapper
import me.newburyminer.customItems.recipes.registrars.OtherRecipeBootstrapper
import me.newburyminer.customItems.recipes.registrars.ToolRecipeBootstrapper
import me.newburyminer.customItems.recipes.registrars.WeaponRecipeBootstrapper
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object RecipeRegistry {

    val recipeList: MutableList<Recipe> = mutableListOf()
    val recipeMap: MutableMap<RecipeType, MutableList<Recipe>> = mutableMapOf(
        RecipeType.WEAPON to mutableListOf(),
        RecipeType.TOOL to mutableListOf(),
        RecipeType.ARMOR to mutableListOf(),
        RecipeType.MATERIAL to mutableListOf(),
        RecipeType.OTHER to mutableListOf(),
    )

    fun addRecipe(recipe: Recipe, recipeType: RecipeType) {
        recipeList.add(recipe)
        recipeMap[recipeType]?.add(recipe)
    }

    private val craftSlots = arrayOf(
        arrayOf(1, 2, 3, 4, 5,),
        arrayOf(10,11,12,13,14),
        arrayOf(19,20,21,22,23),
        arrayOf(28,29,30,31,32),
        arrayOf(37,38,39,40,41),
    )

    fun checkForRecipe(grid: Inventory): Recipe? {
        val itemGrid = mutableListOf<MutableList<ItemStack?>>()

        for (row in 0..4) {
            itemGrid.add(mutableListOf())
            for (slot in craftSlots[row]) {
                itemGrid[row].add(grid.getItem(slot))
            }
        }

        for (recipe in recipeList) {
            if (recipe.matches(itemGrid)) return recipe
        }

        return null
    }

    fun takeRecipeIngredients(grid: Inventory, result: Recipe) {
        for (row in result.items.indices) {
            for (col in result.items[row].indices) {
                val inventorySlot = craftSlots[row][col]
                val recipeItem = result.items[row][col]

                if (grid.getItem(inventorySlot) == null) continue
                (grid.getItem(inventorySlot) ?: return).amount -= recipeItem?.getItem()?.amount ?: 0
            }
        }
    }

    fun getPage(page: Int, recipeType: RecipeType): MutableList<Recipe?> {
        //return a list of recipes from page-1*24 to page*24 non-inclusive
        val pageRecipes = mutableListOf<Recipe?>()
        for (i in page*35..<(page+1)*35) {
            pageRecipes.add(recipeMap[recipeType]?.getOrNull(i))
        }
        return pageRecipes
    }

    fun getTotalEntries(recipeType: RecipeType): Int {
        return recipeMap[recipeType]?.size ?: 0
    }

    fun registerAll() {

        MaterialRecipeBootstrapper.bootstrap()
        ToolRecipeBootstrapper.bootstrap()
        WeaponRecipeBootstrapper.bootstrap()
        ArmorRecipeBootstrapper.bootstrap()
        OtherRecipeBootstrapper.bootstrap()

    }

    /*
    recipe {
        grid {
            row(null, null, null, null, null)
            row(null, null, null, null, null)
            row(null, null, null, null, null)
            row(null, null, null, null, null)
            row(null, null, null, null, null)
        }
        result(CustomItem.HOE)
    }
     */

}