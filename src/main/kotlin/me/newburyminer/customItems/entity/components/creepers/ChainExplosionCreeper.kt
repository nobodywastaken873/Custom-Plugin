package me.newburyminer.customItems.entity.components.creepers

import me.newburyminer.customItems.entity.*
import org.bukkit.entity.Creeper
import org.bukkit.event.entity.EntityDamageByEntityEvent

class ChainExplosionCreeper: EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf()
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.CHAIN_EXPLOSION_CREEPER
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return ChainExplosionCreeper()
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(EntityDamageByEntityEvent::class, wrapper.entity.uniqueId, { e ->
            e.damager == wrapper.entity
        },
        {e ->
            val creeper = e.entity as? Creeper ?: return@register
            e.isCancelled = true
            val wrapper = EntityWrapperManager.getWrapperorNew(creeper)
            if (!creeper.isIgnited) {
                wrapper.addComponent(ChainExplosionCreeper())
                creeper.fuseTicks = (20 * Math.random()).toInt()
                creeper.ignite()
            }
        })
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (e.damager != wrapper.entity) return
                val creeper = e.entity as? Creeper ?: return
                val wrapper = EntityWrapperManager.getWrapperorNew(creeper)
                if (!creeper.isIgnited) {
                    wrapper.addComponent(ChainExplosionCreeper())
                    creeper.fuseTicks = (20 * Math.random()).toInt()
                    creeper.ignite()
                }
            }

        }
    }*/
}