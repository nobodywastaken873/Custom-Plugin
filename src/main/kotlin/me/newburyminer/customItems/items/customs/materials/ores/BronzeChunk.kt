package me.newburyminer.customItems.items.customs.materials.ores

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class BronzeChunk: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.BRONZE_CHUNK

    private val material = Material.COPPER_NUGGET
    private val color = arrayOf(212, 141, 28)
    private val name = Utils.text("Bronze Chunk", color)
    private val lore = Utils.loreBlockToList(
        Utils.text("Found in the deep caves of the Arid Lands.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .build()

}