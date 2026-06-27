package me.newburyminer.customItems.structures

import me.newburyminer.customItems.eventbus.EventRegistry
import me.newburyminer.customItems.eventbus.ListenerEntry
import me.newburyminer.customItems.mobprovider.MobContext
import org.bukkit.event.entity.TrialSpawnerSpawnEvent

object TrialSpawnerSystem {

    fun registerListeners() {
        EventRegistry.register(
            ListenerEntry(
                TrialSpawnerSpawnEvent::class,
                { e ->
                    true
                },
                { e ->
                    e.isCancelled = true
                    val trialSpawner = e.trialSpawner
                    val structureId = trialSpawner.normalConfiguration.possibleRewards.map { it.key }.first().key.key
                    val reference = StructureRegistry.lookupLootTag(structureId)

                    val structure = reference.structure
                    val spawnerConfig = when (reference.difficulty) {
                        StructureReference.Difficulty.NORMAL -> structure.normalSpawner
                        StructureReference.Difficulty.OMINOUS -> structure.ominousSpawner
                    }

                    val context = MobContext(e.entity.location.length(), reference.difficulty, reference.structure, e.entity.location)
                    val newMob = spawnerConfig.mobs.new(context)
                    trialSpawner.startTrackingEntity(newMob)
                })
        )
    }

}