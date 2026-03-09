package me.newburyminer.customItems.effects.behaviors

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectBehavior
import me.newburyminer.customItems.effects.EffectManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityToggleGlideEvent

class ElytraDisableEffect: EffectBehavior {

    override fun registerListeners() {
        register(EntityToggleGlideEvent::class, {e ->
            e.entity is Player &&
            EffectManager.hasEffect(e.entity as Player, CustomEffectType.ELYTRA_DISABLED) &&
            e.isGliding
        },
        {e ->
            Bukkit.getScheduler().runTaskLater(CustomItems.plugin, Runnable {
                (e.entity as Player).isGliding = false
            }, 1)
        })
    }

}