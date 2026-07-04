package me.newburyminer.customItems.structures

import me.newburyminer.customItems.eventbus.EventRegistry
import me.newburyminer.customItems.eventbus.ListenerEntry
import me.newburyminer.customItems.helpers.getValidSpawnLoc
import me.newburyminer.customItems.mobprovider.MobContext
import org.bukkit.Location
import org.bukkit.event.entity.TrialSpawnerSpawnEvent
import org.bukkit.scheduler.BukkitRunnable

object TrialSpawnerSystem: BukkitRunnable() {

    private val spawnerSessions: MutableMap<BlockLocation, SpawnerSession> = mutableMapOf()

    fun registerListeners() {
        EventRegistry.register(
            ListenerEntry(
            TrialSpawnerSpawnEvent::class,
            { e ->
                true
            },
            { e ->
                e.isCancelled = true
                // Determine structure
                val trialSpawner = e.trialSpawner
                val structureId =
                    if (trialSpawner.isOminous) trialSpawner.ominousConfiguration.possibleRewards.map { it.key }.first().key.key
                    else trialSpawner.normalConfiguration.possibleRewards.map { it.key }.first().key.key

                val reference = StructureRegistry.lookupLootTag(structureId)
                val context = MobContext(e.entity.location.length(), reference.difficulty, reference.structure, e.entity.location)

                // Retrieve session
                val session = spawnerSessions[BlockLocation(trialSpawner.location)] ?: SpawnerSession(trialSpawner, reference.structure)
                session.attemptMobSpawn(context, reference)
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