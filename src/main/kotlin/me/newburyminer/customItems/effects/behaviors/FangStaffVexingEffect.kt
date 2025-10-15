package me.newburyminer.customItems.effects.behaviors

import me.newburyminer.customItems.effects.EffectBehavior
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Particle
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class FangStaffVexingEffect: EffectBehavior {
    override val period: Int
        get() = 10

    override fun onTick(player: Player) {
        val centerLoc = player.location.clone().add(Vector(0.0, 1.6, 0.0))
        CustomEffects.particleSphere(Particle.ENCHANTED_HIT.builder(), centerLoc, 5.5, 60)
        for (entity in player.getNearbyEntities(7.0, 10.0, 7.0)) {
            if (entity !is LivingEntity) continue
            if (entity.location.subtract(centerLoc).length() <= 5.5) {
                entity.damage(13.0, DamageSource.builder(DamageType.PLAYER_ATTACK).withDirectEntity(player).withCausingEntity(player).build())
                entity.noDamageTicks = 0
            }
        }
    }
}