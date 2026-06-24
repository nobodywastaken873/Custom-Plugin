package me.newburyminer.customItems.entity.components.projectileshooters

import me.newburyminer.customItems.entity.*
import me.newburyminer.customItems.entity.components.projectiles.HomingProjectile
import me.newburyminer.customItems.helpers.HomingSystem
import org.bukkit.entity.Mob
import org.bukkit.event.entity.ProjectileLaunchEvent

class HomingProjectileShooter(
    private val angleChange: Double,
    private val homingType: HomingSystem.Type
): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "change" to angleChange,
            "homingType" to homingType.name
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.HOMING_PROJECTILE_SHOOTER
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return HomingProjectileShooter(
                map["change"].asDouble(),
                HomingSystem.Type.valueOf(map["homingType"].asString())
            )
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(ProjectileLaunchEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity.shooter == wrapper.entity
        },
        {e ->
            val shooter = wrapper.entity as Mob
            val target = shooter.target ?: return@register

            EntityWrapperManager.getWrapperorNew(e.entity)
                .addComponent(HomingProjectile(
                    angleChange,
                    homingType,
                    target
                ))
        })
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {

                val shooter = wrapper.entity as Mob
                val target = shooter.target ?: return

                EntityWrapperManager.getWrapperorNew(e.entity)
                    .addComponent(HomingProjectile(angleChange, target))
            }

        }
    }*/

}