package me.newburyminer.customItems.recipes

import io.papermc.paper.datacomponent.DataComponentTypes
import me.newburyminer.customItems.Utils.Companion.basePotion
import me.newburyminer.customItems.Utils.Companion.ench
import me.newburyminer.customItems.Utils.Companion.horn
import me.newburyminer.customItems.Utils.Companion.omimous
import me.newburyminer.customItems.Utils.Companion.storeEnch
import me.newburyminer.customItems.items.CustomEnchantments
import org.bukkit.Material
import org.bukkit.MusicInstrument
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.*
import org.bukkit.potion.PotionType

class RecipeItem(material: Material, count: Int = 1): RecipeItemBase {

    private val item = ItemStack(material, count)
    private val flags = mutableMapOf(
        "enchantments" to false,
        "ominous_bottle" to false,
        "stored_enchant" to false,
        "armor_trim" to false,
        "potion" to false,
        "goat_horn" to false,
        "original" to false
    )

    override fun matches(other: ItemStack?): Boolean {
        if (other == null) return false
        if (other.type != item.type) return false
        if (other.amount < item.amount) return false
        val activeFlags = flags.filterValues { it }.keys.toList()
        val allFlagsMatch = checkFlags(other, activeFlags)
        return allFlagsMatch
    }

    private fun checkFlags(other: ItemStack, activeFlags: List<String>): Boolean {
        activeFlags.forEach { when (it) {

            "enchantments" -> {
                for ((key, value) in item.enchantments)
                    if (other.getEnchantmentLevel(key) < value) return false
            }

            "ominous_bottle" -> {
                val otherAmplifier = other.getData(DataComponentTypes.OMINOUS_BOTTLE_AMPLIFIER) ?: return false
                val thisAmplifier = item.getData(DataComponentTypes.OMINOUS_BOTTLE_AMPLIFIER) ?: return false
                if (otherAmplifier.amplifier() != thisAmplifier.amplifier()) return false
            }

            "stored_enchant" -> {

                val otherEnchantsData = other.getData(DataComponentTypes.STORED_ENCHANTMENTS) ?: return false
                val thisEnchantsData = item.getData(DataComponentTypes.STORED_ENCHANTMENTS) ?: return false

                val otherEnchants = otherEnchantsData.enchantments()
                val thisEnchants = thisEnchantsData.enchantments()

                for (enchant in thisEnchants.keys)
                    if (otherEnchants[enchant] != thisEnchants[enchant])
                        return false
            }

            "armor_trim" -> {
                val otherTrimData = other.getData(DataComponentTypes.TRIM) ?: return false
                val thisTrimData = item.getData(DataComponentTypes.TRIM) ?: return false

                if (otherTrimData.armorTrim() != thisTrimData.armorTrim()) return false
            }

            "potion" -> {
                val otherPotionData = other.getData(DataComponentTypes.POTION_CONTENTS) ?: return false
                val thisPotionData = item.getData(DataComponentTypes.POTION_CONTENTS) ?: return false

                if (otherPotionData.potion() != thisPotionData.potion()) return false
            }

            "goat_horn" -> {
                val otherHornData = other.getData(DataComponentTypes.INSTRUMENT) ?: return false
                val thisHornData = item.getData(DataComponentTypes.INSTRUMENT) ?: return false

                if (otherHornData != thisHornData) return false
            }

            "original" -> {
                if (item.getEnchantmentLevel(CustomEnchantments.DUPLICATE) != other.getEnchantmentLevel(CustomEnchantments.DUPLICATE))
                    return false
            }

        } }
        return true
    }

    override fun getItem(): ItemStack {
        return item.clone()
    }

    fun ench(vararg enchantments: String): RecipeItem {
        enchantments.forEach { item.ench(it) }
        flags["enchantments"] = true
        return this
    }

    fun storeEnch(vararg enchantments: String): RecipeItem {
        enchantments.forEach { item.storeEnch(it) }
        flags["stored_enchant"] = true
        return this
    }

    fun setOminous(amount: Int): RecipeItem {
        item.omimous(amount)
        flags["ominous_bottle"] = true
        return this
    }

    fun setPotion(potionType: PotionType): RecipeItem {
        item.basePotion(potionType)
        flags["potion"] = true
        return this
    }

    fun goatHorn(instrument: MusicInstrument): RecipeItem {
        item.horn(instrument)
        flags["goat_horn"] = true
        return this
    }

    fun checkOriginal(): RecipeItem {
        flags["original"] = true
        return this
    }

}