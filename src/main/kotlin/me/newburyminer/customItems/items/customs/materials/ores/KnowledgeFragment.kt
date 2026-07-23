package me.newburyminer.customItems.items.customs.materials.ores

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class KnowledgeFragment: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.KNOWLEDGE_FRAGMENT

    private val material = Material.NETHERITE_SCRAP
    private val color = arrayOf(125, 95, 72)
    private val name = Utils.text("Knowledge Fragment", color)
    private val lore = Utils.loreBlockToList(
        Utils.text("Found in the deep caves of the Arid Lands.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setLore(lore)
        .build()

}