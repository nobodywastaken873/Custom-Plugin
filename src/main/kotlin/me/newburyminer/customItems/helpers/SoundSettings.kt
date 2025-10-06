package me.newburyminer.customItems.helpers

import org.bukkit.Sound

class SoundSettings(val preSound: Sound, private val minPitch: Float, private val maxPitch: Float, val steps: Int, val postSound: Sound = preSound) {
    fun getPitch(step: Int): Float {
        return minPitch + (maxPitch - minPitch) / steps * step
    }
}