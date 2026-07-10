package me.newburyminer.customItems.bosses.actions

import me.newburyminer.customItems.bosses.ActionCategory
import me.newburyminer.customItems.bosses.BossAction
import me.newburyminer.customItems.bosses.BossInstance

class MultiAction(
    boss: BossInstance,
    val actions: List<BossAction>
): BossAction(boss) {

    override val category: ActionCategory = actions.first().category

    override fun start() {
        actions.forEach {it.start()}
    }

    override fun tick() {
        actions.forEach {it.tick()}
        if (actions.all { it.finished }) {
            finish()
        }
    }

    override fun cancel() {
        actions.forEach {it.cancel()}
    }

}