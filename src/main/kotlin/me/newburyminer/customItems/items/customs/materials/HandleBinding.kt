package me.newburyminer.customItems.items.customs.materials

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class HandleBinding: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.HANDLE_BINDING

    private val material = Material.STICK
    private val color = arrayOf(224, 195, 155)
    private val name = text("Handle Binding", color)
    private val lore = Utils.loreBlockToList(
        text("Used for trading with an overmax toolsmith.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

}