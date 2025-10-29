package me.newburyminer.customItems.entity.components

import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import org.bukkit.event.entity.EntityDamageByEntityEvent

class CustomDamage(val damage: Double): EntityComponent {
    override val componentType: EntityComponentType = EntityComponentType.CUSTOM_DAMAGE
    override fun serialize(): Map<String, Any> {
        return mapOf(
            "damage" to damage
        )
    }
    override fun deserialize(map: Map<String, Any>): EntityComponent {
        val newDamage = map["damage"] as Double
        return CustomDamage(newDamage)
    }

    override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (e.damager != wrapper.entity) return
                e.damage = damage
            }

        }
    }

}