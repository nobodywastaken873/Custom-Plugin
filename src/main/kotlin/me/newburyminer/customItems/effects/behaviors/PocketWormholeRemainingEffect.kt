package me.newburyminer.customItems.effects.behaviors

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.removeTag
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectBehavior
import me.newburyminer.customItems.effects.EffectManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityToggleGlideEvent

class PocketWormholeRemainingEffect: EffectBehavior {

    override fun onRemove(player: Player) {
        player.sendMessage(Utils.text("Teleporting...", Utils.GRAY))
        val loc = player.getTag<Location>("pocketwormholeorigin") ?: return
        player.teleport(loc)
        player.playSound(loc, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1F, 1.2F)
        player.removeTag("pocketwormholeorigin")
    }

}