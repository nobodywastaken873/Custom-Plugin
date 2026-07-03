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
import org.bukkit.event.entity.EntityDeathEvent
import java.util.UUID

class DeathEffectsComponent(
    private val effects: HitEffects
): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "effects" to effects.serialize()
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.DEATH_EFFECTS_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return DeathEffectsComponent(
                HitEffects.deserialize(map["effects"])
            )
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(
            EntityDeathEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->
            val damaged = e.entity
            effects.applyTargetless(damaged, damaged.location)
        })
    }
}