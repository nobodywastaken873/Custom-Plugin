package me.newburyminer.customItems.entity.components.creepers

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.utils.LeapingInterface
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Sound
import org.bukkit.entity.Creeper
import kotlin.math.pow

class PreIgniteCreeper(private val minDistance: Double): EntityComponent, LeapingInterface {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "minDistance" to minDistance,
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.PRE_IGNITE_CREEPER
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return PreIgniteCreeper(
                map["minDistance"].asDouble(),
            )
        }
    }

    private var leaping = false
    //private var prevDistSquared = 250.0
    //private var numTicksIncreasing = 0
    //private var totalElapsedTicks = 0

    override fun tick(wrapper: EntityWrapper) {
        if (leaping) {

            val creeper = wrapper.entity as? Creeper ?: return
            val target = creeper.target ?: return
            val separation = creeper.location.toVector().subtract(target.location.toVector()).length()

            if (separation < 1.3) {
                creeper.explode()
            }

            if (creeper.fuseTicks < 5 && separation > 5.0) {
                leaping = false
                creeper.isIgnited = false
                creeper.fuseTicks = 40
            }
            //val currentDistSquared = creeper.location.distanceSquared(target.location)
            //val increasing = currentDistSquared - prevDistSquared > 0

            // if distance is increasing for 2 ticks, instantly explode
            /*if (increasing && numTicksIncreasing > 6) {
                creeper.explode()
            }
            else if (increasing) {
                numTicksIncreasing++
            }
            else if (totalElapsedTicks > 30) {
                creeper.explode()
            }
            else {
                numTicksIncreasing = 0
                creeper.fuseTicks = creeper.maxFuseTicks - 5
            }*/

            //prevDistSquared = currentDistSquared
            //totalElapsedTicks++

        }

        else if (wrapper.entity.ticksLived % 10 == 0) {

            val creeper = wrapper.entity as? Creeper ?: return
            val target = creeper.target ?: return
            if (creeper.location.distanceSquared(target.location) > minDistance.pow(2)) return
            if (!creeper.hasLineOfSight(target)) return

            creeper.velocity = calculateLeapVelocity(creeper.location, target.location, 2.0)
            leaping = true
            creeper.ignite()
            creeper.maxFuseTicks = 100
            creeper.fuseTicks = 40

            CustomEffects.playSound(creeper.location, Sound.ENTITY_BLAZE_HURT, 1.0F, 0.75F)

        }
    }

}