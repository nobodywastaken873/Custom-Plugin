package me.newburyminer.customItems.items.customs.armor.sets.warrior

import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.setArmorSet
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.armorsets.ArmorSet
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class WarriorBoots: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.WARRIOR_BOOTS

    private val material = Material.NETHERITE_BOOTS
    private val color = arrayOf(204, 116, 2)
    private val name = text("Warrior Boots", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+4.0FE","ART+4.0FE", "KNR+0.15FE","MAH+1.5FE","ATD+1.0FE")
        .setArmorSet(ArmorSet.WARRIOR)

    override fun handle(ctx: EventContext) {}

}
