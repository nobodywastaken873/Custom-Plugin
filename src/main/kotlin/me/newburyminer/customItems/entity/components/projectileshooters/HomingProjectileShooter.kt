package me.newburyminer.customItems.entity.components.projectileshooters

import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.CustomDamageProjectile
import me.newburyminer.customItems.entity.components.projectiles.ElytraBreakerFirework
import me.newburyminer.customItems.entity.components.projectiles.HomingProjectile
import me.newburyminer.customItems.entity.components.utils.CooldownInterface
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity3.CustomEntity
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Sound
import org.bukkit.entity.Firework
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileLaunchEvent

class HomingProjectileShooter(private val angleChange: Double): EntityComponent {
    override val componentType: EntityComponentType = EntityComponentType.HOMING_PROJECTILE_SHOOTER

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "change" to angleChange,
        )
    }
    override fun deserialize(map: Map<String, Any>): EntityComponent {
        val newChange = map["change"] as Double
        return HomingProjectileShooter(newChange)
    }

    override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {

                val shooter = wrapper.entity as Mob
                val target = shooter.target ?: return

                EntityWrapperManager.getWrapperorNew(e.entity)
                    .addComponent(HomingProjectile(angleChange, target))
            }

        }
    }

}