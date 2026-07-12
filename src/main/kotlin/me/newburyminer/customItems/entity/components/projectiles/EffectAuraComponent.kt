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
import java.util.UUID

class EffectAuraComponent(
    private val radius: Double,
    private val height: Double,
    duration: Int,
    startDelay: Int,
    private val anchor: Entity?,
    anchorTime: Int,
    private val effects: HitEffects,
    private val applyPeriod: Int,
    private val particleTheme: ParticleTheme,
    private val summoner: Entity?
): EntityComponent {
    override fun serialize(): Map<String, Any> {
        return mapOf(
            "radius" to radius,
            "height" to height,
            "duration" to durationRemaining,
            "start_time" to startDelayRemaining,
            "anchor" to anchor?.uniqueId.toString(),
            "anchor_time" to anchorTimeRemaining,
            "effects" to effects.serialize(),
            "apply_period" to applyPeriod,
            "particle_theme" to particleTheme.name,
            "summoner" to summoner?.uniqueId.toString()
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.EFFECT_AURA_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return EffectAuraComponent(
                map["radius"].asDouble(),
                map["height"].asDouble(),
                map["duration"].asInt(),
                map["start_time"].asInt(),
                fromNullUUID(map["anchor"]),
                map["anchor_time"].asInt(),
                HitEffects.deserialize(map["effects"]),
                map["apply_period"].asInt(),
                ParticleTheme.valueOf(map["particle_theme"].asString()),
                fromNullUUID(map["summoner"]),
            )
        }
    }

    private val particleSettings = particleTheme.settings
    private var startDelayRemaining = startDelay
    private var anchorTimeRemaining = anchorTime
    private var durationRemaining = duration

    override fun tick(wrapper: EntityWrapper) {
        if (durationRemaining != 0) durationRemaining-- // Duration of -1 will last forever
        else wrapper.entity.remove()

        if (startDelayRemaining > 0) startDelayRemaining--
        if (anchorTimeRemaining > 0) {
            anchorTimeRemaining--
            wrapper.entity.teleport(anchor ?: return)
        }

        if (startDelayRemaining <= 0 && wrapper.entity.ticksLived % applyPeriod == 0) {
            for (player in wrapper.entity.location.getNearbyPlayers(radius)) {
                val targetLoc = player.location.toVector()
                val center = wrapper.entity.location.toVector()

                if (targetLoc.setY(0).subtract(center.setY(0)).length() <= radius &&
                    (center.y > targetLoc.y - height) && (center.y < targetLoc.y + player.height)) {
                    effects.apply(player, summoner ?: wrapper.entity, wrapper.entity.location)
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