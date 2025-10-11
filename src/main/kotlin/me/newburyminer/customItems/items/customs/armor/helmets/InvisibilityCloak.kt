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

class InvisibilityCloak: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.INVISIBILITY_CLOAK

    private val material = Material.NETHERITE_HELMET
    private val color = arrayOf(227, 231, 232)
    private val name = text("Invisibility Cloak", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+3.0HE","ART+3.0HE","KNR+0.1HE","MAH+4.0HE","ATD+4.0HE","ATS+0.2HE")

    override fun handle(ctx: EventContext) {}

}