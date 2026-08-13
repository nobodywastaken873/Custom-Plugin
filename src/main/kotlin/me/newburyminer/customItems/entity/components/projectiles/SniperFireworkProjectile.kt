package me.newburyminer.customItems.entity.components.projectiles

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.FireworkExplodeEvent

class SniperFireworkProjectile(private val scalingPerBlock: Double): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "scalingperblock" to scalingPerBlock,
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.SNIPER_FIREWORK_PROJECTILE
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return SniperFireworkProjectile(map["scalingperblock"].asDouble())
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(FireworkExplodeEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->
            val firework = e.entity
            val ticksFlown = firework.ticksFlown

            // ~9 blocks per tick
            val damage = HitEffects(CustomDamageApply(ticksFlown * 9 * scalingPerBlock, CustomDamageType.EXPLOSION_NO_CD, overrideSource = firework.shooter as Entity?))

            for (entity in firework.location.getNearbyEntities(3.0, 3.0, 3.0)) {
                if (entity is LivingEntity) damage.apply(entity, e.entity)
            }

            CustomEffects.playSound(firework.location, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1.0F, 1.3F)

            wrapper.entity.remove()
        })
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is FireworkExplodeEvent -> {

                val firework = e.entity
                val ticksFlown = firework.ticksFlown

                // ~9 blocks per tick
                val damage = HitEffects(CustomDamageApply(ticksFlown * 9 * scalingPerBlock, DamageType.EXPLOSION, overrideSource = firework.shooter as Entity?))

                for (entity in firework.location.getNearbyEntities(3.0, 3.0, 3.0)) {
                    if (entity is LivingEntity) damage.apply(entity, e.entity)
                }

                wrapper.entity.remove()
            }

        }
    }*/
}