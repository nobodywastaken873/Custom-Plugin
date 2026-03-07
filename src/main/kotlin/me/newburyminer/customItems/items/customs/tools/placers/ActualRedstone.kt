package me.newburyminer.customItems.items.customs.tools.placers

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.items.behaviors.MaterialPlacer
import me.newburyminer.customItems.items.behaviors.ScrollCycler
import me.newburyminer.customItems.systems.materials.MaterialConverterRegistry
import me.newburyminer.customItems.systems.materials.MaterialSystem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Container
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class ActualRedstone: CustomItemDefinition, ScrollCycler, MaterialPlacer {

    override val custom: CustomItem = CustomItem.ACTUAL_REDSTONE

    private val material = Material.REDSTONE
    private val color = arrayOf(150, 23, 0)
    private val name = text("Actual Redstone", color)
    private val lore = Utils.loreBlockToList(
        text("Redstone Placer:", arrayOf(199, 19, 6)),
        text("Consumes materials from your redstone box.", Utils.GRAY),
        text(""),
        text("While sneaking, scroll forward or back through your hotbar to cycle through the basic redstone wiring components.", Utils.GRAY),
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

    /*override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is BlockPlaceEvent -> {
                placeBlock(e, "Redstone Box")
            }

            is PlayerItemHeldEvent -> {
                val player = ctx.player ?: return
                val item = ctx.item ?: return
                if (player.isSneaking) {
                    val successful = scrollCycle(item, e)
                    if (successful) CustomEffects.playSoundToPlayer(e.player, Sound.UI_BUTTON_CLICK, 1.0F, 1.1F)
                }
            }

        }

    }*/

    override fun getCycleItems(item: ItemStack): Array<Material> {
        return arrayOf(Material.REDSTONE, Material.REDSTONE_BLOCK, Material.REPEATER, Material.COMPARATOR,
            Material.REDSTONE_TORCH, Material.OBSERVER)
    }

}