package me.newburyminer.customItems.entity.hiteffects.effect

import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffectDeserialization
import me.newburyminer.customItems.entity.hiteffects.HitEffectType
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

class CustomKnockbackApply(val vec: Vector): HitEffect {

    constructor(x: Double, y: Double, z: Double): this(Vector(x, y, z))
    constructor(x: Int, y: Int, z: Int): this(Vector(x, y, z))

    override fun apply(victim: LivingEntity, damager: Entity, sourceLoc: Location?) {
        val source = sourceLoc?.clone() ?: damager.location
        if (victim.location.subtract(source).length() < 0.001) return

        val direction = victim.location.subtract(source).toVector().setY(0).normalize()
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
    companion object: HitEffectDeserialization {
        override val componentType: HitEffectType = HitEffectType.CUSTOM_KNOCKBACK
        override fun deserialize(map: Map<String, Any>): HitEffect {
            return CustomKnockbackApply(
                Vector(
                    map["x"].asDouble(),
                    map["y"].asDouble(),
                    map["z"].asDouble(),
                )
            )
        }
    }
}