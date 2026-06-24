package me.newburyminer.customItems.entity.velocity

import me.newburyminer.customItems.helpers.HomingSystem
import org.bukkit.Location
import org.bukkit.util.Vector


class DelaylessConstantVelocity(
    val speed: Double,
    val angleChange: Double = 0.0,
    val homingType: HomingSystem.Type = HomingSystem.Type.NONE
): VelocityProvider() {

    override fun next(currentLoc: Location, target: Location, currentVel: Vector, timeIndex: Int): Vector {
        return if (timeIndex == 0) currentVel
            else getHomingVelocity(currentLoc, target, currentVel, homingType, angleChange)
    }

    override fun serialize(): Map<String, Any?> {
        return mapOf(
            "type" to "DelaylessConstantVelocity",
            "speed" to speed,
            "angleChange" to angleChange,
            "homingType" to homingType.name
        )
    }
    @Suppress("UNCHECKED_CAST")
    companion object: VelocityProviderDeserialization {
        override fun deserialize(map: Map<String, Any?>): VelocityProvider {
            return DelaylessConstantVelocity(
                map["speed"].asDouble(),
                map["angleChange"].asDouble(),
                HomingSystem.Type.valueOf(map["homingType"].asString())
            )
        }

    }

}