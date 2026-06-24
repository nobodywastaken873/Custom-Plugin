package me.newburyminer.customItems.entity.velocity

import me.newburyminer.customItems.helpers.DeserializationConversion

interface VelocityProviderDeserialization: DeserializationConversion {
    fun deserialize(map: Map<String, Any?>): VelocityProvider
}