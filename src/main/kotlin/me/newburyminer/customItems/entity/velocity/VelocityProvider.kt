package me.newburyminer.customItems.entity.velocity

import me.newburyminer.customItems.helpers.HomingSystem
import org.bukkit.Location
import org.bukkit.util.Vector
import kotlin.reflect.KClass

abstract class VelocityProvider {

    //protected var timeIndex: Int = 0
    abstract fun serialize(): Map<String, Any?>
    /*fun next(currentLoc: Location, target: Location, currentVel: Vector): Vector {
        val newVel = getNextVelocity(currentLoc, target, currentVel)
        timeIndex++
        return newVel
    }*/
    abstract fun next(currentLoc: Location, target: Location, currentVel: Vector, timeIndex: Int): Vector
    protected fun getHomingVelocity(currentLoc: Location, target: Location, currentVelocity: Vector, homingType: HomingSystem.Type, angleChange: Double): Vector {
        val targetDirection = target.clone().subtract(currentLoc).toVector()
        return when (homingType) {
            HomingSystem.Type.BASIC_TURN -> {
                HomingSystem.basicCappedTurn(currentVelocity, targetDirection, angleChange)
            }
            HomingSystem.Type.DISTANCE_SCALED -> {
                HomingSystem.distanceScalingTurn(currentVelocity, targetDirection, angleChange, targetDirection.length())
            }
            HomingSystem.Type.ANGLE_SCALED -> {
                HomingSystem.angleScalingTurn(currentVelocity, targetDirection, angleChange, 0.4)
            }
            HomingSystem.Type.BOTH_SCALED -> {
                HomingSystem.aggressiveScalingTurn(currentVelocity, targetDirection, angleChange, targetDirection.length(), 0.4)
            }
            else -> {
                currentVelocity
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun deserialize(raw: Any?): VelocityProvider {
            val map = raw as Map<String, Any>
            return when (map["type"] as String) {
                "DelaylessConstantVelocity" -> DelaylessConstantVelocity.deserialize(map)
                "DelayedStartVelocity" -> DelayedStartVelocity.deserialize(map)
                "StoppedStartVelocity" -> StoppedStartVelocity.deserialize(map)
                "AcceleratingVelocity" -> AcceleratingVelocity.deserialize(map)
                "DelayedAcceleratingVelocity" -> DelayedAcceleratingVelocity.deserialize(map)
                else -> { DelaylessConstantVelocity(0.0) }
            }
        }

        fun getValidStartVelocity(currentLoc: Location, target: Location, provider: VelocityProvider): Vector {
            return when (provider) {

                is DelaylessConstantVelocity -> {target.clone().subtract(currentLoc).toVector().normalize().multiply(provider.speed)}
                is AcceleratingVelocity -> {target.clone().subtract(currentLoc).toVector().normalize().multiply(provider.speed)}
                is DelayedStartVelocity -> {getValidDirection(currentLoc, provider.initialSpeed * provider.delay).multiply(provider.initialSpeed)}
                is DelayedAcceleratingVelocity -> {getValidDirection(currentLoc, provider.initialSpeed * provider.delay).multiply(provider.initialSpeed)}
                is StoppedStartVelocity -> {getValidDirection(currentLoc, provider.travelDist)}
                else -> {Vector(0, 0, 0)}

            }
        }

        private fun getValidDirection(currentLoc: Location, distance: Double): Vector {
            for (i in 0..40) {
                val randomDirection = Vector.getRandom().normalize()
                val result = currentLoc.world.rayTraceBlocks(currentLoc, randomDirection, distance)
                if (result == null || result.hitBlock == null) {
                    return randomDirection
                }
            }
            return Vector(0, 0, 0)
        }
    }
}