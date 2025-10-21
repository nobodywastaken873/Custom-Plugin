package me.newburyminer.customItems.entity

import org.bukkit.event.Listener


class EntitySpawnManager: Listener {
    companion object {

        private val conversionMap = mutableMapOf<CustomEntity, CustomEntityDefinition>()

        fun register(customEntity: CustomEntity, definition: CustomEntityDefinition) {
            conversionMap[customEntity] = definition
        }

    }

}