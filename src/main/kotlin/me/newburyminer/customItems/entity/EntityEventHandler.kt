package me.newburyminer.customItems.entity

import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBehavior
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.EventItemType
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

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


}