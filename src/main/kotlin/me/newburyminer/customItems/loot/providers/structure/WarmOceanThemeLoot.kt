package me.newburyminer.customItems.loot.providers.structure

import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.loot.LootTable
import me.newburyminer.customItems.loot.ThemeLootProvider
import me.newburyminer.customItems.loot.rewards.LootReward
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object WarmOceanThemeLoot: ThemeLootProvider {
    override val consumableTable: LootTable = weighted(2..8,
        material(Material.GOLDEN_APPLE, 1..2) to 6.0,
        material(Material.ENCHANTED_GOLDEN_APPLE, 1..1) to 0.5,
        material(Material.TOTEM_OF_UNDYING, 1..1) to 1.5,
        custom(CustomItem.STRIDER_STEAK, 4..8) to 7.0,

        custom(CustomItem.TOTEM_OF_CONTINUATION, 1..1) to 1.5,
        custom(CustomItem.EMERALD_BRISKET, 1..2) to 1.5,
        custom(CustomItem.GOLDEN_SLUSHIE, 1..2) to 2.5,
        custom(CustomItem.GOLDEN_TURTLE, 1..2) to 2.0,
        custom(CustomItem.EXTENDED_POTION_STEW, 1..2) to 1.0,
        custom(CustomItem.CHOCOLATE_MILK, 1..2) to 2.0,
    )

    override val vanillaTable: LootTable
        get() = TODO("Not yet implemented")

    override val cosmeticsTable: LootTable = weighted(1..6,
        material(Material.AIR, 1) to 40.0,
        custom(CustomItem.PRISMARINE_TRIM, 1..2) to 1.0,
        custom(CustomItem.ENDER_TRIM, 1..2) to 1.0,
        custom(CustomItem.FLAME_TRIM, 1..2) to 1.0,
    )
}

