package me.newburyminer.customItems.entity.components.spells

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.utils.CooldownInterface
import me.newburyminer.customItems.entity.components.utils.LeapingInterface
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Sound
import org.bukkit.entity.Mob
import kotlin.math.pow

class LeapComponent(private val minDistance: Double, private val extraHeight: Double, private val baseCooldown: Int): EntityComponent,
    CooldownInterface, LeapingInterface {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "minDistance" to minDistance,
            "extraHeight" to extraHeight,
            "baseCooldown" to baseCooldown
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.LEAPING_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return LeapComponent(
                map["minDistance"].asDouble(),
                map["extraHeight"].asDouble(),
                map["baseCooldown"].asInt(),
            )
        }
    }

    override var cooldown: Int = 100

    override fun tick(wrapper: EntityWrapper) {

        if (wrapper.entity.ticksLived % 10 == 0) {

            reduceCooldown(10)
            if (!offCooldown()) return
            val mob = wrapper.entity as? Mob ?: return
            val target = mob.target ?: return
            if (mob.location.distanceSquared(target.location) > minDistance.pow(2)) return
            if (!mob.hasLineOfSight(target)) return

            mob.velocity = calculateLeapVelocity(mob.location, target.location).add(target.velocity)
            applyCooldown(baseCooldown)

            CustomEffects.playSound(wrapper.entity.location, Sound.ENTITY_BREEZE_JUMP, 1.0F, 0.8F)

        }

    }
}