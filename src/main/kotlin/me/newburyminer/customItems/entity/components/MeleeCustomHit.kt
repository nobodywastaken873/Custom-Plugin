package me.newburyminer.customItems.entity.components

import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent

class MeleeCustomHit(val hitEffects: HitEffects): EntityComponent {
    override val componentType: EntityComponentType = EntityComponentType.MELEE_CUSTOM_HIT

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "hiteffects" to hitEffects.serialize()
        )
    }
    override fun deserialize(map: Map<String, Any>): EntityComponent {
        val hiteffects = HitEffects.deserialize(map["hiteffects"])
        return MeleeCustomHit(hiteffects)
    }

    override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (e.entity != wrapper.entity) return
                e.isCancelled = true
                hitEffects.apply(e.entity as LivingEntity, e.damager)
            }

        }
    }
}