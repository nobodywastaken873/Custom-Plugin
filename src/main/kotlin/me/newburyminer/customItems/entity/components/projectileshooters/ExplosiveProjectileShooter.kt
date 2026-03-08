package me.newburyminer.customItems.entity.components.projectileshooters

import me.newburyminer.customItems.entity.*
import me.newburyminer.customItems.entity.components.projectiles.ExplosiveProjectile
import org.bukkit.event.entity.ProjectileLaunchEvent

class ExplosiveProjectileShooter(private val power: Float, private val setFire: Boolean, private val breakBlocks: Boolean = false): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "power" to power,
            "setfire" to setFire,
            "breakblocks" to breakBlocks
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.EXPLOSIVE_PROJECTILE_SHOOTER
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            val newPower = map["power"].toFloat()
            val newSetFire = map["setfire"].toBoolean()
            val newBreakBlocks = map["breakblocks"].toBoolean()
            return ExplosiveProjectile(newPower, newSetFire, newBreakBlocks)
        }
    }

    override fun registerListeners(wrapper: EntityWrapper) {
        register(ProjectileLaunchEvent::class, wrapper.entity.uniqueId, { e ->
            e.entity.shooter == wrapper.entity
        },
        {e ->
            EntityWrapperManager.getWrapperorNew(e.entity)
                .addComponent(ExplosiveProjectile(power, setFire, breakBlocks))
        })
    }

    /*override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {
                EntityWrapperManager.getWrapperorNew(e.entity)
                    .addComponent(ExplosiveProjectile(power, setFire, breakBlocks))
            }

        }
    }*/

}