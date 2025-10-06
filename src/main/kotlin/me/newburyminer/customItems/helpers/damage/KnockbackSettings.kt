package me.newburyminer.customItems.helpers.damage

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector

interface KnockbackSettings {
    fun getKnockback(livingEntity: LivingEntity): Vector
}