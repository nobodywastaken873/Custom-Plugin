package me.newburyminer.customItems.items.customs.materials

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ShadowDiscCore: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.SHADOW_DISC_CORE

    private val material = Material.MUSIC_DISC_5
    private val color = arrayOf(1, 122, 133)
    private val name = text("Shadow Disc Core", color)
    private val lore = Utils.loreBlockToList(
        text("Crafted from music discs found in the ancient city.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {}

}