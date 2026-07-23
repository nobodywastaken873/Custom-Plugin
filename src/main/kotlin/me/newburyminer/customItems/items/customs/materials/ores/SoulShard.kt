package me.newburyminer.customItems.items.customs.materials.ores

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class SoulShard: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.SOUL_SHARD

    private val material = Material.QUARTZ
    private val color = arrayOf(188, 204, 207)
    private val name = Utils.text("Soul Shard", color)
    private val lore = Utils.loreBlockToList(
        Utils.text("Found in the deep caves of the Arid Lands.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .build()

}