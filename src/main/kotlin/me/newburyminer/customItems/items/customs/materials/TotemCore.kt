package me.newburyminer.customItems.items.customs.materials

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class TotemCore: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.TOTEM_CORE

    private val material = Material.GOLD_INGOT
    private val color = arrayOf(255, 225, 0)
    private val name = text("Totem Core", color)
    private val lore = Utils.loreBlockToList(
        text("Material, does not work as a totem.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {}

}