package me.newburyminer.customItems.loot

import me.newburyminer.customItems.loot.rewards.LootReward
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class LootEntry(
    val weight: Double,
    val reward: LootReward,
    val pity: Pity? = null
) {

    fun evaluate(scaler: Double, player: Player): List<ItemStack> {
        if (pity != null) {
            PlayerPityManager.resetPity(pity.id, pity.threshold, player)
        }
        return reward.evaluate(scaler, player)
    }

}