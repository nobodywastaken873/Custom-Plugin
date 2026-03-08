package me.newburyminer.customItems.items.customs.materials

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class CondensedDeepslate: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.CONDENSED_DEEPSLATE

    private val material = Material.BLACK_CANDLE
    private val color = arrayOf(68, 73, 94)
    private val name = text("Condensed Deepslate", color)
    private val lore = Utils.loreBlockToList(
        text("Crafted from a lot of deepslate.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

}