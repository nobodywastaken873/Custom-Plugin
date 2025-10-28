package me.newburyminer.customItems.entity

object ComponentSerializationRegistry {

    private val registry = mutableMapOf<EntityComponentType, EntityComponent>()

    fun register(type: EntityComponentType, component: EntityComponent) {
        registry[type] = component
    }

    fun deserialize(type: EntityComponentType, map: Map<String, Any>): EntityComponent? {

        val deserializer = registry[type] ?: return null
        return deserializer.deserialize(map)

    }

}