package me.newburyminer.customItems.entity

import org.bukkit.entity.LivingEntity

class EntityWrapper(private val entity: LivingEntity, private val components: List<EntityComponent>) {


    @Suppress("UNCHECKED_CAST")
    companion object {
        fun deserialize(map: Map<String, Any>, entity: LivingEntity): EntityWrapper? {

            val components = map.map {
                val type = EntityComponentType.valueOf(it.key)
                ComponentSerializationRegistry.deserialize(type, it.value as Map<String, Any>) ?: return null
            }

            return EntityWrapper(entity, components)

        }
    }

    fun handle(ctx: EntityEventContext) {
        components.forEach {
            it.handle(ctx, this)
        }
    }

    fun tick() {
        components.forEach {
            it.tick(this)
        }
    }

    fun serialize(): Map<String, Any> {
        return components.associate {
            it.componentType.name to it.serialize()
        }
    }

}