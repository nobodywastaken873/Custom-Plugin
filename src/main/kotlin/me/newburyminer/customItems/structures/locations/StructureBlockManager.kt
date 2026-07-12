package me.newburyminer.customItems.structures.locations

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.eventbus.EventRegistry
import me.newburyminer.customItems.eventbus.ListenerEntry
import me.newburyminer.customItems.helpers.FileDatabase
import me.newburyminer.customItems.helpers.getChunkPositions
import me.newburyminer.customItems.structures.StructureRegistry
import me.newburyminer.customItems.structures.structure.AbandonedShip
import me.newburyminer.customItems.systems.ores.CustomOre
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.world.AsyncStructureGenerateEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.BoundingBox
import java.io.File
import java.time.Instant
import java.util.Date
import java.util.UUID

object StructureBlockManager: FileDatabase() {

    private val chunkMap = mutableMapOf<Long, MutableList<StructureRegion>>()
    override val fileName: String = "structureStartData.txt"

    fun set(key: Long, region: StructureRegion) {
        chunkMap.getOrPut(key) { mutableListOf() }.add(region)
    }

    fun get(key: Long): List<StructureRegion> {
        return chunkMap[key] ?: listOf()
    }

    override fun initialize() {

        val text = readFromFile()
        if (text.isEmpty()) return

        val entries = text.split("\n")
        entries.forEach {entry ->
            val splitIndex = entry.indexOf("=")
            val key = entry.substring(0, splitIndex).toLong()
            entry.substring(splitIndex + 1).split(";").forEach { regionString ->
                val regionData = regionString.split(":")
                val minCoords = regionData[1].split(",").map { it.toDouble() }
                val maxCoords = regionData[2].split(",").map { it.toDouble() }
                val region = StructureRegion(
                    StructureRegistry.get(regionData[0]) ?: AbandonedShip,
                    BoundingBox(
                        minCoords[0],
                        minCoords[1],
                        minCoords[2],
                        maxCoords[0],
                        maxCoords[1],
                        maxCoords[2]
                    )
                )

                set(key, region)
            }
        }

    }

    override fun pushToFile(backup: Boolean) {
        val text = chunkMap.toList().map { (k, v) ->
            "$k=" + v.joinToString(";") {
                "${it.definition.id}:${it.bounds.minX},${it.bounds.minY},${it.bounds.minZ}:${it.bounds.maxX},${it.bounds.maxY},${it.bounds.maxZ}"
            }
        }.joinToString("\n")

        writeToFile(text, backup)
    }

}