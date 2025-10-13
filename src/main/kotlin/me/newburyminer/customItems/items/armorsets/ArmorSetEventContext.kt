package me.newburyminer.customItems.items.armorsets

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack

data class ArmorSetEventContext(
    val player: Player,
    val event: Event,
    val pieceCount: Int,
    val sourceEntity: Entity? = null,
    val targetEntity: Entity? = null
)
