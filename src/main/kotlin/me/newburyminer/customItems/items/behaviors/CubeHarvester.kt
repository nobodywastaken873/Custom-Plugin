package me.newburyminer.customItems.items.behaviors

import org.bukkit.Location
import org.bukkit.util.Vector

interface CubeHarvester {

    fun getAround(loc: Location, includeCenter: Boolean = false): MutableList<Location> {
        val locs = mutableListOf<Location>()
        for (x in -1..1) { for (y in -1..1) { for (z in -1..1) {
            if (x == 0 && y == 0 && z == 0 && !includeCenter) continue
            locs.add(loc.clone().add(Vector(x, y, z)))
        } } }
        return locs
    }

}