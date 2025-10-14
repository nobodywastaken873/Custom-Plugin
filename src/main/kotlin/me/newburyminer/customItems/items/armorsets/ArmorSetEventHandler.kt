package me.newburyminer.customItems.items.armorsets

import me.newburyminer.customItems.Utils.Companion.getArmorSet
import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBehavior
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.EventItemType
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.ItemStack

class ArmorSetEventHandler: Listener {
    companion object {
        private val behaviors: MutableMap<ArmorSet, ArmorSetBehavior> = mutableMapOf()

        fun register(armorSet: ArmorSet, behavior: ArmorSetBehavior) {
            behaviors[armorSet] = behavior
        }

        private fun dispatch(
            player: Player,
            event: Event,
            source: Entity? = null,
            target: Entity? = null
        ) {
            val setMap = mutableMapOf<ArmorSet, Int>()
            val armor = player.inventory.armorContents

            for (item in armor) {
                if (item == null) continue
                val armorSet = item.getArmorSet() ?: continue
                setMap[armorSet] = (setMap[armorSet] ?: 0) + 1
            }

            for ((set, count) in setMap) {
                val behavior = behaviors[set] ?: continue
                val context  = ArmorSetEventContext(player, event, count)
                behavior.handle(context)
            }
        }

    }

    @EventHandler fun onPotionApply(e: EntityPotionEffectEvent) {
        val player = e.entity as? Player ?: return
        dispatch(player, e)
    }

    @EventHandler fun onPlayerSneak(e: PlayerToggleSneakEvent) {
        dispatch(e.player, e)
    }
}