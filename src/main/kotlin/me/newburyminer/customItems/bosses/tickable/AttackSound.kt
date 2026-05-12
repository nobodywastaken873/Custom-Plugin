package me.newburyminer.customItems.bosses.tickable

import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Location
import org.bukkit.Sound

class AttackSound(
    val loc: Location,
    val preSound: Sound,
    val minPitch: Float,
    val maxPitch: Float,
    val steps: Int,
    duration: Int,
    val postSound: Sound = preSound,
    val volume: Float = 1.5F
) : TickableEffect(duration) {

    override fun action(age: Int) {
        val soundPeriod = duration / steps
        val step = age / soundPeriod + 1

        if (age == duration) {
            CustomEffects.playSound(loc, postSound, volume, 1.0F)
        }
        else if (age % soundPeriod == 0) {
            val currentPitch = (maxPitch - minPitch) / steps * step
            CustomEffects.playSound(loc, preSound, volume, currentPitch)
        }
    }

}