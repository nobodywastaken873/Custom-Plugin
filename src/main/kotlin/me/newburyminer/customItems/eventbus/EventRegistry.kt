package me.newburyminer.customItems.eventbus

import org.bukkit.event.Event
import java.util.*
import kotlin.reflect.KClass

object EventRegistry {

    private val registeredListeners = mutableMapOf<KClass<*>, MutableList<ListenerEntry<*>>>()
    fun getAllRegisteredListeners(): Map<KClass<*>, MutableList<ListenerEntry<*>>> = registeredListeners.toMap()

    fun <T: Event> register(listenerEntry: ListenerEntry<T>): UUID {
        val eventClass = listenerEntry.kClass

        if (!registeredListeners.containsKey(eventClass))
            EventBus.createDistributor(eventClass)

        registeredListeners.getOrPut(eventClass) { mutableListOf() }.add(listenerEntry)
        return listenerEntry.uuid
    }

    fun remove(uuid: UUID) {
        registeredListeners.values.forEach {
            it.removeIf { entry -> entry.uuid == uuid }
        }
    }

}