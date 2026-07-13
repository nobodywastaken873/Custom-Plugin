package me.newburyminer.customItems.helpers

import org.bukkit.util.Vector

object HomingSystem {

    fun basicCappedTurn(currentDirection: Vector, targetDirection: Vector, turnRate: Double): Vector {
        val cross = currentDirection.getCrossProduct(targetDirection)
        val angle = currentDirection.angle(targetDirection)
        return currentDirection.clone().rotateAroundAxis(cross, angle.coerceAtMost(turnRate.toFloat()).toDouble())
    }

    fun distanceScalingTurn(currentDirection: Vector, targetDirection: Vector, turnRate: Double, targetDistance: Double): Vector {
        val cross = currentDirection.getCrossProduct(targetDirection)
        val angle = currentDirection.angle(targetDirection)
        val distanceFactor = 0.5 + 8.0 / (targetDistance + 2.5)
        return currentDirection.clone().rotateAroundAxis(cross, angle.coerceAtMost((turnRate * distanceFactor).toFloat()).toDouble())
    }

    fun angleScalingTurn(currentDirection: Vector, targetDirection: Vector, turnRate: Double, aggressionFactor: Double): Vector {
        val cross = currentDirection.getCrossProduct(targetDirection)
        val angle = currentDirection.angle(targetDirection)
        val angleFactor = 4.14 / (Math.PI - angle + 1) * aggressionFactor + (1 - aggressionFactor)
        return currentDirection.clone().rotateAroundAxis(cross, angle.coerceAtMost((turnRate * angleFactor).toFloat()).toDouble())
    }

    fun aggressiveScalingTurn(currentDirection: Vector, targetDirection: Vector, turnRate: Double, targetDistance: Double, aggressionFactor: Double): Vector {
        val cross = currentDirection.getCrossProduct(targetDirection)
        val angle = currentDirection.angle(targetDirection)
        val distanceFactor = 0.5 + 8.0 / (targetDistance + 2.5)
        val angleFactor = 4.14 / (Math.PI - angle + 1) * aggressionFactor + (1 - aggressionFactor)
        return currentDirection.clone().rotateAroundAxis(cross, angle.coerceAtMost((turnRate * distanceFactor * angleFactor).toFloat()).toDouble())
    }

    enum class Type {
        BASIC_TURN,
        DISTANCE_SCALED,
        ANGLE_SCALED,
        BOTH_SCALED,
        NONE
    }

}