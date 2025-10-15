package me.newburyminer.customItems.helpers.damage

import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

class ConstantKnockback(private val vec: Vector): KnockbackSettings {
    override fun getKnockback(livingEntity: LivingEntity): Vector {
        return vec
    }
}