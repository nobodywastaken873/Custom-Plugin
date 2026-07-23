package me.newburyminer.customItems.items.customs.materials.midgame

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class EnrichedFeather: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ENRICHED_FEATHER

    private val material = Material.FEATHER
    private val color = arrayOf(185, 148, 224)
    private val name = text("Enriched Feather", color)
    private val lore = Utils.loreBlockToList(
        text("Crafted from various flighty materials.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .build()

}