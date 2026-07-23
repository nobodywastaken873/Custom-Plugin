package me.newburyminer.customItems.items.customs.materials.midgame

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ToolHandle: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.TOOL_HANDLE

    private val material = Material.STICK
    private val color = arrayOf(122, 118, 111)
    private val name = text("Tool Handle", color)
    private val lore = Utils.loreBlockToList(
        text("Crafted from various binding materials.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .build()

}