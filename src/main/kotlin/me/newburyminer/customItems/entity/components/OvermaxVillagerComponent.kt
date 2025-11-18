package me.newburyminer.customItems.entity.components

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityPotionEffectEvent

class OvermaxVillagerComponent: EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf()
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.OVERMAX_VILLAGER_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return OvermaxVillagerComponent()
        }
    }

}