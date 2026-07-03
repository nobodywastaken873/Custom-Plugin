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
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.ProjectileHitEvent

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

            val damager =
                if (e.damager is Projectile) (e.damager as Projectile).shooter as? Entity ?: return@register
                else e.damager

            //println("applying damage 1")
            damage.apply(damaged, damager)
            wrapper.entity.remove()
            //println("finished applying and removing 5")
        })

        register(ProjectileHitEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity == wrapper.entity
        },
        {e ->
            val shooter = e.entity.shooter
            val hit = e.hitEntity
            // No entity is hit or an entity is hit but has iframes
            if (shooter is Player && e.hitEntity != null) {
                damage.applyTargetless(shooter, e.entity.location)
            }
            // If it hits not a player, if it hits the ground, then trigger the targetless
            else if (e.hitEntity !is Player || e.hitEntity == null) {
                damage.applyTargetless(shooter as? Entity ?: return@register, e.entity.location)
            }

            e.isCancelled = true
            wrapper.entity.remove()
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