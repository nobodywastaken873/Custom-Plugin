package me.newburyminer.customItems.items.customs.materials.midgame

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class CondensedIce: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.CONDENSED_ICE

    private val material = Material.LIGHT_BLUE_CANDLE
    private val color = arrayOf(155, 191, 224)
    private val name = text("Condensed Ice", color)
    private val lore = Utils.loreBlockToList(
        text("Crafted from various frozen materials.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .build()

}