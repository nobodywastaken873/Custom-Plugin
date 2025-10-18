package me.newburyminer.customItems.items.customs.materials

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class WardenCarapace: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.WARDEN_CARAPACE

    private val material = Material.ECHO_SHARD
    private val color = arrayOf(1, 122, 133)
    private val name = text("Warden Carapace", color)
    private val lore = Utils.loreBlockToList(
        text("Crafted from The Warden drops.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {}

}