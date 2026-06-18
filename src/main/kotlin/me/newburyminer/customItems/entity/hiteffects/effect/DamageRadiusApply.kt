package me.newburyminer.customItems.entity.hiteffects.effect

import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.SimpleEffectAura
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffectDeserialization
import me.newburyminer.customItems.entity.hiteffects.HitEffectType
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.ParticleTheme
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Marker
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class DamageRadiusApply(
    val radius: Double,
    val height: Double,
    val effects: HitEffects,
    val particleTheme: ParticleTheme
): HitEffect {

    override fun apply(victim: LivingEntity, damager: Entity, sourceLoc: Location?) {

        val centerLoc = sourceLoc ?: return
        val toDamage =
            if (damager is Player) centerLoc.getNearbyEntitiesByType(LivingEntity::class.java, radius)
            else centerLoc.getNearbyPlayers(radius)

        for (entity in toDamage) {
            val targetLoc = entity.location.toVector()
            val center = centerLoc.toVector()

            if (targetLoc.setY(0).subtract(center.setY(0)).length() <= radius &&
                (center.y > targetLoc.y - height) && (center.y < targetLoc.y + entity.height)) {
                effects.apply(entity, damager, centerLoc)
            }

        }

        val particleSettings = particleTheme.settings
        CustomEffects.filledParticleCircle(particleSettings.particle, centerLoc, radius, particleSettings.concentration)

    }

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "radius" to radius,
            "height" to height,
            "effects" to effects.serialize(),
            "particle_theme" to particleTheme.name,
        )
    }
    companion object: HitEffectDeserialization {
        override val componentType: HitEffectType = HitEffectType.DAMAGE_RADIUS
        override fun deserialize(map: Map<String, Any>): HitEffect {
            return DamageRadiusApply(
                map["radius"].asDouble(),
                map["height"].asDouble(),
                HitEffects.deserialize(map["effects"]),
                ParticleTheme.valueOf(map["particle_theme"].asString())
            )
        }
    }
}