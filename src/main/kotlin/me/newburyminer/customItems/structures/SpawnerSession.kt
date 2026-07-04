package me.newburyminer.customItems.structures

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.DefaultEntityComponent
import me.newburyminer.customItems.helpers.getValidSpawnLoc
import me.newburyminer.customItems.mobprovider.MobContext
import org.bukkit.Bukkit
import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.block.TrialSpawner
import org.bukkit.entity.LivingEntity
import org.bukkit.spawner.Spawner
import kotlin.math.pow

class SpawnerSession(
    private val spawner: TrialSpawner,
    private val structureDefinition: StructureDefinition
) {

    init {
        if (spawner.cooldownLength == 36000) {
            // Initialize spawner, not been previously loaded before
            spawner.cooldownLength = 72000
            spawner.normalConfiguration.delay = 20 // TODO: make this correct
            spawner.ominousConfiguration.delay = 20
            spawner.requiredPlayerRange = 10.0.pow(2).toInt()
            spawner.update()
        }
    }



    private var currentlyTrackedPlayers: Int = spawner.trackedPlayers.size
    private var isCurrentlyOminous: Boolean = spawner.isOminous
    private var weightBudget = getWeightBudget(currentlyTrackedPlayers)
    fun attemptMobSpawn(ctx: MobContext, reference: StructureReference) {

        if (isCurrentlyOminous != spawner.isOminous) updateToOminous()
        if (spawner.trackedPlayers.size > currentlyTrackedPlayers) updateWeightBudget()

        if (weightBudget <= 0.0) {
            if (spawner.trackedEntities.isEmpty()) endSession()
            return
        }

        if (tooManyNearbyMobs(ctx)) return
        val newMob = spawnMob(ctx, reference) ?: return

        spawner.startTrackingEntity(newMob)
        spawner.update()

        spawner.world.playEffect(newMob.location, Effect.TRIAL_SPAWNER_SPAWN_MOB_AT, spawner.isOminous)
        spawner.world.playEffect(spawner.location, Effect.TRIAL_SPAWNER_SPAWN, spawner.isOminous)
    }

    private fun spawnMob(ctx: MobContext, reference: StructureReference): LivingEntity? {
        // Get possible mobs
        val possibleMobs = structureDefinition.mobProvider.new(ctx).toMutableList()

        // Determine mob to spawn and location to spawn at
        var newMobType = possibleMobs.removeFirstOrNull()
        var spawnLoc: Location? = null
        while (spawnLoc == null) {
            val box = newMobType?.getHitbox() ?: return null
            spawnLoc = spawner.location.getValidSpawnLoc(box)
        }

        // Spawn new mob
        val newCtx = MobContext(spawner.location.length(), reference.difficulty, structureDefinition, spawnLoc)
        val newMob = (newMobType ?: return null).build(newCtx).createEntity(newCtx)

        weightBudget -= newMobType.tier.weight
        return newMob
    }

    private var sessionEnded: Boolean = false
    fun hasSessionEnded(): Boolean = sessionEnded
    private fun endSession() {
        outputLoot()
        spawner.cooldownEnd = CustomItems.aridWorld.gameTime + spawner.cooldownLength
        sessionEnded = true
        spawner.update()
    }

    private fun outputLoot() {
        val lootPlayers = spawner.trackedPlayers.toMutableList()
        // TODO: create loot gui/thing to give out loot, playsound to show that it is over
    }

    private fun getWeightBudget(playerCount: Int): Double {
        val difficulty = MobContext(spawner.location.length(), spawner.isOminous, structureDefinition, spawner.location).difficulty
        return (difficulty + 30) * playerCount.toDouble().pow(3.0/4.0)
    }

    private fun updateWeightBudget() {
        val previousTotal = getWeightBudget(currentlyTrackedPlayers)
        val newTotal = getWeightBudget(spawner.trackedPlayers.size)
        weightBudget += newTotal - previousTotal
    }

    private fun updateToOminous() {
        weightBudget = getWeightBudget(currentlyTrackedPlayers)
        isCurrentlyOminous = spawner.isOminous
    }

    private fun tooManyNearbyMobs(ctx: MobContext): Boolean {
        val weightSum = spawner.location.getNearbyLivingEntities(12.0).toMutableList().sumOf {
            val wrapper = EntityWrapperManager.getWrapper(it.uniqueId)
            if (wrapper == null) 0

            else {
                val mobComponent = wrapper.getComponents(DefaultEntityComponent::class).firstOrNull()

                if (mobComponent == null) 0
                else (mobComponent as DefaultEntityComponent).getMobTier().weight
            }
        }

        return weightSum > maxNearbyWeight(ctx.difficulty)
    }

    private fun maxNearbyWeight(difficulty: Double): Double {
        return 60.0 + 12 * difficulty.pow(2.0/3.0)
    }

}