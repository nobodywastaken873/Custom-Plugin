package me.newburyminer.customItems.entity.components.projectiles

import me.newburyminer.customItems.entity.DeserializationInterface
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

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "change" to angleChange,
            "target" to target.uniqueId.toString()
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.HOMING_PROJECTILE
        override fun deserialize(map: Map<String, Any>): EntityComponent? {
            val newChange = map["change"] as Double
            val uuid = UUID.fromString(map["target"] as String)
            val target = Bukkit.getEntity(uuid) ?: return null
            return HomingProjectile(newChange, target)
        }
    }

    override fun tick(wrapper: EntityWrapper) {
        if (!target.isValid) { wrapper.entity.remove(); return }
        if (wrapper.entity is Arrow && wrapper.entity.isInBlock) { wrapper.entity.remove(); return }

        val projectile = wrapper.entity
        val cross = projectile.velocity.getCrossProduct(target.location.subtract(projectile.location).toVector())
        val angle = projectile.velocity.angle(target.location.subtract(projectile.location).toVector())
        val newDirection = projectile.velocity.rotateAroundAxis(cross, angle.coerceAtMost(angleChange.toFloat()).toDouble())
        projectile.velocity = newDirection

    }
}