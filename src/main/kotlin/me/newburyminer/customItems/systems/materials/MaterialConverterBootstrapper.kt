package me.newburyminer.customItems.systems.materials

import org.bukkit.Material

object MaterialConverterBootstrapper {
    fun registerAll() {
        MaterialConverterRegistry.registerBulk(
            mapOf(
                Material.OAK_LOG to (MaterialType.OAK_WOOD to 4.0),
                Material.OAK_PLANKS to (MaterialType.OAK_WOOD to 1.0),
                Material.STICK to (MaterialType.OAK_WOOD to 0.5),
                Material.REDSTONE to (MaterialType.REDSTONE to 1.0),
                Material.REDSTONE_BLOCK to (MaterialType.REDSTONE to 9.0),
                Material.STONE to (MaterialType.STONE to 1.0),
                Material.COBBLESTONE to (MaterialType.COBBLESTONE to 1.0),
                Material.GOLD_INGOT to (MaterialType.GOLD to 1.0),
                Material.GOLD_BLOCK to (MaterialType.GOLD to 9.0),
                Material.IRON_INGOT to (MaterialType.IRON to 1.0),
                Material.IRON_BLOCK to (MaterialType.IRON to 9.0),
                Material.STRING to (MaterialType.STRING to 1.0),
                Material.SLIME_BALL to (MaterialType.SLIME to 1.0),
                Material.SLIME_BLOCK to (MaterialType.SLIME to 9.0),
                Material.QUARTZ to (MaterialType.QUARTZ to 1.0),
                Material.QUARTZ_BLOCK to (MaterialType.QUARTZ to 4.0),
                Material.TNT to (MaterialType.TNT to 1.0)
            )
        )

        MaterialConverterRegistry.registerRecipeBulk(
            mutableMapOf(
                Material.REPEATER to mutableMapOf(
                    MaterialType.STONE to 3.0,
                    MaterialType.REDSTONE to 3.0,
                    MaterialType.OAK_WOOD to 1.0,
                ),
                Material.COMPARATOR to mutableMapOf(
                    MaterialType.STONE to 3.0,
                    MaterialType.REDSTONE to 3.0,
                    MaterialType.OAK_WOOD to 1.5,
                    MaterialType.QUARTZ to 1.0
                ),
                Material.OBSERVER to mutableMapOf(
                    MaterialType.COBBLESTONE to 6.0,
                    MaterialType.REDSTONE to 2.0,
                    MaterialType.QUARTZ to 1.0
                ),
                Material.REDSTONE_TORCH to mutableMapOf(
                    MaterialType.OAK_WOOD to 0.5,
                    MaterialType.REDSTONE to 1.0
                )
            )
        )
    }
}