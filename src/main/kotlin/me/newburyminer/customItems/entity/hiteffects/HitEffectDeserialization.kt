package me.newburyminer.customItems.entity.hiteffects

import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType

interface HitEffectDeserialization {
    val componentType: HitEffectType
    fun deserialize(map: Map<String, Any>): HitEffect
}