package me.newburyminer.customItems.entity.hiteffects

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

interface HitEffect {

    //val hitEffectType: HitEffectType

    fun apply(victim: LivingEntity, damager: Entity, sourceLoc: Location? = null)
    fun serialize(): Map<String, Any>
    //fun deserialize(map: Map<String, Any>): HitEffect

}