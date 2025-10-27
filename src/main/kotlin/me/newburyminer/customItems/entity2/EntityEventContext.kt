package me.newburyminer.customItems.entity2

import org.bukkit.entity.Entity
import org.bukkit.event.Event

data class EntityEventContext(val entity: Entity, val event: Event) {
}