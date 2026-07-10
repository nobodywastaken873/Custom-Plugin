package me.newburyminer.customItems.bosses.actions

import me.newburyminer.customItems.bosses.ActionCategory
import me.newburyminer.customItems.bosses.BossAction
import me.newburyminer.customItems.bosses.BossInstance

class DelayAction(
    boss: BossInstance,
    delay: Int,
): BossAction(boss) {
    override val category: ActionCategory = ActionCategory.PRIMARY

    private var remaining = delay
    override fun tick() {
        if (remaining == 0) {finish(); return}
        remaining--
    }
}