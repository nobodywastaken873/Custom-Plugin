package me.newburyminer.customItems.bosses.rendering.shapes

import org.bukkit.World
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector

class NegativeShape(
    layers: List<ShapeLayer>,
    y: Double
): Shape(y) {

    override val version: Int
        get() {
            var hash = localVersion

            for (layer in layers) {
                hash = hash * 31 + layer.shape.version
            }

            return hash
        }

    private val layers = layers.toMutableList()
    fun add(layer: ShapeLayer) {
        layers.add(layer)
        markDirty()
    }

    private var _area = 0.0
    override val area: Double
        get() { ensureCalculated(); return _area }

    private var _bounds = BoundingBox(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    override val bounds: BoundingBox
        get() { ensureCalculated(); return _bounds.clone() }

    private var _perimeter = 0.0
    override val perimeter: Double
        get() { ensureCalculated(); return _perimeter }

    override fun recalculate() {
        calculateArea()
        calculateBounds()
        calculatePerimeter()
    }
    private fun calculateArea() {
        var sum = 0.0
        for (layer in layers) {
            when (layer.operation) {
                ShapeLayer.Operation.ADD -> sum += layer.shape.area
                ShapeLayer.Operation.SUBTRACT -> sum -= layer.shape.area
            }
        }
        _area = sum.coerceAtLeast(0.0)
    }
    private fun calculatePerimeter() {
        _perimeter = layers.sumOf { it.shape.perimeter }
    }
    private fun calculateBounds() {
        val addLayers = layers.filter { it.operation == ShapeLayer.Operation.ADD }
        var box = addLayers.first().shape.bounds
        addLayers.forEach { box = box.union(it.shape.bounds) }
        _bounds = box
    }

    override fun contains(loc: Vector): Boolean {
        ensureCalculated()
        var inside = false

        for ((shape, operation) in layers) {
            when (operation) {
                ShapeLayer.Operation.ADD -> if (shape.contains(loc))
                    inside = true

                ShapeLayer.Operation.SUBTRACT -> if (shape.contains(loc))
                    inside = false
            }
        }

        return inside
    }
    override fun linePoints(conc: Double): List<Vector> {
        ensureCalculated()
        val allPoints = mutableListOf<Vector>()
        layers.forEach { (shape, _) ->
            allPoints.addAll(shape.linePoints(conc))
        }
        return allPoints
    }
    override fun randomPoint(): Vector {
        ensureCalculated()
        val xRadius = (_bounds.maxX - _bounds.minX) / 2
        val zRadius = (_bounds.maxZ - _bounds.minZ) / 2
        var point = Vector(Math.random() * xRadius * 2 + _bounds.minX, y, Math.random() * zRadius * 2 + _bounds.minZ)
        while (!contains(point)) {
            point = Vector(Math.random() * xRadius * 2 + _bounds.minX, y, Math.random() * zRadius * 2 + _bounds.minZ)
        }
        return point
    }

}