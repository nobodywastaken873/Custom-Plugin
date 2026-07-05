package me.newburyminer.customItems.entity.components.utils

import org.bukkit.Location
import org.bukkit.util.Vector
import kotlin.math.max
import kotlin.math.sqrt

interface LeapingInterface {

    fun calculateLeapVelocity(start: Location, end: Location, extraHeight: Double = 2.0, grav: Double = 0.08): Vector {

        if (start.toVector().subtract(end.toVector()).length() < 0.001) {return Vector(0.0, 0.0, 0.0)}

        val distanceVect = end.clone().subtract(start).toVector()
        val horizDist = distanceVect.clone().setY(0).length()
        val yInitial = start.y
        val yFinal = end.y
        val yApex = max(yFinal, yInitial) + extraHeight

        // yVel calculated using 1/2mv^2 = mg deltay
        // Then time is calculated with velocity / acceleration
        // xVel calculated using total distance and time, does not take friction into account
        val yVel = sqrt(2 * grav * (yApex - yInitial))
        val totalTime = (yVel / grav) + (sqrt(2 * grav * (yApex - yFinal)) / grav)
        val xVel = horizDist / totalTime * 2.5

        val direction = distanceVect.setY(0).normalize()
        val velocity = Vector(direction.x * xVel, yVel, direction.z * xVel)

        try {
            velocity.checkFinite()
        } catch (_: Exception) {
            return Vector(0.0, 0.0, 0.0)
        }

        return velocity

    }

}