package me.newburyminer.customItems.items.behaviors

import com.google.common.collect.Lists
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.OvermaxVillagerComponent
import org.bukkit.Material
import org.bukkit.entity.Villager
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe

interface VillagerUpgrade {

    fun upgradeVillager(villager: Villager, trades: List<Pair<Pair<ItemStack, ItemStack>, ItemStack>>) {
        val newRecipes = Lists.newArrayList(villager.recipes)

        for ((ing, result) in trades) {
            val newRecipe = MerchantRecipe(result, 0, 10000, true, 0, 0F, true)

            if (ing.first.type != Material.AIR)
                newRecipe.addIngredient(ing.first)
            if (ing.second.type != Material.AIR)
                newRecipe.addIngredient(ing.second)
            newRecipes.add(newRecipe)
        }

        villager.recipes = newRecipes
        EntityWrapperManager.getWrapperorNew(villager).addComponent(OvermaxVillagerComponent())
    }

}