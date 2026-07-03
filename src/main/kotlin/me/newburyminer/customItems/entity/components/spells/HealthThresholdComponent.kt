package me.newburyminer.customItems.entity.components.spells

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.entity.EntityDamageEvent
import java.util.UUID

class HealthThresholdComponent(
    private val effects: HitEffects,
    private val thresholdActivation: Double = 0.25
): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "effects" to effects.serialize(),
            "thresholdActivation" to thresholdActivation
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.HEALTH_THRESHOLD_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return HealthThresholdComponent(
                HitEffects.deserialize(map["effects"]),
                map["thresholdActivation"].asDouble()
            )
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(
            EntityDamageEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->
            val damaged = e.entity as? LivingEntity ?: return@register

            if (damaged.health > thresholdActivation * damaged.getAttribute(Attribute.MAX_HEALTH)!!.value) return@register
            effects.apply(damaged, damaged)
        })
    }
}