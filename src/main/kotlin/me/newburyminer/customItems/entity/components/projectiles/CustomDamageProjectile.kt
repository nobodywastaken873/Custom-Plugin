package me.newburyminer.customItems.entity.components.projectiles

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.projectileshooters.ProjectileDamageShooter
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomDamageType.Companion.isCustom
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent

class CustomDamageProjectile(private val damage: HitEffects): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "damage" to damage.serialize(),
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.CUSTOM_DAMAGE_PROJECTILE
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            val newDamage = HitEffects.deserialize(map["damage"])
            return ProjectileDamageShooter(newDamage)
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(EntityDamageByEntityEvent::class, wrapper.entity.uniqueId, { e ->
            e.damager == wrapper.entity &&
            !e.damageSource.damageType.isCustom()
        },
        {e ->
            val damaged = e.entity as? LivingEntity ?: return@register
            e.isCancelled = true

            //println("applying damage 1")
            damage.apply(damaged, e.damager)
            wrapper.entity.remove()
            //println("finished applying and removing 5")
        })
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (e.damager != wrapper.entity) return
                val damaged = e.entity as? LivingEntity ?: return
                if (damaged.getTag<Boolean>("damaged") == true) { damaged.setTag("damaged", false); return }

                e.isCancelled = true
                damage.apply(damaged, e.damager)
                wrapper.entity.remove()
            }

        }
    }*/
}