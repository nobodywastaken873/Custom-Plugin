package me.newburyminer.customItems.items.customs.materials

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class CloudFragment: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.CLOUD_FRAGMENT

    private val material = Material.SUGAR
    private val color = arrayOf(204, 234, 235)
    private val name = text("Cloud Fragment", color)
    private val lore = Utils.loreBlockToList(
        text("Crafted from various lightweight materials.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

}