package me.newburyminer.customItems.loot.providers.structure

import me.newburyminer.customItems.loot.LootTable
import me.newburyminer.customItems.loot.RoundTable
import me.newburyminer.customItems.loot.StructureLoot
import me.newburyminer.customItems.structures.StructureReference
import me.newburyminer.customItems.structures.structure.AbandonedShip
import org.bukkit.Material

object AbandonedShipLoot: StructureLoot(
    Material.SPRUCE_BOAT,
) {
    override val spawner: LootTable = weighted(1..1,
        weightedReward(1..5

        ) to 2.0,
        weightedReward(1..1,

        ) to 1.0
    )
    override val vault: LootTable = weighted(1..1,
        weightedReward(1..5

        ) to 2.0,
        weightedReward(1..1,

        ) to 1.0
    )
}