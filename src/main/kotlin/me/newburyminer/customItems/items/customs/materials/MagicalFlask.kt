package me.newburyminer.customItems.items.customs.materials

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class MagicalFlask: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MAGICAL_FLASK

    private val material = Material.AMETHYST_SHARD
    private val color = arrayOf(112, 38, 117)
    private val name = text("Magical Flask", color)
    private val lore = Utils.loreBlockToList(
        text("Used for trading with an overmax cleric.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

}