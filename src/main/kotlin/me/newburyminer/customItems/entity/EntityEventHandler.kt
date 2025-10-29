package me.newburyminer.customItems.entity

import org.bukkit.entity.Entity
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
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

    @EventHandler fun onDamage(e: EntityDamageByEntityEvent) {
        dispatch(e.entity, e)
        dispatch(e.damager, e)
    }

    @EventHandler fun onDeath(e: EntityDeathEvent) {
        dispatch(e.entity, e)
    }


}