package me.newburyminer.customItems.entity.hiteffects

import me.newburyminer.customItems.helpers.DeserializationConversion

interface HitEffectDeserialization: DeserializationConversion {
    val componentType: HitEffectType
    fun deserialize(map: Map<String, Any>): HitEffect
}