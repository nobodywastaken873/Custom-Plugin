package me.newburyminer.customItems.items.customs.armor.sets.assassin

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

class AssassinsRobe: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ASSASSINS_ROBE

    private val material = Material.NETHERITE_CHESTPLATE
    private val color = arrayOf(32, 2, 112)
    private val name = text("Assassin's Robe", color)
    private val lore = Utils.loreBlockToList(
        text("Gain an additive 1/8 chance to dodge arrows.", Utils.GRAY),
        text(""),
        text("Full Set Bonus (4 pieces): Assassin's Set", Utils.GRAY),
        text("Gain permanent invisibility. Gain +4% movement speed, 0.6 attack damage, 0.02 attack speed, and -3% scale every second for up to 10 seconds. Resets upon taking damage.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+6.0CH","ART+2.0CH","ATD+2.0CH","MOS+0.005CH","ATS+0.05CH")
        .setArmorSet(ArmorSet.ASSASSIN)

    override fun handle(ctx: EventContext) {}

}
