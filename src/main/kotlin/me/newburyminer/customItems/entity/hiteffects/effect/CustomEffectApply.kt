package me.newburyminer.customItems.entity.hiteffects.effect

import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffectDeserialization
import me.newburyminer.customItems.entity.hiteffects.HitEffectType
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class CustomEffectApply(val type: CustomEffectType, val data: EffectData): HitEffect {

    override fun apply(victim: LivingEntity, damager: Entity, sourceLoc: Location?) {
        if (victim !is Player) return
        EffectManager.applyEffect(
            victim,
            type,
            data
        )
    }

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "type" to type.name,
            "data" to data.serialize()
        )
    }
    companion object: HitEffectDeserialization {
        override val componentType: HitEffectType = HitEffectType.CUSTOM_EFFECT
        override fun deserialize(map: Map<String, Any>): HitEffect {
            val type = CustomEffectType.valueOf(map["type"].toString())
            val data = EffectData.deserialize(map)
            return CustomEffectApply(type, data)
        }
    }
}