package me.newburyminer.customItems.bosses

abstract class BossAction(
    protected val boss: BossInstance
) {

    abstract val category: ActionCategory

    protected var timer: Int = 0

    var finished: Boolean = false
        protected set

    open fun start() {}

    abstract fun tick()

    open fun cancel() {}
}