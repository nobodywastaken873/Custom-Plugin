package me.newburyminer.customItems.bosses.attacks

import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.ParticleSettings
import me.newburyminer.customItems.helpers.SoundSettings
import me.newburyminer.customItems.helpers.damage.DamageSettings
import org.bukkit.Location

class LineAttack(
    val start: Location,
    val end: Location,
    val particleSettings: ParticleSettings,
    val soundSettings: SoundSettings,
    val damage: DamageSettings,
    delay: Int,
    duration: Int
): TelegraphedAttack(delay, duration) {

    override fun telegraphTick() {
        if (age % particleSettings.preParticleSeparation == 0) {
            CustomEffects.particleLine(particleSettings.preParticle, start, end, 5.0)
        }

        val soundPeriod = delay / soundSettings.steps
        if (age % soundPeriod == 0) {

        }
    }

}