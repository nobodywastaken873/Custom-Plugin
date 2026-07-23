package me.newburyminer.customItems.loot

import me.newburyminer.customItems.loot.rewards.LootReward
import me.newburyminer.customItems.loot.rewards.LootTableReward

interface ThemeLootProvider: TableCreation {

    val consumableTable: LootTable
    val vanillaTable: LootTable
    val cosmeticsTable: LootTable

    fun getConsumables(): LootReward { return LootTableReward(consumableTable) }
    fun getVanillaLoot(): LootTableReward { return LootTableReward(vanillaTable) }
    fun getCosmetics(): LootTableReward { return LootTableReward(cosmeticsTable) }

}