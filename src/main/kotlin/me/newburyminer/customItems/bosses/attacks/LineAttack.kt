package me.newburyminer.customItems.bosses.attacks

import me.newburyminer.customItems.Utils.Companion.applyDamage
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.ParticleSettings
import me.newburyminer.customItems.helpers.damage.DamageSettings
import me.newburyminer.customItems.helpers.rayTraceManyEntities
import org.bukkit.Location
import org.bukkit.entity.Player
import org.checkerframework.checker.units.qual.radians

class LineAttack(
    val start: Location,
    val end: Location,
    val particleSettings: ParticleSettings,
    val damage: DamageSettings,
    delay: Int,
    duration: Int,
    val radius: Double = 0.0
): TelegraphedAttack(delay, duration) {

    override fun telegraphTick() {
        if (age % particleSettings.preParticleSeparation == 0) {
            CustomEffects.particleLine(particleSettings.preParticle, start, end, particleSettings.concentration)
        }
    }

    private val alreadyHit = mutableListOf<Player>()
    override fun activeTick() {
        val direction = end.toVector().subtract(start.toVector())
        val incrementDirection = direction.multiply(1.0 / duration)

        val currentIncrement = age - delay
        val currentStart = start.clone().add(incrementDirection.multiply(currentIncrement))
        val currentEnd = start.clone().add(incrementDirection.multiply(currentIncrement + 1))

        start.world.rayTraceManyEntities(
            currentStart,
            currentEnd,
            radius = radius
        )
            .filter { !alreadyHit.contains(it)}
            .filterIsInstance<Player>()
            .forEach {
                it.applyDamage(damage)
                alreadyHit.add(it)
            }

        CustomEffects.particleLine(particleSettings.particle, currentStart, currentEnd, particleSettings.concentration)
    }

    override fun execute() {
        if (duration != 0) return
        val direction = start.toVector().subtract(end.toVector())
        val hitPlayers = start.world.rayTraceManyEntities(start, direction, direction.length(), 0.0).filterIsInstance<Player>()

        hitPlayers.forEach {
            it.applyDamage(damage)
        }

        CustomEffects.particleLine(particleSettings.particle, start, end, particleSettings.concentration)
    }

}