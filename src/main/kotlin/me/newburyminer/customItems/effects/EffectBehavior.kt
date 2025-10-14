package me.newburyminer.customItems.effects

import org.bukkit.entity.Player

interface EffectBehavior {
    fun onApply(player: Player) {}
    fun onRemove(player: Player) {}
    val period: Int get() = 20
    fun onTick(player: Player) {}
    fun handle(ctx: PotionEventContext) {}
}