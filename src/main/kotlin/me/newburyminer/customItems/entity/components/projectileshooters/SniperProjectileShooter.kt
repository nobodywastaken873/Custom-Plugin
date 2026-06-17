package me.newburyminer.customItems.entity.components.projectileshooters

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.utils.CooldownInterface
import me.newburyminer.customItems.entity.components.utils.ProjectileType
import me.newburyminer.customItems.entity.components.utils.SpellInterface
import org.bukkit.Bukkit
import org.bukkit.entity.Mob

class SniperProjectileShooter(private val baseCooldown: Int, private val projectileType: ProjectileType): EntityComponent, CooldownInterface, SpellInterface {

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
                map["baseCooldown"].toInt(),
                ProjectileType.valueOf(map["projectileType"].toString()),
            )
        }
    }

    override val spellDuration: Int = 30
    override var cooldown: Int = 100
    private var remainingCastTicks = 0

    override fun tick(wrapper: EntityWrapper) {
        if (remainingCastTicks > 0) {
            remainingCastTicks--

            if (remainingCastTicks <= 0) {

                val mob = wrapper.entity as? Mob ?: return
                val targetLoc = mob.target?.location ?: return

                val clazz = projectileType.clazz
                val projectile = mob.launchProjectile(clazz)
                val velocity = targetLoc.subtract(mob.location).toVector()
                projectile.velocity = velocity

                applyCooldown(baseCooldown)
            }

        }

        else if (Bukkit.getCurrentTick() % 10 == 0) {
            reduceCooldown(10)
            if (!offCooldown()) return

            val shooter = wrapper.entity as? Mob ?: return
            val target = shooter.target ?: return
            if (!shooter.hasLineOfSight(target)) return

            if (startCasting(wrapper)) {
                remainingCastTicks = spellDuration
            }
        }
    }
}