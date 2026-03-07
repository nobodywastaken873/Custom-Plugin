package me.newburyminer.customItems.items.customs.materials

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ToolHandle: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.TOOL_HANDLE

    private val material = Material.STICK
    private val color = arrayOf(122, 118, 111)
    private val name = text("Tool Handle", color)
    private val lore = Utils.loreBlockToList(
        text("Traded from an overmax toolsmith, gotten using the Toolsmith Upgrade.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

}