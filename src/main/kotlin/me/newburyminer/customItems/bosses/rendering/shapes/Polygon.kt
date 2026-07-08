package me.newburyminer.customItems.bosses.rendering.shapes

import org.bukkit.World
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class Polygon(
    points: List<Vector>,
    y: Double,
): Shape(y) {

    private val points = points.toMutableList()
    fun add(point: Vector) {
        points.add(point)
        markDirty()
    }

    private var minX = points.first().x
    private var maxX = points.first().x
    private var minZ = points.first().z
    private var maxZ = points.first().z
    override val bounds: BoundingBox
        get() { ensureCalculated(); return BoundingBox(minX, y - 1, minZ, maxX, y + 1, maxZ) }

    private var _area = 0.0
    override val area: Double
        get() { ensureCalculated(); return _area }

    private var _perimeter = 0.0
    override val perimeter: Double
        get() { ensureCalculated(); return _perimeter }

    override fun recalculate() {
        recalculateBounds()
        sortPoints()
        calculateArea()
        calculatePerimeter()
    }
    private fun recalculateBounds() {
        minX = points.first().x
        maxX = points.first().x
        minZ = points.first().z
        maxZ = points.first().z

        for (point in points) {
            minX = min(minX, point.x)
            maxX = max(maxX, point.x)
            minZ = min(minZ, point.z)
            maxZ = max(maxZ, point.z)
        }
    }
    private fun sortPoints() {
        val center = Vector((maxX + minX) / 2, y, (maxZ + minZ) / 2)
        points.sortBy { atan2(it.z - center.z, it.x - center.x) }
    }
    private fun calculateArea() {
        var total = 0.0
        for (i in points.indices) {
            val j: Int = (i + 1) % points.size
            // Shoelace formula
            total += (points[i].x * points[j].z) - (points[j].x * points[i].z)
        }
        _area = total / 2
    }
    private fun calculatePerimeter() {
        var circTotal = 0.0
        // Find each pair of points in order
        for (i in 0..<(points.size)) {
            val p1 = points[i]
            val p2 = points[if (i != points.size - 1) (i + 1) else 0]
            // Use dist formula to calculate the distance between the two
            circTotal += sqrt((p1.x - p2.x).pow(2) + (p1.z - p2.z).pow(2))
        }
        _perimeter = circTotal
    }

    override fun contains(loc: Vector): Boolean {
        ensureCalculated()
        if (loc.x !in minX..maxX || loc.z !in minZ..maxZ) return false

        var inside = false

        var j = points.lastIndex

        for (i in points.indices) {

            val pi = points[i]
            val pj = points[j]

            if ((pi.z > loc.z) != (pj.z > loc.z)) {

                val intersectX =
                    (pj.x - pi.x) *
                            (loc.z - pi.z) /
                            (pj.z - pi.z) +
                            pi.x

                if (loc.x < intersectX)
                    inside = !inside
            }

            j = i
        }

        return inside
    }
    override fun randomPoint(): Vector {
        ensureCalculated()
        val xRadius = (maxX - minX) / 2
        val zRadius = (maxZ - minZ) / 2
        var point = Vector(Math.random() * xRadius * 2 + minX, y, Math.random() * zRadius * 2 + minZ)
        while (!contains(point)) {
            point = Vector(Math.random() * xRadius * 2 + minX, y, Math.random() * zRadius * 2 + minZ)
        }
        return point
    }
    override fun linePoints(conc: Double): List<Vector> {
        ensureCalculated()
        val possPoints = mutableListOf<Vector>()

        var i = 0
        // no idea how this works, think it goes through each pair of points (a line) and steps along the line to get pts
        var partial = points[if (i != points.size-1) (i + 1) else 0].clone().subtract(points[i])
        var j = (partial.length() / conc).toInt()
        var unit = partial.normalize().multiply(1.0 / conc)
        var current = points[i].clone()
        for (z in 0..(conc * perimeter).toInt()) {
            if (j == 0) {
                i++
                if (i == points.size) break
                partial = points[if (i != points.size-1) (i + 1) else 0].clone().subtract(points[i])
                j = (partial.length() / conc).toInt()
                unit = partial.normalize().multiply(1.0 / conc)
                current = points[i].clone()
            }
            possPoints.add(current)
            current.add(unit)
            j--
        }

        return possPoints
    }
}