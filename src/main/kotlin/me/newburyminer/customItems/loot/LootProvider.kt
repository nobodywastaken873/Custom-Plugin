package me.newburyminer.customItems.loot

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.loot.rewards.CustomReward
import me.newburyminer.customItems.loot.rewards.ItemReward
import me.newburyminer.customItems.loot.rewards.LootReward
import me.newburyminer.customItems.loot.rewards.LootTableReward
import me.newburyminer.customItems.loot.rewards.MaterialReward
import me.newburyminer.customItems.loot.rewards.VanillaTableReward
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootTables

interface LootProvider {

    val id: String
        get() = this::class.simpleName!!
    val name: String
        get() = id
            .replace(Regex("([a-z0-9])([A-Z])"), "$1 $2") // Space between lower/digit and upper
            .replace(Regex("([A-Z])([A-Z][a-z])"), "$1 $2") // Space between consecutive capitals (e.g., XMLParser -> XML Parser)

    fun getMarker(amount: Int, context: LootContext): ItemStack
    fun getLoot(table: String, scaler: Int, player: Player, count: Int = 1): List<ItemStack>

    fun round(vararg reward: LootReward): RoundTable {
        return RoundTable(*reward.map { LootEntry(1.0, it) }.toTypedArray())
    }
    fun roundReward(vararg reward: LootReward): LootTableReward {
        return LootTableReward(RoundTable(*reward.map { LootEntry(1.0, it) }.toTypedArray()))
    }

    private fun toWeightedTable(vararg reward: Any): Array<LootEntry> {
        return reward.map {
            when (it) {
                is LootEntry -> it
                is Pair<*, *> -> if (it.first is LootReward) {
                    LootEntry(it.second as Double, it.first as LootReward)
                } else if (it.first is Pair<*, *>) {
                    LootEntry((it.first as Pair<*, *>).second as Double, (it.first as Pair<*, *>).first as LootReward, it.second as Pity)
                } else {
                    LootEntry(0.0, MaterialReward(Material.AIR, 1))
                }
                else -> { LootEntry(0.0, MaterialReward(Material.AIR, 1)) }
            }
        }.toTypedArray()
    }
    fun weighted(rolls: IntRange, vararg reward: Any): WeightedTable {
        return WeightedTable(
            *toWeightedTable(reward),
            rolls = rolls
        )
    }
    fun weightedReward(rolls: IntRange, vararg reward: Any): LootTableReward {
        return LootTableReward(WeightedTable(
            *toWeightedTable(reward),
            rolls = rolls
        ))
    }

    fun scale(range: IntRange, scaler: Double): Int {
        return (range.first + (range.last - range.first) * scaler).toInt()
    }

    fun material(material: Material, count: Any): LootReward {
        return when (count) {
            is IntRange -> MaterialReward(material, {scale(count, it)})
            is Int -> MaterialReward(material, count)
            else -> MaterialReward(material, 1)
        }
    }
    fun item(item: ItemStack, count: Any): LootReward {
        return when (count) {
            is IntRange -> ItemReward({ ItemStack(item) }, {scale(count, it)})
            is Int -> ItemReward({ ItemStack(item) }, count)
            else -> ItemReward({ ItemStack(item) }, 1)
        }
    }
    fun item(item: (Double) -> ItemStack, count: Any): LootReward {
        return when (count) {
            is IntRange -> ItemReward(item, {scale(count, it)})
            is Int -> ItemReward(item, count)
            else -> ItemReward(item, 1)
        }
    }
    fun custom(custom: CustomItem, count: Any): LootReward {
        return when (count) {
            is IntRange -> CustomReward(custom, {scale(count, it)})
            is Int -> CustomReward(custom, count)
            else -> CustomReward(custom, 1)
        }
    }
    fun vanillaTable(lootTables: LootTables, count: Any): LootReward {
        return when (count) {
            is IntRange -> VanillaTableReward(lootTables.lootTable, {scale(count, it)})
            is Int -> VanillaTableReward(lootTables.lootTable, count)
            else -> VanillaTableReward(lootTables.lootTable, 1)
        }
    }

}