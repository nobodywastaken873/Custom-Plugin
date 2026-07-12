package me.newburyminer.customItems.entity.components.projectiles

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.velocity.VelocityProvider
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
import kotlin.math.pow

class MagicMissileComponent(
    private val size: Double,
    private val velocityProvider: VelocityProvider,
    private val initialVel: Vector,
    private val effects: HitEffects,
    private val particleTheme: ParticleTheme,
    private val shooter: Entity?,
    private val target: Entity?
): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "size" to size,
            "velocityProvider" to velocityProvider.serialize(),
            "initialVel" to initialVel.serialize(),
            "effects" to effects.serialize(),
            "particleTheme" to particleTheme.name,
            "shooter" to shooter?.uniqueId.toString(),
            "target" to target?.uniqueId.toString()
        )
    }
    @Suppress("UNCHECKED_CAST")
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.MAGIC_MISSILE_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return MagicMissileComponent(
                map["size"].asDouble(),
                VelocityProvider.deserialize(map["velocityProvider"]),
                Vector.deserialize(map["initialVel"] as Map<String, Any>),
                HitEffects.deserialize(map["effects"]),
                ParticleTheme.valueOf(map["particleTheme"].asString()),
                fromNullUUID(map["shooter"]),
                fromNullUUID(map["target"])
            )
        }
    }

    private val particleSettings = particleTheme.settings
    private var currentVelocity: Vector = initialVel
    private var timeIndex: Int = 0

    override fun tick(wrapper: EntityWrapper) {
        // need target validity check
        if (target?.isValid == false || target?.world != wrapper.entity.world) { wrapper.entity.remove(); return }

        val projectile = wrapper.entity as Marker
        val currentLocation = projectile.location
        val targetLocation = target.getUpperCenter()

        currentVelocity = velocityProvider.next(currentLocation, targetLocation, currentVelocity, timeIndex)
        timeIndex++

        try {
            currentVelocity.checkFinite()
        } catch (e: Exception) {
            println(currentVelocity)
        }

        // Stop it if it would hit a block
        if (projectile.world.rayTraceBlocks(currentLocation, currentVelocity,
                currentVelocity.length(), FluidCollisionMode.NEVER, true)?.hitBlock != null) {
            projectile.remove()
            effects.applyTargetless(shooter ?: return, projectile.location)
            return
        }

        // Stop it if it hits the target
        if (target.boundingBox.expand(size)
            .rayTrace(currentLocation.toVector(), currentVelocity, currentVelocity.length()) != null) {
            projectile.remove()

            effects.apply(target as? LivingEntity ?: return, shooter ?: return, currentLocation)
            return
        }

        val newLocation = currentLocation.clone().add(currentVelocity)
        projectile.teleport(newLocation)

        // Need to implement more different ways of showing the missile
        CustomEffects.raycastParticleLine(
            particleSettings.preParticle,
            newLocation,
            currentVelocity,
            currentVelocity.length(),
            particleSettings.concentration * 2 * particleSettings.spread.pow(2).coerceAtLeast(1.0),
            offset = particleSettings.spread
        )

    }
}