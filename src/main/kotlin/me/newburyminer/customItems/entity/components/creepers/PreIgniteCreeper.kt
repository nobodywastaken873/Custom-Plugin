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

            if (separation < 1.7) {
                creeper.explode()
            }

            if (creeper.fuseTicks > 50 && separation > 5.0) {
                leaping = false
                creeper.isIgnited = false
                creeper.fuseTicks = 0
            }

        }

        else if (wrapper.entity.ticksLived % 10 == 0) {

            val creeper = wrapper.entity as? Creeper ?: return
            val target = creeper.target ?: return
            if (creeper.location.distanceSquared(target.location) > minDistance.pow(2)) return
            if (!creeper.hasLineOfSight(target)) return

            creeper.velocity = calculateLeapVelocity(creeper.location, target.location, 2.0)
            leaping = true
            creeper.ignite()
            creeper.maxFuseTicks = 80
            creeper.fuseTicks = 0

            CustomEffects.playSound(creeper.location, Sound.ENTITY_BLAZE_HURT, 1.0F, 0.75F)

        }
    }

}