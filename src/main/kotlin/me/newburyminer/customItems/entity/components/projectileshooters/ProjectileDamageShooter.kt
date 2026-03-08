package me.newburyminer.customItems.entity.components.projectileshooters

import me.newburyminer.customItems.entity.*
import me.newburyminer.customItems.entity.components.projectiles.CustomDamageProjectile
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import org.bukkit.event.entity.ProjectileLaunchEvent

class ProjectileDamageShooter(private val damage: HitEffects): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "damage" to damage.serialize(),
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.PROJECTILE_DAMAGE_SHOOTER
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            val newDamage = HitEffects.deserialize(map["damage"])
            return ProjectileDamageShooter(newDamage)
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(ProjectileLaunchEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity.shooter == wrapper.entity
        },
        {e ->
            EntityWrapperManager.getWrapperorNew(e.entity)
                .addComponent(CustomDamageProjectile(damage))
        })
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {
                EntityWrapperManager.getWrapperorNew(e.entity)
                    .addComponent(CustomDamageProjectile(damage))
            }

        }
    }*/

}