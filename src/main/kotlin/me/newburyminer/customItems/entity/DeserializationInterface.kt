package me.newburyminer.customItems.entity

import me.newburyminer.customItems.helpers.DeserializationConversion

interface DeserializationInterface: DeserializationConversion {
    val componentType: EntityComponentType
    fun deserialize(map: Map<String, Any>): EntityComponent?
}