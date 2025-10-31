package me.newburyminer.customItems.entity

import org.bukkit.entity.Entity

class EntityWrapper(val entity: Entity, private val components: MutableList<EntityComponent> = mutableListOf()) {


    @Suppress("UNCHECKED_CAST")
    companion object {
        fun deserialize(map: Map<String, Any>, entity: Entity): EntityWrapper? {

            val components = map.map {
                val type = EntityComponentType.valueOf(it.key)
                ComponentSerializationRegistry.deserialize(type, it.value as Map<String, Any>) ?: return null
            }.toMutableList()

            return EntityWrapper(entity, components)

        }
    }

    fun addComponent(component: EntityComponent) {
        components += component
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