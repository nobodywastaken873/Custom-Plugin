package me.newburyminer.customItems.items.customs.materials

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class MegamixDiscTrack: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MEGAMIX_DISC_TRACK

    private val material = Material.MUSIC_DISC_11
    private val color = arrayOf(17, 166, 89)
    private val name = text("Megamix Disc Track", color)
    private val lore = Utils.loreBlockToList(
        text("Material made from all creeper-dropped music discs.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

}