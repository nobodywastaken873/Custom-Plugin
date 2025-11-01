package me.newburyminer.customItems.entity.components

import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.utils.CooldownInterface
import me.newburyminer.customItems.entity.components.utils.LeapingInterface
import org.bukkit.Bukkit
import org.bukkit.entity.Creeper
import org.bukkit.entity.Entity
import org.bukkit.entity.Mob
import kotlin.math.pow

class LeapComponent(private val minDistance: Double, private val extraHeight: Double, private val baseCooldown: Int): EntityComponent, CooldownInterface, LeapingInterface {
    override val componentType: EntityComponentType = EntityComponentType.LEAPING_COMPONENT

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "minDistance" to minDistance,
            "extraHeight" to extraHeight,
            "baseCooldown" to baseCooldown
        )
    }
    override fun deserialize(map: Map<String, Any>): EntityComponent {
        return LeapComponent(
            map["minDistance"] as Double,
            map["extraHeight"] as Double,
            map["baseCooldown"] as Int,
        )
    }

    override var cooldown: Int = 100

    override fun tick(wrapper: EntityWrapper) {

        if (Bukkit.getCurrentTick() % 10 == 0) {

            reduceCooldown(10)
            if (!offCooldown()) return
            val mob = wrapper.entity as? Mob ?: return
            val target = mob.target ?: return
            if (mob.location.distanceSquared(target.location) > minDistance.pow(2)) return
            if (!mob.hasLineOfSight(target)) return

            mob.velocity = calculateLeapVelocity(mob.location, target.location, 2.0)
            setCooldown(baseCooldown)

        }

    }
}