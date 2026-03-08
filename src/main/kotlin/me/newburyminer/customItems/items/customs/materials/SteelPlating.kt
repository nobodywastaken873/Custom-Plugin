package me.newburyminer.customItems.items.customs.materials

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class SteelPlating: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.STEEL_PLATING

    private val material = Material.IRON_INGOT
    private val color = arrayOf(105, 106, 112)
    private val name = text("Steel Plating", color)
    private val lore = Utils.loreBlockToList(
        text("Traded from an overmax armorer, gotten using the Armorer Upgrade.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

}