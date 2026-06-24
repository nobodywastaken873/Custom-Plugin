package me.newburyminer.customItems.entity.components.defensive

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.utils.CooldownInterface
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomDamageType.Companion.isCustom
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.helpers.getIntersectingBlocks
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.util.Vector
import kotlin.math.sqrt
import kotlin.random.Random

class LeapDodgeComponent(
    private val dodgeRate: Double,
    private val baseCooldown: Int,
    private val jumpStrength: Double,
): EntityComponent, CooldownInterface {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "dodgeRate" to dodgeRate,
            "baseCooldown" to baseCooldown,
            "jumpStrength" to jumpStrength,
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.LEAP_DODGE_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return LeapDodgeComponent(
                map["dodgeRate"].asDouble(),
                map["baseCooldown"].asInt(),
                map["jumpStrength"].asDouble()
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
            if (!offCooldown()) return@register
            if (Math.random() > dodgeRate) return@register

            e.isCancelled = true
            CustomEffects.playSound(wrapper.entity.location, Sound.ENTITY_BREEZE_DEFLECT, 1.0F, 0.8F)

            val baseDirection = wrapper.entity.location.subtract(e.damager.location)
            val randomAngle = (Math.random() * 2 - 1) * Math.PI / 3
            val newDirection = baseDirection.toVector()
                .setY(0)
                .normalize()
                .rotateAroundY(randomAngle)
                .multiply(jumpStrength)

            val velocity = Vector(newDirection.x, jumpStrength, newDirection.z)
            wrapper.entity.velocity = velocity
            applyCooldown(baseCooldown)

            // Particle, sound effects
        })
    }

    override var cooldown: Int = baseCooldown / 2
    override fun tick(wrapper: EntityWrapper) {
        reduceCooldown(1)
    }

}