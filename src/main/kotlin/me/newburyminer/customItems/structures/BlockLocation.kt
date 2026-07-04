package me.newburyminer.customItems.structures

import org.bukkit.Location
import java.util.UUID

data class BlockLocation(
    val world: UUID,
    val x: Int,
    val y: Int,
    val z: Int
) {
    constructor(location: Location) : this(
        location.world.uid,
        location.blockX,
        location.blockY,
        location.blockZ
    )
}