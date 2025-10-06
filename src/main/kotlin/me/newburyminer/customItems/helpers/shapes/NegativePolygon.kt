package me.newburyminer.customItems.helpers.shapes

import org.bukkit.Location
import kotlin.math.*

class NegativePolygon(val y: Double, private var points: List<Location>, private val exclude: List<Shape>): Shape() {

    private var maxX: Double = points.first().x
    private var minX: Double = points.first().x
    private var maxZ: Double = points.first().z
    private var minZ: Double = points.first().z

    override val xRadius: Double
    override val zRadius: Double
    override val center: Location

    override val area: Double
    override val boundingArea: Double
    override val circumference: Double

    init {

        for (point in points) {
            minX = min(minX, point.x)
            maxX = max(maxX, point.x)
            minZ = min(minZ, point.z)
            maxZ = max(maxZ, point.z)
        }

        xRadius = (maxX - minX) / 2
        zRadius = (maxZ - minZ) / 2
        boundingArea = (maxX - minX) * (maxZ - minZ)

        center = Location(points.first().world, (maxX + minX) / 2, y, (maxZ + minZ) / 2)

        points = points.sortedWith(compareBy { atan2(it.z - center.z, it.x - center.x) } )

        var total = 0.0
        for (i in points.indices) {
            val j: Int = (i + 1) % points.size
            total += (points[i].x * points[j].z) - (points[j].x * points[i].z)
        }
        area = total / 2

        var circTotal = 0.0
        for (i in 0..<(points.size)) {
            val p1 = points[i]
            val p2 = points[if (i != points.size - 1) (i + 1) else 0]

            circTotal += sqrt((p1.x - p2.x).pow(2) + (p1.z - p2.z).pow(2))
        }
        circumference = circTotal
    }

    override fun contains(loc: Location): Boolean {
        if (loc.x < minX || loc.x > maxX || loc.z < minZ || loc.z > maxZ) /*{Bukkit.getLogger().info("mins1"); return false}*/return false

        for (shape in exclude) {
            if (shape.contains(loc)) /*{Bukkit.getLogger().info("ininner"); return false}*/return false
        }

        var crosses = 0
        //vertical line

        //Bukkit.getLogger().info(points.toString())
        //Bukkit.getLogger().info(loc.toString())

        for (i in 0..<(points.size)) {
            val p1 = points[i]
            val p2 = points[(i + 1) % points.size]

            if (loc.z < min(p1.z, p2.z) || loc.z > max(p1.z, p2.z) || loc.x > max(p1.x, p2.x)) /*{Bukkit.getLogger().info("actual1"); continue}*/continue
            val intersectX = (p2.x - p1.x) / (p2.z - p1.z) * (loc.z - p1.z) + p1.x
            if (loc.x > intersectX) /*{Bukkit.getLogger().info("actual2"); continue}*/continue

            //Bukkit.getLogger().info("actual3")
            crosses += 1
        }

        return crosses % 2 == 1
    }

    override fun randomPoint(): Location {
        var point = Location(points.first().world, Math.random() * xRadius * 2 + minX, y, Math.random() * zRadius * 2 + minZ)
        while (!contains(point)) {
            point = Location(points.first().world, Math.random() * xRadius * 2 + minX, y, Math.random() * zRadius * 2 + minZ)
        }
        return point
    }

    override fun linePoints(conc: Double): List<Location> {

        val possPoints = mutableListOf<Location>()

        var i = 0

        var partial = points[if (i != points.size-1) (i + 1) else 0].clone().subtract(points[i])
        var j = (partial.length() / conc).toInt()
        var unit = partial.toVector().normalize().multiply(1.0 / conc)
        var current = points[i].clone()
        for (z in 0..(conc * circumference).toInt()) {
            if (j == 0) {
                i++
                if (i == points.size) break
                partial = points[if (i != points.size-1) (i + 1) else 0].clone().subtract(points[i])
                j = (partial.length() / conc).toInt()
                unit = partial.toVector().normalize().multiply(1.0 / conc)
                current = points[i].clone()
            }
            possPoints.add(current)
            current.add(unit)
            j--
        }

        for (shape in exclude) {
            possPoints.addAll(shape.linePoints(conc))
        }

        return possPoints
    }
}