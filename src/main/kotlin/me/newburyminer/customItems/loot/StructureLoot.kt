package me.newburyminer.customItems.loot

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.lore
import me.newburyminer.customItems.Utils.Companion.maxStack
import me.newburyminer.customItems.Utils.Companion.name
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class StructureLoot(
    val material: Material,
    val spawner: LootTable,
    val vault: LootTable,
    val color: Array<Int> = arrayOf(135, 130, 108),
): LootProvider {

    override fun getMarker(amount: Int, context: LootContext): ItemStack {
        val item = ItemStack(material)
            .name(Utils.text(name, color))
            .lore(
                Utils.text("Table: ${context.table.capitalize()}", Utils.GRAY),
                Utils.text("Difficulty: ${context.scaler}", Utils.GRAY),
                Utils.text("Owned amount: $amount crates", Utils.GRAY),
            )
            .maxStack(99)

        item.amount = amount.coerceAtMost(99)
        return item
    }

    override fun getLoot(table: String, scaler: Int, player: Player, count: Int): List<ItemStack> {
        val lootTable = when (table) {
            "spawner" -> spawner
            "vault" -> vault
            else -> spawner
        }

        val allLoot = mutableListOf<ItemStack>()
        repeat(count) {
            allLoot.addAll(LootFactory.evaluate(lootTable, scaler / 30.0, player))
        }

        return allLoot
    }

}