package me.newburyminer.customItems.entity

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent
import com.google.gson.Gson
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.eventbus.EventRegistry
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class EntityWrapperManager: Listener, BukkitRunnable() {

    companion object {
        private val wrappers = mutableMapOf<UUID, EntityWrapper>()

        fun register(uuid: UUID, wrapper: EntityWrapper) {
            wrappers[uuid] = wrapper
        }

        private fun remove(uuid: UUID) {
            wrappers.remove(uuid)
        }

        fun getWrapper(uuid: UUID): EntityWrapper? {
            return wrappers[uuid]
        }

        fun getWrapperorNew(entity: Entity): EntityWrapper {
            val current = wrappers[entity.uniqueId]
            if (current != null) {return current}

            val new = EntityWrapper(entity)
            register(entity.uniqueId, new)
            return new
        }

        fun removeWrapper(entity: Entity) {
            // Remove all listeners for an entity
            EventRegistry.remove(entity.uniqueId)
            // Check if uuid in wrapper map, serialize, add tag, remove from map
            val wrapper = getWrapper(entity.uniqueId) ?: return
            val map = wrapper.serialize()
            val jsonText = Gson().toJson(map)
            entity.setTag("wrapperjson", jsonText)
            remove(entity.uniqueId)
        }

        fun removeAllWrappers() {
            for ((uuid, _) in wrappers) {
                val entity = Bukkit.getEntity(uuid) ?: continue
                removeWrapper(entity)
            }
        }
    }

    @EventHandler fun entityRemove(e: EntityRemoveFromWorldEvent) {
        removeWrapper(e.entity)
    }
    // Always store values if it is in the wrapper thing

    @Suppress("UNCHECKED_CAST")
    // Only register if it has the tag data to do so, on spawn will register a wrapper
    @EventHandler(priority = EventPriority.LOWEST) fun entityAdd(e: EntityAddToWorldEvent) {
        // Check if it has tag, deserialize, remove tag?, register to map
        val entity = e.entity as? LivingEntity ?: return
        val jsonText = entity.getTag<String>("wrapperjson") ?: return
        val map = Gson().fromJson(jsonText, Map::class.java) as Map<String, Any>
        val wrapper = EntityWrapper.deserialize(map, entity) ?: return
        register(entity.uniqueId, wrapper)
    }

    override fun run() {
        wrappers.forEach { it.value.tick() }
    }

}