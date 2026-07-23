package me.newburyminer.customItems.loot.tables

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemEnchantments
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.newburyminer.customItems.Utils.Companion.storeEnch
import me.newburyminer.customItems.loot.LootTable
import me.newburyminer.customItems.loot.RoundTable
import me.newburyminer.customItems.loot.rewards.LootReward
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.Random

object AncientCityLoot: LootReward {

    private val table = round(
        weightedReward(8..8,
            material(Material.AIR, 1..1) to 38.0,

            material(Material.SCULK_CATALYST, 1..2) to 2.0,
            material(Material.SCULK, 6..12) to 3.0,
            material(Material.COAL, 1..3) to 3.0,

            material(Material.COAL, 6..15) to 5.0,
            material(Material.BONE, 1..15) to 5.0,
            material(Material.BOOK, 3..10) to 5.0,
            item({ItemStack(Material.ENCHANTED_BOOK).storeEnch("SS3")}, 1..1) to 2.0,
            material(Material.DISC_FRAGMENT_5, 1..3) to 4.0,
            material(Material.ECHO_SHARD, 1..3) to 4.0,
            material(Material.AMETHYST_SHARD, 1..15) to 3.0,
            material(Material.EXPERIENCE_BOTTLE, 4..8) to 3.0,
            material(Material.LEATHER, 3..7) to 3.0,
            material(Material.ENCHANTED_GOLDEN_APPLE, 1..2) to 1.0,
            item({
                val enchant = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)
                    .iterator().asSequence()
                    .filter { it.key.namespace == "minecraft" }
                    .toList().random()
                val item = ItemStack(Material.ENCHANTED_BOOK)
                item.setData(DataComponentTypes.STORED_ENCHANTMENTS, ItemEnchantments.itemEnchantments().add(enchant, enchant.maxLevel))
                return@item item
            }, 1..1) to 3.0,
        ),
        weightedReward(1..1,
            material(Material.AIR, 1..1) to 75.0,
            material(Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, 1..1) to 4.0,
            material(Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, 1..1) to 1.0,
        )
    )

    override fun evaluate(scaler: Double, player: Player): List<ItemStack> {
        return table.evaluate(scaler, player)
    }

}