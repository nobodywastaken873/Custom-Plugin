package me.newburyminer.customItems.items.customs.tools.placers

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.behaviors.MaterialPlacer
import me.newburyminer.customItems.items.behaviors.ScrollCycler
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack

class Containers: CustomItemDefinition, ScrollCycler, MaterialPlacer {

    override val custom: CustomItem = CustomItem.CONTAINERS

    private val material = Material.BARREL
    private val color = arrayOf(163, 5, 5)
    private val name = text("Containers", color)
    private val lore = Utils.loreBlockToList(
        text("Redstone Placer:", arrayOf(199, 19, 6)),
        text("Consumes materials from your redstone box.", Utils.GRAY),
        text(""),
        text("While sneaking, scroll forward or back through your hotbar to cycle through all containers and pistons.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(PlayerItemHeldEvent::class, { e ->
            e.player.inventory.getItem(e.previousSlot).isItem(custom) &&
            e.player.isSneaking
        },
        {e ->
            if (scrollCycle(item, e)) CustomEffects.playSoundToPlayer(e.player, Sound.UI_BUTTON_CLICK, 1.0F, 1.1F)
        })

        register(BlockPlaceEvent::class, { e ->
            e.itemInHand.isItem(custom)
        },
        {e ->
            placeBlock(e, "Redstone Box")
        })
    }

    override fun getCycleItems(item: ItemStack): Array<Material> {
        return arrayOf(Material.BARREL, Material.HOPPER, Material.CHEST, Material.CRAFTER,
            Material.DISPENSER, Material.DROPPER, Material.NOTE_BLOCK, Material.PISTON, Material.STICKY_PISTON, Material.SLIME_BLOCK)
    }

}