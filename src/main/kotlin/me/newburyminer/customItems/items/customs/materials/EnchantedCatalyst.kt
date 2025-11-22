package me.newburyminer.customItems.items.customs.materials

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class EnchantedCatalyst: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ENCHANTED_CATALYST

    private val material = Material.YELLOW_DYE
    private val color = arrayOf(237, 230, 36)
    private val name = text("Enchanted Catalyst", color)
    private val lore = Utils.loreBlockToList(
        text("Traded from an overmax cleric, gotten using the Cleric Upgrade.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {}

}