package me.newburyminer.customItems.items.armorsets.behaviours

import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.armorsets.ArmorSet
import me.newburyminer.customItems.items.armorsets.ArmorSetBehavior
import me.newburyminer.customItems.items.armorsets.ArmorSetEventContext
import org.bukkit.Sound
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class TankBehavior : ArmorSetBehavior {

    override val set: ArmorSet = ArmorSet.TANK

    override fun handle(ctx: ArmorSetEventContext) {
        when (val e = ctx.event) {

            is PlayerToggleSneakEvent -> {
                if (ctx.pieceCount != 4) return
                val player = ctx.player

                if (!e.isSneaking) return
                if (!player.offCooldown(CustomItem.TURTLE_SHELL)) return
                player.absorptionAmount = (player.absorptionAmount + 20).coerceAtMost(20.0)
                for (custom in arrayOf(CustomItem.HARD_HAT, CustomItem.TURTLE_SHELL, CustomItem.ENCRUSTED_PANTS, CustomItem.STEEL_TOED_BOOTS)) {
                    player.setCooldown(custom, 60.0)
                }
                CustomEffects.playSound(player.location, Sound.BLOCK_BEACON_ACTIVATE, 1.0F, 0.8F)
            }

        }
    }

}