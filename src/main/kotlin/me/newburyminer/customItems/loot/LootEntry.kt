package me.newburyminer.customItems.loot

import me.newburyminer.customItems.loot.rewards.LootReward

class LootEntry(
    val weight: Double,
    val reward: LootReward,
    val pity: Pity? = null
) {
}