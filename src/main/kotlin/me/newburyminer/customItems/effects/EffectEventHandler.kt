package me.newburyminer.customItems.effects

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityToggleGlideEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

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

    private val storedEffects = mutableMapOf<UUID, List<EffectManager.ActiveEffect>>()

    @EventHandler fun onPlayerLogout(e: PlayerQuitEvent) {
        val activeEffects = EffectManager.getActiveEffects(e.player)
        storedEffects[e.player.uniqueId] = activeEffects
        EffectManager.removeEffect(e.player)
    }

    @EventHandler fun onPlayerJoin(e: PlayerJoinEvent) {
        val activeEffects = storedEffects[e.player.uniqueId] ?: return
        activeEffects.forEach {
            val newData = EffectData(it.remaining, it.data.attributeData, it.data.unique)
            EffectManager.applyEffect(e.player, it.type, newData)
        }
    }

    @EventHandler fun onPlayerDeath(e: PlayerDeathEvent) {
        if (e.isCancelled) return
        EffectManager.removeEffect(e.player)
    }

    @EventHandler fun onPlayerElytra(e: EntityToggleGlideEvent) {
        val player = e.entity as? Player ?: return
        val effects = EffectManager.getActiveEffects(player)
        effects.forEach { dispatch(player, it, e) }
    }

}