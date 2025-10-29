package me.newburyminer.customItems.entity.components

import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.utils.CooldownInterface
import me.newburyminer.customItems.entity3.CustomEntity
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Sound
import org.bukkit.entity.Firework
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class ElytraBreakerShooter(private val damage: Double, private val baseCooldown: Int, private val duration: Int): EntityComponent, CooldownInterface {
    override val componentType: EntityComponentType = EntityComponentType.ELYTRA_BREAKER_SHOOTER

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "damage" to damage,
            "cooldown" to baseCooldown,
            "duration" to duration,
        )
    }
    override fun deserialize(map: Map<String, Any>): EntityComponent {
        val newDamage = map["damage"] as Double
        val newCooldown = map["cooldown"] as Int
        val newDuration = map["duration"] as Int
        return ElytraBreakerShooter(newDamage, newCooldown, newDuration)
    }

    override var cooldown: Int = 100

    override fun tick(wrapper: EntityWrapper) {
        if (Bukkit.getCurrentTick() % 5 == 0) reduceCooldown(5)

        if (Bukkit.getCurrentTick() % 10 == 0) {
            if (!offCooldown()) return
            val target = getTarget(wrapper) ?: return

            val missile = target.world.spawn(wrapper.entity.location.add(0.0, 1.5, 0.0), Firework::class.java) {
                it.setTag("id", CustomEntity.ENTITY_SHOT_PROJECTILE.name)
                it.shooter = wrapper.entity as LivingEntity
                val newMeta = it.fireworkMeta
                newMeta.addEffects(
                    FireworkEffect.builder()
                        .with(FireworkEffect.Type.BALL_LARGE)
                        .withColor(Color.BLACK, Color.GRAY, Color.ORANGE)
                        .withFade(Color.GRAY)
                        .trail(true)
                        .build()
                )
                newMeta.power = 100
                it.fireworkMeta = newMeta
            }
            EntityWrapperManager.register(missile.uniqueId, EntityWrapper(missile, listOf(ElytraBreakerFirework(damage, duration, target))))

            setCooldown(baseCooldown)
            CustomEffects.playSound(wrapper.entity.location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 5F, 0.4F)
        }

    }

    private fun getTarget(wrapper: EntityWrapper): Player? {

        val center = wrapper.entity.location
        val nearestGliding = center.getNearbyPlayers(90.0)
            .filter { it.isGliding }
            .filter { it.hasLineOfSight(center) }
            .minByOrNull { it.location.distanceSquared(center) }

        return nearestGliding

    }

}