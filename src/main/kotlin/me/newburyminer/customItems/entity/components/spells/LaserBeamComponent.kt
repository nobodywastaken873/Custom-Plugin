package me.newburyminer.customItems.entity.components.spells

import me.newburyminer.customItems.Utils.Companion.getNearestPlayer
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.utils.AbstractSpellComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.helpers.getUpperCenter
import me.newburyminer.customItems.helpers.rayTraceEntity
import me.newburyminer.customItems.helpers.rayTraceManyEntities
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Mob
import org.bukkit.entity.Player

class LaserBeamComponent(
    private val range: Double,
    private val radius: Double,
    private val followPlayer: Boolean,
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
            "followPlayer" to followPlayer,
            "piercing" to piercing,
            "effects" to effects.serialize(),
            "castTime" to spellDuration,
            "baseCooldown" to baseCooldown,
            "particleTheme" to particleTheme.name
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.LASER_BEAM_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return LaserBeamComponent(
                map["range"].asDouble(),
                map["radius"].asDouble(),
                map["followPlayer"].asBoolean(),
                map["piercing"].asBoolean(),
                HitEffects.deserialize(map["effects"]),
                map["castTime"].asInt(),
                map["baseCooldown"].asInt(),
                ParticleTheme.valueOf(map["particleTheme"].asString())
            )
        }
    }

    private val particleSettings = particleTheme.settings
    private var targetLoc: Location? = null
    private var targetPlayer: Player? = null

    override fun tick(wrapper: EntityWrapper) {
        val caster = wrapper.entity as? Mob ?: return
        if (castingTicks > 0 && followPlayer && !(targetPlayer?.isValid ?: return)) {cancelCasting(wrapper); return}
        reduceCooldown(1)

        if (castingTicks > 0) {
            castingTicks -= 1

            if (followPlayer && !checkValidTarget(wrapper, targetPlayer)) {cancelCasting(wrapper); return}

            if (wrapper.entity.ticksLived % 4 == 0) {
                val direction = // Get the direction to the targetLoc or to the targetPlayer from eyes
                    ((if (followPlayer) targetPlayer?.getUpperCenter() else targetLoc) ?: return).clone()
                        .subtract(wrapper.entity.eyeLocation).toVector()
                CustomEffects.raycastParticleLine(
                    Particle.FIREWORK.builder(),
                    wrapper.entity.eyeLocation,
                    direction,
                    range,
                    4.0,
                    collideEntity = !piercing,
                    predicate = { it is Player }
                )
            }
            if (castingTicks <= 0) {
                val direction = // Get the direction to the targetLoc or to the targetPlayer from eyes
                    ((if (followPlayer) targetPlayer?.getUpperCenter() else targetLoc) ?: return).clone()
                        .subtract(wrapper.entity.eyeLocation).toVector()
                CustomEffects.raycastParticleLine(
                    Particle.BLOCK.builder().data(Material.AMETHYST_BLOCK.createBlockData()),
                    wrapper.entity.eyeLocation,
                    direction,
                    range,
                    3.0,
                    collideEntity = !piercing,
                    predicate = { it is Player }
                )

                val hitPlayers =
                    (if (piercing) wrapper.entity.world.rayTraceManyEntities(wrapper.entity.eyeLocation, direction, range, radius, {it is Player})
                    else listOf(wrapper.entity.world.rayTraceEntity(wrapper.entity.eyeLocation, direction, range, radius, {it is Player})))
                        .filterIsInstance<Player>()

                hitPlayers.forEach { effects.apply(it, wrapper.entity) }

                applyCooldown(baseCooldown)
                targetLoc = null
                targetPlayer = null
                CustomEffects.playSound(caster.location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3.0F, 1.7F)
            }
        }

        if (wrapper.entity.ticksLived % 10 == 0 && offCooldown()) {

            if (startCasting(wrapper)) {
                val target = (caster.target ?: caster.getNearestPlayer(range) ?: return) as? Player ?: return
                if (!caster.hasLineOfSight(target)) return

                if (followPlayer) targetPlayer = target
                else targetLoc = target.location.add(0.0, 1.33, 0.0)
                CustomEffects.playSound(caster.location, Sound.ENTITY_EVOKER_PREPARE_SUMMON, 3.0F, 1.7F)
            }

        }
    }
}