package me.newburyminer.customItems.recipes.registrars

import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.recipes.RecipeBootstrapper
import me.newburyminer.customItems.recipes.RecipeType
import org.bukkit.Material
import org.bukkit.potion.PotionType

object OtherRecipeBootstrapper: RecipeBootstrapper {
    override val recipeType: RecipeType = RecipeType.OTHER
    override fun bootstrap() {
        recipe {
            grid {
                row(null, null, null, null, null)
                row(null, item(Material.QUARTZ, 2), item(Material.FEATHER, 2), item(Material.QUARTZ, 2), null)
                row(null, item(Material.PHANTOM_MEMBRANE, 2), item(Material.ENDER_PEARL, 8), item(Material.PHANTOM_MEMBRANE, 2), null)
                row(null, item(Material.QUARTZ, 2), item(Material.FEATHER, 2), item(Material.QUARTZ, 2), null)
                row(null, null, null, null, null)
            }
            result(CustomItem.SHULKER_PEARL)
        }
        recipe {
            grid {
                row(null, null, null, null, null)
                row(null, item(Material.DIAMOND, 2), item(Material.BREAD, 32), item(Material.DIAMOND, 2), null)
                row(null, item(Material.EMERALD, 32), item(Material.DRIED_KELP, 32), item(Material.GOLDEN_DANDELION, 32), null)
                row(null, item(Material.DIAMOND, 2), item(Material.BREAD, 32), item(Material.DIAMOND, 2), null)
                row(null, null, null, null, null)
            }
            result(CustomItem.LUCKY_CLOVER_SANDWICH)
        }
        recipe {
            grid {
                row(item(Material.BLAST_FURNACE, 4), item(Material.LAVA_BUCKET), item(Material.FURNACE, 8), item(Material.LAVA_BUCKET), item(Material.BLAST_FURNACE, 4))
                row(item(Material.CHARCOAL, 32), item(Material.GOLD_ORE, 8), item(Material.DEEPSLATE_DIAMOND_ORE), item(Material.DEEPSLATE_IRON_ORE, 8), item(Material.AMETHYST_BLOCK, 32))
                row(item(Material.LIGHTNING_ROD), item(Material.DEEPSLATE_DIAMOND_ORE), custom(CustomItem.FIRE_RESISTANT_RESIN), item(Material.DEEPSLATE_DIAMOND_ORE), item(Material.LIGHTNING_ROD))
                row(item(Material.AMETHYST_BLOCK, 32), item(Material.DEEPSLATE_GOLD_ORE, 8), item(Material.DEEPSLATE_DIAMOND_ORE), item(Material.IRON_ORE, 8), item(Material.CHARCOAL, 32))
                row(item(Material.BLAST_FURNACE, 4), item(Material.LAVA_BUCKET), item(Material.FURNACE, 8), item(Material.LAVA_BUCKET), item(Material.BLAST_FURNACE, 4))
            }
            result(CustomItem.FIERY_SHARD)
        }
        recipe {
            grid {
                row(item(Material.ARROW, 64), item(Material.FLINT, 16), item(Material.WIND_CHARGE, 32), item(Material.FLINT, 16), item(Material.ARROW, 64))
                row(item(Material.FEATHER, 16), item(Material.FLETCHING_TABLE, 8), item(Material.STICK, 32), item(Material.FLETCHING_TABLE, 8), item(Material.FEATHER, 16))
                row(item(Material.ENDER_PEARL, 16), item(Material.STICK, 32), item(Material.TIPPED_ARROW, 32), item(Material.STICK, 32), item(Material.POINTED_DRIPSTONE, 32))
                row(item(Material.FEATHER, 16), item(Material.FLETCHING_TABLE, 8), item(Material.STICK, 32), item(Material.FLETCHING_TABLE, 8), item(Material.FEATHER, 16))
                row(item(Material.ARROW, 64), item(Material.FLINT, 16), item(Material.BONE, 32), item(Material.FLINT, 16), item(Material.ARROW, 64))
            }
            result(CustomItem.FLETCHER_UPGRADE)
        }
        recipe {
            grid {
                row(item(Material.IRON_INGOT, 16), item(Material.LAVA_BUCKET), item(Material.ENCHANTED_BOOK).storeEnch("FP4"), item(Material.LAVA_BUCKET), item(Material.IRON_INGOT, 16))
                row(item(Material.LAVA_BUCKET), item(Material.MAGMA_BLOCK, 16), item(Material.CAMPFIRE), item(Material.MAGMA_BLOCK, 16), item(Material.LAVA_BUCKET))
                row(item(Material.ENCHANTED_BOOK).storeEnch("FP4"), item(Material.CAMPFIRE), item(Material.BLAZE_ROD, 8), item(Material.CAMPFIRE), item(Material.ENCHANTED_BOOK).storeEnch("FP4"))
                row(item(Material.LAVA_BUCKET), item(Material.MAGMA_BLOCK, 16), item(Material.CAMPFIRE), item(Material.MAGMA_BLOCK, 16), item(Material.LAVA_BUCKET))
                row(item(Material.IRON_INGOT, 16), item(Material.LAVA_BUCKET), item(Material.ENCHANTED_BOOK).storeEnch("FP4"), item(Material.LAVA_BUCKET), item(Material.IRON_INGOT, 16))
            }
            result(CustomItem.NETHERITE_COATING)
        }
        recipe {
            grid {
                row(null, item(Material.IRON_BLOCK), item(Material.OBSIDIAN, 4), item(Material.IRON_BLOCK), null)
                row(item(Material.IRON_BLOCK), item(Material.COBBLED_DEEPSLATE, 32), item(Material.GUNPOWDER, 8), item(Material.COBBLED_DEEPSLATE, 32), item(Material.IRON_BLOCK))
                row(item(Material.OBSIDIAN, 4), item(Material.GUNPOWDER, 8), item(Material.WITHER_SKELETON_SKULL), item(Material.GUNPOWDER, 8), item(Material.OBSIDIAN, 4))
                row(item(Material.IRON_BLOCK), item(Material.COBBLED_DEEPSLATE, 32), item(Material.GUNPOWDER, 8), item(Material.COBBLED_DEEPSLATE, 32), item(Material.IRON_BLOCK))
                row(null, item(Material.IRON_BLOCK), item(Material.OBSIDIAN, 4), item(Material.IRON_BLOCK), null)
            }
            result(CustomItem.WITHER_COATING)
        }
        recipe {
            grid {
                row(item(Material.GOLDEN_APPLE, 4), item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.CHORUS_FRUIT, 64), item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.GOLDEN_APPLE, 4))
                row(item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.CHORUS_FLOWER, 16), item(Material.DISPENSER, 16), item(Material.CHORUS_FLOWER, 16), item(Material.POPPED_CHORUS_FRUIT, 32))
                row(item(Material.CHORUS_FRUIT, 64), item(Material.SHULKER_SHELL, 16), custom(CustomItem.DYE_PALETTE), item(Material.SHULKER_SHELL, 16), item(Material.CHORUS_FRUIT, 64))
                row(item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.CHORUS_FLOWER, 16), item(Material.DIAMOND_PICKAXE).ench("EF5","MN1","UN3"), item(Material.CHORUS_FLOWER, 16), item(Material.POPPED_CHORUS_FRUIT, 32))
                row(item(Material.GOLDEN_APPLE, 4), item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.CHORUS_FRUIT, 64), item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.GOLDEN_APPLE, 4))
            }
            result(CustomItem.SHULKER_FRUIT)
        }
        recipe {
            grid {
                row(null, null, null, null, null)
                row(null, item(Material.DISC_FRAGMENT_5, 2), item(Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE).ench("DU1"), item(Material.ECHO_SHARD, 2), null)
                row(null, item(Material.AMETHYST_SHARD, 32), item(Material.DRAGON_BREATH, 8), item(Material.AMETHYST_SHARD, 32), null)
                row(null, item(Material.ECHO_SHARD, 2), item(Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE).ench("DU1"), item(Material.DISC_FRAGMENT_5, 2), null)
                row(null, null, null, null, null)
            }
            result(CustomItem.NORMAL_WARDEN_SPAWNER)
        }
        recipe {
            grid {
                row(null, null, null, null, null)
                row(null, item(Material.SCULK_SHRIEKER, 2), item(Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE).ench("DU1"), item(Material.SCULK_SENSOR, 2), null)
                row(null, item(Material.SCULK_CATALYST), item(Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.SCULK_CATALYST), null)
                row(null, item(Material.SCULK_SENSOR, 2), item(Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE).ench("DU1"), item(Material.SCULK_SHRIEKER, 2), null)
                row(null, null, null, null, null)
            }
            result(CustomItem.HARD_WARDEN_SPAWNER)
        }


    }
}