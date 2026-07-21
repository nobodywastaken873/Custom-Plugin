package me.newburyminer.customItems.entity.components.spells

import me.newburyminer.customItems.Utils.Companion.applyDamage
import me.newburyminer.customItems.Utils.Companion.containsLoc
import me.newburyminer.customItems.Utils.Companion.getNearestPlayer
import me.newburyminer.customItems.Utils.Companion.rotateToAxis
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.utils.AbstractSpellComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.helpers.arcTraceManyEntities
import me.newburyminer.customItems.helpers.getCenterLoc
import me.newburyminer.customItems.helpers.getUpperCenter
import me.newburyminer.customItems.helpers.rayTraceEntity
import me.newburyminer.customItems.helpers.rayTraceManyEntities
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.util.UUID
import kotlin.math.cos
import kotlin.math.sin

class SlashSpellComponent(
    private val radius: Double,
    private val spreadAngle: Double,
    private val count: Int,
    private val delay: Int,
    private val dashStrength: Double,
    private val effects: HitEffects,
    castTime: Int,
    baseCooldown: Int,
    private val particleTheme: ParticleTheme
): AbstractSpellComponent(baseCooldown, castTime + delay * count) {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "radius" to radius,
            "spreadAngle" to spreadAngle,
            "count" to count,
            "delay" to delay,
            "dashStrength" to dashStrength,
            "effects" to effects.serialize(),
            "castTime" to spellDuration,
            "baseCooldown" to baseCooldown,
            "particleTheme" to particleTheme.name
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.SLASH_SPELL_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return SlashSpellComponent(
                map["radius"].asDouble(),
                map["spreadAngle"].asDouble(),
                map["count"].asInt(),
                map["delay"].asInt(),
                map["dashStrength"].asDouble(),
                HitEffects.deserialize(map["effects"]),
                map["castTime"].asInt(),
                map["baseCooldown"].asInt(),
                ParticleTheme.valueOf(map["particleTheme"].asString())
            )
        }
    }

    private val particleSettings = particleTheme.settings
    private var targetPlayer: Player? = null

    override fun tick(wrapper: EntityWrapper) {
        val caster = wrapper.entity as? Mob ?: return
        reduceCooldown(1)

        if (castingTicks > 0) {
            castingTicks -= 1

            if (!checkValidTarget(wrapper, targetPlayer)) {cancelCasting(wrapper); return}
            val ticksTillFirst = castingTicks - delay * (count - 1)

            if (ticksTillFirst <= 0 && ticksTillFirst % delay == 0) {
                val direction = (targetPlayer ?: return).getCenterLoc().subtract(caster.eyeLocation).toVector()
                if (direction.length() > radius) {
                    caster.velocity = caster.velocity.add(direction.add(Vector(0, 1, 0)).normalize().multiply(dashStrength))
                }

                val hitPlayers = caster.world.arcTraceManyEntities(
                    caster.eyeLocation, direction, radius, spreadAngle, {it is Player}
                ).filterIsInstance<Player>()
                CustomEffects.rotatedArc(Particle.CRIT.builder(), caster.eyeLocation, radius, spreadAngle, 6.0, direction)

                hitPlayers.forEach { effects.apply(it, wrapper.entity) }
                CustomEffects.playSound(caster.location, Sound.ENTITY_WITHER_SHOOT, 1.0F, 1.7F)

                if (castingTicks == 0) {
                    applyCooldown(baseCooldown)
                    targetPlayer = null
                }
            }
        }

        if (wrapper.entity.ticksLived % 10 == 0 && offCooldown()) {

            if (startCasting(wrapper)) {
                val target = caster.target as? Player ?: return
                if (!caster.hasLineOfSight(target)) return

                targetPlayer = target
                CustomEffects.playSound(caster.location, Sound.ENTITY_EVOKER_PREPARE_SUMMON, 1.0F, 1.7F)
            }

        }
    }
}