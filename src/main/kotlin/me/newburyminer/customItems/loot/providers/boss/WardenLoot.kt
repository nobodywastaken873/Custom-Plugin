package me.newburyminer.customItems.loot.providers.boss

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.newburyminer.customItems.Utils.Companion.ench
import me.newburyminer.customItems.Utils.Companion.storeEnch
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.loot.BossLoot
import me.newburyminer.customItems.loot.LootEntry
import me.newburyminer.customItems.loot.LootTable
import me.newburyminer.customItems.loot.Pity
import me.newburyminer.customItems.loot.RoundTable
import me.newburyminer.customItems.loot.WeightedTable
import me.newburyminer.customItems.loot.rewards.CustomReward
import me.newburyminer.customItems.loot.rewards.LootTableReward
import me.newburyminer.customItems.loot.rewards.VanillaTableReward
import me.newburyminer.customItems.loot.tables.AncientCityLoot
import org.bukkit.Material
import org.bukkit.Registry
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootTables
import java.util.Random
import kotlin.math.sqrt

object WardenLoot: BossLoot(
    Material.ECHO_SHARD
) {

    override val normal: LootTable =
        round(
            weightedReward(20..20, AncientCityLoot to 1.0),
            weightedReward(2..2,
                custom(CustomItem.FRAGMENT_OF_SOUND, 1) to 14.0,
                custom(CustomItem.WARDEN_HEART, 1) to 1.0 to Pity("warden_heart", 9)
            ),
            weightedReward(1..1,
                material(Material.AIR, 1..1) to 18.0,
                custom(CustomItem.ECHO_SHARD_TRIM, 1..2) to 1.0 to Pity("echo_shard_trim", 15),
                custom(CustomItem.PASTEL_BLUE, 1..2) to 1.0 to Pity("pastel_blue", 15),
            )
        )
    override val hard: LootTable =
        round(
            weightedReward(30..30, AncientCityLoot to 1.0),
            weightedReward(3..3,
                custom(CustomItem.FRAGMENT_OF_SOUND, 1) to 8.0,
                custom(CustomItem.WARDEN_HEART, 1) to 1.0 to Pity("warden_heart", 4)
            ),
            weightedReward(1..1,
                material(Material.AIR, 1..1) to 3.0,
                custom(CustomItem.ECHO_SHARD_TRIM, 1..2) to 1.0 to Pity("echo_shard_trim", 5),
                custom(CustomItem.PASTEL_BLUE, 1..2) to 1.0 to Pity("pastel_blue", 5),
            )
        )

}