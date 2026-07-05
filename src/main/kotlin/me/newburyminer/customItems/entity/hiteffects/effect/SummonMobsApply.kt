package me.newburyminer.customItems.entity.hiteffects.effect

import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffectDeserialization
import me.newburyminer.customItems.entity.hiteffects.HitEffectType
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.getValidSpawnLocs
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobRegistry
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.structures.StructureReference
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

class SummonMobsApply(val mob: MobDefinition, val count: Int): HitEffect {

    override fun apply(victim: LivingEntity, damager: Entity, sourceLoc: Location?) {
        val boundingBox = mob.getHitbox()

        val locs = damager.location.getValidSpawnLocs(boundingBox, count)
        CustomEffects.playSound(damager.location, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 3.0F, 1.2F)

        for (loc in locs) {
            val newCtx = MobContext(damager.location.length(), StructureReference.Difficulty.OMINOUS, loc)
            mob.build(newCtx).createEntity(newCtx)
        }
    }

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "mob" to mob.id,
            "count" to count
        )
    }
    companion object: HitEffectDeserialization {
        override val componentType: HitEffectType = HitEffectType.SUMMON_MOBS
        override fun deserialize(map: Map<String, Any>): HitEffect {
            return SummonMobsApply(
                MobRegistry.getMob(map["mob"].asString()) ?: BasicZombie,
                map["count"].asInt(),
            )
        }
    }
}