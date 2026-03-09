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

}