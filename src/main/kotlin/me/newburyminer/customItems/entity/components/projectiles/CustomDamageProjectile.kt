package me.newburyminer.customItems.entity.components.projectiles

import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.projectileshooters.ProjectileDamageShooter
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class CustomDamageProjectile(private val damage: HitEffects): EntityComponent {
    override val componentType: EntityComponentType = EntityComponentType.CUSTOM_DAMAGE_PROJECTILE

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "damage" to damage.serialize(),
        )
    }
    override fun deserialize(map: Map<String, Any>): EntityComponent {
        val newDamage = HitEffects.deserialize(map["damage"])
        return ProjectileDamageShooter(newDamage)
    }

    override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (e.damager != wrapper.entity) return
                val player = e.entity as? Player ?: return

                e.isCancelled = true
                damage.apply(player, e.damager)
            }

        }
    }
}