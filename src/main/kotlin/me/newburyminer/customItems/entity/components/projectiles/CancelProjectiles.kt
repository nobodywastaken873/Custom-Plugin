package me.newburyminer.customItems.entity.components.projectiles

import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import org.bukkit.event.entity.ProjectileLaunchEvent

class CancelProjectiles: EntityComponent {
    override val componentType: EntityComponentType = EntityComponentType.CANCEL_PROJECTILES

    override fun serialize(): Map<String, Any> {
        return mapOf()
    }
    override fun deserialize(map: Map<String, Any>): EntityComponent {
        return CancelProjectiles()
    }

    override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {
                e.isCancelled = true
            }

        }
    }
}