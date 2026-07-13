package me.newburyminer.customItems.entity.velocity

import me.newburyminer.customItems.helpers.HomingSystem
import org.bukkit.Location
import org.bukkit.util.Vector


class DelayedStartVelocity(
    val speed: Double,
    val initialSpeed: Double,
    val angleChange: Double,
    val homingType: HomingSystem.Type,
    val delay: Int,
    val resetOnBegin: Boolean = true,
): VelocityProvider() {

    override fun next(currentLoc: Location, target: Location, currentVel: Vector, timeIndex: Int): Vector {
        return if (timeIndex < delay) currentVel.clone().normalize().multiply(initialSpeed)
            else if (timeIndex == delay && resetOnBegin) target.clone().subtract(currentLoc).toVector().normalize().multiply(speed)
            else getHomingVelocity(currentLoc, target, currentVel, homingType, angleChange)
    }

    override fun serialize(): Map<String, Any?> {
        return mapOf(
            "type" to "DelayedStartVelocity",
            "speed" to speed,
            "initialSpeed" to initialSpeed,
            "angleChange" to angleChange,
            "homingType" to homingType.name,
            "delay" to delay,
            "resetOnBegin" to resetOnBegin,
        )
    }
    @Suppress("UNCHECKED_CAST")
    companion object: VelocityProviderDeserialization {
        override fun deserialize(map: Map<String, Any?>): VelocityProvider {
            return DelayedStartVelocity(
                map["speed"].asDouble(),
                map["initialSpeed"].asDouble(),
                map["angleChange"].asDouble(),
                HomingSystem.Type.valueOf(map["homingType"].asString()),
                map["delay"].asInt(),
                map["resetOnBegin"].asBoolean(),
            )
        }

    }

}