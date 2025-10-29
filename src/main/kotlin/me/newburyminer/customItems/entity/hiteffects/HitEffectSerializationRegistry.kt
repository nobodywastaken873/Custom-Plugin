package me.newburyminer.customItems.entity.hiteffects

object HitEffectSerializationRegistry {

    private val registry = mutableMapOf<HitEffectType, HitEffect>()

    fun register(type: HitEffectType, component: HitEffect) {
        registry[type] = component
    }

    fun deserialize(type: HitEffectType, map: Map<String, Any>): HitEffect? {

        val deserializer = registry[type] ?: return null
        return deserializer.deserialize(map)

    }

}