package me.newburyminer.customItems.systems.ores

import me.newburyminer.customItems.Utils.Companion.setCount
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.ItemRegistry
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class CustomOre(
    val material: Material,
    val drops: (Int) -> List<ItemStack>,
    val experience: IntRange,
) {

    SOUL_SHARD(
        Material.QUARTZ_BLOCK,
        {listOf(ItemRegistry.get(CustomItem.SOUL_SHARD)
            .setCount(fortuneMult(it)))},
        25..50
    ),
    ANCIENT_FOSSIL(
        Material.BONE_BLOCK,
        {listOf(ItemRegistry.get(CustomItem.ANCIENT_FOSSIL)
            .setCount(fortuneMult(it)))},
        0..0
    ),
    KNOWLEDGE_FRAGMENT(
        Material.ANCIENT_DEBRIS,
        {listOf(ItemRegistry.get(CustomItem.KNOWLEDGE_FRAGMENT))},
        50..100
    ),
    BRONZE(
        Material.RAW_GOLD_BLOCK,
        {listOf(ItemRegistry.get(CustomItem.BRONZE_CHUNK)
            .setCount(fortuneMult(it)))},
        0..0
    ),
    SILVER(
        Material.IRON_BLOCK,
        {listOf(ItemRegistry.get(CustomItem.SILVER_CHUNK)
            .setCount(fortuneMult(it)))},
        0..0
    ),
    MYTHRIL(
        Material.PRISMARINE,
        {listOf(ItemRegistry.get(CustomItem.MYTHRIL_SCRAP)
            .setCount(fortuneMult(it)))},
        5..10
    ),
    ENRICHED_AMETHYST(
        Material.BUDDING_AMETHYST,
        {listOf(ItemRegistry.get(CustomItem.ENRICHED_AMETHYST))},
        100..250
    ),

    ;

    companion object {
        fun getFromState(material: Material): CustomOre? {
            return entries.firstOrNull { ore ->
                ore.material == material
            }
        }

        fun fortuneMult(level: Int): Int {

            // level -> number of tiers
            // chance for each tier -> 1 / level

            val random = Math.random()
            val partChance = 1.0 / (2 + level)
            for (i in 1..level) {
                if (random <= partChance * i) {
                    return i
                }
            }

            return 1

        }

    }
}