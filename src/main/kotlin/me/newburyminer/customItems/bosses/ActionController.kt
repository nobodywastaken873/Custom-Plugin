package me.newburyminer.customItems.bosses

import kotlin.reflect.KClass

abstract class ActionController(
    protected val boss: BossInstance
) {
    val actions: MutableList<BossAction> = mutableListOf()

    open fun tick() {
        actions.removeIf {
            it.tick()
            it.finished
        }
    }

    fun cancelAll() {
        actions.forEach { it.cancel() }
        actions.clear()
    }

    protected fun numActiveTasksOf(category: ActionCategory): Int {
        return actions.filter { it.category == category }.size
    }

    protected fun endTasksOf(klass: KClass<out BossAction>) {
        actions.toMutableList().forEach {
            if (it::class == klass) {
                it.cancel()
                actions.remove(it)
            }
        }
    }

    protected fun endTasks(category: ActionCategory) {
        actions.toMutableList().forEach {
            if (it.category == category) {
                it.cancel()
                actions.remove(it)
            }
        }
    }
}