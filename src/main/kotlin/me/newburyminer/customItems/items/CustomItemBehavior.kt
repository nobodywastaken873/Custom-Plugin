package me.newburyminer.customItems.items

import me.newburyminer.customItems.eventbus.EventRegistry
import me.newburyminer.customItems.eventbus.ListenerEntry
import org.bukkit.entity.Player
import org.bukkit.event.Event
import kotlin.reflect.KClass

interface CustomItemBehavior {
    val extraTasks: Map<Int, (Player) -> Unit> get() = emptyMap()

    fun <T: Event> register(event: KClass<T>, predicate: (T) -> Boolean, handler: (T) -> Unit) {
        EventRegistry.register(ListenerEntry(event, predicate, handler))
    }
}