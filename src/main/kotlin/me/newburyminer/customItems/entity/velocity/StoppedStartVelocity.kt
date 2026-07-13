package me.newburyminer.customItems.entity.velocity

import me.newburyminer.customItems.helpers.HomingSystem
import org.bukkit.Location
import org.bukkit.util.Vector
import kotlin.math.pow


class StoppedStartVelocity(
    val speed: Double,
    val angleChange: Double,
    val homingType: HomingSystem.Type,
    val delay: Int,
    val travelDist: Double,
): VelocityProvider() {

    override fun next(currentLoc: Location, target: Location, currentVel: Vector, timeIndex: Int): Vector {
        val prevVel = if (timeIndex == 0) (2 * travelDist) / (3 * delay) else currentVel.length()
        val scaledSpeed = prevVel - (2 * travelDist) / (3 * delay.toDouble().pow(2))

        return if (timeIndex < delay) currentVel.clone().normalize().multiply(scaledSpeed)
            else if (timeIndex == delay) target.clone().subtract(currentLoc).toVector().normalize().multiply(speed)
            else getHomingVelocity(currentLoc, target, currentVel, homingType, angleChange)
    }

    override fun serialize(): Map<String, Any?> {
        return mapOf(
            "type" to "StoppedStartVelocity",
            "speed" to speed,
            "angleChange" to angleChange,
            "homingType" to homingType.name,
            "delay" to delay,
            "travelDist" to travelDist,
        )
    }
    @Suppress("UNCHECKED_CAST")
    companion object: VelocityProviderDeserialization {
        override fun deserialize(map: Map<String, Any?>): VelocityProvider {
            return StoppedStartVelocity(
                map["speed"].asDouble(),
                map["angleChange"].asDouble(),
                HomingSystem.Type.valueOf(map["homingType"].asString()),
                map["delay"].asInt(),
                map["travelDist"].asDouble(),
            )
        }

    }

}