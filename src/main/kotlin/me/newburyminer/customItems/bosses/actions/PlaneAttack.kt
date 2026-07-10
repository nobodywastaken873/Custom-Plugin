package me.newburyminer.customItems.bosses.actions

import me.newburyminer.customItems.Utils.Companion.getHitboxCorners
import me.newburyminer.customItems.bosses.ActionCategory
import me.newburyminer.customItems.bosses.ActionTimeline
import me.newburyminer.customItems.bosses.BossAction
import me.newburyminer.customItems.bosses.BossInstance
import me.newburyminer.customItems.bosses.rendering.RenderManager
import me.newburyminer.customItems.bosses.rendering.combinator.FloorCombinator
import me.newburyminer.customItems.bosses.rendering.shapes.Shape
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.ParticleSettings
import me.newburyminer.customItems.helpers.SoundSettings
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import java.util.UUID

class PlaneAttack(
    boss: BossInstance,
    val shape: Shape,
    val yLevel: Double,
    val delay: Int,
    val above: Boolean,
    val particleSettings: ParticleSettings,
    val soundSettings: SoundSettings,
    val effects: HitEffects,
    val cellsize: Double = 0.5
): BossAction(boss) {

    override val category: ActionCategory = ActionCategory.PRIMARY

    private val timeline = ActionTimeline()

    private val world = boss.boss.world

    private val renderManager = RenderManager()

    override fun start() {

        val planeRendering = FloorCombinator(
            shape,
            cellsize,
            Material.YELLOW_CONCRETE,
            Particle.DUST.builder().color(235, 225, 52),
            0.25,
            world
        )
        timeline.after(0) {
            planeRendering.spawn(world)
            renderManager.add(planeRendering)
        }

        timeline.repeat(0, delay) {
            renderManager.tick()
        }

        timeline.at(delay / 3) { planeRendering.particle = Particle.DUST.builder().color(235, 134, 52); planeRendering.material = Material.ORANGE_CONCRETE }
        timeline.at(2 * delay / 3) { planeRendering.particle = Particle.DUST.builder().color(235, 67, 52); planeRendering.material = Material.RED_CONCRETE }

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
        val toDamage = mutableSetOf<UUID>()
        val bounds = shape.bounds
        for (player in shape.center.toLocation(world).getNearbyEntitiesByType(Player::class.java, bounds.widthX / 2 + 2.0, 20.0, bounds.widthZ / 2 + 2.0)) {
            for (corner in player.getHitboxCorners(true)) {
                // One of player's hitbox corners is in the shape
                if (!shape.contains(corner.toVector())) continue
                // If not above and player is at level or if above and player is above or at
                if ((!above && player.y == yLevel) || (above && player.y >= yLevel)) {
                    toDamage.add(player.uniqueId)
                }
            }
        }

        toDamage.forEach {
            effects.apply(Bukkit.getPlayer(it) ?: return@forEach, boss.boss)
        }

        boss.playSound(boss.getCenter(), soundSettings.postSound, 1.5F, soundSettings.getEndPitch())
        CustomEffects.particleFullShape(world, particleSettings.particle, shape, particleSettings.concentration)
    }

    override fun cancel() {
        renderManager.clear()
    }

}