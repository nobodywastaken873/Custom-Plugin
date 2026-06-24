package me.newburyminer.customItems.entity.hiteffects.effect

import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.MagicMissileComponent
import me.newburyminer.customItems.entity.components.projectiles.SimpleEffectAura
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffectDeserialization
import me.newburyminer.customItems.entity.hiteffects.HitEffectType
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.velocity.VelocityProvider
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.helpers.getNearestLivingEntity
import me.newburyminer.customItems.helpers.getNearestPlayer
import me.newburyminer.customItems.helpers.getUpperCenter
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Marker
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class MagicMissileApply(
    val count: Int,
    val size: Double,
    val velocityProvider: VelocityProvider,
    val effects: HitEffects,
    val particleTheme: ParticleTheme
): HitEffect {

    override fun apply(victim: LivingEntity, damager: Entity, sourceLoc: Location?) {

        val realVictim =
            if (victim.getTag<Boolean>("faketarget") == true)
                if (damager is Player) (sourceLoc ?: return).getNearestLivingEntity(50.0) ?: return
                else (sourceLoc ?: return).getNearestPlayer(50.0) ?: return
            else victim

        val shooter = damager as? LivingEntity ?: return
        for (i in 0..<count) {
            damager.world.spawn(sourceLoc ?: return, Marker::class.java) {
                val newWrapper = EntityWrapperManager.getWrapperorNew(it)
                newWrapper.addComponent(
                    MagicMissileComponent(
                        size,
                        velocityProvider,
                        VelocityProvider.getValidStartVelocity(damager.eyeLocation, realVictim.getUpperCenter(), velocityProvider),
                        effects,
                        particleTheme,
                        shooter,
                        realVictim
                    )
                )
            }
        }
    }

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "count" to count,
            "size" to size,
            "velocityProvider" to velocityProvider.serialize(),
            "effects" to effects.serialize(),
            "particle_theme" to particleTheme.name,
        )
    }
    companion object: HitEffectDeserialization {
        override val componentType: HitEffectType = HitEffectType.MAGIC_MISSILE
        override fun deserialize(map: Map<String, Any>): HitEffect {
            return MagicMissileApply(
                map["count"].asInt(),
                map["size"].asDouble(),
                VelocityProvider.deserialize(map["velocityProvider"]),
                HitEffects.deserialize(map["effects"]),
                ParticleTheme.valueOf(map["particle_theme"].asString())
            )
        }
    }
}