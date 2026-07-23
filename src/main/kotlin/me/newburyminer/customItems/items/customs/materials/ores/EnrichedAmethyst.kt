package me.newburyminer.customItems.items.customs.materials.ores

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class EnrichedAmethyst: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ENRICHED_AMETHYST

    private val material = Material.AMETHYST_SHARD
    private val color = arrayOf(101, 8, 158)
    private val name = Utils.text("Enriched Amethyst", color)
    private val lore = Utils.loreBlockToList(
        Utils.text("Found in the deep caves of the Arid Lands.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .build()

}