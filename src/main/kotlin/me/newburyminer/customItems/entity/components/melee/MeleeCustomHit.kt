package me.newburyminer.customItems.entity.components.melee

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomDamageType.Companion.isCustom
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent

class MeleeCustomHit(val hitEffects: HitEffects): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "hiteffects" to hitEffects.serialize()
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.MELEE_CUSTOM_HIT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            val hiteffects = HitEffects.deserialize(map["hiteffects"])
            return MeleeCustomHit(hiteffects)
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(
            EntityDamageByEntityEvent::class, wrapper.entity.uniqueId, { e ->
            e.damager == wrapper.entity &&
            !e.damageSource.damageType.isCustom() &&
            e.damageSource.damageType != DamageType.EXPLOSION &&
            e.damageSource.damageType != DamageType.PLAYER_EXPLOSION
        },
        {e ->
            e.isCancelled = true
            hitEffects.apply(e.entity as LivingEntity, e.damager)
        })
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (e.entity != wrapper.entity) return
                if (e.entity.getTag<Boolean>("damaged") == true) { e.entity.setTag("damaged", false); return }

                e.isCancelled = true
                hitEffects.apply(e.entity as LivingEntity, e.damager)
            }

        }
    }*/
}