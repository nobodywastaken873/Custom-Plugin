package me.newburyminer.customItems.entity

import me.newburyminer.customItems.Utils.Companion.setTag
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.event.Event

interface CustomEntityDefinition {

    val baseEntity: EntityType
    val customEntity: CustomEntity
    val spawnWeight: Int

    fun convert(entity: Entity) {
        entity.setTag("id", customEntity.name)
    }

    fun handle(ctx: EntityEventContext) {}
    val tasks: MutableMap<Int, (Entity) -> Unit>
        get() = mutableMapOf()
}