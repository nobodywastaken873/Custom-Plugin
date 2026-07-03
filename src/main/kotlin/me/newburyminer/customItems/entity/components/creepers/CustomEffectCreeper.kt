package me.newburyminer.customItems.entity.components.creepers

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import org.bukkit.entity.Creeper
import org.bukkit.event.entity.EntityExplodeEvent

class CustomEffectCreeper(
    val effects: HitEffects
): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "effects" to effects.serialize()
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.CUSTOM_EFFECT_CREEPER
        override fun deserialize(map: Map<String, Any>): CustomEffectCreeper {
            return CustomEffectCreeper(
                HitEffects.deserialize(map["effects"]),
            )
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(EntityExplodeEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->
            val creeper = e.entity as? Creeper ?: return@register
            effects.applyTargetless(creeper, creeper.location)
        })
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityExplodeEvent -> {
                if (e.entity.getTag<Boolean>("exploding") != true) return
                val creeper = e.entity as? Creeper ?: return
                creeper.addPotionEffect(PotionEffect(
                    type,
                    duration,
                    potency,
                    ambient,
                    showParticles
                ))
            }

        }
    }*/

}