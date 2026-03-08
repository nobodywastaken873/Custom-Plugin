package me.newburyminer.customItems.items.armorsets.behaviours

import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.armorsets.ArmorSet
import me.newburyminer.customItems.items.armorsets.ArmorSetBehavior
import org.bukkit.Sound
import org.bukkit.event.player.PlayerToggleSneakEvent

class TankBehavior : ArmorSetBehavior {

    override val set: ArmorSet = ArmorSet.TANK

    init {
        register(PlayerToggleSneakEvent::class, { e ->
            getPieces(e.player, set) == 4 &&
            e.isSneaking &&
            e.player.offCooldown(CustomItem.TURTLE_SHELL)
        },
        {e ->
            val player = e.player
            player.absorptionAmount = (player.absorptionAmount + 20).coerceAtMost(20.0)
            for (custom in arrayOf(CustomItem.HARD_HAT, CustomItem.TURTLE_SHELL, CustomItem.ENCRUSTED_PANTS, CustomItem.STEEL_TOED_BOOTS)) {
                player.setCooldown(custom, 60.0)
            }
            CustomEffects.playSound(player.location, Sound.BLOCK_BEACON_ACTIVATE, 1.0F, 0.8F)
        })
    }

}