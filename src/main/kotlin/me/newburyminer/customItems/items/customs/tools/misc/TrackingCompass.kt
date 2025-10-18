package me.newburyminer.customItems.items.customs.tools.misc

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class TrackingCompass: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.TRACKING_COMPASS

    private val material = Material.COMPASS
    private val color = arrayOf(7, 121, 186)
    private val name = text("Tracking Compass", color)
    private val lore = Utils.loreBlockToList(
        text("Allows you to track other players, right click to open menu. Tracking a player will cost additional resources.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {}

}