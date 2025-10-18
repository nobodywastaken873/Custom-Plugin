package me.newburyminer.customItems.items.customs.tools.placers

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.gui.GuiInventory
import me.newburyminer.customItems.gui.MaterialsHolder
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.systems.materials.MaterialCategory
import me.newburyminer.customItems.systems.materials.MaterialSystem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class RedstoneBox: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.REDSTONE_BOX

    private val material = Material.REDSTONE_BLOCK
    private val color = arrayOf(196, 18, 41)
    private val name = text("Redstone Box", color)
    private val lore = Utils.loreBlockToList(
        text("Right click in your inventory to open up material storage for redstone materials.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is InventoryClickEvent -> {
                val player = ctx.player ?: return
                if (e.inventory.holder is GuiInventory) return
                if (e.action != InventoryAction.PICKUP_HALF) return
                e.isCancelled = true
                CustomEffects.playSound(player.location, Sound.BLOCK_SHULKER_BOX_OPEN, 0.5F, (1.0F - Math.random() * 0.1F).toFloat())
                Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                    player.closeInventory()
                    player.openInventory(MaterialsHolder(MaterialSystem.getMaterials(player), MaterialCategory.REDSTONE).inventory)
                })
            }

        }

    }

}