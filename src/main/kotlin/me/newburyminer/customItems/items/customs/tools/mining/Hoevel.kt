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

class Hoevel: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.HOEVEL

    private val material = Material.NETHERITE_SHOVEL
    private val color = arrayOf(99, 45, 40)
    private val name = text("Hoevel", color)
    private val lore = Utils.loreBlockToList(
        text("Works as a shovel and hoe when breaking blocks.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .tool(1, 1F,
            BlockTypeTagKeys.MINEABLE_HOE to 9F,
            BlockTypeTagKeys.MINEABLE_SHOVEL to 9F)
        .build()

    override fun handle(ctx: EventContext) {}

}