package me.newburyminer.customItems.entity.components.defensive

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomDamageType.Companion.isCustom
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.ParticleTheme
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.Vector
import kotlin.math.sqrt

class DamageBlockerComponent(
    private val shieldCount: Int,
    private val regenRate: Int,
    private val particleTheme: ParticleTheme
): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "shieldCount" to shieldCount,
            "regenRate" to regenRate,
            "particleTheme" to particleTheme.name
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.DAMAGE_BLOCKER_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return DamageBlockerComponent(
                map["shieldCount"].asInt(),
                map["regenRate"].asInt(),
                ParticleTheme.valueOf(map["particleTheme"].asString())
            )
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(
            EntityDamageByEntityEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->
            if (e.isCancelled) return@register
            if (currentShieldCount == 0) return@register
            currentShieldCount--
            e.isCancelled = true
            CustomEffects.playSound(wrapper.entity.location, Sound.ENTITY_BREEZE_DEFLECT, 1.0F, 0.8F)
        })
    }

    private var currentShieldCount = shieldCount
    private val particleSettings = particleTheme.settings

    override fun tick(wrapper: EntityWrapper) {
        if (wrapper.entity.ticksLived % regenRate == 0) {
            currentShieldCount = (currentShieldCount + 1).coerceAtMost(shieldCount)
        }

        if (wrapper.entity.ticksLived % 4 == 0 && currentShieldCount > 0) {

            val height = wrapper.entity.height

            val separation = height / currentShieldCount
            for (i in 0..<currentShieldCount) {

                CustomEffects.particleCircle(
                    Particle.OMINOUS_SPAWNING.builder(),
                    wrapper.entity.location.add(0.0, separation / 2 + separation * i, 0.0),
                    wrapper.entity.width / 2 * sqrt(2.0),
                    4.0
                )

            }

        }
    }

}