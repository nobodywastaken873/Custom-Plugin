package me.newburyminer.customItems.entity.hiteffects.effect

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffectType
import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class VanillaEffectApply(val type: PotionEffectType, val duration: Int, val potency: Int, val ambient: Boolean = false, val showParticles: Boolean = true): HitEffect {
    override val hitEffectType: HitEffectType = HitEffectType.VANILLA_EFFECT

    override fun apply(victim: LivingEntity, damager: Entity) {
        victim.addPotionEffect(PotionEffect(
            type,
            duration,
            potency,
            ambient,
            showParticles
        ))
    }

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "type" to type.key.asString(),
            "duration" to duration,
            "potency" to potency,
            "ambient" to ambient,
            "showparticles" to showParticles
        )
    }
    override fun deserialize(map: Map<String, Any>): HitEffect {
        val key = NamespacedKey.fromString(map["type"] as String)!!
        val newType = RegistryAccess.registryAccess().getRegistry(RegistryKey.MOB_EFFECT).get(key)!!
        val newDuration = map["duration"] as Int
        val newPotency = map["potency"] as Int
        val newAmbient = map["ambient"] as Boolean
        val newShowParticles = map["showparticles"] as Boolean
        return VanillaEffectApply(newType, newDuration, newPotency, newAmbient, newShowParticles)
    }
}