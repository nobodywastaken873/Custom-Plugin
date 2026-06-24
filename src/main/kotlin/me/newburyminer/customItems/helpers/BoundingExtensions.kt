package me.newburyminer.customItems.helpers

import me.newburyminer.customItems.Utils.Companion.containsLoc
import me.newburyminer.customItems.Utils.Companion.rotateToAxis
import org.bukkit.FluidCollisionMode
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector
import java.util.UUID
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

fun World.rayTraceManyEntities(start: Location, direction: Vector, maxDist: Double, ignore: Entity? = null, radius: Double = 0.0): List<Entity> {
    return this.rayTraceManyEntities(start, direction, maxDist, radius, {it != ignore})
}

fun World.rayTraceManyEntities(start: Location, end: Location, ignore: Entity? = null, radius: Double = 0.0): List<Entity> {
    val direction = end.toVector().subtract(start.toVector())
    val maxDist = direction.length()
    return this.rayTraceManyEntities(start, direction, maxDist, ignore, radius)
}

fun World.rayTraceEntity(start: Location, direction: Vector, maxDist: Double, radius: Double = 0.0, predicate: (Entity) -> Boolean = {true}): Entity? {
    // Find endpoint at a block
    val endpoint = this.rayTraceBlocks(start, direction, maxDist, FluidCollisionMode.NEVER, true)
    val actualDist = endpoint?.hitPosition?.toLocation(start.world)?.subtract(start)?.length() ?: maxDist

    val result = this.rayTraceEntities(start, direction, actualDist, radius, predicate)

    return result?.hitEntity
}

fun World.rayTraceManyEntities(start: Location, direction: Vector, maxDist: Double, radius: Double = 0.0, predicate: (Entity) -> Boolean = {true}): List<Entity> {
    // Find endpoint at a block
    val endpoint = this.rayTraceBlocks(start, direction, maxDist, FluidCollisionMode.NEVER, true)
    val actualDist = endpoint?.hitPosition?.toLocation(start.world)?.subtract(start)?.length() ?: maxDist
    // Create a large AABB around the area of the ray
    val rayBox = BoundingBox.of(start, start).expandDirectional(direction.normalize().multiply(actualDist))

    val hitEntities = mutableListOf<Entity>()
    // Loop through all entities near the ray and test if the ray hits them
    for (entity in start.world.getNearbyEntities(rayBox)) {
        if (!predicate(entity)) continue

        // Perform raycast on only this entity's hit
        val result = entity.boundingBox.expand(radius).rayTrace(start.toVector(), direction, actualDist)
        if (result != null)
            hitEntities.add(entity)
    }

    return hitEntities
}

fun World.arcTraceManyEntities(start: Location, direction: Vector, radius: Double, spreadAngle: Double, predicate: (Entity) -> Boolean = {true}): List<Entity> {

    val totalDegrees = Math.toDegrees(spreadAngle)
    val hitEntities = mutableSetOf<Entity>()

    for (i in 0..totalDegrees.toInt()) {
        val currentDegree = -totalDegrees / 2 + i
        val currentRad = Math.toRadians(currentDegree)
        val vect = Vector(cos(currentRad), 0.0, sin(currentRad)).rotateToAxis(direction)

        val intersecting = rayTraceManyEntities(start, vect, radius, predicate = predicate)
        hitEntities.addAll(intersecting)
    }

    return hitEntities.toList()
}

fun Player.getCenterLoc(): Location {
    val bottomCenter = location.clone()
    val hitboxHeight = this.height
    return bottomCenter.add(0.0, hitboxHeight / 2, 0.0)
}

fun Entity.getUpperCenter(): Location {
    val bottomCenter = location.clone()
    val hitboxHeight = this.height
    return bottomCenter.add(0.0, hitboxHeight * 2/3, 0.0)
}

fun World.getIntersectingBlocks(box: BoundingBox): List<Block> {
    val foundBlocks = mutableListOf<Block>()

    for (x in floor(box.minX).toInt()..floor(box.maxX).toInt()) {
        for (y in floor(box.minY).toInt()..floor(box.maxY).toInt()) {
            for (z in floor(box.minZ).toInt()..floor(box.maxZ).toInt()) {

                val block = this.getBlockAt(x, y, z)

                if (!block.type.isSolid) continue

                if (block.boundingBox.overlaps(box))
                    foundBlocks.add(block)
            }
        }
    }

    return foundBlocks.toList()
}

fun Location.getNearestPlayer(radius: Double): Player? {

    return getNearbyPlayers(radius).minByOrNull { it.location.subtract(this).length() }

}

fun Location.getNearestLivingEntity(radius: Double): LivingEntity? {

    return getNearbyEntitiesByType(LivingEntity::class.java, radius).minByOrNull { it.location.subtract(this).length() }

}