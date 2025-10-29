package me.newburyminer.customItems.entity.hiteffects

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent

interface HitEffect {

    val hitEffectType: HitEffectType

    fun apply(damaged: LivingEntity, damager: Entity)
    fun serialize(): Map<String, Any>
    fun deserialize(map: Map<String, Any>): HitEffect

}