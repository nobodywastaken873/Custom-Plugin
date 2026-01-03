package me.newburyminer.customItems.eventbus

import me.newburyminer.customItems.CustomItems
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

object EventBus: Listener {

    fun <T: Event> createDistributor(eventType: KClass<T>) {
        CustomItems.plugin.server.pluginManager.registerEvent(
            eventType.java,
            EventBus,
            EventPriority.NORMAL,
            EventDistributor(),
            CustomItems.plugin
        )
    }

    private class EventDistributor: EventExecutor {
        override fun execute(listener: Listener, event: Event) {
            onEvent(event)
        }
    }

    fun <T: Event> onEvent(event: T) {
        val listeners = EventRegistry.getAllRegisteredListeners()[event::class] ?: return
        if (listeners.isEmpty()) {return}

        // Collection of lists of ListenerEntries
        listeners.forEach { entry ->
            @Suppress("UNCHECKED_CAST")
            val castedEntry = entry as ListenerEntry<T>
            if (entry.predicate(event)) {
                entry.handler(event)
            }
        }
    }

}