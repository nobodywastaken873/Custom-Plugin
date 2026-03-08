package me.newburyminer.customItems.entity.components

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import org.bukkit.Material
import org.bukkit.event.entity.EntityDeathEvent

class LavaOnDeath: EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf()
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.LAVA_ON_DEATH
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return LavaOnDeath()
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(EntityDeathEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->
            val locToSpawn = e.entity.location
            locToSpawn.block.type = Material.LAVA
        })
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityDeathEvent -> {
                val locToSpawn = e.entity.location
                locToSpawn.block.type = Material.LAVA
            }

        }
    }*/
}