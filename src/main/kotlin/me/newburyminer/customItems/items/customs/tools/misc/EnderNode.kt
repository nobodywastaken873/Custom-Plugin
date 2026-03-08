package me.newburyminer.customItems.items.customs.tools.misc

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isBeingTracked
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.gui.CustomGui
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

class EnderNode: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ENDER_NODE

    private val material = Material.POPPED_CHORUS_FRUIT
    private val nameColor = arrayOf(5, 105, 81)
    private val name = text("Ender Node", nameColor)
    private val lore = Utils.loreBlockToList(text("Right click in your inventory to open up your ender chest.", Utils.GRAY))

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(InventoryClickEvent::class, { e ->
            e.whoClicked is Player &&
            !(e.whoClicked as Player).isBeingTracked() &&
            e.action == InventoryAction.PICKUP_HALF &&
            e.inventory.type != InventoryType.ENDER_CHEST &&
            e.inventory.holder !is CustomGui &&
            e.clickedInventory?.getItem(e.slot).isItem(custom)
        }, 
        {e ->
            val player = e.whoClicked as Player
            CustomEffects.playSoundToPlayer(player, Sound.BLOCK_ENDER_CHEST_OPEN, 0.5F, (1.0F - Math.random() * 0.1F).toFloat())
            e.isCancelled = true
            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                player.closeInventory()
                player.openInventory(player.enderChest)
            })
        })
    }

}