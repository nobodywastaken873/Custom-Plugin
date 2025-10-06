package me.newburyminer.customItems.helpers.shapes

import org.bukkit.Location

abstract class Shape {
    abstract val area: Double
    abstract val boundingArea: Double
    abstract val xRadius: Double
    abstract val zRadius: Double
    abstract val center: Location
    abstract val circumference: Double

    abstract fun contains(loc: Location): Boolean
    abstract fun randomPoint(): Location
    abstract fun linePoints(conc: Double): List<Location>
}