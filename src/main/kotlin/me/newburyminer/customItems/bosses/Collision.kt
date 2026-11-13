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
        val lengthSquared = axis.lengthSquared()

        if (lengthSquared == 0.0) {
            val closest = Vector(
                start.x.coerceIn(box.minX, box.maxX),
                start.y.coerceIn(box.minY, box.maxY),
                start.z.coerceIn(box.minZ, box.maxZ)
            )
            return closest.distanceSquared(start.toVector()) <= radius * radius
        }

        // Iteratively find the closest point between the axis segment and AABB.
        var t = 0.5

        repeat(10) {
            val point = start.toVector().add(axis.clone().multiply(t))

            val closest = Vector(
                point.x.coerceIn(box.minX, box.maxX),
                point.y.coerceIn(box.minY, box.maxY),
                point.z.coerceIn(box.minZ, box.maxZ)
            )

            t = ((closest.subtract(start.toVector()).dot(axis)) /
                    lengthSquared).coerceIn(0.0, 1.0)
        }

        val point = start.toVector().add(axis.multiply(t))

        val closest = Vector(
            point.x.coerceIn(box.minX, box.maxX),
            point.y.coerceIn(box.minY, box.maxY),
            point.z.coerceIn(box.minZ, box.maxZ)
        )

        return point.distanceSquared(closest) <= radius * radius
    }

}