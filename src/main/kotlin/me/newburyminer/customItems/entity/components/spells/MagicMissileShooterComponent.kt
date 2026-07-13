package me.newburyminer.customItems.entity.components.spells

import me.newburyminer.customItems.Utils.Companion.getNearestPlayer
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.MagicMissileComponent
import me.newburyminer.customItems.entity.components.utils.AbstractSpellComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.velocity.VelocityProvider
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.HomingSystem
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.helpers.getUpperCenter
import org.bukkit.Sound
import org.bukkit.entity.Marker
import org.bukkit.entity.Mob
import org.bukkit.entity.Player

class MagicMissileShooterComponent(
    private val range: Double,
    private val size: Double,
    private val velocityProvider: VelocityProvider,
    private val effects: HitEffects,
    castTime: Int,
    baseCooldown: Int,
    private val particleTheme: ParticleTheme
): AbstractSpellComponent(baseCooldown, castTime) {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "range" to range,
            "size" to size,
            "velocityProvider" to velocityProvider.serialize(),
            "effects" to effects.serialize(),
            "castTime" to spellDuration,
            "baseCooldown" to baseCooldown,
            "particleTheme" to particleTheme.name
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.MAGIC_MISSILE_SHOOTER_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return MagicMissileShooterComponent(
                map["range"].asDouble(),
                map["size"].asDouble(),
                VelocityProvider.deserialize(map["velocityProvider"]),
                HitEffects.deserialize(map["effects"]),
                map["castTime"].asInt(),
                map["baseCooldown"].asInt(),
                ParticleTheme.valueOf(map["particleTheme"].asString())
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
                caster.world.spawn(caster.eyeLocation, Marker::class.java) {
                    val wrapper = EntityWrapperManager.getWrapperorNew(it)
                    wrapper.addComponent(MagicMissileComponent(
                        size,
                        velocityProvider,
                        VelocityProvider.getValidStartVelocity(caster.eyeLocation, targetPlayer?.getUpperCenter() ?: return@spawn, velocityProvider),
                        effects,
                        particleTheme,
                        caster,
                        targetPlayer
                    ))
                }
                applyCooldown(baseCooldown)
                targetPlayer = null

                CustomEffects.playSound(caster.location, Sound.ENTITY_SKELETON_SHOOT, 1.5F, 1.6F)
            }
        }

        if (offCooldown()) {

            if (startCasting(wrapper)) {
                val target = (caster.target ?: caster.getNearestPlayer(range) ?: return) as? Player ?: return
                if (!caster.hasLineOfSight(target)) return
                CustomEffects.playSound(caster.location, Sound.ITEM_BOTTLE_FILL_DRAGONBREATH, 1.0F, 1.5F)

                targetPlayer = target
            }

        }
    }
}