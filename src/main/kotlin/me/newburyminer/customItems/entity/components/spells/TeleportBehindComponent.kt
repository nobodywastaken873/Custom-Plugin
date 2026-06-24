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
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Sound
import org.bukkit.entity.FallingBlock
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import kotlin.math.pow

class TeleportBehindComponent(
    private val range: Double,
    castTime: Int,
    baseCooldown: Int
): AbstractSpellComponent(baseCooldown, castTime) {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "range" to range,
            "castTime" to spellDuration,
            "baseCooldown" to baseCooldown
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.TELEPORT_BEHIND
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return TeleportBehindComponent(
                map["range"].asDouble(),
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

                if (caster.location.subtract(targetPlayer?.location ?: return).lengthSquared() > range.pow(2)) return
                val teleportVector = (targetPlayer ?: return).location.direction
                    .setY(0)
                    .normalize()
                    .multiply(-2)

                val teleportLocation = targetPlayer?.location?.add(teleportVector) ?: return
                if (
                    !teleportLocation.block.isPassable ||
                    !teleportLocation.clone().add(0.0, 1.0, 0.0).block.isPassable
                ) return

                caster.teleport(teleportLocation)

                applyCooldown(baseCooldown)
                targetPlayer = null
                CustomEffects.playSound(caster.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.1F)
            }
        }

        if (wrapper.entity.ticksLived % 10 == 0 && offCooldown()) {

            if (startCasting(wrapper)) {
                val target = caster.target as? Player ?: return
                if (!caster.hasLineOfSight(target)) return

                targetPlayer = target
                CustomEffects.playSound(caster.location, Sound.BLOCK_FROGSPAWN_HATCH, 1.5F, 0.4F)
            }

        }
    }
}