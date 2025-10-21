package me.newburyminer.customItems.entity

import org.bukkit.entity.Entity

class EntityTask(val task: (Entity) -> Unit) {
    fun runTask(entity: Entity) {
        task(entity)
    }
}