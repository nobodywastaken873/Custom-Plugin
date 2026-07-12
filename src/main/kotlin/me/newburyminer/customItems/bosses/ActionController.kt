package me.newburyminer.customItems.bosses

import kotlin.reflect.KClass

abstract class ActionController(
    protected val boss: BossInstance
) {
    val actions: MutableList<BossAction> = mutableListOf()
    fun add(action: BossAction) {
        actions.add(action)
        action.start()
    }

    open fun tick() {
        actions.toList().forEach {
            it.tick()
            if (it.finished) actions.remove(it)
        }
    }

    fun cancelAll() {
        actions.forEach { it.cancel() }
        actions.clear()
    }

    protected fun numActiveTasksOf(category: ActionCategory): Int {
        return actions.filter { it.category == category }.size
    }

    protected fun endTaskOf(klass: KClass<out BossAction>) {
        actions.toMutableList().forEach {
            if (it::class == klass) {
                it.cancel()
                actions.remove(it)
            }
        }
    }

    protected fun endTasks(vararg categories: ActionCategory) {
        categories.forEach { category ->
            actions.toMutableList().forEach {
                if (it.category == category) {
                    it.cancel()
                    actions.remove(it)
                }
            }
        }
    }
}