package me.newburyminer.customItems.effects.behaviors

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.effects.EffectBehavior
import me.newburyminer.customItems.effects.PotionEventContext
import org.bukkit.Bukkit
import org.bukkit.event.entity.EntityToggleGlideEvent

class ElytraDisableEffect: EffectBehavior {
    override fun handle(ctx: PotionEventContext) {
        when (val e = ctx.event) {

            is EntityToggleGlideEvent -> {
                if (!e.isGliding) return
                val player = ctx.player
                Bukkit.getScheduler().runTaskLater(CustomItems.plugin, Runnable {
                    player.isGliding = false
                }, 1)
            }

        }
    }

}