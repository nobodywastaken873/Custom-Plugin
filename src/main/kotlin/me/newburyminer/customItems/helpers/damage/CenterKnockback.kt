package me.newburyminer.customItems.helpers.damage

import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

class CenterKnockback(private val center: Location, private val magnitude: Double, private val extra: Vector = Vector()): KnockbackSettings {
    override fun getKnockback(livingEntity: LivingEntity): Vector {
        return livingEntity.location.subtract(center.clone()).toVector().normalize().multiply(magnitude).add(extra)
    }
}