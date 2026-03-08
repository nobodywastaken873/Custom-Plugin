package me.newburyminer.customItems.entity.components

import com.destroystokyo.paper.MaterialTags
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.Tag
import org.bukkit.entity.Villager
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe

class VillagerTradeComponent(startingTrades: MutableList<MerchantRecipe> = mutableListOf()): EntityComponent {

    private val currentTrades = startingTrades.toMutableList()

    override fun serialize(): Map<String, Any> {
        return currentTrades.withIndex().associate {
            it.index.toString() to it.value.serialize()
        }
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.VILLAGER_TRADE_COMPONENT
        @Suppress("UNCHECKED_CAST")
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            val list = mutableListOf<MerchantRecipe>()
            map.forEach {
                list.add(it.key.toIntOrNull() ?: 0, deserializeMerchantRecipe(it.value as Map<String, Any>))
            }
            return VillagerTradeComponent(list)
        }

        @Suppress("UNCHECKED_CAST")
        private fun deserializeMerchantRecipe(map: Map<String, Any?>): MerchantRecipe {
            val materialBytes1 = (map["material1"] as ArrayList<Byte>?)?.toByteArray()
            val materialBytes2 = (map["material2"] as ArrayList<Byte>?)?.toByteArray()
            val resultBytes = (map["result"] as ArrayList<Byte>).toByteArray()

            val material1 =
                if (materialBytes1 != null) ItemStack.deserializeBytes(materialBytes1)
                else null
            val material2 =
                if (materialBytes2 != null) ItemStack.deserializeBytes(materialBytes2)
                else null
            val result = ItemStack.deserializeBytes(resultBytes)

            val demand = map["demand"].toInt()
            val uses = map["uses"].toInt()

            val recipe = MerchantRecipe(result, uses, 0, true, 0, 0f)
            recipe.demand = demand
            if (material1 != null) recipe.addIngredient(ItemStack(material1))
            if (material2 != null) recipe.addIngredient(ItemStack(material2))
            return recipe
        }
    }

    private fun MerchantRecipe.serialize(): Map<String, Any?> {
        val material1 = this.ingredients.getOrNull(0)?.serializeAsBytes()
        val material2 = this.ingredients.getOrNull(1)?.serializeAsBytes()
        val result = this.result.serializeAsBytes()

        return mapOf(
            "material1" to material1,
            "material2" to material2,
            "result" to result,
            "demand" to this.demand,
            "uses" to this.uses,
        )
    }

    fun refreshTrades(wrapper: EntityWrapper) {
        val villager = wrapper.entity as Villager
        villager.recipes.forEach {
            it.demand = 0
            it.uses = 0
        }
    }

    fun rerollTrades(wrapper: EntityWrapper) {
        val villager = wrapper.entity as Villager

        //if (villager.profession == Villager.Profession.CARTOGRAPHER) return

        mergeTrades(villager.recipes)

        val maxLevel = villager.villagerLevel
        val experience = villager.villagerExperience

        villager.villagerExperience = 0
        villager.villagerLevel = 1
        villager.recipes = mutableListOf()

        for (i in 1..maxLevel) {
            villager.villagerLevel = i
            villager.addTrades(2)
        }

        villager.villagerExperience = experience
        CustomEffects.playSound(villager.location, Sound.BLOCK_BAMBOO_BREAK, 1.0F, 1.0F)

        setTradeValues(villager.recipes)
    }

    private fun isSameTrade(oldRecipe: MerchantRecipe, newRecipe: MerchantRecipe): Boolean {

        val oldRecipeMaterials = Pair(
            oldRecipe.ingredients.getOrElse(0) { ItemStack(Material.AIR) },
            oldRecipe.ingredients.getOrElse(1) { ItemStack(Material.AIR) })
        val oldRecipeResult = oldRecipe.result

        val newRecipeMaterials = Pair(
            newRecipe.ingredients.getOrElse(0) { ItemStack(Material.AIR) },
            newRecipe.ingredients.getOrElse(1) { ItemStack(Material.AIR) })
        val newRecipeResult = newRecipe.result


        val recipePairs = listOf(
            Pair(oldRecipeMaterials.first, newRecipeMaterials.first),
            Pair(oldRecipeMaterials.second, newRecipeMaterials.second),
            Pair(oldRecipeResult, newRecipeResult),
        ).toMap()


        val materialPairs = recipePairs.toList().associate {
            Pair(it.first.type, it.second.type)
        }

        if (materialPairs.any { !isSameMaterialsOrTag(it.toPair()) }) return false

        recipePairs.toList().forEach { pair ->
            if (!isSameMetadata(pair)) return false
        }

        return true

    }
    private fun isSameMaterialsOrTag(materials: Pair<Material, Material>): Boolean {
        return (
                materials.first == materials.second ||
                arrayOf(Tag.ITEMS_BANNERS, Tag.ITEMS_BEDS, Tag.ITEMS_WOOL_CARPETS, Tag.ITEMS_WOOL, MaterialTags.STAINED_TERRACOTTA,
                    MaterialTags.STAINED_TERRACOTTA).any { it.isTagged(materials.first) && it.isTagged(materials.second) }
        )
    }
    private fun isSameMetadata(items: Pair<ItemStack, ItemStack>): Boolean {
        when (items.first.type) {

            Material.ENCHANTED_BOOK -> {
                return true
            }

            else -> {}
        }

        return true
    }
    private fun mergeTrades(trades: MutableList<MerchantRecipe>) {

        trades.forEach { newTrade ->
            val index = currentTrades.indexOfFirst { oldTrade -> isSameTrade(oldTrade, newTrade) }

            if (index != -1) currentTrades[index] = newTrade
            else currentTrades.add(newTrade)
        }

    }
    private fun setTradeValues(trades: MutableList<MerchantRecipe>)  {
        trades.forEach { newTrade ->
            val index = currentTrades.indexOfFirst { isSameTrade(it, newTrade) }

            if (index != -1) {
                newTrade.demand = currentTrades[index].demand
                newTrade.uses = currentTrades[index].uses
            }
        }
    }

}