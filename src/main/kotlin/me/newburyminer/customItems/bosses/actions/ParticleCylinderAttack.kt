package me.newburyminer.customItems.bosses.actions

import me.newburyminer.customItems.bosses.ActionCategory
import me.newburyminer.customItems.bosses.ActionTimeline
import me.newburyminer.customItems.bosses.BossAction
import me.newburyminer.customItems.bosses.BossInstance
import me.newburyminer.customItems.bosses.Collision
import me.newburyminer.customItems.bosses.rendering.RenderManager
import me.newburyminer.customItems.bosses.rendering.Transform
import me.newburyminer.customItems.bosses.rendering.combinator.CylinderCombinator
import me.newburyminer.customItems.bosses.rendering.combinator.FloorCombinator
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.ParticleSettings
import me.newburyminer.customItems.helpers.SoundSettings
import me.newburyminer.customItems.helpers.rayTraceManyEntities
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player

class ParticleCylinderAttack(
    boss: BossInstance,
    val start: Location,
    val end: Location,
    val radius: Double,
    val delay: Int,
    val lineDuration: Int,
    val particleSettings: ParticleSettings,
    val soundSettings: SoundSettings,
    val effects: HitEffects,
): BossAction(boss) {

    override val category: ActionCategory = ActionCategory.PRIMARY

    private val timeline = ActionTimeline()

    private val renderManager = RenderManager()

    override fun start() {
        val cylinderRendering = CylinderCombinator(
            Transform(start.toVector(), Transform.lookRotation(end.toVector().subtract(start.toVector()))),
            radius,
            start.clone().subtract(end).length(),
            Material.YELLOW_CONCRETE,
            Particle.DUST.builder().color(235, 225, 52),
            0.25,
            boss.boss.world
        )
        timeline.after(0) {
            cylinderRendering.spawn(boss.boss.world)
            renderManager.add(cylinderRendering)
        }

        timeline.at(delay / 3) { cylinderRendering.particle = Particle.DUST.builder().color(235, 134, 52); cylinderRendering.material = Material.ORANGE_CONCRETE }
        timeline.at(2 * delay / 3) { cylinderRendering.particle = Particle.DUST.builder().color(235, 67, 52); cylinderRendering.material = Material.RED_CONCRETE }

        timeline.repeat(0, delay) {
            cylinderRendering.transform.rotateLocalZ(0.1F)
            renderManager.tick()
        }

        val soundDelay = delay / soundSettings.steps
        timeline.every(0, delay, delay / soundSettings.steps) {
            boss.playSound(boss.getCenter(), soundSettings.preSound, 1.5F, soundSettings.getPitch(timer / soundDelay))
        }

        if (lineDuration == 0) {
            timeline.at(delay) {
                executeAttack()
                finish()
            }
        } else {
            timeline.repeat(delay, delay + lineDuration) {
                tickAttack()
            }
            timeline.at(delay + lineDuration) {
                finish()
            }
        }
    }

    override fun tick() {
        timeline.tick()
        timer++
    }

    private fun executeAttack() {
        for (player in boss.currentPlayers) {
            val box = player.boundingBox
            if (Collision.cylinderIntersects(start, end, radius, box))
                effects.apply(player, boss.boss)
        }

        CustomEffects.rotatedCylinder(particleSettings.preParticle, start, end, radius, particleSettings.preConcentration)
        boss.playSound(boss.getCenter(), soundSettings.postSound, 1.5F, soundSettings.getEndPitch())

    }

    private val alreadyHit = mutableListOf<Player>()
    private fun tickAttack() {

        val direction = end.toVector().subtract(start.toVector())
        val incrementDirection = direction.multiply(1.0 / lineDuration)

        val currentIncrement = timer - delay
        val currentStart = start.clone().add(incrementDirection.multiply(currentIncrement))
        val currentEnd = start.clone().add(incrementDirection.multiply(currentIncrement + 1))

        val hittable = boss.currentPlayers
            .filter { !alreadyHit.contains(it)}

        for (player in hittable) {
            val box = player.boundingBox
            if (Collision.cylinderIntersects(start, end, radius, box)) {
                effects.apply(player, boss.boss)
                alreadyHit.add(player)
            }
        }

        boss.playSound(boss.getCenter(), soundSettings.postSound, 1.5F, (soundSettings.minPitch + soundSettings.maxPitch) / 2)
        CustomEffects.rotatedCylinder(particleSettings.preParticle, currentStart, currentEnd, radius, particleSettings.preConcentration)
    }

    override fun cancel() {
        renderManager.clear()
    }

}