package me.newburyminer.customItems.effects

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityToggleGlideEvent

class EffectEventHandler: Listener {

    companion object {
        private val behaviors: MutableMap<CustomEffectType, EffectBehavior> = mutableMapOf()

        fun register(type: CustomEffectType, behavior: EffectBehavior) {
            behaviors[type] = behavior
        }

        private fun dispatch(
            player: Player,
            effect: EffectManager.ActiveEffect,
            event: Event,
        ) {
            val type = effect.type
            val behavior = behaviors[type] ?: return
            behavior.handle(
                PotionEventContext(
                    player = player,
                    active = effect,
                    event = event,
                )
            )
        }
    }

    @EventHandler fun onPlayerElytra(e: EntityToggleGlideEvent) {
        val player = e.entity as? Player ?: return
        val effects = EffectManager.getActiveEffects(player)
        effects.forEach { dispatch(player, it, e) }
    }

}