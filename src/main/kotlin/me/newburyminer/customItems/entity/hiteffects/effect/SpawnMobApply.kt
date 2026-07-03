package me.newburyminer.customItems.entity.hiteffects.effect

import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffectDeserialization
import me.newburyminer.customItems.entity.hiteffects.HitEffectType
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobFactory
import me.newburyminer.customItems.mobprovider.MobRegistry
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.structures.StructureReference
import me.newburyminer.customItems.structures.structure.AbandonedShip
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

class SpawnMobApply(val mob: MobDefinition, val count: Int): HitEffect {

    override fun apply(victim: LivingEntity, damager: Entity, sourceLoc: Location?) {
        val ctx = MobContext(damager.location.length(), StructureReference.Difficulty.OMINOUS, AbandonedShip, damager.location)
        for (i in 0..<count) {
            MobFactory.create(mob.build(ctx), ctx)
        }
    }

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "mob" to mob.id,
            "count" to count
        )
    }
    companion object: HitEffectDeserialization {
        override val componentType: HitEffectType = HitEffectType.SPAWN_MOB
        override fun deserialize(map: Map<String, Any>): HitEffect {
            return SpawnMobApply(
                MobRegistry.getMob(map["mob"].asString()) ?: BasicZombie,
                map["count"].asInt(),
            )
        }
    }
}