package me.newburyminer.customItems.entity.components.projectiles

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.HomingSystem
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.helpers.getUpperCenter
import org.bukkit.Bukkit
import org.bukkit.FluidCollisionMode
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Marker
import org.bukkit.util.Vector
import java.util.*

class MagicMissileComponent(
    private val speed: Double,
    private val size: Double,
    private val angleChange: Double,
    private val homingType: HomingSystem.Type,
    private val effects: HitEffects,
    private val particleTheme: ParticleTheme,
    private val shooter: Entity?,
    private val target: Entity?
): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "speed" to speed,
            "size" to size,
            "angleChange" to angleChange,
            "homingType" to homingType.name,
            "effects" to effects.serialize(),
            "particleTheme" to particleTheme.name,
            "shooter" to shooter?.uniqueId.toString(),
            "target" to target?.uniqueId.toString()
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.MAGIC_MISSILE_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return MagicMissileComponent(
                map["speed"].asDouble(),
                map["size"].asDouble(),
                map["angleChange"].asDouble(),
                HomingSystem.Type.valueOf(map["homingType"].asString()),
                HitEffects.deserialize(map["effects"]),
                ParticleTheme.valueOf(map["particleTheme"].asString()),
                Bukkit.getEntity(UUID.fromString(map["shooter"].asString())),
                Bukkit.getEntity(UUID.fromString(map["target"].asString()))
            )
        }
    }

    private val particleSettings = particleTheme.settings
    private var currentVelocity = target?.location?.toVector()
        ?.subtract(shooter?.location?.toVector() ?: Vector(0, 0, 0))
        ?.normalize()
        ?.multiply(speed)

    override fun tick(wrapper: EntityWrapper) {
        // need target validity check

        val projectile = wrapper.entity as Marker
        val currentLocation = projectile.location
        val targetLocation = target?.getUpperCenter() ?: return
        val targetDirection = targetLocation.clone().subtract(currentLocation).toVector()

        currentVelocity = when (homingType) {
            HomingSystem.Type.BASIC_TURN -> {
                HomingSystem.basicCappedTurn(currentVelocity ?: return, targetDirection, angleChange)
            }
            HomingSystem.Type.DISTANCE_SCALED -> {
                HomingSystem.distanceScalingTurn(currentVelocity ?: return, targetDirection, angleChange, targetDirection.length())
            }
            HomingSystem.Type.ANGLE_SCALED -> {
                HomingSystem.angleScalingTurn(currentVelocity ?: return, targetDirection, angleChange, 0.4)
            }
            HomingSystem.Type.BOTH_SCALED -> {
                HomingSystem.aggressiveScalingTurn(currentVelocity ?: return, targetDirection, angleChange, targetDirection.length(), 0.4)
            }
        }

        // Stop it if it would hit a block
        if (projectile.world.rayTraceBlocks(currentLocation, currentVelocity ?: return,
                speed, FluidCollisionMode.NEVER, true)?.hitBlock != null) {
            projectile.remove()
            return
        }

        // Stop it if it hits the target
        if (target.boundingBox.expand(size)
            .rayTrace(currentLocation.toVector(), currentVelocity ?: return, currentVelocity?.length() ?: 0.0) != null) {
            projectile.remove()

            effects.apply(target as? LivingEntity ?: return, shooter ?: return, currentLocation)
            return
        }

        val newLocation = currentLocation.clone().add(currentVelocity ?: return)
        projectile.teleport(newLocation)

        // Need to implement more different ways of showing the missile
        CustomEffects.raycastParticleLine(
            particleSettings.preParticle,
            newLocation,
            currentVelocity ?: return,
            speed,
            particleSettings.concentration * 2,
        )

    }
}