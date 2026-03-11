package me.newburyminer.customItems.bosses.attacks

abstract class TelegraphedAttack(protected val delay: Int, protected val duration: Int) {

    protected var age: Int = 0

    // Returns true when completed
    fun tick(): Boolean {
        when {
            age < delay -> telegraphTick()
            duration == 0 && age == delay -> execute()
            duration > 0 -> activeTick()
            age > duration + delay -> return true
        }
        age++
        return false
    }

    open fun telegraphTick() {}
    open fun execute() {}
    open fun activeTick() {}

}