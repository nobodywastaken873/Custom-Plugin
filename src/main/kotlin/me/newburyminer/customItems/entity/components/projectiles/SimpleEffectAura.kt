package me.newburyminer.customItems.entity.components.projectiles

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.HomingSystem
import me.newburyminer.customItems.helpers.ParticleTheme
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import java.util.UUID

class SimpleEffectAura(
    val radius: Double,
    val height: Double,
    duration: Int,
    val effects: HitEffects,
    val applyPeriod: Int,
    val particleTheme: ParticleTheme,
    val summoner: Entity?
): EntityComponent {
    override fun serialize(): Map<String, Any> {
        return mapOf(
            "radius" to radius,
            "height" to height,
            "duration" to durationRemaining,
            "effects" to effects.serialize(),
            "apply_period" to applyPeriod,
            "particle_theme" to particleTheme.name,
            "summoner" to summoner?.uniqueId.toString()
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.SIMPLE_EFFECT_AURA
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            val uuid = map["uuid"]
            val newSummoner = if (uuid == null) null
                else Bukkit.getEntity(UUID.fromString(uuid as String))
            return SimpleEffectAura(
                map["radius"].asDouble(),
                map["height"].asDouble(),
                map["duration"].asInt(),
                HitEffects.deserialize(map["effects"]),
                map["apply_period"].asInt(),
                ParticleTheme.valueOf(map["particle_theme"].asString()),
                newSummoner
            )
        }
    }

    private val particleSettings = particleTheme.settings
    private var durationRemaining = duration

    override fun tick(wrapper: EntityWrapper) {
        if (durationRemaining != 0) durationRemaining-- // Duration of -1 will last forever
        else wrapper.entity.remove()

        if (wrapper.entity.ticksLived % applyPeriod == 0) {
            val toDamage =
                if (summoner is Player) wrapper.entity.location.getNearbyEntitiesByType(LivingEntity::class.java, radius)
                else wrapper.entity.location.getNearbyPlayers(radius)

            for (entity in toDamage) {
                val targetLoc = entity.location.toVector()
                val center = wrapper.entity.location.toVector()

                if (targetLoc.setY(0).subtract(center.setY(0)).length() <= radius &&
                    (center.y > targetLoc.y - height) && (center.y < targetLoc.y + entity.height)) {
                    effects.apply(entity, summoner ?: wrapper.entity, wrapper.entity.location)
                }

            }

            // Use/create better particle manager way of doing this, also for this use a filled circle instead of a ring
            CustomEffects.filledParticleCircle(particleSettings.particle, wrapper.entity.location, radius, particleSettings.concentration)
        }

        if (wrapper.entity.ticksLived % particleSettings.preParticleSeparation == 0) {
            CustomEffects.particleCircle(particleSettings.preParticle, wrapper.entity.location, radius, (2*Math.PI*radius*particleSettings.concentration).toInt())
        }
    }
}