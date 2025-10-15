package me.newburyminer.customItems.helpers.shapes

import org.bukkit.Location
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class Circle(val radius: Double, override val center: Location): Shape() {
    private var maxX: Double = center.x + radius
    private var minX: Double = center.x - radius
    private var maxZ: Double = center.z + radius
    private var minZ: Double = center.z - radius

    override val xRadius: Double = radius
    override val zRadius: Double = radius
    override val circumference: Double = 2 * Math.PI * radius

    override val area: Double = Math.PI * radius.pow(2)
    override val boundingArea: Double = (radius * 2).pow(2)

    override fun contains(loc: Location): Boolean {
        if (loc.x < minX || loc.x > maxX || loc.z < minZ || loc.z > maxZ) return false
        return sqrt((loc.x - center.x).pow(2) + (loc.z - center.z).pow(2)) < radius
    }

    override fun randomPoint(): Location {
        val theta = Math.random() * 2 * Math.PI
        val r = sqrt(Math.random()) * radius
        return center.clone().add(r * cos(theta), 0.0, r * sin(theta))
    }

    override fun linePoints(conc: Double): List<Location> {

        val possPoints = mutableListOf<Location>()

        val totalPoints = conc * circumference

        for (i in 0..totalPoints.toInt()) {
            val theta = Math.PI * 2 * Math.random()
            possPoints.add(center.clone().add(radius * cos(theta), 0.0, radius * sin(theta)))
        }

        return possPoints

    }
}