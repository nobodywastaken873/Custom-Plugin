package me.newburyminer.customItems.bosses

data class AttackContext<T: BossPhase<*>>(
    val phase: T,
    val cycle: Int,
    val scalingCycle: Int,
    val maxCycle: Int,
    val difficulty: BossDifficulty,
    val boss: BossInstance
)
