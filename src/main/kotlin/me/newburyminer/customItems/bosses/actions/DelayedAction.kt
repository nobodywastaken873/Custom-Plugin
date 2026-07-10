package me.newburyminer.customItems.bosses.actions

import me.newburyminer.customItems.bosses.ActionCategory
import me.newburyminer.customItems.bosses.BossAction
import me.newburyminer.customItems.bosses.BossInstance

class DelayedAction(
    boss: BossInstance,
    val action: BossAction,
    val delay: Int,
): BossAction(boss) {

    override val category: ActionCategory = action.category

    private var remaining = delay
    override fun tick() {
        remaining--
        if (remaining <= 0) {
            action.start()
            if (!action.finished) {
                action.tick()
            } else {
                finish()
            }
        }
    }

    override fun cancel() {
        action.cancel()
    }

}