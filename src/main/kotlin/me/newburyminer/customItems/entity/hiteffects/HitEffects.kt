package me.newburyminer.customItems.entity.hiteffects

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

class HitEffects(private vararg val hitEffects: HitEffect) {

    fun apply(damaged: LivingEntity, damager: Entity) {
        hitEffects.forEach { it.apply(damaged, damager) }
    }

    fun serialize(): Map<String, Any> {
        return hitEffects.associate {
            it.hitEffectType.name to it.serialize()
        }
    }
    @Suppress("UNCHECKED_CAST")
    fun deserialize(map: Map<String, Any>): HitEffects? {
        val effects = map.map {
            val type = HitEffectType.valueOf(it.key)
            HitEffectSerializationRegistry.deserialize(type, it.value as Map<String, Any>) ?: return null
        }

        return HitEffects(*effects.toTypedArray())
    }

}