package me.newburyminer.customItems.systems.materials

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object MaterialConverterRegistry {
    private val conversions = mutableMapOf<Material, MaterialCollection>()

    private fun register(baseMaterial: Material, newMaterialType: MaterialType, amount: Double) {
        conversions[baseMaterial] = MaterialCollection(
            mutableMapOf(newMaterialType to amount)
        )
    }

    fun registerBulk(materialMap: Map<Material, Pair<MaterialType, Double>>) {
        materialMap.forEach { (key, value) ->
            register(key, value.first, value.second)
        }
    }

    private fun registerRecipe(baseMaterial: Material, map: MutableMap<MaterialType, Double>) {
        conversions[baseMaterial] = MaterialCollection(
            map
        )
    }

    fun registerRecipeBulk(materialMap: MutableMap<Material, MutableMap<MaterialType, Double>>) {
        materialMap.forEach { (material, map) ->
            registerRecipe(material, map)
        }
    }

    fun convert(itemStack: ItemStack): MaterialCollection? {
        val baseValue = conversions[itemStack.type] ?: return null
        val scaledValue = baseValue * itemStack.amount
        return scaledValue
    }
}