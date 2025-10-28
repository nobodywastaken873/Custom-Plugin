package me.newburyminer.customItems.entity

import org.bukkit.entity.Entity
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent

class EntityEventHandler: Listener {
    companion object {

        private fun dispatch(
            entity: Entity,
            event: Event,
        ) {

            val wrapper = EntityWrapperManager.getWrapper(entity.uniqueId) ?: return
            wrapper.handle(EntityEventContext(entity, event))

        }
    }

    @EventHandler fun onCreeperExplode(e: EntityExplodeEvent) {

    }


}