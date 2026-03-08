package me.newburyminer.customItems.items.customs.materials

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class LightweightAmalgamation: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.LIGHTWEIGHT_AMALGAMATION

    private val material = Material.FEATHER
    private val color = arrayOf(235, 197, 237)
    private val name = text("Lightweight Amalgamation", color)
    private val lore = Utils.loreBlockToList(
        text("Used for trading with an overmax cleric.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

}