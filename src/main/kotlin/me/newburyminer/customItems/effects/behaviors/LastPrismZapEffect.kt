package me.newburyminer.customItems.effects.behaviors

import me.newburyminer.customItems.effects.EffectBehavior
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player

class LastPrismZapEffect: EffectBehavior {

    override val period: Int
        get() = 2

    override fun onTick(player: Player) {
        player.damage(2.0, DamageSource.builder(DamageType.LIGHTNING_BOLT).build())
        player.noDamageTicks = 0
    }

}