package me.newburyminer.customItems.bosses.actions

import me.newburyminer.customItems.bosses.ActionCategory
import me.newburyminer.customItems.bosses.BossAction
import me.newburyminer.customItems.bosses.BossInstance

class FixedRepeatAction(
    boss: BossInstance,
    val factory: () -> BossAction,
    val count: Int,
    val separation: Int = 10,
    override val category: ActionCategory = ActionCategory.PRIMARY
): BossAction(boss) {

    private var currentActions = mutableListOf<BossAction>()

    private var remaining = count - 1
    private var separationRemaining = separation
    override fun tick() {
        currentActions.toList().forEach {
            it.tick()
            if (it.finished) {
                currentActions.remove(it)
                if (remaining <= 0 && currentActions.isEmpty()) {finish(); return}
            }
        }

        separationRemaining--
        if (separationRemaining <= 0 && remaining > 0) {
            val newAction = factory()
            newAction.start()
            currentActions.add(newAction)
            remaining--
            separationRemaining = separation
        }
    }

    override fun cancel() {
        currentActions.forEach { it.cancel() }
    }

}