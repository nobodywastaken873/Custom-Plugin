package me.newburyminer.customItems.entity.components.projectiles

import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityEventContext
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.projectileshooters.HomingProjectileShooter
import me.newburyminer.customItems.entity.components.utils.DetonationInterface
import org.bukkit.Bukkit
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.util.Vector

class ExplosiveProjectile(private val power: Float, private val setFire: Boolean, private val breakBlocks: Boolean = false): EntityComponent, DetonationInterface {
    override val componentType: EntityComponentType = EntityComponentType.EXPLOSIVE_PROJECTILE

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "power" to power,
            "setfire" to setFire,
            "breakblocks" to breakBlocks
        )
    }
    override fun deserialize(map: Map<String, Any>): EntityComponent {
        val newPower = map["power"] as Float
        val newSetFire = map["setfire"] as Boolean
        val newBreakBlocks = map["breakblocks"] as Boolean
        return ExplosiveProjectile(newPower, newSetFire, newBreakBlocks)
    }

    override fun handle(ctx: EntityEventContext, wrapper: EntityWrapper) {
        when (val e = ctx.event) {
            is ProjectileHitEvent -> {
                detonate(e.entity, power, setFire, breakBlocks)
            }
        }
    }
}