package me.newburyminer.customItems.items.customs.armor.helmets

import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.inventory.ItemStack

class MinersHelm: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MINERS_HELM

    private val material = Material.NETHERITE_HELMET
    private val color = arrayOf(122, 119, 69)
    private val name = text("Miner's Helm", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+4.0HE","ART+4.0HE","KNR+0.1HE","MIE+10.0HE")

    override fun handle(ctx: EventContext) {}

}