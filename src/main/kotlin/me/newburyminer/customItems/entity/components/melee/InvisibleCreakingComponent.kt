package me.newburyminer.customItems.entity.components.melee

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import org.bukkit.GameEvent
import org.bukkit.Location
import org.bukkit.entity.Creaking
import org.bukkit.entity.Creeper
import org.bukkit.event.world.GenericGameEvent
import org.bukkit.util.Vector

class InvisibleCreakingComponent: EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf()
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.INVISIBLE_CREAKING_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return InvisibleCreakingComponent()
        }
    }

    private var previousLocation: Location? = null
    override fun tick(wrapper: EntityWrapper) {
        if (wrapper.entity.ticksLived % 5 == 0) {
            val mob = wrapper.entity as? Creaking ?: return
            if (previousLocation == null) {previousLocation = mob.location; return}

            val distance = mob.location.distance(previousLocation ?: return)
            if (distance < 0.1) {
                mob.isInvisible = true
            } else {
                mob.isInvisible = false
            }
        }
    }
}