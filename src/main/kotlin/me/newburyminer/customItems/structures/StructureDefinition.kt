package me.newburyminer.customItems.structures

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.beautify
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.loot.LootProvider
import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.loot.TableCreation
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobEntry
import me.newburyminer.customItems.mobprovider.MobProvider
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.components.CustomModelDataComponent

interface StructureDefinition {
    val id: String
    val name: String get() = id.beautify()

    val mobProvider: MobProvider

    val lootProvider: StructureLoot

    operator fun MobDefinition.times(multiplier: Double): MobEntry =
        MobEntry(this, multiplier)

    fun getKey(difficulty: StructureReference.Difficulty): ItemStack {

        val (diffInfo, color) = when (difficulty) {
            StructureReference.Difficulty.NORMAL -> Material.TRIAL_KEY to "Normal" to NamedTextColor.GOLD
            StructureReference.Difficulty.OMINOUS -> Material.OMINOUS_TRIAL_KEY to "Ominous" to NamedTextColor.DARK_AQUA
        }
        val (material, diffName) = diffInfo

        val diffId = when (difficulty) {
            StructureReference.Difficulty.NORMAL -> "normal"
            StructureReference.Difficulty.OMINOUS -> "ominous"
        }

        val item = ItemStack(material)
        item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addString("${id}_${diffId}_key"))
        item.name(Utils.text("$name $diffName Key").style(Style.style(color).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true)))

        return item
    }
}