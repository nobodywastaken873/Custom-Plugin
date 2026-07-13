package me.newburyminer.customItems.helpers

import me.newburyminer.customItems.Utils.Companion.rotateToAxis
import me.newburyminer.customItems.structures.locations.ChunkPos
import org.bukkit.FluidCollisionMode
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector
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

fun World.hasIntersectingBlocks(box: BoundingBox): Boolean {
    val foundBlocks = mutableListOf<Block>()

    for (x in floor(box.minX).toInt()..floor(box.maxX).toInt()) {
        for (y in floor(box.minY).toInt()..floor(box.maxY).toInt()) {
            for (z in floor(box.minZ).toInt()..floor(box.maxZ).toInt()) {

                val block = this.getBlockAt(x, y, z)

                if (!block.type.isSolid) continue

                return true

            }
        }
    }

    return false
}

fun Location.shiftToGround(maxDistance: Int = 8): Location? {
    val world = world ?: return null

    for (dy in 0..maxDistance) {
        for (dir in listOf(1, -1)) {
            val test = clone().add(0.0, dy * dir.toDouble(), 0.0)

            val below = test.clone().subtract(0.0, 1.0, 0.0).block

            if (below.type.isSolid && test.block.isPassable)
                return test
        }
    }

    return null
}

fun BoundingBox.copyTo(bottomCenter: Vector): BoundingBox {
    return BoundingBox.of(
        bottomCenter.subtract(Vector(this.widthX / 2, 0.0, this.widthZ / 2)),
        bottomCenter.add(Vector(this.widthX / 2, this.height, this.widthZ / 2))
    )
}

fun Location.getValidSpawnLocs(boundingBox: BoundingBox, count: Int, startRadius: Int = 2, maxRadius: Int = 5): List<Location> {
    val locations = mutableListOf<Location>()
    for (i in 0..<count) {
        locations.add(getValidSpawnLoc(boundingBox, startRadius, maxRadius) ?: continue)
    }
    return locations
}

fun Location.getValidSpawnLoc(boundingBox: BoundingBox, startRadius: Int = 2, maxRadius: Int = 5): Location? {

    val center = toVector()

    for (radius in startRadius..maxRadius) {
        for (i in 0..15) {

            val randomAngle = Math.random() * Math.PI * 2

            val direction = Vector(sin(randomAngle), 0.0, cos(randomAngle)).normalize().multiply(radius)
            val newLoc = clone().add(direction).shiftToGround() ?: continue
            val newBox = boundingBox.copyTo(newLoc.toVector())

            if (!this.world.hasIntersectingBlocks(newBox))
                return newLoc
        }
    }

    return null
}

fun Location.getNearestPlayer(radius: Double): Player? {
    return getNearbyPlayers(radius).minByOrNull { it.location.subtract(this).length() }
}

fun Location.getNearestLivingEntity(radius: Double): LivingEntity? {
    return getNearbyEntitiesByType(LivingEntity::class.java, radius).minByOrNull { it.location.subtract(this).length() }
}

fun BoundingBox.getChunkPositions(): List<ChunkPos> {
    val minChunkX = floor(minX).toInt() shr 4
    val maxChunkX = floor(maxX).toInt() shr 4

    val minChunkZ = floor(minZ).toInt() shr 4
    val maxChunkZ = floor(maxZ).toInt() shr 4

    val chunks = ArrayList<ChunkPos>((maxChunkX - minChunkX + 1) * (maxChunkZ - minChunkZ + 1))

    for (chunkX in minChunkX..maxChunkX) {
        for (chunkZ in minChunkZ..maxChunkZ) {
            chunks += ChunkPos(chunkX, chunkZ)
        }
    }

    return chunks
}