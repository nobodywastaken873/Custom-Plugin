package me.newburyminer.customItems.items.customs.armor.sets.tank

import me.newburyminer.customItems.Utils
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
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class HardHat: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.HARD_HAT

    private val material = Material.NETHERITE_HELMET
    private val color = arrayOf(245, 218, 66)
    private val name = text("Hard Hat", color)
    private val lore = Utils.loreBlockToList(
        text(""),
        text("Full Set Bonus (4 pieces): Tank Set", Utils.GRAY),
        text("Sneak to gain 10 absorption hearts, with a 60 second cooldown.", Utils.GRAY)
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+7.0HE","ART+4.0HE","KNR+0.2HE","MAH+2.0HE")
        .setArmorSet(ArmorSet.TANK)

    override fun handle(ctx: EventContext) {
    }

}
