package me.newburyminer.customItems.entity

import org.bukkit.entity.Entity
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.safeCast

class EntityWrapper(val entity: Entity, private val components: MutableList<EntityComponent> = mutableListOf()) {

    init {
        components.forEach { component -> component.registerListeners(this) }
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
        component.registerListeners(this)
    }

    /*fun handle(ctx: EntityEventContext) {
        components.forEach {
            it.handle(ctx, this)
        }
    }*/

    fun tick() {
        components.forEach {
            it.tick(this)
        }
    }

    fun serialize(): Map<String, Any> {
        return components.associate {
            val companion = it::class.companionObjectInstance as DeserializationInterface
            companion.componentType.name to it.serialize()
        }
    }

    fun <T: EntityComponent> hasComponent(type: KClass<T>): Boolean {
        components.forEach {
            if (it::class == type)
                return true
        }
        return false
    }

    fun <T: EntityComponent> getComponents(type: KClass<T>): List<EntityComponent> {
        return components
            .filter {
                it::class == type
            }
    }

    fun <T: EntityComponent> getComponentsExtending(type: KClass<T>): List<T> {
        return components
            .mapNotNull {
                type.safeCast(it)
            }
    }

    private var isCasting = false
    fun isCasting(): Boolean {return isCasting}
    fun setCasting(newIsCasting: Boolean) {
        isCasting = newIsCasting
        if (newIsCasting) components.forEach {it.onCast(this)}
        else components.forEach {it.onFinishCast(this)}
    }


}