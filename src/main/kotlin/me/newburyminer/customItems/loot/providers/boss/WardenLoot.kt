package me.newburyminer.customItems.loot.providers.boss

import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.loot.BossLoot
import me.newburyminer.customItems.loot.LootEntry
import me.newburyminer.customItems.loot.Pity
import me.newburyminer.customItems.loot.RoundTable
import me.newburyminer.customItems.loot.WeightedTable
import me.newburyminer.customItems.loot.rewards.CustomReward
import me.newburyminer.customItems.loot.rewards.LootTableReward
import me.newburyminer.customItems.loot.rewards.VanillaTableReward
import org.bukkit.Material
import org.bukkit.loot.LootTables
import kotlin.math.sqrt

object WardenLoot: BossLoot(
    Material.ECHO_SHARD,
    RoundTable(
        LootEntry(1.0, VanillaTableReward(LootTables.ANCIENT_CITY.lootTable, 20)),
        LootEntry(2.0 - sqrt(3.0),
            LootTableReward(
                WeightedTable(
                    LootEntry(1.0, CustomReward(CustomItem.WARDEN_HEART), Pity("warden_heart", 4)),
                    LootEntry(sqrt(3.0), CustomReward(CustomItem.FRAGMENT_OF_SOUND)),
                    rolls = 2..2,
                )
            )
        )
    ),
    RoundTable(
        LootEntry(1.0, VanillaTableReward(LootTables.ANCIENT_CITY.lootTable, 20)),
        LootEntry(2.0 - sqrt(3.0),
            LootTableReward(
                WeightedTable(
                    LootEntry(1.0, CustomReward(CustomItem.WARDEN_HEART), Pity("warden_heart", 4)),
                    LootEntry(sqrt(3.0), CustomReward(CustomItem.FRAGMENT_OF_SOUND)),
                    rolls = 2..2,
                )
            )
        )
    ),
) {
}