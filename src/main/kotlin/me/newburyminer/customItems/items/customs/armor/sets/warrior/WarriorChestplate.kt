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

class WarriorChestplate: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.WARRIOR_CHESTPLATE

    private val material = Material.NETHERITE_CHESTPLATE
    private val color = arrayOf(204, 116, 2)
    private val name = text("Warrior Chestplate", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+9.0CH","ART+4.0CH","KNR+0.15CH","MAH+1.5CH","ATD+2.0CH")
        .setArmorSet(ArmorSet.WARRIOR)

    override fun handle(ctx: EventContext) {}

}
