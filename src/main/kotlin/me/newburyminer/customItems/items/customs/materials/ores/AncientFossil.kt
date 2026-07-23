package me.newburyminer.customItems.items.customs.materials.ores

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class AncientFossil: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ANCIENT_FOSSIL

    private val material = Material.NAUTILUS_SHELL
    private val color = arrayOf(181, 167, 125)
    private val name = Utils.text("Ancient Fossil", color)
    private val lore = Utils.loreBlockToList(
        Utils.text("Found in the deep caves of the Arid Lands.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .build()

}