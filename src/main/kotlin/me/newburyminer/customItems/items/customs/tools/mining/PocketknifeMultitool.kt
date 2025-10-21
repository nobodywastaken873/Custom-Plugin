package me.newburyminer.customItems.items.customs.tools.mining

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class PocketknifeMultitool: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.POCKETKNIFE_MULTITOOL

    private val material = Material.SHEARS
    private val color = arrayOf(166, 166, 166)
    private val name = text("Pocketknife-multitool", color)
    private val lore = Utils.loreBlockToList(
        text("Right click while sneaking to cycle through shears, flint and steel, and a brush.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setUnbreakable()
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEvent -> {
                if (!ctx.itemType.isHand()) return
                val item = ctx.item ?: return
                val player = ctx.player ?: return
                if (!player.offCooldown(CustomItem.POCKETKNIFE_MULTITOOL)) return
                if (!player.isSneaking) return
                if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
                val toolNum = item.getTag<Int>("tool") ?: 0
                val newMat = when (toolNum) {
                    0 -> Material.SHEARS
                    1 -> Material.FLINT_AND_STEEL
                    2 -> Material.BRUSH
                    else -> Material.AIR
                }
                item.type = newMat
                item.setTag("tool", if (toolNum == 2) 0 else toolNum + 1)
                player.setCooldown(CustomItem.POCKETKNIFE_MULTITOOL, 0.5)
            }

        }

    }

}