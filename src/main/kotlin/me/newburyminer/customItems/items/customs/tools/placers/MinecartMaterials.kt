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
import org.bukkit.event.entity.EntityPlaceEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack

class MinecartMaterials: CustomItemDefinition, ScrollCycler, MaterialPlacer {

    override val custom: CustomItem = CustomItem.MINECART_MATERIALS

    private val material = Material.MINECART
    private val color = arrayOf(92, 85, 81)
    private val name = text("Minecart Materials", color)
    private val lore = Utils.loreBlockToList(
        text("Redstone Placer:", arrayOf(199, 19, 6)),
        text("Consumes materials from your redstone box.", Utils.GRAY),
        text(""),
        text("While sneaking, scroll forward or back through your hotbar to cycle through all rails and minecarts.", Utils.GRAY),
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
            val item = e.player.inventory.getItem(e.previousSlot) ?: return@register
            if (scrollCycle(item, e)) CustomEffects.playSoundToPlayer(e.player, Sound.UI_BUTTON_CLICK, 1.0F, 1.1F)
        })

        register(BlockPlaceEvent::class, { e ->
            e.itemInHand.isItem(custom)
        },
        {e ->
            placeBlock(e, "Redstone Box")
        })

        register(EntityPlaceEvent::class, { e ->
            e.player?.inventory?.getItem(e.hand).isItem(custom)
        },
        {e ->
            placeEntity(e, "Redstone Box")
        })
    }

    override fun getCycleItems(item: ItemStack): Array<Material> {
        return arrayOf(Material.MINECART, Material.DETECTOR_RAIL, Material.RAIL, Material.POWERED_RAIL,
            Material.ACTIVATOR_RAIL, Material.HOPPER_MINECART, Material.CHEST_MINECART, Material.FURNACE_MINECART, Material.TNT_MINECART)
    }

}