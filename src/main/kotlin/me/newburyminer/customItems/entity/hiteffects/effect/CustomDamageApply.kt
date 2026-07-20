package me.newburyminer.customItems.entity.hiteffects.effect

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.keys.tags.DamageTypeTagKeys
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffectDeserialization
import me.newburyminer.customItems.entity.hiteffects.HitEffectType
import me.newburyminer.customItems.helpers.CustomDamageType.Companion.isCustom
import net.kyori.adventure.key.Key
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.tag.DamageTypeTags

class CustomDamageApply(val amount: Double, val damageType: DamageType, val iFrames: Int = 10, val overrideSource: Entity? = null): HitEffect {

    init {
        if (!damageType.isCustom()) {
            CustomItems.plugin.logger.warning("Improper damage type used in CustomDamageApply, $damageType")
        }
    }

    override fun apply(victim: LivingEntity, damager: Entity, sourceLoc: Location?) {
        val realDamager = overrideSource ?: damager
        //println("Executing custom damage apply")
        //println("before damage: ${victim.velocity}")
        val oldVelocity = victim.velocity

        val bypassesKey = DamageTypeTagKeys.create(Key.key("minecraft:bypasses_cooldown"))
        val registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.DAMAGE_TYPE)
        val bypassesCooldown = registry.getTagValues(bypassesKey).contains(damageType)

        // Manually ignore attacks
        if (!bypassesCooldown && victim.noDamageTicks > 0) return

        victim.damage(
            amount,
            DamageSource.builder(damageType)
                .withDamageLocation(sourceLoc?.clone() ?: realDamager.location)
                .withDirectEntity(realDamager)
                .withCausingEntity(realDamager)
                .build()
        )

        victim.velocity = oldVelocity

        val newIframes =
            if (bypassesCooldown) 0
            else iFrames

        victim.noDamageTicks = newIframes
        if (newIframes == 0) victim.lastDamage = 0.0
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
            val newAmount = map["amount"].asDouble()
            val key = NamespacedKey.fromString(map["type"].asString())!!
            val newType = RegistryAccess.registryAccess().getRegistry(RegistryKey.DAMAGE_TYPE).get(key)!!
            val newIFrames = map["iframes"].asInt()
            return CustomDamageApply(newAmount, newType, newIFrames)
        }
    }
}