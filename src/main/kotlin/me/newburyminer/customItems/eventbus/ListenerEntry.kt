package me.newburyminer.customItems.eventbus

import org.bukkit.event.Event
import java.util.*
import kotlin.reflect.KClass

class ListenerEntry<T: Event>(
    val kClass: KClass<T>,
    val predicate: (T) -> Boolean,
    val handler: (T) -> Unit,
    val uuid: UUID = UUID.randomUUID(),
) {}