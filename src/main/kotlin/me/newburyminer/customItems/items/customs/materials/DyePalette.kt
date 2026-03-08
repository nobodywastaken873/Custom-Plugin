package me.newburyminer.customItems.items.customs.materials

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class DyePalette: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.DYE_PALETTE

    private val material = Material.PINK_DYE
    private val color = arrayOf(210, 104, 212)
    private val name = text("Dye Palette", color)
    private val lore = Utils.loreBlockToList(
        text("Material made from all dyes.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

}