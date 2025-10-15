package me.newburyminer.customItems.items.customs.armor.leggings

import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class HermessTrousers: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.HERMESS_TROUSERS

    private val material = Material.NETHERITE_LEGGINGS
    private val color = arrayOf(145, 192, 219)
    private val name = text("Hermes's Trousers", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+7.0LE","ART+3.0LE","MOS+0.04LE","WAM+0.3LE","STH+1.0LE")

    override fun handle(ctx: EventContext) {}

}
