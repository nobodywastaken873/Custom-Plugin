package me.newburyminer.customItems.structures.locations

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.eventbus.EventRegistry
import me.newburyminer.customItems.eventbus.ListenerEntry
import me.newburyminer.customItems.helpers.getChunkPositions
import me.newburyminer.customItems.structures.StructureRegistry
import me.newburyminer.customItems.structures.structure.AbandonedShip
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockBurnEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.world.AsyncStructureGenerateEvent
import org.bukkit.event.world.AsyncStructureSpawnEvent
import org.bukkit.util.BoundingBox
import kotlin.collections.set

object StructureBounding {

    fun registerListeners() {
        EventRegistry.register(ListenerEntry(AsyncStructureSpawnEvent::class,
            { e ->
                e.world == CustomItems.aridWorld
            },
            {e ->
                val structureName = RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE).getKey(e.structure)?.value() ?: "abandoned_ship"
                val defintion = StructureRegistry.get(structureName) ?: AbandonedShip
                val box = e.boundingBox.clone().expand(0.0, 0.0, 0.0, 1.0, 0.0, 1.0)
                val region = StructureRegion(
                    defintion,
                    box
                )

                val chunks = box.getChunkPositions()
                chunks.forEach { chunk ->
                    StructureBlockManager.set(chunk.key, region)
                }
            })
        )

        EventRegistry.register(ListenerEntry(BlockBreakEvent::class,
            { e ->
                e.block.world == CustomItems.aridWorld
            },
            {e ->
                if (!isInStructure(e.block)) return@ListenerEntry
                // we now know it is in a structure, cancel
                e.isCancelled = true
            })
        )

        EventRegistry.register(ListenerEntry(EntityExplodeEvent::class,
            { e ->
                e.entity.world == CustomItems.aridWorld
            },
            {e ->
                val blocks = e.blockList()
                blocks.removeIf {
                    isInStructure(it)
                }
            })
        )

        EventRegistry.register(ListenerEntry(BlockExplodeEvent::class,
            { e ->
                e.block.world == CustomItems.aridWorld
            },
            {e ->
                val blocks = e.blockList()
                blocks.removeIf {
                    isInStructure(it)
                }
            })
        )

        EventRegistry.register(ListenerEntry(BlockBurnEvent::class,
            { e ->
                e.block.world == CustomItems.aridWorld
            },
            {e ->
                if (!isInStructure(e.block)) return@ListenerEntry
                // we now know it is in a structure, cancel
                e.isCancelled = true
            })
        )

        EventRegistry.register(ListenerEntry(BlockPlaceEvent::class,
            { e ->
                e.block.world == CustomItems.aridWorld
            },
            {e ->
                if (!isInStructure(e.block)) return@ListenerEntry
                // we now know it is in a structure, cancel
                e.isCancelled = true
            })
        )

    }

    private fun isInStructure(block: Block): Boolean {
        val centerLoc = block.location.add(0.5, 0.5, 0.5)
        val possibleRegions = StructureBlockManager.get(ChunkPos(centerLoc.chunk.x, centerLoc.chunk.z).key)
        return possibleRegions.any { it.bounds.contains(centerLoc.toVector()) }
    }

}