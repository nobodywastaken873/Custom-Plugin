package me.newburyminer.customItems.entity.hiteffects.effect

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffectDeserialization
import me.newburyminer.customItems.entity.hiteffects.HitEffectType
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.metadata.MetadataValue
import org.bukkit.metadata.MetadataValueAdapter

class CustomDamageApply(val amount: Double, val damageType: DamageType, val iFrames: Int = 10, val overrideSource: Entity? = null): HitEffect {

    override fun apply(victim: LivingEntity, damager: Entity) {

        val realDamager = overrideSource ?: damager

        if (overrideSource == null || overrideSource == damager) {
            victim.setTag("damaged", true)
        }
        victim.damage(
            amount,
            DamageSource.builder(damageType)
                .withDamageLocation(realDamager.location)
                .withDirectEntity(realDamager)
                .withCausingEntity(realDamager)
                .build()
        )
        victim.noDamageTicks = iFrames
        victim.lastDamage = 0.0
    }

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "amount" to amount,
            "type" to damageType.key.asString(),
            "iframes" to iFrames
        )
    }
    companion object: HitEffectDeserialization {
        override val componentType: HitEffectType = HitEffectType.CUSTOM_DAMAGE
        override fun deserialize(map: Map<String, Any>): HitEffect {
            val newAmount = map["amount"].toDouble()
            val key = NamespacedKey.fromString(map["type"].toString())!!
            val newType = RegistryAccess.registryAccess().getRegistry(RegistryKey.DAMAGE_TYPE).get(key)!!
            val newIFrames = map["iframes"].toInt()
            return CustomDamageApply(newAmount, newType, newIFrames)
        }
    }
}