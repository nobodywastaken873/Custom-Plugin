package me.newburyminer.customItems.entity.components.spells

import me.newburyminer.customItems.Utils.Companion.getNearestPlayer
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.ArcingEffectProjectile
import me.newburyminer.customItems.entity.components.projectiles.MagicMissileComponent
import me.newburyminer.customItems.entity.components.utils.AbstractSpellComponent
import me.newburyminer.customItems.entity.components.utils.LeapingInterface
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.HomingSystem
import me.newburyminer.customItems.helpers.ParticleTheme
import org.bukkit.Material
import org.bukkit.entity.FallingBlock
import org.bukkit.entity.Marker
import org.bukkit.entity.Mob
import org.bukkit.entity.Player

class ArcingProjectileShooterComponent(
    private val range: Double,
    private val peakHeight: Double,
    private val block: Material,
    private val effects: HitEffects,
    castTime: Int,
    baseCooldown: Int
): AbstractSpellComponent(baseCooldown, castTime), LeapingInterface {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "range" to range,
            "peakHeight" to peakHeight,
            "block" to block.name,
            "effects" to effects.serialize(),
            "castTime" to spellDuration,
            "baseCooldown" to baseCooldown,
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.ARCING_PROJECTILE_SHOOTER_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return ArcingProjectileShooterComponent(
                map["range"].asDouble(),
                map["peakHeight"].asDouble(),
                Material.valueOf(map["block"].asString()),
                HitEffects.deserialize(map["effects"]),
                map["castTime"].asInt(),
                map["baseCooldown"].asInt(),
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
                caster.world.spawn(caster.eyeLocation, FallingBlock::class.java) {
                    val newWrapper = EntityWrapperManager.getWrapperorNew(it)
                    newWrapper.addComponent(ArcingEffectProjectile(
                        effects,
                        caster
                    ))

                    it.blockData = block.createBlockData()
                    it.velocity = calculateLeapVelocity(it.location, targetPlayer?.location ?: return@spawn, peakHeight)
                    it.cancelDrop = true
                    it.dropItem = false
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