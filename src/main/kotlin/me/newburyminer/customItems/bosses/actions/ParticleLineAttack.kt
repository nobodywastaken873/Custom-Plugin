package me.newburyminer.customItems.bosses.actions

import me.newburyminer.customItems.Utils.Companion.applyDamage
import me.newburyminer.customItems.bosses.ActionCategory
import me.newburyminer.customItems.bosses.ActionTimeline
import me.newburyminer.customItems.bosses.BossAction
import me.newburyminer.customItems.bosses.BossInstance
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.ParticleSettings
import me.newburyminer.customItems.helpers.SoundSettings
import me.newburyminer.customItems.helpers.rayTraceManyEntities
import org.bukkit.Location
import org.bukkit.entity.Player

class ParticleLineAttack(
    boss: BossInstance,
    val start: Location,
    val end: Location,
    val delay: Int,
    val lineDuration: Int,
    val particleSettings: ParticleSettings,
    val soundSettings: SoundSettings,
    val effects: HitEffects,
): BossAction(boss) {

    override val category: ActionCategory = ActionCategory.PRIMARY

    private val timeline = ActionTimeline()

    override fun start() {
        timeline.every(0, delay, particleSettings.preParticleSeparation) {
            CustomEffects.particleLine(particleSettings.preParticle, start, end, particleSettings.preConcentration)
        }

        val soundDelay = delay / soundSettings.steps
        timeline.every(0, delay, delay / soundSettings.steps) {
            boss.playSound(boss.getCenter(), soundSettings.preSound, 1.5F, soundSettings.getPitch(timer / soundDelay))
        }

        if (lineDuration == 0) {
            timeline.after(delay) {
                executeLine()
            }
        } else {
            timeline.repeat(delay, delay + lineDuration) {
                tickLine()
            }
        }
    }

    override fun tick() {
        timeline.tick()
        timer++
    }

    private fun executeLine() {
        val direction = start.toVector().subtract(end.toVector())
        val hitPlayers = start.world.rayTraceManyEntities(start, direction, direction.length(), 0.0).filterIsInstance<Player>()

        hitPlayers.forEach {
            effects.apply(it, boss.boss)
        }

        boss.playSound(boss.getCenter(), soundSettings.postSound, 1.5F, soundSettings.getEndPitch())
        CustomEffects.particleLine(particleSettings.particle, start, end, particleSettings.concentration)
    }

    private val alreadyHit = mutableListOf<Player>()
    private fun tickLine() {
        val direction = end.toVector().subtract(start.toVector())
        val incrementDirection = direction.multiply(1.0 / lineDuration)

        val currentIncrement = timer - delay
        val currentStart = start.clone().add(incrementDirection.multiply(currentIncrement))
        val currentEnd = start.clone().add(incrementDirection.multiply(currentIncrement + 1))

        start.world.rayTraceManyEntities(
            currentStart,
            currentEnd
        )
            .filterIsInstance<Player>()
            .filter { !alreadyHit.contains(it)}
            .forEach {
                effects.apply(it, boss.boss)
                alreadyHit.add(it)
            }

        boss.playSound(boss.getCenter(), soundSettings.postSound, 1.5F, soundSettings.getEndPitch())
        CustomEffects.particleLine(particleSettings.particle, currentStart, currentEnd, particleSettings.concentration)
    }

}