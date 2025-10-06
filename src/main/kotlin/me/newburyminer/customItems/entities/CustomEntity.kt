package me.newburyminer.customItems.entities

import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.setTag
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Creeper
import org.bukkit.entity.Entity
import org.bukkit.entity.MagmaCube
import org.bukkit.entity.Skeleton
import org.bukkit.entity.Slime
import org.bukkit.entity.Spider
import org.bukkit.entity.TNTPrimed
import org.bukkit.entity.Zombie

enum class CustomEntity {

    JERRY_IDOL,
    MAX_FLETCHER,
    WIND_HOOK_SHOT,
    REDSTONE_REPEATER_SHOT,
    MULTI_LOAD_CROSSBOW_SHOT,
    TRUE_LLAMA_SPIT,
    GRAVE_MARKER,
    LEAPING_CREEPER,
    FIREBOMB_CREEPER,
    ARROWBOMB_CREEPER,
    BREACHING_CREEPER,
    FIREWORK_CREEPER,
    CHAIN_REACTION_CREEPER,
    TNTHEAD_CREEPER,
    SHIELD_BREAKER_CREEPER,
    PREIGNITION_CREEPER,
    BABY_CREEPER,
    HOPPING_CREEPER,
    NUCLEAR_CREEPER,
    MINI_BREACHING_CREEPER,
    HOMING_SKELETON,
    EXPLOSIVE_SKELETON,
    SWORDSMAN_SKELETON,

    SNIPER_SKELETON,
    ENERGY_SHIELD_SKELETON,
    MACHINE_GUN_SKELETON,
    SHIELD_BREAKER_SKELETON,
    BABY_SKELETON,
    SWARMER_SKELETON,
    HOMING_SKELETON_ARROW,
    ELYTRA_BREAKER_SKELETON,
    ELYTRA_BREAKER_SKELETON_ARROW,
    SNIPER_SKELETON_ARROW,
    EXPLOSIVE_SKELETON_ARROW,
    MACHINE_GUN_SKELETON_ARROW,
    SHIELD_BREAKER_ARROW,
    SWARMER_SKELETON_ARROW,
    JUMPING_ZOMBIE,
    INFECTIOUS_ZOMBIE,
    SHADOW_ASSASSIN_ZOMBIE,
    TANK_ZOMBIE,
    SWARMER_ZOMBIE,
    CAVE_BROODMOTHER_SPIDER,
    BROODMOTHER_SPIDER,
    TARANTULA_SPIDER,
    WEAVER_SPIDER,
    LEAPING_SPIDER,
    SWARMER_SPIDER,
    BROODMOTHER_SPAWN,
    SWARMER_SLIME,
    LEAPING_SLIME,
    REPLICATING_SLIME,
    LAUNCHING_SLIME,
    SWARMER_CUBE,
    LEAPING_CUBE,
    LAUNCHING_CUBE,
    LAVA_CUBE,
    BIOWEAPON_WITCH,
    ENDER_WITCH,
    SNIPER_WITCH,
    CLERIC_WITCH,
    COLONIEL_WITCH,
    BOSS_SPAWNED_WARDEN,
    ELYTRA_BREAKER_ARROW,
    WIND_CANNON_CHARGE,
    SNIPER_RIFLE_SHOT,
    LANDMINE_SHOT,
    DUAL_BARRELED_CROSSBOW_SHOT,

    ;

    val id: Int
        get() {
            return this.ordinal
        }

    fun get(id: Int): CustomEntity {
        return entries[id]
    }

    companion object {
        fun convert(entity: Entity, entityType: CustomEntity, diff: Double = -1.0) {
            entity.setTag("id", entityType.id)
            var difficulty = diff
            if (diff == -1.0)
                difficulty = entity.getTag<Double>("difficulty") ?: 1.0
            when (entityType) {
                NUCLEAR_CREEPER ->
                    {(entity as Creeper).maxFuseTicks = (600 * (1 - difficulty/120).coerceAtLeast(0.5)).toInt(); entity.explosionRadius *= 4}
                BABY_CREEPER ->
                    {(entity as Creeper).getAttribute(Attribute.SCALE)!!.baseValue = 0.45}
                LEAPING_CREEPER ->
                    {(entity as Creeper).getAttribute(Attribute.FALL_DAMAGE_MULTIPLIER)!!.baseValue = 0.0}
                TNTHEAD_CREEPER ->
                    {val newTnt = (entity as Creeper).world.spawn(entity.location, TNTPrimed::class.java); newTnt.fuseTicks = 32767; entity.addPassenger(newTnt)}
                CHAIN_REACTION_CREEPER ->
                    {entity.setTag("chain", true)}
                SNIPER_SKELETON ->
                    {(entity as Skeleton).getAttribute(Attribute.MOVEMENT_SPEED)?.baseValue = (entity.getAttribute(Attribute.MOVEMENT_SPEED)?.baseValue ?: 0.25) * 0.4}
                MACHINE_GUN_SKELETON ->
                    {(entity as Skeleton).getAttribute(Attribute.MOVEMENT_SPEED)?.baseValue = (entity.getAttribute(Attribute.MOVEMENT_SPEED)?.baseValue ?: 0.25) * 0.4}
                BABY_SKELETON ->
                    {(entity as Skeleton).getAttribute(Attribute.SCALE)!!.baseValue = 0.45}
                SWORDSMAN_SKELETON ->
                    {(entity as Skeleton).getAttribute(Attribute.MOVEMENT_SPEED)?.baseValue = (entity.getAttribute(Attribute.MOVEMENT_SPEED)?.baseValue ?: 0.25) * 1.2}
                TANK_ZOMBIE ->
                    {(entity as Zombie).getAttribute(Attribute.MAX_HEALTH)?.baseValue = (entity.getAttribute(Attribute.MAX_HEALTH)?.baseValue!!) * 4
                     entity.health = entity.getAttribute(Attribute.MAX_HEALTH)?.baseValue!!}
                JUMPING_ZOMBIE ->
                    {(entity as Zombie).getAttribute(Attribute.FALL_DAMAGE_MULTIPLIER)!!.baseValue = 0.0}
                TARANTULA_SPIDER ->
                    {(entity as Spider).getAttribute(Attribute.SCALE)!!.baseValue = 0.17}
                CAVE_BROODMOTHER_SPIDER ->
                    {(entity as Spider).getAttribute(Attribute.SCALE)!!.baseValue = 0.5}
                BROODMOTHER_SPAWN ->
                    {(entity as Spider).getAttribute(Attribute.SCALE)!!.baseValue = 0.25}
                LEAPING_SPIDER ->
                    {(entity as Spider).getAttribute(Attribute.FALL_DAMAGE_MULTIPLIER)!!.baseValue = 0.0}
                LEAPING_CUBE ->
                    {(entity as MagmaCube).getAttribute(Attribute.FALL_DAMAGE_MULTIPLIER)!!.baseValue = 0.0}
                LEAPING_SLIME ->
                    {(entity as Slime).getAttribute(Attribute.FALL_DAMAGE_MULTIPLIER)!!.baseValue = 0.0}
                else -> {}
            }
        }
    }


}