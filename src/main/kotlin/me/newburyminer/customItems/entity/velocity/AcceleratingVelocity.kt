package me.newburyminer.customItems.entity.velocity

import me.newburyminer.customItems.helpers.HomingSystem
import org.bukkit.Location
import org.bukkit.util.Vector


class AcceleratingVelocity(
    val speed: Double,
    val acceleration: Double,
    val angleChange: Double = 0.0,
    val homingType: HomingSystem.Type = HomingSystem.Type.NONE
): VelocityProvider() {

    override fun next(currentLoc: Location, target: Location, currentVel: Vector, timeIndex: Int): Vector {
        val accelerationFactor = (currentVel.length() + acceleration) / currentLoc.length()
        return if (timeIndex == 0) currentVel
            else getHomingVelocity(currentLoc, target, currentVel, homingType, angleChange).multiply(accelerationFactor)
    }

    override fun serialize(): Map<String, Any?> {
        return mapOf(
            "type" to "AcceleratingVelocity",
            "speed" to speed,
            "acceleration" to acceleration,
            "angleChange" to angleChange,
            "homingType" to homingType.name,
        )
    }
    @Suppress("UNCHECKED_CAST")
    companion object: VelocityProviderDeserialization {
        override fun deserialize(map: Map<String, Any?>): VelocityProvider {
            return AcceleratingVelocity(
                map["speed"].asDouble(),
                map["acceleration"].asDouble(),
                map["angleChange"].asDouble(),
                HomingSystem.Type.valueOf(map["homingType"].asString()),
            )
        }

    }

}