package me.newburyminer.customItems.entity.components.melee

import com.destroystokyo.paper.ParticleBuilder
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomDamageType.Companion.isCustom
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent

class SuicideBomberComponent(val hitEffects: HitEffects): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "hiteffects" to hitEffects.serialize()
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.SUICIDE_BOMBER_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            val hiteffects = HitEffects.deserialize(map["hiteffects"])
            return SuicideBomberComponent(hiteffects)
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
            val damager = e.damager as? LivingEntity ?: return@register
            damager.damage(1000.0)

            hitEffects.apply(e.entity as LivingEntity, e.damager)
        })
    }

    override fun tick(wrapper: EntityWrapper) {
        if (wrapper.entity.ticksLived % 4 == 0) {
            ParticleBuilder(Particle.SMOKE)
                .location(wrapper.entity.location.add(0.0, 0.3 + wrapper.entity.height, 0.0))
                .receivers(60)
                .spawn()
        }
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