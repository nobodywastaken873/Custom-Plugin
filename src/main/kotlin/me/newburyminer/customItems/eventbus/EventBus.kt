package me.newburyminer.customItems.eventbus

import me.newburyminer.customItems.CustomItems
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.plugin.EventExecutor
import kotlin.reflect.KClass

object EventBus: Listener {

    fun <T: Event> createDistributor(eventType: KClass<T>) {
        CustomItems.plugin.server.pluginManager.registerEvent(
            eventType.java,
            EventBus,
            EventPriority.NORMAL,
            EventDistributor(eventType),
            CustomItems.plugin
        )
    }

    private class EventDistributor(val filterType: KClass<out Event>): EventExecutor {
        override fun execute(listener: Listener, event: Event) {
            if (!filterType.java.isAssignableFrom(event.javaClass)) return
            onEvent(event, filterType)
        }
    }

    fun <T: Event> onEvent(event: T, filterType: KClass<*>) {
        val listeners = (EventRegistry.getAllRegisteredListeners()[filterType] ?: return).toList()
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