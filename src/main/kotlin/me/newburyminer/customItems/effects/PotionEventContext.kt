package me.newburyminer.customItems.effects

import org.bukkit.entity.Player
import org.bukkit.event.Event

data class PotionEventContext(
    val player: Player,
    val active: EffectManager.ActiveEffect,
    val event: Event
)
