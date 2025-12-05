package me.newburyminer.customItems.items.customs.materials

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class SteelChunk: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.STEEL_CHUNK

    private val material = Material.IRON_NUGGET
    private val color = arrayOf(89, 90, 92)
    private val name = text("Steel Chunk", color)
    private val lore = Utils.loreBlockToList(
        text("Used for trading with an overmax armorer.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {}

}