package me.newburyminer.customItems.eventbus

import org.bukkit.event.Event
import kotlin.reflect.KClass

interface EventRegistrar {
    fun registerListeners() {}
    fun <T: Event> register(event: KClass<T>, predicate: (T) -> Boolean, handler: (T) -> Unit) {
        EventRegistry.register(ListenerEntry(event, predicate, handler))
    }
}