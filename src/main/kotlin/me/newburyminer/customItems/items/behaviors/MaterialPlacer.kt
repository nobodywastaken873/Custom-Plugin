package me.newburyminer.customItems.items.behaviors

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.systems.materials.MaterialConverterRegistry
import me.newburyminer.customItems.systems.materials.MaterialSystem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Container
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityPlaceEvent
import org.bukkit.inventory.ItemStack

interface MaterialPlacer {

    fun placeBlock(e: BlockPlaceEvent, box: String) {
        val item = e.itemInHand
        val player = e.player

        val collection = MaterialConverterRegistry.convert(item)
        if (collection == null) {e.isCancelled = true; return}

        if (!MaterialSystem.hasMaterials(player, collection)) {
            e.isCancelled = true
            e.player.sendActionBar(text("Not enough materials in your ${box}.", Utils.FAILED_COLOR))
            CustomEffects.playSoundToPlayer(e.player, Sound.ENTITY_VILLAGER_NO, 1F, 0.8F)
            return
        }

        MaterialSystem.removeMaterials(player, collection)

        val slot = if (e.itemInHand == e.player.inventory.itemInMainHand) e.player.inventory.heldItemSlot else 40
        val savedItem = ItemStack(e.itemInHand).clone()
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            if (e.player.inventory.getItem(slot)?.type == Material.AIR || e.player.inventory.getItem(slot) == null) {
                e.player.inventory.setItem(slot, savedItem)
            } else {
                e.player.addItemorDrop(savedItem)
            }
            if (e.blockPlaced.state is Container) {
                val newState = (e.block.state as Container)
                newState.customName(null)
                newState.update()
            }
        })
    }
    fun placeEntity(e: EntityPlaceEvent, box: String) {
        val player = e.player ?: return
        val item = player.inventory.getItem(e.hand)

        val collection = MaterialConverterRegistry.convert(item)
        if (collection == null) {e.isCancelled = true; return}

        if (!MaterialSystem.hasMaterials(player, collection)) {
            e.isCancelled = true
            player.sendActionBar(text("Not enough materials in your ${box}.", Utils.FAILED_COLOR))
            CustomEffects.playSoundToPlayer(player, Sound.ENTITY_VILLAGER_NO, 1F, 0.8F)
            return
        }

        MaterialSystem.removeMaterials(player, collection)

        val slot = if (item == player.inventory.itemInMainHand) player.inventory.heldItemSlot else 40
        val savedItem = ItemStack(item).clone()
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            if (player.inventory.getItem(slot)?.type == Material.AIR || player.inventory.getItem(slot) == null)
                player.inventory.setItem(slot, savedItem)
            else
                player.addItemorDrop(savedItem)

            e.entity.customName(null)
        })
    }

}