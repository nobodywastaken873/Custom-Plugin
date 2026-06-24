package me.newburyminer.customItems.entity.components.projectileshooters

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.entity.*
import me.newburyminer.customItems.entity.components.projectiles.CustomDamageProjectile
import me.newburyminer.customItems.entity.components.projectiles.ElytraBreakerFirework
import me.newburyminer.customItems.entity.components.utils.CooldownInterface
import me.newburyminer.customItems.entity.components.utils.ProjectileType
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Sound
import org.bukkit.entity.Firework
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class MachineGunShooter(
    private val damage: HitEffects,
    private val delay: Int,
    private val range: Double,
    private val slowdown: Double,
    private val projectileType: ProjectileType,
): EntityComponent, CooldownInterface {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "damage" to damage.serialize(),
            "delay" to delay,
            "range" to range,
            "slowdown" to slowdown,
            "projectileType" to projectileType.name
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.MACHINE_GUN_SHOOTER
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return MachineGunShooter(
                HitEffects.deserialize(map["damage"]),
                map["delay"].asInt(),
                map["range"].asDouble(),
                map["slowdown"].asDouble(),
                ProjectileType.valueOf(map["projectileType"].asString())
            )
        }
    }

    override var cooldown: Int = delay

    override fun tick(wrapper: EntityWrapper) {
        reduceCooldown(1)

        if (offCooldown()) {
            val shooter = wrapper.entity as? Mob ?: return
            val target = shooter.target ?: return

            val vect = when (projectileType) {
                ProjectileType.ARROW -> {
                    val locDiff = target.location.subtract(shooter.location).toVector().multiply(0.119).add(Vector(
                        Utils.randomRange(-0.05, 0.05), Utils.randomRange(-0.05, 0.05), Utils.randomRange(-0.05, 0.05))
                    )
                    val locDiffHoriz = Vector(locDiff.x, 0.0, locDiff.y)
                    Vector(locDiff.x, locDiffHoriz.length() * 0.2 + locDiff.y, locDiff.z)
                }
                ProjectileType.SPLASH_POTION -> {
                    val locDiff = target.location.subtract(shooter.location).toVector().multiply(0.119).add(Vector(
                        Utils.randomRange(-0.05, 0.05), Utils.randomRange(-0.05, 0.05), Utils.randomRange(-0.05, 0.05))
                    )
                    val locDiffHoriz = Vector(locDiff.x, 0.0, locDiff.y)
                    Vector(locDiff.x, locDiffHoriz.length() * 0.2 + locDiff.y, locDiff.z)
                }
                else -> {
                    target.location.subtract(shooter.location).toVector().multiply(0.119).add(Vector(
                        Utils.randomRange(-0.05, 0.05), Utils.randomRange(-0.05, 0.05), Utils.randomRange(-0.05, 0.05))
                    )
                }
            }

            shooter.launchProjectile(projectileType.clazz, vect) {
                it.shooter = wrapper.entity
                val newWrapper = EntityWrapperManager.getWrapperorNew(it)
                newWrapper.addComponent(CustomDamageProjectile(damage))
                it.setTag("spellsummoned", true)
            }

            CustomEffects.playSound(wrapper.entity.location, Sound.ENTITY_ARROW_SHOOT, 1.0F, 1.4F)

            applyCooldown(delay)
        }
    }
}