package me.newburyminer.customItems.entity.hiteffects

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import kotlin.reflect.full.companionObjectInstance

class HitEffects(private vararg val hitEffects: HitEffect) {

    fun apply(damaged: LivingEntity, damager: Entity, sourceLoc: Location? = null) {
        // Apply knockback before damage
        hitEffects.filter {
            it is CustomKnockbackApply || it is VanillaKnockbackApply // Custom or vanilla knockback components
        }.forEach { it.apply(damaged, damager) }
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            // Apply all other effects after
            hitEffects.filter {
                it !is VanillaKnockbackApply && it !is CustomKnockbackApply // Not a knockback component
            }.forEach { it.apply(damaged, damager) }
        })
    }

    fun serialize(): Map<String, Any> {
        return hitEffects.associate {
            val companion = it::class.companionObjectInstance as HitEffectDeserialization
            companion.componentType.name to it.serialize()
        }
    }
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun deserialize(raw: Any?): HitEffects {
            val map = raw as Map<String, Any>
            val effects = map.map {
                val type = HitEffectType.valueOf(it.key)
                HitEffectSerializationRegistry.deserialize(type, it.value as Map<String, Any>)!!
            }

            return HitEffects(*effects.toTypedArray())
        }
    }
}