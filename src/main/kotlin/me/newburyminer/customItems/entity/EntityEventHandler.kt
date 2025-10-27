package me.newburyminer.customItems.entity

import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.entity2.EntityEventContext
import org.bukkit.entity.Entity
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent

class EntityEventHandler: Listener {
    companion object {
        private val behaviors: MutableMap<CustomEntity, CustomEntityDefinition> = mutableMapOf()

        fun register(customEntity: CustomEntity, behavior: CustomEntityDefinition) {
            behaviors[customEntity] = behavior
        }

        private fun dispatch(
            entity: Entity,
            event: Event,
        ) {
            val custom = entity.getCustom() ?: return
            val behavior = behaviors[custom] ?: return
            behavior.handle(
                EntityEventContext(
                    entity,
                    event
                )
            )
        }
    }

    @EventHandler fun onCreeperExplode(e: EntityExplodeEvent) {

    }


}