package me.newburyminer.customItems.loot.rewards

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootContext
import org.bukkit.loot.LootTable
import org.bukkit.loot.LootTables
import kotlin.math.sqrt

class VanillaTableReward(
    val table: LootTable,
    val count: (Double) -> Int = {1}
): LootReward {

    constructor(
        table: LootTable,
        count: Int
    ) : this(table, { count })

    override fun evaluate(scaler: Double, player: Player): List<ItemStack> {
        val allItems = mutableListOf<ItemStack>()
        repeat(count(scaler)) {
            val lootContext = LootContext.Builder(Location(Bukkit.getWorlds()[0], Math.random() * 1000, Math.random() * 1000, Math.random() * 1000))
                .luck(1.0F * sqrt(scaler.toFloat())).build()
            val chestLoot = table.populateLoot(null, lootContext)
            allItems.addAll(chestLoot)
        }
        return allItems
    }

}