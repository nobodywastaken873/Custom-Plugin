package me.newburyminer.customItems.items.customs.materials

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ReinforcedHandle: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.REINFORCED_HANDLE

    private val material = Material.STICK
    private val color = arrayOf(145, 137, 125)
    private val name = text("Reinforced Handle", color)
    private val lore = Utils.loreBlockToList(
        text("Traded from an overmax toolsmith, gotten using the Toolsmith Upgrade.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

}