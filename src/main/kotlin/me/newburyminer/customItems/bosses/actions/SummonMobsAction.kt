package me.newburyminer.customItems.bosses.actions

import me.newburyminer.customItems.bosses.ActionCategory
import me.newburyminer.customItems.bosses.ActionTimeline
import me.newburyminer.customItems.bosses.AttackContext
import me.newburyminer.customItems.bosses.BossAction
import me.newburyminer.customItems.bosses.BossInstance
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.ParticleSettings
import me.newburyminer.customItems.helpers.SoundSettings
import me.newburyminer.customItems.helpers.getValidSpawnLoc
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobProvider
import org.bukkit.Sound
import org.bukkit.util.Vector

class SummonMobsAction(
    boss: BossInstance,
    val provider: MobProvider,
    val count: Int,
    val delay: Int,
    val particles: ParticleSettings,
    val ctx: AttackContext<*>,
    val soundSettings: SoundSettings,
    override val category: ActionCategory = ActionCategory.SECONDARY
): BossAction(boss) {

    private val timeline = ActionTimeline()

    private val spawnMap = mutableMapOf<Vector, MobDefinition>()
    override fun start() {
        repeat(count) {
            val factor = when (ctx.difficulty) {
                1 -> {ctx.cycle.toDouble() / (ctx.maxCycle - 1) * 0.6}
                2 -> {ctx.cycle.toDouble() / (ctx.maxCycle - 1) * 0.6 + 0.4}
                else -> 0.0
            } * 30.0

            val context = MobContext(factor, boss.getCenter())
            val mob = provider.new(context).first()
            val spawnLoc = boss.getLowerCenter().getValidSpawnLoc(mob.getHitbox(), 7, 15) ?: return@repeat
            spawnMap[spawnLoc.toVector()] = mob
        }

        timeline.at(0) {
            boss.playSound(boss.getCenter(), soundSettings.preSound, soundSettings.volume, soundSettings.getEndPitch())
        }

        timeline.at(delay) {
            boss.playSound(boss.getCenter(), soundSettings.postSound, soundSettings.volume, soundSettings.getEndPitch())
        }

        timeline.at(delay) {
            for ((spawnLoc, mob) in spawnMap) {
                val factor = when (ctx.difficulty) {
                    1 -> {ctx.cycle.toDouble() / (ctx.maxCycle - 1) * 0.6}
                    2 -> {ctx.cycle.toDouble() / (ctx.maxCycle - 1) * 0.6 + 0.4}
                    else -> 0.0
                } * 30.0

                val context = MobContext(factor, spawnLoc.toLocation(boss.boss.world))
                val entity = mob.build(context).createEntity(context)
                boss.addEntity(entity)
            }
            finish()
        }

        timeline.every(0, delay, particles.preParticleSeparation) {
            spawnMap.keys.forEach {
                CustomEffects.particleLine(
                    particles.preParticle,
                    it.toLocation(boss.boss.world),
                    it.clone().add(Vector(0, 2, 0)).toLocation(boss.boss.world),
                    2.0,
                )
            }
        }
    }


    override fun tick() {
        timeline.tick()
    }

}