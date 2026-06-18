package me.newburyminer.customItems.entity.components.projectileshooters

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.utils.AbstractSpellComponent
import me.newburyminer.customItems.entity.components.utils.ProjectileType
import org.bukkit.entity.Mob

class SniperProjectileShooter(baseCooldown: Int, private val projectileType: ProjectileType): AbstractSpellComponent(baseCooldown, 30) {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "baseCooldown" to baseCooldown,
            "projectileType" to projectileType.name
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.SNIPER_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return SniperProjectileShooter(
                map["baseCooldown"].asInt(),
                ProjectileType.valueOf(map["projectileType"].asString()),
            )
        }
    }

    override fun tick(wrapper: EntityWrapper) {
        if (castingTicks > 0) {
            castingTicks--

            if (castingTicks <= 0) {

                val mob = wrapper.entity as? Mob ?: return
                val targetLoc = mob.target?.location ?: return

                val clazz = projectileType.clazz
                val projectile = mob.launchProjectile(clazz)
                val velocity = targetLoc.subtract(mob.location).toVector()
                projectile.velocity = velocity

                applyCooldown(baseCooldown)
            }

        }

        else if (wrapper.entity.ticksLived % 10 == 0) {
            reduceCooldown(10)
            if (!offCooldown()) return
            if (wrapper.isCasting()) return

            val shooter = wrapper.entity as? Mob ?: return
            val target = shooter.target ?: return
            if (!shooter.hasLineOfSight(target)) return

            startCasting(wrapper)
        }
    }
}