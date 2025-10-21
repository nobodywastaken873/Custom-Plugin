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
import org.bukkit.Tag
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class NetheriteMultitool: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.NETHERITE_MULTITOOL

    private val material = Material.NETHERITE_PICKAXE
    private val color = arrayOf(89, 14, 7)
    private val name = text("Netherite Multitool", color)
    private val lore = Utils.loreBlockToList(
        text("Right click while sneaking to cycle through a netherite pickaxe, axe, shovel, and hoe.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEvent -> {
                val item = ctx.item ?: return
                val player = ctx.player ?: return
                if (!player.offCooldown(CustomItem.NETHERITE_MULTITOOL)) return
                if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
                // Hoe block because it doesnt fucking work
                else if (item.type == Material.NETHERITE_HOE && e.action == Action.RIGHT_CLICK_BLOCK &&
                    Tag.DIRT.isTagged(e.clickedBlock!!.type) && !player.isSneaking &&
                    player.world.getBlockAt(e.clickedBlock!!.location.add(0.0, 1.0, 0.0)).type == Material.AIR) {
                    e.clickedBlock!!.type = Material.FARMLAND
                }
                if (!player.isSneaking) return
                val toolNum = item.getTag<Int>("tool") ?: 0
                val newMat = when (toolNum) {
                    0 -> Material.NETHERITE_PICKAXE
                    1 -> Material.NETHERITE_AXE
                    2 -> Material.NETHERITE_SHOVEL
                    3 -> Material.NETHERITE_HOE
                    else -> Material.AIR
                }
                item.type = newMat
                item.setTag("tool", if (toolNum == 3) 0 else toolNum + 1)
                player.setCooldown(CustomItem.NETHERITE_MULTITOOL, 0.5)
            }

        }

    }

}