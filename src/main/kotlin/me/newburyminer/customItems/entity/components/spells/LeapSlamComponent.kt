package me.newburyminer.customItems.entity.components.spells

import me.newburyminer.customItems.Utils.Companion.getNearestPlayer
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.ArcingEffectProjectile
import me.newburyminer.customItems.entity.components.utils.AbstractSpellComponent
import me.newburyminer.customItems.entity.components.utils.CooldownInterface
import me.newburyminer.customItems.entity.components.utils.LeapingInterface
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Sound
import org.bukkit.entity.FallingBlock
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import kotlin.math.pow

class LeapSlamComponent(
    private val minDistance: Double,
    private val extraHeight: Double,
    private val effects: HitEffects,
    baseCooldown: Int,
    castTime: Int
): AbstractSpellComponent(baseCooldown, castTime), LeapingInterface {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "minDistance" to minDistance,
            "extraHeight" to extraHeight,
            "effects" to effects.serialize(),
            "baseCooldown" to baseCooldown,
            "spellDuration" to spellDuration
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.LEAP_SLAM_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return LeapSlamComponent(
                map["minDistance"].asDouble(),
                map["extraHeight"].asDouble(),
                HitEffects.deserialize(map["effects"]),
                map["baseCooldown"].asInt(),
                map["spellDuration"].asInt(),
            )
        }
    }

    private var targetPlayer: Player? = null
    private var wasOnGround = true
    private var peakHeight = -1000.0

    override fun tick(wrapper: EntityWrapper) {
        val caster = wrapper.entity as? Mob ?: return
        reduceCooldown(1)

        if (caster.isOnGround) {
            if (!wasOnGround && peakHeight - caster.location.y > 3.0) {
                effects.applyTargetless(caster, caster.location)
                CustomEffects.playSound(caster.location, Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.3F)
            }
            wasOnGround = true
            peakHeight = -1000.0
        }
        else {
            wasOnGround = false
            peakHeight = caster.location.y.coerceAtLeast(peakHeight)
        }

        if (castingTicks > 0) {
            castingTicks -= 1

            if (!checkValidTarget(wrapper, targetPlayer)) {cancelCasting(wrapper); return}

            if (castingTicks <= 0) {
                caster.velocity = calculateLeapVelocity(caster.location, targetPlayer?.location ?: return, extraHeight)
                applyCooldown(baseCooldown)
                CustomEffects.playSound(wrapper.entity.location, Sound.ENTITY_BREEZE_JUMP, 1.0F, 0.8F)
                targetPlayer = null
            }
        }

        if (wrapper.entity.ticksLived % 10 == 0 && offCooldown()) {

            if (startCasting(wrapper)) {
                val target = (caster.target ?: caster.getNearestPlayer(minDistance + 5) ?: return) as? Player ?: return
                if (!caster.hasLineOfSight(target)) return

                targetPlayer = target
            }

        }
    }
}