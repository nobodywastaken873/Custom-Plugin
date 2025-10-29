package me.newburyminer.customItems.entity.components.utils

interface CooldownInterface {
    var cooldown: Int
    fun reduceCooldown(ticks: Int) {
        if (cooldown != 0)
            cooldown = (cooldown - ticks).coerceAtLeast(0)
    }
    fun setCooldown(ticks: Int) {
        cooldown = ticks
    }
    fun offCooldown(): Boolean {
        return cooldown == 0
    }
}