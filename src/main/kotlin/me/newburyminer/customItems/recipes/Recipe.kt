package me.newburyminer.customItems.recipes

import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.ItemRegistry
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack


//NEED NBT IN NON-CUSTOM ITEMS AND MAYBE CUSTOM TOO
//advancement check
data class Recipe(val grid: List<List<RecipeItemBase?>>, val result: ItemStack) {

    fun matches(otherGrid: List<List<ItemStack?>>): Boolean {

        for (row in 0..4) for (col in 0..4) {

            val gridItem = grid[row][col]
            val otherItem = otherGrid[row][col]

            if (gridItem == null && otherItem == null) continue
            if ((gridItem == null) xor (otherItem == null)) return false

            if (gridItem?.matches(otherItem) != true) return false
        }

        return true

    }


    /*
    init {
        for (row in 0..4) {
            items.add(mutableListOf())
            for (item in grid[row]) {
                if (item is Array<*>) {
                    if (item[0] is CustomItem) {
                        val itemStack = ItemRegistry.get(item[0] as CustomItem)
                        itemStack.amount = item[1] as Int
                        items[row].add(itemStack)
                    } else if (item[0] is Material) {
                        items[row].add(ItemStack(item[0] as Material, item[1] as Int))
                    } else if (item[0] is ItemStack) {
                        val newItem = ItemStack(item[0] as ItemStack)
                        newItem.amount = item[1] as Int
                        items[row].add(newItem)
                    }
                } else if (item is CustomItem) {
                    items[row].add(ItemRegistry.get(item))
                } else if (item is Material) {
                    items[row].add(ItemStack(item))
                } else if (item is ItemStack) {
                    items[row].add(ItemStack(item))
                } else if (item == null) {
                    items[row].add(null)
                }
            }
        }

        if (result is Array<*>) {
            if (result[0] is CustomItem) {
                val itemStack = ItemRegistry.get(result[0] as CustomItem)
                itemStack.amount = result[1] as Int
                resultItem = itemStack
            } else if (result[0] is Material) {
                resultItem = ItemStack(result[0] as Material, result[1] as Int)
            } else if (result[0] is ItemStack) {
                val newItem = ItemStack(result[0] as ItemStack)
                newItem.amount = result[1] as Int
                resultItem = newItem
            }
        } else if (result is CustomItem) {
            resultItem = ItemRegistry.get(result)
        } else if (result is Material) {
            resultItem = ItemStack(result)
        } else if (result is ItemStack) {
            resultItem = ItemStack(result)
        }
    }
    */

}