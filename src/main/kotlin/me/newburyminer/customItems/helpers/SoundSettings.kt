package me.newburyminer.customItems.helpers

import org.bukkit.Sound

class SoundSettings(
    val preSound: Sound,
    val minPitch: Float,
    val maxPitch: Float,
    val steps: Int,
    val postSound: Sound = preSound
) {
    fun getPitch(step: Int): Float {
        return minPitch + (maxPitch - minPitch) / steps * step
    }

    fun getEndPitch(): Float {
        return (maxPitch - minPitch) / 2
    }
}