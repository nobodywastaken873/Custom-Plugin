package me.newburyminer.customItems.loot.providers.structure

import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.loot.LootTable
import me.newburyminer.customItems.loot.ThemeLootProvider
import me.newburyminer.customItems.loot.rewards.LootReward
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object BlackstoneThemeLoot: ThemeLootProvider {
    override val consumableTable: LootTable = weighted(2..8,
        material(Material.GOLDEN_APPLE, 1..2) to 6.0,
        material(Material.ENCHANTED_GOLDEN_APPLE, 1..1) to 1.2,
        material(Material.TOTEM_OF_UNDYING, 1..1) to 1.8,
        material(Material.GOLDEN_CARROT, 4..8) to 10.0,

        custom(CustomItem.BARRIER_TOTEM, 1..1) to 1.5,
        custom(CustomItem.MINI_GOLDEN_APPLE, 1..2) to 2.0,
        custom(CustomItem.GOLDEN_FRIES, 1..2) to 2.0,
        custom(CustomItem.GHAST_APPLE, 1..2) to 2.0,
        custom(CustomItem.ENCHANTED_GOLDEN_STEAK, 1..2) to 1.5,
        custom(CustomItem.POTION_STEW, 1..2) to 1.0,
        custom(CustomItem.CHOCOLATE_MILK, 1..2) to 2.0,
    )

    override val vanillaTable: LootTable
        get() = TODO("Not yet implemented")

    override val cosmeticsTable: LootTable = weighted(1..6,
        material(Material.AIR, 1) to 40.0,
        custom(CustomItem.NETHER_BRICK_TRIM, 1..2) to 1.0,
        custom(CustomItem.GLOWSTONE_TRIM, 1..2) to 1.0,
        custom(CustomItem.FLAME_TRIM, 1..2) to 1.0,
    )
}

