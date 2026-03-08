package me.newburyminer.customItems.entity.components

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityPotionEffectEvent

class DefaultEntityComponent: EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf()
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.DEFAULT_ENTITY_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return DefaultEntityComponent()
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(EntityDamageByEntityEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity &&
            (e.damageSource.causingEntity !is Player && e.damageSource.directEntity !is Player)
        },
        {e ->
            e.isCancelled = true
        })

        register(EntityPotionEffectEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity &&
            e.cause in arrayOf(EntityPotionEffectEvent.Cause.POTION_SPLASH, EntityPotionEffectEvent.Cause.AREA_EFFECT_CLOUD)
        },
        {e ->
            e.isCancelled = true
        })
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (e.damageSource.causingEntity is Player || e.damageSource.directEntity is Player) return
                e.isCancelled = true
            }

            is EntityPotionEffectEvent -> {
                if (e.cause !in arrayOf(EntityPotionEffectEvent.Cause.POTION_SPLASH, EntityPotionEffectEvent.Cause.AREA_EFFECT_CLOUD)) return
                e.isCancelled = true
            }

        }
    }*/
}