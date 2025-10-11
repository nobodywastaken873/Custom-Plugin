package me.newburyminer.customItems.items.customs.armor.leggings

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

class Toolbelt: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.TOOLBELT

    private val material = Material.NETHERITE_LEGGINGS
    private val color = arrayOf(125, 63, 5)
    private val name = text("Toolbelt", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+6.0LE","ART+3.0LE","KNR+0.1LE","BLI+3.0LE")

    override fun handle(ctx: EventContext) {}

}