package me.newburyminer.customItems.effects

import me.newburyminer.customItems.eventbus.EventRegistrar
import org.bukkit.entity.Player

interface EffectBehavior: EventRegistrar {
    fun onApply(player: Player) {}
    fun onRemove(player: Player) {}
    val period: Int get() = 20
    fun onTick(player: Player) {}
}