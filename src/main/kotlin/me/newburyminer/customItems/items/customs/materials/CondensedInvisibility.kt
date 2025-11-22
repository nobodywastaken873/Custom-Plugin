package me.newburyminer.customItems.items.customs.materials

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class CondensedInvisibility: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.CONDENSED_INVISIBILITY

    private val material = Material.DIORITE
    private val color = arrayOf(218, 222, 240)
    private val name = text("Condensed Invisibility", color)
    private val lore = Utils.loreBlockToList(
        text("Crafted from a lot of invisbility potions.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {}

}