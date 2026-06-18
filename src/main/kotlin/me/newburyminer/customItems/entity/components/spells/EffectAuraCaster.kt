package me.newburyminer.customItems.entity.components.spells

import me.newburyminer.customItems.Utils.Companion.getNearestPlayer
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.EffectAuraComponent
import me.newburyminer.customItems.entity.components.projectiles.MagicMissileComponent
import me.newburyminer.customItems.entity.components.utils.AbstractSpellComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.HomingSystem
import me.newburyminer.customItems.helpers.ParticleTheme
import org.bukkit.entity.Entity
import org.bukkit.entity.Marker
import org.bukkit.entity.Mob
import org.bukkit.entity.Player

class EffectAuraCaster(
    private val radius: Double,
    private val height: Double,
    private val duration: Int,
    private val startDelay: Int,
    private val anchorTime: Int,
    private val effects: HitEffects,
    private val applyPeriod: Int,
    private val particleTheme: ParticleTheme,
    castTime: Int,
    baseCooldown: Int,
    private val range: Double,
): AbstractSpellComponent(baseCooldown, castTime) {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "radius" to radius,
            "height" to height,
            "duration" to duration,
            "startDelay" to startDelay,
            "anchorTime" to anchorTime,
            "effects" to effects.serialize(),
            "applyPeriod" to applyPeriod,
            "particleTheme" to particleTheme,
            "castTime" to spellDuration,
            "baseCooldown" to baseCooldown,
            "range" to range,
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.EFFECT_AURA_CASTER
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return EffectAuraCaster(
                map["radius"].asDouble(),
                map["height"].asDouble(),
                map["duration"].asInt(),
                map["startDelay"].asInt(),
                map["anchorTime"].asInt(),
                HitEffects.deserialize(map["effects"]),
                map["applyPeriod"].asInt(),
                ParticleTheme.valueOf(map["particleTheme"].asString()),
                map["castTime"].asInt(),
                map["baseCooldown"].asInt(),
                map["range"].asDouble(),
            )
        }
    }

    private var targetPlayer: Player? = null

    override fun tick(wrapper: EntityWrapper) {
        val caster = wrapper.entity as? Mob ?: return
        reduceCooldown(1)

        if (castingTicks > 0) {
            castingTicks -= 1

            if (!checkValidTarget(wrapper, targetPlayer)) {cancelCasting(wrapper); return}

            if (castingTicks <= 0) {
                caster.world.spawn(targetPlayer?.location ?: return, Marker::class.java) {
                    val newWrapper = EntityWrapperManager.getWrapperorNew(it)
                    newWrapper.addComponent(EffectAuraComponent(
                        radius,
                        height,
                        duration,
                        startDelay,
                        targetPlayer ?: return@spawn,
                        anchorTime,
                        effects,
                        applyPeriod,
                        particleTheme,
                        wrapper.entity
                    ))
                }
                applyCooldown(baseCooldown)
                targetPlayer = null
            }
        }

        if (wrapper.entity.ticksLived % 10 == 0 && offCooldown()) {

            if (startCasting(wrapper)) {
                val target = (caster.target ?: caster.getNearestPlayer(range) ?: return) as? Player ?: return
                if (!caster.hasLineOfSight(target)) return

                targetPlayer = target
            }

        }
    }
}