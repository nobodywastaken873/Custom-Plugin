package me.newburyminer.customItems.items

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack

data class EventContext(
    val player: Player?,
    val item: ItemStack?,
    val itemType: EventItemType = EventItemType.MAINHAND,
    val event: Event,
    val sourceEntity: Entity? = null,
    val targetEntity: Entity? = null
)
