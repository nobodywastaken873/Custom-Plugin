package me.newburyminer.customItems.entity.hiteffects.effect

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffectType
import org.bukkit.NamespacedKey
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.metadata.MetadataValue
import org.bukkit.metadata.MetadataValueAdapter

class CustomDamageApply(val amount: Double, val damageType: DamageType, val iFrames: Int = 10): HitEffect {
    override val hitEffectType: HitEffectType = HitEffectType.CUSTOM_DAMAGE

    override fun apply(victim: LivingEntity, damager: Entity) {
        if (victim.getTag<Boolean>("damaged") == true) {
            victim.setTag("damaged", false)
            return
        }

        victim.setTag("damaged", true)
        victim.damage(
            amount,
            DamageSource.builder(damageType)
                .withDamageLocation(damager.location)
                .withDirectEntity(damager)
                .withCausingEntity(damager)
                .build()
        )
        victim.noDamageTicks = iFrames
    }

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "amount" to amount,
            "type" to damageType.key.asString(),
            "iframes" to iFrames
        )
    }
    override fun deserialize(map: Map<String, Any>): HitEffect {
        val newAmount = map["amount"] as Double
        val key = NamespacedKey.fromString(map["type"] as String)!!
        val newType = RegistryAccess.registryAccess().getRegistry(RegistryKey.DAMAGE_TYPE).get(key)!!
        val newIFrames = map["iframes"] as Int
        return CustomDamageApply(newAmount, newType, newIFrames)
    }
}