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
                ),
                Material.STONE_BUTTON to mutableMapOf(
                    MaterialType.STONE to 1.0,
                ),
                Material.OAK_BUTTON to mutableMapOf(
                    MaterialType.OAK_WOOD to 1.0,
                ),
                Material.LEVER to mutableMapOf(
                    MaterialType.OAK_WOOD to 0.5,
                    MaterialType.STONE to 1.0
                ),
                Material.OAK_PRESSURE_PLATE to mutableMapOf(
                    MaterialType.OAK_WOOD to 2.0,
                ),
                Material.STONE_PRESSURE_PLATE to mutableMapOf(
                    MaterialType.STONE to 2.0,
                ),
                Material.LIGHT_WEIGHTED_PRESSURE_PLATE to mutableMapOf(
                    MaterialType.GOLD to 2.0,
                ),
                Material.HEAVY_WEIGHTED_PRESSURE_PLATE to mutableMapOf(
                    MaterialType.IRON to 2.0,
                ),
                Material.MINECART to mutableMapOf(
                    MaterialType.IRON to 5.0,
                ),
                Material.DETECTOR_RAIL to mutableMapOf(
                    MaterialType.REDSTONE to 0.16,
                    MaterialType.IRON to 1.0,
                    MaterialType.STONE to 0.32
                ),
                Material.RAIL to mutableMapOf(
                    MaterialType.IRON to 0.38,
                    MaterialType.OAK_WOOD to 0.06
                ),
                Material.POWERED_RAIL to mutableMapOf(
                    MaterialType.GOLD to 1.0,
                    MaterialType.OAK_WOOD to 0.08,
                    MaterialType.REDSTONE to 0.16
                ),
                Material.ACTIVATOR_RAIL to mutableMapOf(
                    MaterialType.IRON to 1.0,
                    MaterialType.REDSTONE to 0.16,
                    MaterialType.OAK_WOOD to 0.25
                ),
                Material.HOPPER_MINECART to mutableMapOf(
                    MaterialType.IRON to 10.0,
                    MaterialType.OAK_WOOD to 8.0
                ),
                Material.CHEST_MINECART to mutableMapOf(
                    MaterialType.IRON to 5.0,
                    MaterialType.OAK_WOOD to 8.0
                ),
                Material.FURNACE_MINECART to mutableMapOf(
                    MaterialType.IRON to 5.0,
                    MaterialType.COBBLESTONE to 8.0
                ),
                Material.TNT_MINECART to mutableMapOf(
                    MaterialType.IRON to 5.0,
                    MaterialType.TNT to 1.0
                ),
                Material.BARREL to mutableMapOf(
                    MaterialType.OAK_WOOD to 7.0,
                ),
                Material.HOPPER to mutableMapOf(
                    MaterialType.IRON to 5.0,
                    MaterialType.OAK_WOOD to 8.0
                ),
                Material.CHEST to mutableMapOf(
                    MaterialType.OAK_WOOD to 8.0
                ),
                Material.CRAFTER to mutableMapOf(
                    MaterialType.IRON to 5.0,
                    MaterialType.OAK_WOOD to 4.0,
                    MaterialType.REDSTONE to 3.0,
                    MaterialType.COBBLESTONE to 7.0
                ),
                Material.DISPENSER to mutableMapOf(
                    MaterialType.COBBLESTONE to 7.0,
                    MaterialType.OAK_WOOD to 1.5,
                    MaterialType.STRING to 3.0,
                    MaterialType.REDSTONE to 1.0
                ),
                Material.DROPPER to mutableMapOf(
                    MaterialType.REDSTONE to 1.0,
                    MaterialType.COBBLESTONE to 7.0
                ),
                Material.NOTE_BLOCK to mutableMapOf(
                    MaterialType.REDSTONE to 1.0,
                    MaterialType.OAK_WOOD to 8.0
                ),
                Material.PISTON to mutableMapOf(
                    MaterialType.IRON to 1.0,
                    MaterialType.OAK_WOOD to 3.0,
                    MaterialType.COBBLESTONE to 4.0,
                    MaterialType.REDSTONE to 1.0
                ),
                Material.STICKY_PISTON to mutableMapOf(
                    MaterialType.SLIME to 1.0,
                    MaterialType.IRON to 1.0,
                    MaterialType.OAK_WOOD to 3.0,
                    MaterialType.COBBLESTONE to 4.0,
                    MaterialType.REDSTONE to 1.0
                ),
                Material.SLIME_BLOCK to mutableMapOf(
                    MaterialType.SLIME to 9.0,
                ),
            )
        )
    }
}