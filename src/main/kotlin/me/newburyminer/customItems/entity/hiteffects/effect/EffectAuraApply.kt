package me.newburyminer.customItems.entity.hiteffects.effect

import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.SimpleEffectAura
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffectDeserialization
import me.newburyminer.customItems.entity.hiteffects.HitEffectType
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.ParticleTheme
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Marker
import org.bukkit.util.Vector

class EffectAuraApply(
    val radius: Double,
    val height: Double,
    val duration: Int,
    val effects: HitEffects,
    val applyPeriod: Int,
    val particleTheme: ParticleTheme
): HitEffect {

    override fun apply(victim: LivingEntity, damager: Entity, sourceLoc: Location?) {

        damager.world.spawn(sourceLoc ?: return, Marker::class.java) {
            val newWrapper = EntityWrapperManager.getWrapperorNew(it)
            newWrapper.addComponent(
                SimpleEffectAura(radius, height, duration, effects, applyPeriod, particleTheme, damager)
            )
        }

    }

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "radius" to radius,
            "height" to height,
            "duration" to duration,
            "effects" to effects.serialize(),
            "applyPeriod" to applyPeriod,
            "particle_theme" to particleTheme.name,
        )
    }
    companion object: HitEffectDeserialization {
        override val componentType: HitEffectType = HitEffectType.EFFECT_AURA
        override fun deserialize(map: Map<String, Any>): HitEffect {
            return EffectAuraApply(
                map["radius"].asDouble(),
                map["height"].asDouble(),
                map["duration"].asInt(),
                HitEffects.deserialize(map["effects"]),
                map["applyPeriod"].asInt(),
                ParticleTheme.valueOf(map["particle_theme"].asString())
            )
        }
    }
}