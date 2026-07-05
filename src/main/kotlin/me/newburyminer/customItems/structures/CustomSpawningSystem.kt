package me.newburyminer.customItems.structures

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.eventbus.EventRegistry
import me.newburyminer.customItems.eventbus.ListenerEntry
import me.newburyminer.customItems.helpers.copyTo
import me.newburyminer.customItems.helpers.hasIntersectingBlocks
import me.newburyminer.customItems.mobprovider.AridLandsMobs
import me.newburyminer.customItems.mobprovider.MobContext
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.TrialSpawnerSpawnEvent
import org.bukkit.scheduler.BukkitRunnable

object CustomSpawningSystem: BukkitRunnable() {

    private val spawnerSessions: MutableMap<BlockLocation, SpawnerSession> = mutableMapOf()

    fun registerListeners() {
        EventRegistry.register(
            ListenerEntry(
            TrialSpawnerSpawnEvent::class,
            { e ->
                e.entity.world == CustomItems.aridWorld
            },
            { e ->
                e.isCancelled = true
                // Determine structure
                val trialSpawner = e.trialSpawner
                val structureId =
                    if (trialSpawner.isOminous) trialSpawner.ominousConfiguration.possibleRewards.map { it.key }.first().key.key
                    else trialSpawner.normalConfiguration.possibleRewards.map { it.key }.first().key.key

                val reference = StructureRegistry.lookupLootTag(structureId)
                val context = MobContext(e.entity.location.length(), reference.difficulty, e.entity.location)

                // Retrieve session
                val session = spawnerSessions[BlockLocation(trialSpawner.location)] ?: SpawnerSession(trialSpawner, reference.structure)
                session.attemptMobSpawn(context, reference)
            })
        )

        EventRegistry.register(
            ListenerEntry(
                CreatureSpawnEvent::class,
                { e ->
                    e.entity.world == CustomItems.aridWorld &&
                    e.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL
                },
                { e ->
                    e.isCancelled = true
                    val yCoordinate = e.entity.y
                    // Below y=0.0 is harder, considered ominous
                    val isOminous = yCoordinate < 0.0
                    val provider =
                        if (yCoordinate > 30.0) AridLandsMobs.SURFACE
                        else AridLandsMobs.CAVES

                    val context = MobContext(e.entity.location.length(), isOminous, e.entity.location)
                    val possibleMobs = provider.new(context).toMutableList()

                    var possibleSpawn = possibleMobs.removeFirstOrNull() ?: return@ListenerEntry
                    while (e.entity.world
                        .hasIntersectingBlocks(
                            possibleSpawn.getHitbox().copyTo(e.entity.location.toVector()))
                    ) {
                        possibleSpawn = possibleMobs.removeFirstOrNull() ?: return@ListenerEntry
                    }

                    possibleSpawn.build(context).createEntity(context)
                })
        )
    }

    override fun run() {
        spawnerSessions.entries.toMutableList().forEach { (key, value) ->
            if (value.hasSessionEnded())
                spawnerSessions.remove(key)
        }
    }

}