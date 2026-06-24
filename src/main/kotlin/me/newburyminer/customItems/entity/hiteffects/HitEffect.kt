package me.newburyminer.customItems.entity.hiteffects

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

interface HitEffect {

    //val hitEffectType: HitEffectType

    fun apply(victim: LivingEntity, damager: Entity, sourceLoc: Location? = null)
    fun serialize(): Map<String, Any>
    //fun deserialize(map: Map<String, Any>): HitEffect
    fun isBlocking(hitterFacing: Vector, takerFacing: Vector): Boolean {
        return hitterFacing.normalize().dot(takerFacing.normalize()) < 0
    }

}