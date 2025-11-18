package me.newburyminer.customItems.entity

interface DeserializationInterface {
    val componentType: EntityComponentType
    fun deserialize(map: Map<String, Any>): EntityComponent?
}