package me.newburyminer.customItems.entity.hiteffects.effect

import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffectType
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

class CustomKnockbackApply(val vec: Vector): HitEffect {
    override val hitEffectType: HitEffectType = HitEffectType.CUSTOM_KNOCKBACK

    override fun apply(victim: LivingEntity, damager: Entity) {
        val direction = victim.location.subtract(damager.location).toVector().normalize()
        val knockback = Vector(direction.x * vec.x, vec.y, direction.z * vec.z)
        victim.velocity = victim.velocity.add(knockback)
    }

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "x" to vec.x,
            "y" to vec.y,
            "z" to vec.z
        )
    }
    override fun deserialize(map: Map<String, Any>): HitEffect {
        return CustomKnockbackApply(Vector(
            map["x"] as Double,
            map["y"] as Double,
            map["z"] as Double,
        ))
    }
}