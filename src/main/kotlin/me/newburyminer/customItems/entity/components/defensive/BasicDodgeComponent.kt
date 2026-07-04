package me.newburyminer.customItems.entity.components.defensive

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.hasIntersectingBlocks
import org.bukkit.Sound
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.util.Vector

class BasicDodgeComponent(
    private val dodgeRate: Double
): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "dodgeRate" to dodgeRate,
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.BASIC_DODGE_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return BasicDodgeComponent(
                map["dodgeRate"].asDouble(),
            )
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(
            EntityDamageByEntityEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->
            if (e.isCancelled) return@register
            if (Math.random() > dodgeRate) return@register

            e.isCancelled = true
            CustomEffects.playSound(wrapper.entity.location, Sound.ENTITY_BREEZE_DEFLECT, 1.0F, 0.8F)

            for (i in 0..20) {
                val randomOffset = Vector.getRandom().setY(0).normalize().multiply(2.5)
                val newBox = wrapper.entity.boundingBox.shift(randomOffset)

                if (!wrapper.entity.world.hasIntersectingBlocks(newBox)) {
                    wrapper.entity.teleport(wrapper.entity.location.add(randomOffset))
                    break
                }
            }

            // Particle, sound effects
        })
    }

}