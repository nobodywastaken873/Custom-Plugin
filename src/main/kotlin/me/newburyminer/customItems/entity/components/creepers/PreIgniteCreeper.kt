package me.newburyminer.customItems.entity.components.creepers

import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.utils.LeapingInterface
import me.newburyminer.customItems.entity.components.utils.CooldownInterface
import org.bukkit.Bukkit
import org.bukkit.entity.Creeper
import kotlin.math.pow

class PreIgniteCreeper(private val minDistance: Double): EntityComponent, LeapingInterface {
    override val componentType: EntityComponentType = EntityComponentType.PRE_IGNITE_CREEPER

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "minDistance" to minDistance,
        )
    }
    override fun deserialize(map: Map<String, Any>): EntityComponent {
        return PreIgniteCreeper(
            map["minDistance"] as Double,
        )
    }

    private var leaping = false
    private var prevDistSquared = 250.0
    private var wasIncreasingLastTick = false
    private var totalElapsedTicks = 0

    override fun tick(wrapper: EntityWrapper) {
        if (leaping) {

            val creeper = wrapper.entity as? Creeper ?: return
            val target = creeper.target ?: return
            val currentDistSquared = creeper.location.distanceSquared(target.location)
            val increasing = currentDistSquared - prevDistSquared > 0

            // if distance is increasing for 2 ticks, instantly explode
            if (increasing && wasIncreasingLastTick) {
                creeper.explode()
            }

            else if (increasing) {
                wasIncreasingLastTick = true
            }

            else if (totalElapsedTicks > 30) {
                creeper.explode()
            }

            else {
                wasIncreasingLastTick = false
                creeper.fuseTicks = creeper.maxFuseTicks - 5
            }

            prevDistSquared = currentDistSquared
            totalElapsedTicks++

        }

        else if (Bukkit.getCurrentTick() % 10 == 0) {

            val creeper = wrapper.entity as? Creeper ?: return
            val target = creeper.target ?: return
            if (creeper.location.distanceSquared(target.location) > 15.0.pow(2)) return
            if (!creeper.hasLineOfSight(target)) return

            creeper.velocity = calculateLeapVelocity(creeper.location, target.location, 2.0)
            leaping = true
            creeper.ignite()

        }
    }

}