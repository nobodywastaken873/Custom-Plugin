package me.newburyminer.customItems.entity

import me.newburyminer.customItems.entity.components.DefaultEntityComponent
import org.bukkit.entity.Entity
import kotlin.reflect.KClass

class EntityWrapper(val entity: Entity, private val components: MutableList<EntityComponent> = mutableListOf()) {

    init {
        components.add(0, DefaultEntityComponent())
    }

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

    fun <T: EntityComponent> hasComponent(type: KClass<T>): Boolean {
        components.forEach {
            if (it::class == type)
                return true
        }
        return false
    }

    private var isCasting = false
    fun isCasting(): Boolean {return isCasting}
    fun setCasting(newIsCasting: Boolean) {isCasting = newIsCasting}


}