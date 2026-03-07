package me.newburyminer.customItems.items.behaviors

import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import kotlin.math.max

interface VeinFinder: CubeHarvester {

    fun getConnected(start: Block, maxDepth: Int): List<Location> {
        var total = 1
        val checked = mutableListOf(start.location.clone())
        val toContinue = mutableListOf(start.location.clone())

        val foundLocations = mutableListOf<Location>()

        while (toContinue.isNotEmpty() && total <= maxDepth) {
            val currentLoc = toContinue[0]
            for (loc in getAround(currentLoc)) {
                if (loc in checked || loc in foundLocations) continue
                if (start.world.getBlockAt(loc).type == start.type) {
                    //for (drop in start.world.getBlockAt(loc).getDrops(pickaxe, e.player)) drops.add(drop)
                    //start.world.getBlockAt(loc).type = Material.AIR
                    //if (total < 5) CustomEffects.playSound(loc, start.blockData.soundGroup.breakSound, 1.0F, start.blockData.soundGroup.pitch)
                    total++
                    foundLocations.add(loc)
                    toContinue.add(loc)
                } else {
                    checked.add(loc)
                }
            }
            toContinue.removeFirst()
        }

        return foundLocations.toList()
    }

}