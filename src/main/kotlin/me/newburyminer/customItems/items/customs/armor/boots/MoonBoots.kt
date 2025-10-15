package me.newburyminer.customItems.items.customs.armor.boots

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

class MoonBoots: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MOON_BOOTS

    private val material = Material.NETHERITE_BOOTS
    private val color = arrayOf(191, 218, 245)
    private val name = text("Moon Boots", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("GRA-0.84%FE", "JUS0.05FE", "SAF20FE","ART+3.0FE","ARM+3.0FE","KNR+0.1FE")

    override fun handle(ctx: EventContext) {}

}
