package me.newburyminer.customItems.entity3

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getDifficultyIndex
import me.newburyminer.customItems.helpers.RandomSelector
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import kotlin.math.pow


class EntitySpawnManager: Listener {
    companion object {

        private val conversionMap = mutableMapOf<CustomEntity, CustomEntityDefinition>()

        fun register(customEntity: CustomEntity, definition: CustomEntityDefinition) {
            conversionMap[customEntity] = definition
        }

    }

    private val defaults = mapOf(
        EntityType.CREEPER to CustomEntity.ARID_CREEPER,
        EntityType.SKELETON to CustomEntity.ARID_SKELETON,
        EntityType.ZOMBIE to CustomEntity.ARID_ZOMBIE,
        EntityType.SPIDER to CustomEntity.ARID_SPIDER,
        EntityType.SLIME to CustomEntity.ARID_SLIME,
        EntityType.MAGMA_CUBE to CustomEntity.ARID_MAGMA_CUBE,
        EntityType.WITCH to CustomEntity.ARID_WITCH
    )

    private val weights = mapOf(

        EntityType.CREEPER to RandomSelector(
            Pair(CustomEntity.LEAPING_CREEPER, 20), Pair(CustomEntity.FIREBOMB_CREEPER, 10), Pair(CustomEntity.BREACHING_CREEPER, 20),
            Pair(CustomEntity.FIREWORK_CREEPER, 10), Pair(CustomEntity.ARROWBOMB_CREEPER, 20), Pair(CustomEntity.CHAIN_REACTION_CREEPER, 15),
            Pair(CustomEntity.TNTHEAD_CREEPER, 5), Pair(CustomEntity.SHIELD_BREAKER_CREEPER, 15), Pair(CustomEntity.NUCLEAR_CREEPER, 5),
            Pair(CustomEntity.PREIGNITION_CREEPER, 15), Pair(CustomEntity.HOPPING_CREEPER, 40), Pair(CustomEntity.MINI_BREACHING_CREEPER, 25),
            Pair(CustomEntity.BABY_CREEPER, 40),
        ),

        EntityType.SKELETON to RandomSelector(
            Pair(CustomEntity.HOMING_SKELETON, 40), Pair(CustomEntity.EXPLOSIVE_SKELETON, 10), Pair(CustomEntity.ELYTRA_BREAKER_SKELETON, 25),
            Pair(CustomEntity.SWORDSMAN_SKELETON, 60), Pair(CustomEntity.SNIPER_SKELETON, 10), Pair(CustomEntity.ENERGY_SHIELD_SKELETON, 10),
            Pair(CustomEntity.MACHINE_GUN_SKELETON, 10), Pair(CustomEntity.SHIELD_BREAKER_SKELETON, 20), Pair(CustomEntity.BABY_SKELETON, 40),
        ),

        EntityType.ZOMBIE to RandomSelector(
            Pair(CustomEntity.JUMPING_ZOMBIE, 30), Pair(CustomEntity.INFECTIOUS_ZOMBIE, 30), Pair(CustomEntity.SHADOW_ASSASSIN_ZOMBIE, 30),
            Pair(CustomEntity.TANK_ZOMBIE, 15), Pair(CustomEntity.SWARMER_ZOMBIE, 45),
        ),

        EntityType.SPIDER to RandomSelector(
            Pair(CustomEntity.BROODMOTHER_SPIDER, 10), Pair(CustomEntity.CAVE_BROODMOTHER_SPIDER, 40), Pair(CustomEntity.LEAPING_SPIDER, 40),
            Pair(CustomEntity.SWARMER_SPIDER, 50), Pair(CustomEntity.TARANTULA_SPIDER, 25), Pair(CustomEntity.WEAVER_SPIDER, 25),
        ),

        EntityType.SLIME to RandomSelector(
            Pair(CustomEntity.SWARMER_SLIME, 40), Pair(CustomEntity.LEAPING_SLIME, 25), Pair(CustomEntity.LAUNCHING_SLIME, 20)
        ),

        EntityType.MAGMA_CUBE to RandomSelector(
            Pair(CustomEntity.SWARMER_CUBE, 40), Pair(CustomEntity.LEAPING_CUBE, 20), Pair(CustomEntity.LAUNCHING_CUBE, 25), Pair(CustomEntity.LAVA_CUBE, 80)
        ),

        EntityType.WITCH to RandomSelector(
            Pair(CustomEntity.ENDER_WITCH, 20), Pair(CustomEntity.CLERIC_WITCH, 10), Pair(CustomEntity.BIOWEAPON_WITCH, 20),
            Pair(CustomEntity.SNIPER_WITCH, 10), Pair(CustomEntity.COLONIEL_WITCH, 10),
        )

    )



    @EventHandler fun onEntitySpawn(e: CreatureSpawnEvent) {

        if (e.entity.world != CustomItems.aridWorld) return

        val (difficulty, spawnType) = getDifficulty(e)

        var spawnRate = 0.25
        when (spawnType) {
            CustomSpawnType.NATURAL -> {}
            CustomSpawnType.NORMAL_SPAWNER -> {spawnRate *= 0.5}
            CustomSpawnType.OMINOUS_SPAWNER -> {spawnRate += 0.25}
        }

        if (Math.random() > spawnRate) {e.isCancelled = true; return}

        val customSpawnPercent = difficulty.pow(0.86).coerceAtMost(100.0)
        val customEntityType =
            if (Utils.randomPercent(customSpawnPercent)) {
                weights[e.entityType]?.next() ?: return
            } else {
                defaults[e.entityType] ?: return
            }
        conversionMap[customEntityType]?.convert(e.entity)

    }

    private fun getDifficulty(e: CreatureSpawnEvent): Pair<Double, CustomSpawnType> {

        var difficulty = e.location.world.getDifficultyIndex(e.location)
        var spawnType = CustomSpawnType.NATURAL

        if (e.entity.isSilent && !e.entity.hasAI()) {

            // manipulate difficulty here
            difficulty *= 2.0
            spawnType = CustomSpawnType.OMINOUS_SPAWNER
            e.entity.isSilent = false
            e.entity.setAI(true)

        } else if (!e.entity.hasAI()) {

            // manipulate difficulty here
            difficulty *= 1.5
            spawnType = CustomSpawnType.NORMAL_SPAWNER
            e.entity.setAI(true)

        }

        return difficulty to spawnType
    }

}