package me.newburyminer.customItems.entity.components.spells

import com.destroystokyo.paper.ParticleBuilder
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
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.HomingSystem
import me.newburyminer.customItems.helpers.ParticleTheme
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.FallingBlock
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Marker
import org.bukkit.entity.Mob
import org.bukkit.entity.Player

class MobHealerComponent(
    private val range: Double,
    private val health: Double,
    private val absorption: Double,
    private val effects: HitEffects,
    castTime: Int,
    baseCooldown: Int
): AbstractSpellComponent(baseCooldown, castTime), LeapingInterface {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "range" to range,
            "health" to health,
            "absorption" to absorption,
            "effects" to effects.serialize(),
            "castTime" to spellDuration,
            "baseCooldown" to baseCooldown,
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.MOB_HEALER_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return MobHealerComponent(
                map["range"].asDouble(),
                map["health"].asDouble(),
                map["absorption"].asDouble(),
                HitEffects.deserialize(map["effects"]),
                map["castTime"].asInt(),
                map["baseCooldown"].asInt(),
            )
        }
    }

    override fun tick(wrapper: EntityWrapper) {
        val caster = wrapper.entity as? Mob ?: return
        reduceCooldown(1)

        if (castingTicks > 0) {
            castingTicks -= 1

            if (castingTicks <= 0) {

                for (entity in caster.getNearbyEntities(range, range/2, range).filter { it !is Player }.filterIsInstance<LivingEntity>()) {
                    entity.heal(health)
                    entity.absorptionAmount = (entity.absorptionAmount + absorption).coerceAtMost(2047.0)
                    effects.apply(entity, caster)
                }

                applyCooldown(baseCooldown)
                CustomEffects.playSound(caster.location, Sound.ITEM_TRIDENT_RETURN, 1.5F, 0.3F)
                CustomEffects.filledParticleCircle(
                    ParticleBuilder(Particle.FIREWORK), caster.location, range / 2, 2.5
                )
            }
        }

        if (wrapper.entity.ticksLived % 10 == 0 && offCooldown()) {

            if (startCasting(wrapper)) {
                CustomEffects.playSound(caster.location, Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1.5F, 1.1F)
            }

        }
    }
}