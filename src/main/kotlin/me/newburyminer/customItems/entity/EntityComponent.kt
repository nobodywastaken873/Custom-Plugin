package me.newburyminer.customItems.entity

import me.newburyminer.customItems.eventbus.EventRegistry
import me.newburyminer.customItems.eventbus.ListenerEntry
import org.bukkit.event.Event
import java.util.*
import kotlin.reflect.KClass

interface EntityComponent {

    //val componentType: EntityComponentType
    fun serialize(): Map<String, Any>
    //fun deserialize(map: Map<String, Any>): EntityComponent?
    fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {}
    fun tick(wrapper: EntityWrapper) {}
    fun onAdd(wrapper: EntityWrapper) {}
    fun onCast(wrapper: EntityWrapper) {}
    fun onFinishCast(wrapper: EntityWrapper) {}

    fun registerListeners(wrapper: EntityWrapper) {
        /*EventRegistry.register(ListenerEntry(
            EntityDamageByEntityEvent::class,
            {e -> e.entity == wrapper.entity},
            {e -> e.isCancelled = true},
            wrapper.entity.uniqueId
        ))*/
    }
    fun <T: Event> register(event: KClass<T>, uuid: UUID, predicate: (T) -> Boolean, handler: (T) -> Unit) {
        EventRegistry.register(ListenerEntry(event, predicate, handler, uuid))
    }

}