package me.newburyminer.customItems.items.customs.materials

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class FireResistantResin: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.FIRE_RESISTANT_RESIN

    private val material = Material.RESIN_CLUMP
    private val color = arrayOf(128, 110, 91)
    private val name = text("Fire-resistant Resin", color)
    private val lore = Utils.loreBlockToList(
        text("Traded from an overmax armorer, gotten using the Armorer Upgrade.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {}

}