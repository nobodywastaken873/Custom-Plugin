package me.newburyminer.customItems.items.armorsets

import me.newburyminer.customItems.Utils.Companion.getArmorSet
import me.newburyminer.customItems.eventbus.EventRegistry
import me.newburyminer.customItems.eventbus.ListenerEntry
import org.bukkit.entity.Player
import org.bukkit.event.Event
import kotlin.reflect.KClass

interface ArmorSetBehavior {
    val set: ArmorSet
    fun <T: Event> register(event: KClass<T>, predicate: (T) -> Boolean, handler: (T) -> Unit) {
        EventRegistry.register(ListenerEntry(event, predicate, handler))
    }

    fun getPieces(player: Player, armorSet: ArmorSet): Int {
        return player.inventory.armorContents.count { it?.getArmorSet() == armorSet }
    }

    val period: Int
        get() = 20
    fun runTask(player: Player) {}
}