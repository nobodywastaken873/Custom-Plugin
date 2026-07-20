package me.newburyminer.customItems.entity.components.spells

import me.newburyminer.customItems.Utils.Companion.getNearestPlayer
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.utils.AbstractSpellComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.HomingSystem
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.helpers.getUpperCenter
import me.newburyminer.customItems.helpers.rayTraceEntity
import me.newburyminer.customItems.helpers.rayTraceManyEntities
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class TrackingBeamComponent(
    private val range: Double,
    private val radius: Double,
    private val angleChange: Double,
    private val damageDelay: Int,
    private val piercing: Boolean,
    private val effects: HitEffects,
    castTime: Int,
    baseCooldown: Int,
    private val particleTheme: ParticleTheme
): AbstractSpellComponent(baseCooldown, castTime) {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "range" to range,
            "radius" to radius,
            "angleChange" to angleChange,
            "damageDelay" to damageDelay,
            "piercing" to piercing,
            "effects" to effects.serialize(),
            "castTime" to spellDuration,
            "baseCooldown" to baseCooldown,
            "particleTheme" to particleTheme.name
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.TRACKING_BEAM_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return TrackingBeamComponent(
                map["range"].asDouble(),
                map["radius"].asDouble(),
                map["angleChange"].asDouble(),
                map["damageDelay"].asInt(),
                map["piercing"].asBoolean(),
                HitEffects.deserialize(map["effects"]),
                map["castTime"].asInt(),
                map["baseCooldown"].asInt(),
                ParticleTheme.valueOf(map["particleTheme"].asString())
            )
        }
    }

    private val particleSettings = particleTheme.settings
    private var targetPlayer: Player? = null
    private var currentDirection: Vector? = null

    override fun tick(wrapper: EntityWrapper) {
        val caster = wrapper.entity as? Mob ?: return
        reduceCooldown(1)

        if (castingTicks > 0) {
            castingTicks -= 1

            if (!checkValidTarget(wrapper, targetPlayer)) {cancelCasting(wrapper); return}
            val targetDirection = targetPlayer?.getUpperCenter()?.subtract(caster.eyeLocation)?.toVector() ?: return
            val distance = targetDirection.length()
            val newDirection = HomingSystem.aggressiveScalingTurn(currentDirection ?: return, targetDirection, angleChange, distance, 0.4)
            currentDirection = newDirection

            if (wrapper.entity.ticksLived % 3 == 0) {
                CustomEffects.raycastParticleLine(
                    Particle.ELECTRIC_SPARK.builder(),
                    wrapper.entity.eyeLocation,
                    currentDirection ?: return,
                    range,
                    4.0,
                    collideEntity = !piercing,
                    predicate = { it is Player }
                )
            }

            if (wrapper.entity.ticksLived % 3 == 0) {
                CustomEffects.playSound(caster.location, Sound.BLOCK_BEACON_AMBIENT, 6.0F, 0.5F)
            }

            if (wrapper.entity.ticksLived % damageDelay == 0) {
                val hitPlayers =
                    (if (piercing) wrapper.entity.world.rayTraceManyEntities(wrapper.entity.eyeLocation, currentDirection ?: return, range, radius, {it is Player})
                    else listOf(wrapper.entity.world.rayTraceEntity(wrapper.entity.eyeLocation, currentDirection ?: return, range, radius, {it is Player})))
                        .filterIsInstance<Player>()

                hitPlayers.forEach { effects.apply(it, wrapper.entity) }
            }

            if (castingTicks <= 0) {
                applyCooldown(baseCooldown)
                targetPlayer = null
                currentDirection = null
            }
        }

        if (wrapper.entity.ticksLived % 10 == 0 && offCooldown()) {

            if (startCasting(wrapper)) {
                val target = (caster.target ?: caster.getNearestPlayer(range) ?: return) as? Player ?: return

                targetPlayer = target
                currentDirection = caster.eyeLocation.direction
                CustomEffects.playSound(caster.location, Sound.ENTITY_EVOKER_CAST_SPELL, 3.0F, 0.5F)
            }

        }
    }
}