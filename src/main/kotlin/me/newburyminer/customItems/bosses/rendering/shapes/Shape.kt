package me.newburyminer.customItems.bosses.rendering.shapes

import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector

abstract class Shape(y: Double) {

    var y: Double = y
         set(value) {
             field = value
             markDirty()
         }

    abstract val area: Double
    abstract val bounds: BoundingBox
    abstract val perimeter: Double

    val center: Vector
        get() {
            return bounds.center
        }

    abstract fun contains(loc: Vector): Boolean
    abstract fun randomPoint(): Vector
    abstract fun linePoints(conc: Double): List<Vector>

    fun getRandomPoints(conc: Double): List<Vector> {
        val points = mutableListOf<Vector>()
        repeat((conc * area).toInt()) {
            points.add(randomPoint())
        }
        return points
    }

    private var dirty = true
    var localVersion = 0
        private set
    open val version: Int
        get() = localVersion

    protected fun markDirty() {
        dirty = true
        localVersion++
    }

    protected abstract fun recalculate()
    protected fun ensureCalculated() {
        if (dirty) {
            recalculate()
            dirty = false
        }
    }
}