package me.newburyminer.customItems.bosses.tickable

abstract class TickableEffect(
    val duration: Int,
) {
    private var age: Int = 0

    fun tick() {
        action(age)
        age++
    }

    abstract fun action(age: Int)
    fun isFinished() = age >= duration
}