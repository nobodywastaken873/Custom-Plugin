package me.newburyminer.customItems.items.customs.tools.mining

import io.papermc.paper.registry.keys.tags.BlockTypeTagKeys
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class Axepick: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.AXEPICK

    private val material = Material.NETHERITE_PICKAXE
    private val color = arrayOf(96, 99, 40)
    private val name = text("Axepick", color)
    private val lore = Utils.loreBlockToList(
        text("Works as an axe and pickaxe when breaking blocks.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .tool(1, 1F,
            BlockTypeTagKeys.MINEABLE_AXE to 9F,
            BlockTypeTagKeys.MINEABLE_PICKAXE to 9F)
        .build()

    override fun handle(ctx: EventContext) {}

}