package me.newburyminer.customItems.bosses.actions

import me.newburyminer.customItems.bosses.ActionCategory
import me.newburyminer.customItems.bosses.BossAction
import me.newburyminer.customItems.bosses.BossInstance

class RepeatAction(
    boss: BossInstance,
    val factory: () -> BossAction,
    val count: Int,
    val separation: Int = 10,
    override val category: ActionCategory = ActionCategory.PRIMARY
): BossAction(boss) {

    private var currentAction: BossAction? = null

    private var remaining = count - 1
    private var separationRemaining = separation
    override fun tick() {
        if (currentAction?.finished == true) {
            if (remaining == 0) {finish(); return}
            currentAction = null
            remaining--
        }

        if (currentAction == null) {
            if (separationRemaining == 0) {
                currentAction = factory()
                currentAction?.start()
                separationRemaining = separation
            }
            else {
                separationRemaining--
            }
        }

        currentAction?.tick()
    }

    override fun cancel() {
        currentAction?.cancel()
    }

}