package me.newburyminer.customItems.helpers

import org.bukkit.FluidCollisionMode
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector

fun World.rayTraceEntities(start: Location, direction: Vector, maxDist: Double, ignore: Entity? = null, radius: Double = 0.0): List<Entity> {
    // Find endpoint at a block
    val endpoint = this.rayTraceBlocks(start, direction, maxDist, FluidCollisionMode.NEVER, true)
    val actualDist = endpoint?.hitPosition?.toLocation(start.world)?.subtract(start)?.length() ?: maxDist
    // Create a large AABB around the area of the ray
    val rayBox = BoundingBox.of(start, start).expandDirectional(direction.normalize().multiply(actualDist))

    val hitEntities = mutableListOf<Entity>()
    // Loop through all entities near the ray and test if the ray hits them
    for (entity in start.world.getNearbyEntities(rayBox)) {
        if (entity == ignore) continue

        // Perform raycast on only this entity's hit
        val result = entity.boundingBox.expand(radius).rayTrace(start.toVector(), direction, actualDist)
        if (result != null)
            hitEntities.add(entity)
    }

    return hitEntities
}