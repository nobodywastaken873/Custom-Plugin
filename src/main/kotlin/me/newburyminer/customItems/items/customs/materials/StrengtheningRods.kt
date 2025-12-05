package me.newburyminer.customItems.items.customs.materials

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class StrengtheningRods: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.STRENGTHENING_RODS

    private val material = Material.IRON_CHAIN
    private val color = arrayOf(130, 131, 135)
    private val name = text("Strengthening Rods", color)
    private val lore = Utils.loreBlockToList(
        text("Used for trading with an overmax toolsmith.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {}

}