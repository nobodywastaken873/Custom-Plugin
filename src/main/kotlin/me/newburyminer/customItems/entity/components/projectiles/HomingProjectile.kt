package me.newburyminer.customItems.entity.components.projectiles

import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.projectileshooters.HomingProjectileShooter
import org.bukkit.Bukkit
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.util.UUID

class HomingProjectile(private val angleChange: Double, private val target: Entity): EntityComponent {
    override val componentType: EntityComponentType = EntityComponentType.HOMING_PROJECTILE

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "change" to angleChange,
            "target" to target.uniqueId.toString()
        )
    }
    override fun deserialize(map: Map<String, Any>): EntityComponent? {
        val newChange = map["change"] as Double
        val uuid = UUID.fromString(map["target"] as String)
        val target = Bukkit.getEntity(uuid) ?: return null
        return HomingProjectile(newChange, target)
    }

    override fun tick(wrapper: EntityWrapper) {
        if (!target.isValid) { wrapper.entity.remove(); return }
        if (wrapper.entity is Arrow && wrapper.entity.isInBlock) { wrapper.entity.remove(); return }
        val currentVelocity = wrapper.entity.velocity.length() * 1.05

        val newDirection = wrapper.entity.velocity
            .add(
                target.location.subtract(wrapper.entity.location)
                    .toVector()
                    .add(Vector(0.0, 0.5, 0.0))
                    .normalize()
                    .multiply(50)
            )
        wrapper.entity.velocity = newDirection.normalize().multiply(currentVelocity)
    }
}