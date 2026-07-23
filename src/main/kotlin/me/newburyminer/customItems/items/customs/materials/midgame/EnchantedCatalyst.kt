package me.newburyminer.customItems.items.customs.materials.midgame

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class EnchantedCatalyst: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ENCHANTED_CATALYST

    private val material = Material.YELLOW_DYE
    private val color = arrayOf(237, 230, 36)
    private val name = text("Enchanted Catalyst", color)
    private val lore = Utils.loreBlockToList(
        text("Crafted from various magical materials.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .build()

}