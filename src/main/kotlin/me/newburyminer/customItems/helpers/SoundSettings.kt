package me.newburyminer.customItems.helpers

import org.bukkit.Sound

class SoundSettings(
    val preSound: Sound,
    val minPitch: Float,
    val maxPitch: Float,
    val steps: Int,
    val postSound: Sound = preSound,
    val postPeriod: Int = 5,
    val volume: Float = 1.5f,
) {
    fun getPitch(step: Int): Float {
        return minPitch + (maxPitch - minPitch) / steps * step
    }

    fun getEndPitch(): Float {
        return (maxPitch - minPitch) / 2
    }
}