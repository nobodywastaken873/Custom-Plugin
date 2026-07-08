package me.newburyminer.customItems.bosses

import me.newburyminer.customItems.Utils.Companion.getCorners
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector
import kotlin.math.pow

object Collision {

    fun cylinderIntersects(start: Location, end: Location, radius: Double, box: BoundingBox): Boolean {

        val axis = end.toVector().subtract(start.toVector())
        val length = axis.length()
        val unit = axis.normalize()

        for (corner in box.getCorners(start.world)) {
            val relative = corner.toVector().subtract(start.toVector())

            val along = relative.dot(unit)

            if (along !in 0.0..length)
                continue

            val closest = start.toVector().add(unit.multiply(along))

            if (corner.distanceSquared(closest.toLocation(start.world)) < radius.pow(2))
                return true
        }

        return false
    }

}