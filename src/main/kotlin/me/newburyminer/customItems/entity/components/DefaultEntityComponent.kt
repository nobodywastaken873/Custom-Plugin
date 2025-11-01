package me.newburyminer.customItems.entity.components

import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class DefaultEntityComponent: EntityComponent {
    override val componentType: EntityComponentType = EntityComponentType.DEFAULT_ENTITY_COMPONENT

    override fun serialize(): Map<String, Any> {
        return mapOf()
    }
    override fun deserialize(map: Map<String, Any>): EntityComponent {
        return DefaultEntityComponent()
    }

    override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (e.damageSource.causingEntity is Player || e.damageSource.directEntity is Player) return

                e.isCancelled = true

            }

        }
    }
}