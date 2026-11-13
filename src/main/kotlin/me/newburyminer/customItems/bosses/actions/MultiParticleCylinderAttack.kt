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

class MultiParticleCylinderAttack(
    boss: BossInstance,
    val starts: List<Location>,
    val ends: List<Location>,
    val radius: Double,
    val delay: Int,
    val particleSettings: ParticleSettings,
    val soundSettings: SoundSettings,
    val effects: HitEffects,
): BossAction(boss) {

    override val category: ActionCategory = ActionCategory.PRIMARY

    private val timeline = ActionTimeline()

    private val renderManager = RenderManager()

    override fun start() {

        val cylinders = mutableListOf<CylinderCombinator>()

        starts.indices.forEach { i ->
            val start = starts[i]
            val end = ends[i]

            val cylinderRendering = CylinderCombinator(
                Transform(start.toVector(), Transform.lookRotation(end.toVector().subtract(start.toVector()))),
                radius,
                start.clone().subtract(end).length(),
                Material.YELLOW_STAINED_GLASS,
                Particle.DUST.builder().color(235, 225, 52),
                particleSettings.preConcentration,
                boss.boss.world
            )

            cylinderRendering.spawn(boss.boss.world)
            renderManager.add(cylinderRendering)
            cylinders.add(cylinderRendering)
        }

        timeline.at(delay / 3) { cylinders.forEach { cylinderRendering ->
            cylinderRendering.particle = Particle.DUST.builder().color(235, 134, 52)
            cylinderRendering.material = Material.ORANGE_STAINED_GLASS
        } }
        timeline.at(2 * delay / 3) { cylinders.forEach { cylinderRendering ->
            cylinderRendering.particle = Particle.DUST.builder().color(235, 67, 52)
            cylinderRendering.material = Material.RED_STAINED_GLASS
        } }

        timeline.repeat(0, delay) { cylinders.forEach { cylinderRendering ->
            cylinderRendering.transform.rotateLocalZ(0.1F)
            renderManager.tick()
        } }

        val soundDelay = delay / soundSettings.steps
        timeline.every(0, delay, delay / soundSettings.steps) {
            boss.playSound(boss.getCenter(), soundSettings.preSound, 1.5F, soundSettings.getPitch(timer / soundDelay))
        }


        timeline.at(delay) {
            executeAttack()
            finish()
        }
    }

    override fun tick() {
        timeline.tick()
        timer++
    }

    private fun executeAttack() {
        for (player in boss.currentPlayers) {
            val box = player.boundingBox
            val hitsPlayer = starts.indices.any { i ->
                Collision.cylinderIntersects(starts[i], ends[i], radius, box)
            }
            if (hitsPlayer) {
                effects.apply(player, boss.boss)
            }
        }

        starts.indices.forEach { i ->
            val start = starts[i]
            val end = ends[i]
            CustomEffects.rotatedCylinder(particleSettings.particle, start, end, radius, particleSettings.concentration)
        }
        boss.playSound(boss.getCenter(), soundSettings.postSound, 1.5F, soundSettings.getEndPitch())

    }

    override fun cancel() {
        renderManager.clear()
    }

}