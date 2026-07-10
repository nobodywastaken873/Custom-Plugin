package me.newburyminer.customItems.bosses.rendering.shapes

import org.bukkit.World
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class Circle(
    y: Double,
    center: Vector,
    radius: Double,
): Shape(y) {

    var circleCenter: Vector = center.clone()
        set(value) {
            field = value.clone()
            markDirty()
        }
    var radius: Double = radius
        set(value) {
            field = value
            markDirty()
        }

    private var minX = center.x
    private var maxX = center.x
    private var minZ = center.z
    private var maxZ = center.z
    override val bounds: BoundingBox
        get() { ensureCalculated(); return BoundingBox(minX, y - 1, minZ, maxX, y + 1, maxZ) }

    private var _area = 0.0
    override val area: Double
        get() { ensureCalculated(); return _area }

    private var _perimeter = 0.0
    override val perimeter: Double
        get() { ensureCalculated(); return _perimeter }

    override fun recalculate() {
        _area = Math.PI * radius.pow(2)
        _perimeter = Math.PI * radius * 2
        recalculateBounds()
    }
    private fun recalculateBounds() {
        minX = circleCenter.x - radius
        maxX = circleCenter.x + radius
        minZ = circleCenter.z - radius
        maxZ = circleCenter.z + radius
    }

    override fun contains(loc: Vector): Boolean {
        ensureCalculated()
        if (loc.x !in minX..maxX || loc.z !in minZ..maxZ) return false
        return sqrt((loc.x - circleCenter.x).pow(2) + (loc.z - circleCenter.z).pow(2)) < radius
    }

    override fun randomPoint(): Vector {
        val theta = Math.random() * 2 * Math.PI
        val r = sqrt(Math.random()) * radius
        return circleCenter.clone().add(Vector(r * cos(theta), 0.0, r * sin(theta)))
    }

    override fun linePoints(conc: Double): List<Vector> {
        ensureCalculated()

        val possPoints = mutableListOf<Vector>()
        val totalPoints = conc * _perimeter

        for (i in 0..totalPoints.toInt()) {
            val theta = Math.PI * 2 * Math.random()
            possPoints.add(circleCenter.clone().add(Vector(radius * cos(theta), 0.0, radius * sin(theta))))
        }

        return possPoints
    }

}