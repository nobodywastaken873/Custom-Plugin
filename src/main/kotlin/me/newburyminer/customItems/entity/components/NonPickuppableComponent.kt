package me.newburyminer.customItems.entity.components

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType

class NonPickuppableComponent: EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf()
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.NONPICKUPPABLE_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return NonPickuppableComponent()
        }
    }

}