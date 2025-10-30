package me.newburyminer.customItems.entity.hiteffects.effect

import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffectType
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class CustomEffectApply(val type: CustomEffectType, val data: EffectData): HitEffect {
    override val hitEffectType: HitEffectType = HitEffectType.CUSTOM_EFFECT

    override fun apply(victim: LivingEntity, damager: Entity) {
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
    override fun deserialize(map: Map<String, Any>): HitEffect {
        val type = CustomEffectType.valueOf(map["type"] as String)
        val data = EffectData.deserialize(map)
        return CustomEffectApply(type, data)
    }
}