package me.newburyminer.customItems.entity3

import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.entity.EntityEventContext
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType

interface CustomEntityDefinition {

    val baseEntity: EntityType
    val customEntity: CustomEntity

    fun convert(entity: Entity) {
        entity.setTag("id", customEntity.name)
    }

    fun handle(ctx: EntityEventContext) {}
    val tasks: MutableMap<Int, (Entity) -> Unit>
        get() = mutableMapOf()
}