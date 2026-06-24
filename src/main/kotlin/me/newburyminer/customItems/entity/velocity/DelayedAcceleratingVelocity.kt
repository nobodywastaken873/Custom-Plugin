package me.newburyminer.customItems.entity.velocity

import me.newburyminer.customItems.helpers.HomingSystem
import org.bukkit.Location
import org.bukkit.util.Vector


class DelayedAcceleratingVelocity(
    val speed: Double,
    val initialSpeed: Double,
    val acceleration: Double,
    val angleChange: Double,
    val homingType: HomingSystem.Type,
    val delay: Int,
    val resetOnBegin: Boolean = true,
): VelocityProvider() {

    override fun next(currentLoc: Location, target: Location, currentVel: Vector, timeIndex: Int): Vector {
        val accelerationFactor = (currentVel.length() + acceleration) / currentLoc.length()
        return if (timeIndex < delay) currentVel
            else if (timeIndex == delay && resetOnBegin) target.clone().subtract(currentLoc).toVector().normalize().multiply(speed)
            else getHomingVelocity(currentLoc, target, currentVel, homingType, angleChange).multiply(accelerationFactor)
    }

    override fun serialize(): Map<String, Any?> {
        return mapOf(
            "type" to "DelayedAcceleratingVelocity",
            "speed" to speed,
            "initialSpeed" to initialSpeed,
            "acceleration" to acceleration,
            "angleChange" to angleChange,
            "homingType" to homingType.name,
            "delay" to delay,
            "resetOnBegin" to resetOnBegin,
        )
    }
    @Suppress("UNCHECKED_CAST")
    companion object: VelocityProviderDeserialization {
        override fun deserialize(map: Map<String, Any?>): VelocityProvider {
            return DelayedAcceleratingVelocity(
                map["speed"].asDouble(),
                map["initialSpeed"].asDouble(),
                map["acceleration"].asDouble(),
                map["angleChange"].asDouble(),
                HomingSystem.Type.valueOf(map["homingType"].asString()),
                map["delay"].asInt(),
                map["resetOnBegin"].asBoolean(),
            )
        }

    }

}