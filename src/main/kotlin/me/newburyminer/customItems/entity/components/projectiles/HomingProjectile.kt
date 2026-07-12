package me.newburyminer.customItems.entity.components.projectiles

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.helpers.HomingSystem
import me.newburyminer.customItems.helpers.getUpperCenter
import org.bukkit.Bukkit
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.Marker
import java.util.*

class HomingProjectile(
    private val angleChange: Double,
    private val homingType: HomingSystem.Type,
    private val target: Entity
): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "change" to angleChange,
            "homingType" to homingType.name,
            "target" to target.uniqueId.toString()
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.HOMING_PROJECTILE
        override fun deserialize(map: Map<String, Any>): EntityComponent? {
            return HomingProjectile(
                map["change"].asDouble(),
                HomingSystem.Type.valueOf(map["homingType"].asString()),
                Bukkit.getEntity(UUID.fromString(map["target"].asString())) ?: return null
            )
        }
    }

    override fun tick(wrapper: EntityWrapper) {
        if (!target.isValid || target.world != wrapper.entity.world) { wrapper.entity.remove(); return }
        if (wrapper.entity is Arrow && wrapper.entity.isInBlock) { wrapper.entity.remove(); return }

        val projectile = wrapper.entity
        val currentLocation = projectile.location
        val targetLocation = target.getUpperCenter()
        val targetDirection = targetLocation.clone().subtract(currentLocation).toVector()
        val currentVelocity = projectile.velocity

        projectile.velocity = when (homingType) {
            HomingSystem.Type.BASIC_TURN -> {
                HomingSystem.basicCappedTurn(currentVelocity, targetDirection, angleChange)
            }
            HomingSystem.Type.DISTANCE_SCALED -> {
                HomingSystem.distanceScalingTurn(currentVelocity, targetDirection, angleChange, targetDirection.length())
            }
            HomingSystem.Type.ANGLE_SCALED -> {
                HomingSystem.angleScalingTurn(currentVelocity, targetDirection, angleChange, 0.4)
            }
            HomingSystem.Type.BOTH_SCALED -> {
                HomingSystem.aggressiveScalingTurn(currentVelocity, targetDirection, angleChange, targetDirection.length(), 0.4)
            }
            else -> {
                currentVelocity
            }
        }

    }
}