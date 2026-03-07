package me.newburyminer.customItems.items.customs.materials

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class MoltenMixture: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MOLTEN_MIXTURE

    private val material = Material.MAGMA_CREAM
    private val color = arrayOf(186, 110, 4)
    private val name = text("Molten Mixture", color)
    private val lore = Utils.loreBlockToList(
        text("Used for trading with an overmax armorer.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

}