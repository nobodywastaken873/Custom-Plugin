package me.newburyminer.customItems.recipes

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
        if (other.amount != item.amount) return false
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
                val otherMeta = other.itemMeta as? OminousBottleMeta ?: return false
                val thisMeta = item.itemMeta as OminousBottleMeta
                if (!thisMeta.hasAmplifier() || !otherMeta.hasAmplifier()) return false
                if (thisMeta.amplifier != otherMeta.amplifier) return false
            }

            "stored_enchant" -> {
                val otherMeta = other.itemMeta as? EnchantmentStorageMeta ?: return false
                val thisMeta = item.itemMeta as EnchantmentStorageMeta
                if (!thisMeta.hasStoredEnchants() || !otherMeta.hasStoredEnchants()) return false
                for (enchant in thisMeta.storedEnchants.keys)
                    if (otherMeta.storedEnchants[enchant] != thisMeta.storedEnchants[enchant])
                        return false
            }

            "armor_trim" -> {
                val otherMeta = other.itemMeta as? ArmorMeta ?: return false
                val thisMeta = item.itemMeta as ArmorMeta
                if (!thisMeta.hasTrim() || !otherMeta.hasTrim()) return false
                val otherTrim = otherMeta.trim ?: return false
                val thisTrim = thisMeta.trim ?: return false
                if (thisTrim.material != otherTrim.material || thisTrim.pattern != otherTrim.pattern) return false
            }

            "potion" -> {
                val otherMeta = other.itemMeta as? PotionMeta ?: return false
                val thisMeta = item.itemMeta as PotionMeta
                if (!thisMeta.hasBasePotionType() || !otherMeta.hasBasePotionType()) return false
                if (thisMeta.basePotionType != otherMeta.basePotionType) return false
            }

            "goat_horn" -> {
                val otherMeta = other.itemMeta as? MusicInstrumentMeta ?: return false
                val thisMeta = item.itemMeta as MusicInstrumentMeta
                if (thisMeta.instrument != otherMeta.instrument) return false
            }

            "original" -> {
                if (item.getEnchantmentLevel(CustomEnchantments.DUPLICATE) != other.getEnchantmentLevel(CustomEnchantments.DUPLICATE))
                    return false
            }

        } }
        return true
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