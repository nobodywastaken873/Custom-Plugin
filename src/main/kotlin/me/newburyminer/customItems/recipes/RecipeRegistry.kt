package me.newburyminer.customItems.recipes

import me.newburyminer.customItems.Utils.Companion.basePotion
import me.newburyminer.customItems.Utils.Companion.ench
import me.newburyminer.customItems.Utils.Companion.storeEnch
import me.newburyminer.customItems.Utils.Companion.useEnch
import me.newburyminer.customItems.Utils.Companion.useOriginal
import me.newburyminer.customItems.Utils.Companion.usePotion
import me.newburyminer.customItems.Utils.Companion.useStoredEnch
import me.newburyminer.customItems.items.CustomItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionType

object RecipeRegistry {

    val recipes: MutableList<Recipe> = mutableListOf()

    private fun recipe(applyRecipe: RecipeBuilder.() -> Unit) {
        val builder = RecipeBuilder()
        builder.applyRecipe()
        val recipe = builder.build()
        recipes += recipe
    }

    private fun item(material: Material, amount: Int = 1): RecipeItem {
        return RecipeItem(material, amount)
    }

    private fun custom(customItem: CustomItem, amount: Int = 1): RecipeCustom {
        return RecipeCustom(customItem, amount)
    }

    fun registerAll() {

        recipe {
            grid {
                row(item(Material.PALE_OAK_BOAT), item(Material.RAIL, 64), item(Material.MANGROVE_BOAT), item(Material.POWERED_RAIL, 64), item(Material.ACACIA_BOAT))
                row(item(Material.MINECART), item(Material.GREEN_BED), item(Material.ENDER_PEARL, 16), item(Material.EMERALD_ORE, 8), item(Material.FURNACE_MINECART))
                row(item(Material.CHERRY_BOAT), item(Material.CHEST, 32), item(Material.CONDUIT), item(Material.BARREL, 8), item(Material.DARK_OAK_BOAT))
                row(item(Material.FURNACE_MINECART), item(Material.EMERALD_ORE, 8), item(Material.ENDER_PEARL, 16), item(Material.BROWN_BED), item(Material.MINECART))
                row(item(Material.JUNGLE_BOAT), item(Material.DETECTOR_RAIL, 64), item(Material.SPRUCE_BOAT), item(Material.ACTIVATOR_RAIL, 64), item(Material.BIRCH_BOAT))
            }
            result(CustomItem.VILLAGER_ATOMIZER)
        }

        recipe {
            grid {
                row(item(Material.LIME_STAINED_GLASS, 32), item(Material.TOTEM_OF_UNDYING), item(Material.EMERALD_BLOCK, 32), item(Material.TOTEM_OF_UNDYING), item(Material.GREEN_STAINED_GLASS, 32))
                row(item(Material.TOTEM_OF_UNDYING), item(Material.RABBIT_STEW), item(Material.OMINOUS_BOTTLE, 8).setOminous(0), item(Material.BEETROOT_SOUP), item(Material.TOTEM_OF_UNDYING))
                row(item(Material.MUSHROOM_STEW), item(Material.OMINOUS_BOTTLE, 8).setOminous(3), item(Material.NETHER_STAR, 2), item(Material.OMINOUS_BOTTLE, 8).setOminous(1), item(Material.SUSPICIOUS_STEW))
                row(item(Material.TOTEM_OF_UNDYING), item(Material.SLIME_BALL, 32), item(Material.OMINOUS_BOTTLE, 8).setOminous(2), item(Material.SLIME_BALL, 32), item(Material.TOTEM_OF_UNDYING))
                row(item(Material.OBSIDIAN, 32), item(Material.TOTEM_OF_UNDYING), item(Material.EMERALD_BLOCK, 32), item(Material.TOTEM_OF_UNDYING), item(Material.OBSIDIAN, 32))
            }
            result(CustomItem.JERRY_IDOL)
        }

        recipe {
            grid {
                row(item(Material.ROTTEN_FLESH, 32), item(Material.RAW_GOLD_BLOCK, 4), item(Material.FERMENTED_SPIDER_EYE, 32), item(Material.RAW_GOLD_BLOCK, 4), item(Material.ROTTEN_FLESH, 32))
                row(item(Material.RAW_GOLD_BLOCK, 4), item(Material.GOLDEN_APPLE, 4), item(Material.GILDED_BLACKSTONE, 16), item(Material.GOLDEN_APPLE, 4), item(Material.RAW_GOLD_BLOCK, 4))
                row(item(Material.BLAZE_POWDER, 16), item(Material.NETHER_GOLD_ORE, 16), item(Material.ENCHANTED_GOLDEN_APPLE, 2), item(Material.DEEPSLATE_GOLD_ORE, 16), item(Material.BLAZE_POWDER, 16))
                row(item(Material.RAW_GOLD_BLOCK, 4), item(Material.GOLDEN_APPLE, 4), item(Material.GOLD_ORE, 16), item(Material.GOLDEN_APPLE, 4), item(Material.RAW_GOLD_BLOCK, 4))
                row(item(Material.COBBLESTONE, 64), item(Material.RAW_GOLD_BLOCK, 4), item(Material.BLAZE_ROD, 16), item(Material.RAW_GOLD_BLOCK, 4), item(Material.COBBLESTONE, 64))
            }
            result(CustomItem.GOLDEN_ZOMBIE)
        }

        recipe {
            grid {
                row(item(Material.ARROW, 64), item(Material.FLINT, 32), item(Material.WIND_CHARGE, 32), item(Material.FLINT, 32), item(Material.ARROW, 64))
                row(item(Material.FEATHER, 32), item(Material.FLETCHING_TABLE, 8), item(Material.STICK, 32), item(Material.FLETCHING_TABLE, 8), item(Material.FEATHER, 32))
                row(item(Material.ENDER_PEARL, 16), item(Material.STICK, 32), item(Material.TIPPED_ARROW, 32), item(Material.STICK, 32), item(Material.POINTED_DRIPSTONE, 64))
                row(item(Material.FEATHER, 32), item(Material.FLETCHING_TABLE, 8), item(Material.STICK, 32), item(Material.FLETCHING_TABLE, 8), item(Material.FEATHER, 32))
                row(item(Material.ARROW, 64), item(Material.FLINT, 32), item(Material.WITHER_SKELETON_SKULL, 1), item(Material.FLINT, 32), item(Material.ARROW, 64))
            }
            result(CustomItem.FLETCHER_UPGRADE)
        }

        recipe {
            grid {
                row(item(Material.ARROW, 64), item(Material.FLINT, 32), item(Material.WIND_CHARGE, 32), item(Material.FLINT, 32), item(Material.ARROW, 64))
                row(item(Material.FEATHER, 32), item(Material.FLETCHING_TABLE, 8), item(Material.STICK, 32), item(Material.FLETCHING_TABLE, 8), item(Material.FEATHER, 32))
                row(item(Material.ENDER_PEARL, 16), item(Material.STICK, 32), item(Material.TIPPED_ARROW, 32), item(Material.STICK, 32), item(Material.POINTED_DRIPSTONE, 64))
                row(item(Material.FEATHER, 32), item(Material.FLETCHING_TABLE, 8), item(Material.STICK, 32), item(Material.FLETCHING_TABLE, 8), item(Material.FEATHER, 32))
                row(item(Material.ARROW, 64), item(Material.FLINT, 32), item(Material.WITHER_SKELETON_SKULL, 1), item(Material.FLINT, 32), item(Material.ARROW, 64))
            }
            result(CustomItem.FLETCHER_UPGRADE)
        }

        recipe {
            grid {
                row(item(Material.BARREL, 4), item(Material.FLETCHING_TABLE, 4), item(Material.GRINDSTONE, 4), item(Material.SMOKER, 4), item(Material.BLAST_FURNACE, 4))
                row(item(Material.FURNACE, 4), item(Material.DIAMOND_PICKAXE), item(Material.EMERALD, 32), item(Material.DIAMOND_AXE), item(Material.CARTOGRAPHY_TABLE, 4))
                row(item(Material.ENCHANTING_TABLE, 4), item(Material.EMERALD, 32), item(Material.GLOW_INK_SAC, 16), item(Material.EMERALD, 32), item(Material.LOOM, 4))
                row(item(Material.ANVIL, 4), item(Material.DIAMOND_AXE), item(Material.EMERALD, 32), item(Material.DIAMOND_PICKAXE), item(Material.BREWING_STAND, 4))
                row(item(Material.CRAFTING_TABLE, 4), item(Material.CRAFTER, 4), item(Material.LECTERN, 4), item(Material.STONECUTTER, 4), item(Material.CAULDRON, 4))
            }
			result(CustomItem.TRADING_SCRAMBLER)
        }
        recipe {
            grid {
                row(null, null, null, null, null)
                row(null, null, item(Material.NETHERITE_PICKAXE), null, null)
                row(null, item(Material.NETHERITE_AXE), item(Material.PHANTOM_MEMBRANE, 64), item(Material.NETHERITE_SHOVEL), null)
                row(null, null, item(Material.NETHERITE_HOE), null, null)
                row(null, null, null, null, null)
            }
			result(CustomItem.NETHERITE_MULTITOOL)
        }
        recipe {
            grid {
                row(item(Material.RAW_COPPER, 32), item(Material.RAW_IRON, 32), item(Material.DIAMOND, 16), item(Material.PALE_OAK_DOOR, 32), item(Material.DARK_OAK_FENCE_GATE, 32))
                row(item(Material.GRANITE, 32), item(Material.COBBLESTONE, 32), item(Material.STICK, 64), item(Material.OAK_LOG, 32), item(Material.SPRUCE_LOG, 32))
                row(item(Material.COBBLED_DEEPSLATE, 32), item(Material.NETHERITE_PICKAXE).ench("EF5", "UN3", "MN1"), item(Material.STRING, 64), item(Material.NETHERITE_AXE).ench("EF5", "UN3", "MN1"), item(Material.BIRCH_LOG, 32))
                row(item(Material.DIORITE, 32), item(Material.ANDESITE, 32), item(Material.STICK, 64), item(Material.DARK_OAK_LOG, 32), item(Material.JUNGLE_LOG, 32))
                row(item(Material.RAW_GOLD, 32), item(Material.COAL, 32), item(Material.DIAMOND, 16), item(Material.CRIMSON_TRAPDOOR, 32), item(Material.MANGROVE_SLAB, 32))
            }
			result(CustomItem.AXEPICK)
        }
        recipe {
            grid {
                row(null, null, item(Material.MELON, 32), null, null)
                row(null, item(Material.PUMPKIN_SEEDS, 32), item(Material.MELON_SEEDS, 32), item(Material.POTATO, 32), null)
                row(null, item(Material.WHEAT_SEEDS, 32), item(Material.NETHERITE_HOE), item(Material.CARROT, 32), null)
                row(null, item(Material.WHEAT, 32), item(Material.BEETROOT, 32), item(Material.BEETROOT_SEEDS, 32), null)
                row(null, null, item(Material.PUMPKIN, 32), null, null)
            }
			result(CustomItem.HOE)
        }
        recipe {
            grid {
                row(item(Material.PALE_MOSS_BLOCK, 32), item(Material.WARPED_WART_BLOCK, 32), item(Material.DIAMOND, 16), item(Material.CLAY, 32), item(Material.SOUL_SAND, 32))
                row(item(Material.ACACIA_LEAVES, 32), item(Material.AZALEA_LEAVES, 32), item(Material.STICK, 64), item(Material.COARSE_DIRT, 32), item(Material.MUD, 32))
                row(item(Material.MOSS_BLOCK, 32), item(Material.NETHERITE_SHOVEL).ench("EF5", "UN3", "MN1"), item(Material.STRING, 64), item(Material.NETHERITE_HOE).ench("EF5", "UN3", "MN1"), item(Material.SNOW_BLOCK, 32))
                row(item(Material.OAK_LEAVES, 32), item(Material.HAY_BLOCK, 32), item(Material.STICK, 64), item(Material.DIRT, 32), item(Material.GRAVEL, 32))
                row(item(Material.PALE_MOSS_BLOCK, 32), item(Material.NETHER_WART_BLOCK, 32), item(Material.DIAMOND, 16), item(Material.SAND, 32), item(Material.SOUL_SOIL, 32))
            }
			result(CustomItem.HOEVEL)
        }
        recipe {
            grid {
                row(item(Material.LAPIS_ORE, 16), item(Material.DEEPSLATE_LAPIS_ORE, 16), item(Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.DEEPSLATE_COPPER_ORE, 16), item(Material.COPPER_ORE, 16))
                row(item(Material.IRON_BLOCK, 16), item(Material.IRON_PICKAXE), item(Material.COPPER_BLOCK, 16), item(Material.NETHERITE_PICKAXE), item(Material.DEEPSLATE_DIAMOND_ORE, 16))
                row(item(Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.TNT, 16), item(Material.END_CRYSTAL, 16), item(Material.TNT, 16), item(Material.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal())
                row(item(Material.COAL_ORE, 16), item(Material.NETHERITE_PICKAXE), item(Material.COPPER_BLOCK, 16), item(Material.GOLDEN_PICKAXE), item(Material.GOLD_BLOCK, 16))
                row(item(Material.REDSTONE_ORE, 16), item(Material.DEEPSLATE_REDSTONE_ORE, 16), item(Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.DEEPSLATE_IRON_ORE, 16), item(Material.IRON_ORE, 16))
            }
			result(CustomItem.VEINY_PICKAXE)
        }
        recipe {
            grid {
                row(item(Material.DARK_OAK_LOG, 16), item(Material.OAK_LEAVES, 64), item(Material.ACACIA_LOG, 16), item(Material.SPRUCE_LEAVES, 64), item(Material.JUNGLE_LOG, 16))
                row(item(Material.OAK_LOG, 32), item(Material.GOLDEN_AXE), item(Material.WOODEN_AXE), item(Material.GOLDEN_AXE), item(Material.DARK_OAK_LEAVES, 64))
                row(item(Material.MANGROVE_LOG, 16), item(Material.DIAMOND_AXE), item(Material.NETHERITE_AXE), item(Material.STONE_AXE), item(Material.BIRCH_LOG, 16))
                row(item(Material.ACACIA_LEAVES, 64), item(Material.GOLDEN_AXE), item(Material.IRON_AXE), item(Material.GOLDEN_AXE), item(Material.OAK_LOG, 32))
                row(item(Material.CHERRY_LOG, 16), item(Material.BIRCH_LEAVES, 64), item(Material.PALE_OAK_LOG, 16), item(Material.JUNGLE_LEAVES, 64), item(Material.SPRUCE_LOG, 16))
            }
			result(CustomItem.TREECAPITATOR)
        }
        recipe {
            grid {
                row(null, null, item(Material.SHEARS), null, null)
                row(null, item(Material.IRON_INGOT, 32), item(Material.SHEARS), item(Material.FEATHER, 32), null)
                row(item(Material.FLINT, 32), item(Material.FLINT_AND_STEEL), item(Material.SHEARS), item(Material.BRUSH), item(Material.COPPER_INGOT, 32))
                row(null, item(Material.IRON_INGOT, 32), item(Material.SHEARS), item(Material.STICK, 32), null)
                row(null, null, item(Material.SHEARS), null, null)
            }
			result(CustomItem.POCKETKNIFE_MULTITOOL)
        }
        recipe {
            grid {
                row(item(Material.DIAMOND, 16), item(Material.STONE, 32), item(Material.AMETHYST_SHARD, 32), item(Material.STONE, 32), item(Material.DIAMOND, 16))
                row(item(Material.PISTON, 16), item(Material.GUNPOWDER, 32), item(Material.TNT, 32), item(Material.GUNPOWDER, 32), item(Material.DROPPER, 16))
                row(item(Material.REDSTONE_TORCH, 16), item(Material.TNT, 32), item(Material.NETHERITE_PICKAXE).ench("EF5", "UN3", "MN1"), item(Material.TNT, 32), item(Material.REDSTONE_TORCH, 16))
                row(item(Material.DROPPER, 16), item(Material.GUNPOWDER, 32), item(Material.TNT, 32), item(Material.GUNPOWDER, 32), item(Material.PISTON, 16))
                row(item(Material.DIAMOND, 16), item(Material.DEEPSLATE, 32), item(Material.AMETHYST_SHARD, 32), item(Material.DEEPSLATE, 32), item(Material.DIAMOND, 16))
            }
			result(CustomItem.EXCAVATOR)
        }
        recipe {
            grid {
                row(item(Material.DIAMOND_ORE), item(Material.DIAMOND, 8), item(Material.SUSPICIOUS_SAND, 2), item(Material.DIAMOND, 8), item(Material.MYCELIUM))
                row(null, item(Material.ANCIENT_DEBRIS), item(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE), item(Material.ANCIENT_DEBRIS), null)
                row(null, custom(CustomItem.AXEPICK), item(Material.NETHER_STAR, 2), custom(CustomItem.HOEVEL), null)
                row(null, item(Material.ANCIENT_DEBRIS), item(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE), item(Material.ANCIENT_DEBRIS), null)
                row(item(Material.DEEPSLATE_COAL_ORE), item(Material.DIAMOND, 8), item(Material.SUSPICIOUS_GRAVEL, 2), item(Material.DIAMOND, 8), item(Material.MUDDY_MANGROVE_ROOTS))
            }
			result(CustomItem.NETHERITE_MATTOCK)
        }
        recipe {
            grid {
                row(item(Material.LIGHT_GRAY_CONCRETE, 32), item(Material.IRON_INGOT, 16), item(Material.SEA_LANTERN, 32), item(Material.IRON_INGOT, 16), item(Material.BLUE_CONCRETE, 32))
                row(item(Material.IRON_INGOT, 16), item(Material.PRISMARINE_SHARD, 16), item(Material.HEART_OF_THE_SEA), item(Material.PRISMARINE_CRYSTALS, 16), item(Material.IRON_INGOT, 16))
                row(item(Material.DARK_PRISMARINE, 32), item(Material.PRISMARINE, 32), item(Material.HEAVY_CORE), item(Material.PRISMARINE, 32), item(Material.DARK_PRISMARINE, 32))
                row(item(Material.IRON_INGOT, 16), item(Material.PRISMARINE_CRYSTALS, 16), item(Material.HEART_OF_THE_SEA), item(Material.PRISMARINE_SHARD, 16), item(Material.IRON_INGOT, 16))
                row(item(Material.RED_CONCRETE, 32), item(Material.IRON_INGOT, 16), item(Material.SEA_LANTERN, 32), item(Material.IRON_INGOT, 16), item(Material.LIGHT_GRAY_CONCRETE, 32))
            }
			result(CustomItem.POLARIZED_MAGNET)
        }
        recipe {
            grid {
                row(item(Material.WAXED_COPPER_BLOCK, 32), item(Material.CROSSBOW), item(Material.GUSTER_BANNER_PATTERN), item(Material.DIAMOND, 8), item(Material.WAXED_OXIDIZED_COPPER, 32))
                row(item(Material.ENCHANTED_BOOK).storeEnch("WB1"), item(Material.MUSIC_DISC_PRECIPICE), item(Material.BREEZE_ROD, 16), item(Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.CROSSBOW))
                row(item(Material.FLOW_BANNER_PATTERN), item(Material.WIND_CHARGE, 32), item(Material.HEAVY_CORE), item(Material.WIND_CHARGE, 32), item(Material.FLOW_BANNER_PATTERN))
                row(item(Material.CROSSBOW), item(Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.BREEZE_ROD, 16), item(Material.MUSIC_DISC_CREATOR), item(Material.ENCHANTED_BOOK).storeEnch("WB1"))
                row(item(Material.WAXED_COPPER_BLOCK, 32), item(Material.DIAMOND, 8), item(Material.GUSTER_BANNER_PATTERN), item(Material.CROSSBOW), item(Material.WAXED_OXIDIZED_COPPER, 32))
            }
			result(CustomItem.WIND_CHARGE_CANNON)
        }
        recipe {
            grid {
                row(item(Material.NETHER_BRICK, 32), item(Material.ARROW, 32), item(Material.CARROT_ON_A_STICK), item(Material.STRING, 32), item(Material.BRICK, 32))
                row(item(Material.ARMADILLO_SCUTE, 8), item(Material.SADDLE), item(Material.EXPLORER_POTTERY_SHERD), item(Material.SADDLE), item(Material.ARMADILLO_SCUTE, 8))
                row(item(Material.LEAD, 16), item(Material.ARMS_UP_POTTERY_SHERD), item(Material.CROSSBOW).ench("QC3", "PR4", "MN1", "UN3"), item(Material.SNORT_POTTERY_SHERD), item(Material.LEAD, 16))
                row(item(Material.ARMADILLO_SCUTE, 8), item(Material.SADDLE), item(Material.ARCHER_POTTERY_SHERD), item(Material.SADDLE), item(Material.ARMADILLO_SCUTE, 8))
                row(item(Material.BRICK, 32), item(Material.STRING, 32), item(Material.WARPED_FUNGUS_ON_A_STICK), item(Material.ARROW, 32), item(Material.NETHER_BRICK, 32))
            }
			result(CustomItem.RIDABLE_CROSSBOW)
        }
        recipe {
            grid {
                row(null, null, item(Material.LEATHER, 32), null, null)
                row(item(Material.SUGAR, 32), item(Material.IRON_HORSE_ARMOR), item(Material.DIAMOND_HORSE_ARMOR), item(Material.LEATHER_HORSE_ARMOR), item(Material.RABBIT_FOOT, 2))
                row(item(Material.SADDLE), item(Material.GOLDEN_HORSE_ARMOR), item(Material.NETHERITE_HELMET), item(Material.GOLDEN_HORSE_ARMOR), item(Material.SADDLE))
                row(item(Material.RABBIT_FOOT, 2), item(Material.LEATHER_HORSE_ARMOR), item(Material.DIAMOND_HORSE_ARMOR), item(Material.IRON_HORSE_ARMOR), item(Material.SUGAR, 32))
                row(null, null, item(Material.LEATHER, 32), null, null)
            }
			result(CustomItem.COWBOY_HAT)
        }
        recipe {
            grid {
                row(null, null, item(Material.RAW_IRON_BLOCK, 16), null, null)
                row(null, item(Material.OCHRE_FROGLIGHT, 2), item(Material.DIAMOND_PICKAXE), item(Material.COPPER_BULB, 2), null)
                row(item(Material.ENCHANTED_BOOK).storeEnch("EF5"), item(Material.DIAMOND_PICKAXE), item(Material.NETHERITE_HELMET), item(Material.DIAMOND_PICKAXE), item(Material.ENCHANTED_BOOK).storeEnch("EF5"))
                row(null, item(Material.VERDANT_FROGLIGHT, 2), item(Material.DIAMOND_PICKAXE), item(Material.PEARLESCENT_FROGLIGHT, 2), null)
                row(null, null, item(Material.RAW_GOLD_BLOCK, 16), null, null)
            }
			result(CustomItem.MINERS_HELM)
        }
        recipe {
            grid {
                row(item(Material.CYAN_GLAZED_TERRACOTTA, 8), item(Material.REDSTONE_TORCH, 32), item(Material.QUARTZ_BLOCK, 16), item(Material.LEVER, 32), item(Material.BROWN_GLAZED_TERRACOTTA, 8))
                row(item(Material.SCRAPE_POTTERY_SHERD), item(Material.STICK, 32), item(Material.SCAFFOLDING, 32), item(Material.STICK, 32), item(Material.MINER_POTTERY_SHERD))
                row(item(Material.STICKY_PISTON, 32), item(Material.SCAFFOLDING, 32), item(Material.NETHERITE_LEGGINGS), item(Material.SCAFFOLDING, 32), item(Material.STICKY_PISTON, 32))
                row(item(Material.FRIEND_POTTERY_SHERD), item(Material.STICK, 32), item(Material.SCAFFOLDING, 32), item(Material.STICK, 32), item(Material.SHELTER_POTTERY_SHERD))
                row(item(Material.GREEN_GLAZED_TERRACOTTA, 8), item(Material.LEVER, 32), item(Material.QUARTZ_BLOCK, 16), item(Material.REDSTONE_TORCH, 32), item(Material.BLUE_GLAZED_TERRACOTTA, 8))
            }
			result(CustomItem.TOOLBELT)
        }
        recipe {
            grid {
                row(item(Material.REDSTONE_TORCH, 32), item(Material.BLAZE_ROD, 32), item(Material.MAGMA_CREAM, 32), item(Material.BLAZE_ROD, 32), item(Material.REDSTONE_TORCH, 32))
                row(item(Material.REDSTONE_BLOCK, 16), item(Material.FIREWORK_ROCKET, 64), item(Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.FIREWORK_ROCKET, 64), item(Material.REDSTONE_BLOCK, 16))
                row(item(Material.REPEATER, 32), item(Material.FIREWORK_ROCKET, 64), item(Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.FIREWORK_ROCKET, 64), item(Material.REPEATER, 32))
                row(item(Material.COMPARATOR, 32), item(Material.FIREWORK_ROCKET, 64), item(Material.IRON_BLOCK, 32), item(Material.FIREWORK_ROCKET, 64), item(Material.COMPARATOR, 32))
                row(item(Material.LAVA_BUCKET), item(Material.CAMPFIRE, 32), item(Material.SOUL_CAMPFIRE, 32), item(Material.CAMPFIRE, 32), item(Material.LAVA_BUCKET))
            }
			result(CustomItem.JETPACK_CONTROLLER_SET)
        }
        recipe {
            grid {
                row(item(Material.SPYGLASS), item(Material.SOUL_CAMPFIRE, 32), item(Material.CANDLE, 16), item(Material.CAMPFIRE, 32), item(Material.SPYGLASS))
                row(item(Material.FIREWORK_ROCKET, 32), item(Material.DISC_FRAGMENT_5, 4), item(Material.COMPASS, 16), item(Material.DISC_FRAGMENT_5, 4), item(Material.CLOCK, 16))
                row(item(Material.BLACK_CANDLE, 16), item(Material.COMPASS, 16), item(Material.RECOVERY_COMPASS, 4), item(Material.COMPASS, 16), item(Material.BLACK_CANDLE, 16))
                row(item(Material.CLOCK, 16), item(Material.DISC_FRAGMENT_5, 4), item(Material.COMPASS, 16), item(Material.DISC_FRAGMENT_5, 4), item(Material.FIREWORK_ROCKET, 32))
                row(item(Material.SPYGLASS), item(Material.CAMPFIRE, 32), item(Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.SOUL_CAMPFIRE, 32), item(Material.SPYGLASS))
            }
			result(CustomItem.TRACKING_COMPASS)
        }
        recipe {
            grid {
                row(item(Material.SCULK, 64), item(Material.SCULK_VEIN, 64), item(Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.SCULK_VEIN, 64), item(Material.SCULK, 64))
                row(item(Material.SCULK_SHRIEKER, 32), item(Material.ECHO_SHARD, 16), item(Material.MUSIC_DISC_OTHERSIDE), item(Material.ECHO_SHARD, 16), item(Material.SCULK_SENSOR, 32))
                row(item(Material.SCULK_CATALYST, 8), item(Material.MUSIC_DISC_5), item(Material.EXPERIENCE_BOTTLE, 64), item(Material.MUSIC_DISC_5), item(Material.SCULK_CATALYST, 8))
                row(item(Material.SCULK_SENSOR, 32), item(Material.ECHO_SHARD, 16), item(Material.MUSIC_DISC_OTHERSIDE), item(Material.ECHO_SHARD, 16), item(Material.SCULK_SHRIEKER, 32))
                row(item(Material.SCULK, 64), item(Material.SCULK_VEIN, 64), item(Material.GLASS_BOTTLE, 64), item(Material.SCULK_VEIN, 64), item(Material.SCULK, 64))
            }
			result(CustomItem.EXPERIENCE_FLASK)
        }
        recipe {
            grid {
                row(item(Material.ENCHANTED_BOOK), item(Material.MUSIC_DISC_CAT), item(Material.EXPERIENCE_BOTTLE, 16), item(Material.MUSIC_DISC_13), item(Material.ENCHANTED_BOOK))
                row(item(Material.GLOW_BERRIES, 32), item(Material.SCULK, 64), item(Material.APPLE, 32), item(Material.SCULK, 64), item(Material.GLOW_BERRIES, 32))
                row(item(Material.SOUL_TORCH, 32), item(Material.APPLE, 32), item(Material.ENCHANTED_GOLDEN_APPLE), item(Material.APPLE, 32), item(Material.SOUL_TORCH, 32))
                row(item(Material.GLOW_BERRIES, 32), item(Material.SCULK, 64), item(Material.APPLE, 32), item(Material.SCULK, 64), item(Material.GLOW_BERRIES, 32))
                row(item(Material.ENCHANTED_BOOK), item(Material.MUSIC_DISC_13), item(Material.EXPERIENCE_BOTTLE, 16), item(Material.MUSIC_DISC_CAT), item(Material.ENCHANTED_BOOK))
            }
			result(CustomItem.MYSTICAL_GREEN_APPLE)
        }
        recipe {
            grid {
                row(item(Material.IRON_BARS, 32), item(Material.CHAIN, 32), item(Material.IRON_BARS, 32), item(Material.CHAIN, 32), item(Material.IRON_BARS, 32))
                row(item(Material.CHAIN, 32), item(Material.CHAINMAIL_BOOTS).ench("PT4", "UN3","MN1"), item(Material.SHULKER_SHELL, 4), item(Material.CHAINMAIL_LEGGINGS).ench("PT4", "UN3","MN1"), item(Material.CHAIN, 32))
                row(item(Material.IRON_BARS, 32), item(Material.COBWEB, 32), item(Material.LEAD, 16), item(Material.COBWEB, 32), item(Material.IRON_BARS, 32))
                row(item(Material.CHAIN, 32), item(Material.CHAINMAIL_CHESTPLATE).ench("PT4", "UN3","MN1"), item(Material.SHULKER_SHELL, 4), item(Material.CHAINMAIL_HELMET).ench("PT4", "UN3","MN1"), item(Material.CHAIN, 32))
                row(item(Material.IRON_BARS, 32), item(Material.CHAIN, 32), item(Material.IRON_BARS, 32), item(Material.CHAIN, 32), item(Material.IRON_BARS, 32))
            }
			result(CustomItem.REINFORCED_CAGE)
        }
        recipe {
            grid {
                row(null, null, null, null, null)
                row(null, custom(CustomItem.INPUT_DEVICES), item(Material.COPPER_GRATE, 32), custom(CustomItem.MINECART_MATERIALS), null)
                row(null, item(Material.COPPER_GRATE, 32), item(Material.HONEY_BLOCK, 16), item(Material.COPPER_GRATE, 32), null)
                row(null, custom(CustomItem.ACTUAL_REDSTONE), item(Material.COPPER_GRATE, 32), custom(CustomItem.CONTAINERS), null)
                row(null, null, null, null, null)
            }
			result(CustomItem.REDSTONE_AMALGAMATION)
        }
        recipe {
            grid {
                row(null, null, item(Material.DEEPSLATE_REDSTONE_ORE, 16), null, null)
                row(null, item(Material.OAK_BUTTON, 64), item(Material.STONE_PRESSURE_PLATE, 64), item(Material.REDSTONE, 32), null)
                row(null, item(Material.OAK_PRESSURE_PLATE, 64), item(Material.LEVER, 64), item(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, 64), null)
                row(null, item(Material.REDSTONE, 32), item(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 64), item(Material.STONE_BUTTON, 64), null)
                row(null, null, item(Material.DEEPSLATE_REDSTONE_ORE, 16), null, null)
            }
			result(CustomItem.INPUT_DEVICES)
        }
        recipe {
            grid {
                row(null, null, item(Material.DEEPSLATE_REDSTONE_ORE, 16), null, null)
                row(null, item(Material.RAIL, 64), item(Material.CHEST_MINECART), item(Material.POWERED_RAIL, 64), null)
                row(item(Material.REDSTONE, 32), item(Material.FURNACE_MINECART), item(Material.TNT_MINECART), item(Material.HOPPER_MINECART), item(Material.REDSTONE, 32))
                row(null, item(Material.ACTIVATOR_RAIL, 64), item(Material.MINECART), item(Material.DETECTOR_RAIL, 64), null)
                row(null, null, item(Material.DEEPSLATE_REDSTONE_ORE, 16), null, null)
            }
			result(CustomItem.MINECART_MATERIALS)
        }
        recipe {
            grid {
                row(null, null, item(Material.DEEPSLATE_REDSTONE_ORE, 16), null, null)
                row(null, item(Material.OBSERVER, 16), item(Material.REDSTONE, 32), item(Material.COMPARATOR, 16), null)
                row(null, item(Material.REDSTONE_TORCH, 32), item(Material.REDSTONE_BLOCK, 16), item(Material.REDSTONE_TORCH, 32), null)
                row(null, item(Material.REPEATER, 16), item(Material.REDSTONE, 32), item(Material.OBSERVER, 16), null)
                row(null, null, item(Material.DEEPSLATE_REDSTONE_ORE, 16), null, null)
            }
			result(CustomItem.ACTUAL_REDSTONE)
        }
        recipe {
            grid {
                row(null, null, item(Material.DEEPSLATE_REDSTONE_ORE, 16), null, null)
                row(null, item(Material.STICKY_PISTON, 16), item(Material.BARREL, 32), item(Material.DROPPER, 16), null)
                row(item(Material.SLIME_BLOCK, 8), item(Material.CRAFTER, 16), item(Material.NOTE_BLOCK, 32), item(Material.HOPPER, 32), item(Material.SLIME_BLOCK, 8))
                row(null, item(Material.DISPENSER, 16), item(Material.CHEST, 32), item(Material.PISTON, 16), null)
                row(null, null, item(Material.DEEPSLATE_REDSTONE_ORE, 16), null, null)
            }
			result(CustomItem.CONTAINERS)
        }
        recipe {
            grid {
                row(item(Material.GOLDEN_APPLE, 4), item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.CHORUS_FRUIT, 64), item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.GOLDEN_APPLE, 4))
                row(item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.CHORUS_FLOWER, 32), item(Material.DISPENSER, 16), item(Material.CHORUS_FLOWER, 32), item(Material.POPPED_CHORUS_FRUIT, 32))
                row(item(Material.CHORUS_FRUIT, 64), item(Material.SHULKER_SHELL, 16), item(Material.CHORUS_FRUIT, 64), item(Material.SHULKER_SHELL, 16), item(Material.CHORUS_FRUIT, 64))
                row(item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.CHORUS_FLOWER, 32), item(Material.DIAMOND_PICKAXE).ench("EF5","UN3","MN1"), item(Material.CHORUS_FLOWER, 32), item(Material.POPPED_CHORUS_FRUIT, 32))
                row(item(Material.GOLDEN_APPLE, 4), item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.CHORUS_FRUIT, 64), item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.GOLDEN_APPLE, 4))
            }
			result(CustomItem.SHULKER_FRUIT)
        }
        recipe {
            grid {
                row(null, item(Material.BLAZE_ROD, 8), item(Material.CHORUS_FRUIT, 16), item(Material.BLAZE_ROD, 8), null)
                row(item(Material.BLAZE_POWDER, 8), item(Material.ENDER_EYE, 16), item(Material.CRYING_OBSIDIAN, 16), item(Material.ENDER_PEARL, 16), item(Material.BLAZE_POWDER, 8))
                row(item(Material.ENDER_CHEST, 4), item(Material.OBSIDIAN, 16), item(Material.DRAGON_HEAD), item(Material.OBSIDIAN, 16), item(Material.ENDER_CHEST, 4))
                row(item(Material.BLAZE_POWDER, 8), item(Material.ENDER_PEARL, 16), item(Material.CRYING_OBSIDIAN, 16), item(Material.ENDER_EYE, 16), item(Material.BLAZE_POWDER, 8))
                row(null, item(Material.BLAZE_ROD, 8), item(Material.CHORUS_FRUIT, 16), item(Material.BLAZE_ROD, 8), null)
            }
			result(CustomItem.ENDER_NODE)
        }
        recipe {
            grid {
                row(item(Material.BLAZE_POWDER, 16), item(Material.END_ROD, 16), item(Material.ENDER_EYE, 16), item(Material.END_ROD, 16), item(Material.BLAZE_ROD, 16))
                row(item(Material.END_ROD, 16), item(Material.CRYING_OBSIDIAN, 32), item(Material.DRAGON_HEAD), item(Material.OBSIDIAN, 32), item(Material.END_ROD, 16))
                row(item(Material.ENDER_EYE, 16), item(Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.NETHERITE_SWORD).ench("SH5","FA2","UN3","MN1"), item(Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.ENDER_EYE, 16))
                row(item(Material.END_ROD, 16), item(Material.OBSIDIAN, 32), item(Material.DRAGON_HEAD), item(Material.CRYING_OBSIDIAN, 32), item(Material.END_ROD, 16))
                row(item(Material.BLAZE_ROD, 16), item(Material.END_ROD, 16), item(Material.ENDER_EYE, 16), item(Material.END_ROD, 16), item(Material.BLAZE_POWDER, 16))
            }
			result(CustomItem.ENDER_BLADE)
        }
        recipe {
            grid {
                row(item(Material.STRING, 64), item(Material.ARROW, 64), null, null, null)
                row(item(Material.STRING, 64), item(Material.WIND_CHARGE, 64), item(Material.FEATHER, 32), item(Material.FIREWORK_ROCKET, 64), null)
                row(item(Material.STRING, 64), item(Material.BOW).ench("PW5","UN3","IN1"), item(Material.CROSSBOW).ench("PR4","UN3","MN1"), item(Material.TRIDENT).ench("LY3","UN3","MN1"), item(Material.TRIPWIRE_HOOK, 64))
                row(item(Material.STRING, 64), item(Material.WIND_CHARGE, 64), item(Material.FEATHER, 32), item(Material.FIREWORK_ROCKET, 64), null)
                row(item(Material.STRING, 64), item(Material.ARROW, 64), null, null, null)
            }
			result(CustomItem.WIND_HOOK)
        }
        recipe {
            grid {
                row(item(Material.FIREWORK_ROCKET, 64), item(Material.FIREWORK_ROCKET, 64), item(Material.TNT_MINECART), null, null)
                row(item(Material.IRON_BLOCK, 8), item(Material.IRON_BLOCK, 8), item(Material.IRON_BLOCK, 8), item(Material.TNT_MINECART), null)
                row(item(Material.CROSSBOW).ench("PR4","UN3","MN1","QC3"), item(Material.CROSSBOW).ench("PR4","UN3","MN1","QC3"), item(Material.CROSSBOW).ench("PR4","UN3","MN1","QC3"), item(Material.IRON_BLOCK, 8), item(Material.TNT, 32))
                row(item(Material.IRON_BLOCK, 8), item(Material.IRON_BLOCK, 8), item(Material.IRON_BLOCK, 8), item(Material.TNT_MINECART), null)
                row(item(Material.FIREWORK_ROCKET, 64), item(Material.FIREWORK_ROCKET, 64), item(Material.TNT_MINECART), null, null)
            }
			result(CustomItem.SURFACE_TO_AIR_MISSILE)
        }
        recipe {
            grid {
                row(item(Material.SUGAR, 16), item(Material.BLAZE_POWDER, 16), item(Material.MAGMA_CREAM, 16), item(Material.GHAST_TEAR, 16), item(Material.GLISTERING_MELON_SLICE, 16))
                row(item(Material.NETHER_WART, 16), item(Material.BREWING_STAND, 16), item(Material.REDSTONE_BLOCK, 32), item(Material.BEACON), item(Material.GOLDEN_CARROT, 16))
                row(item(Material.TURTLE_HELMET), item(Material.REDSTONE_ORE, 16), item(Material.NETHERITE_HELMET).ench("PT4","UN3","MN1","AA1","RS3"), item(Material.REDSTONE_ORE, 16), item(Material.PUFFERFISH, 16))
                row(item(Material.BREEZE_ROD, 16), item(Material.BEACON), item(Material.REDSTONE_BLOCK, 32), item(Material.BREWING_STAND, 16), item(Material.RABBIT_FOOT, 16))
                row(item(Material.COBWEB, 16), item(Material.SLIME_BLOCK, 16), item(Material.FERMENTED_SPIDER_EYE, 16), item(Material.SPIDER_EYE, 16), item(Material.PHANTOM_MEMBRANE, 16))
            }
			result(CustomItem.DRINKING_HAT)
        }
        recipe {
            grid {
                row(item(Material.SOUL_LANTERN, 32), item(Material.FLOWER_BANNER_PATTERN), item(Material.DRAGON_HEAD, 4), item(Material.FLOWER_BANNER_PATTERN), item(Material.LANTERN, 32))
                row(item(Material.SPYGLASS), item(Material.SPECTRAL_ARROW, 64), item(Material.OPEN_EYEBLOSSOM, 32), item(Material.SPECTRAL_ARROW, 64), item(Material.SPYGLASS))
                row(item(Material.ENDER_EYE, 32), item(Material.TINTED_GLASS, 32), item(Material.NETHERITE_HELMET).ench("PT4","UN3","MN1","AA1","RS3"), item(Material.TINTED_GLASS, 32), item(Material.ENDER_EYE, 32))
                row(item(Material.SPYGLASS), item(Material.SPECTRAL_ARROW, 64), item(Material.OPEN_EYEBLOSSOM, 32), item(Material.SPECTRAL_ARROW, 64), item(Material.SPYGLASS))
                row(item(Material.LANTERN, 32), item(Material.FLOWER_BANNER_PATTERN), item(Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.FLOWER_BANNER_PATTERN), item(Material.SOUL_LANTERN, 32))
            }
			result(CustomItem.XRAY_GOGGLES)
        }
        recipe {
            grid {
                row(null, item(Material.NETHERITE_INGOT), item(Material.IRON_BLOCK, 64), item(Material.COPPER_BLOCK, 64), item(Material.ANVIL, 16))
                row(null, item(Material.RABBIT_HIDE, 16), item(Material.GOLD_BLOCK, 64), item(Material.HEAVY_CORE), item(Material.COPPER_BLOCK, 64))
                row(null, item(Material.LEATHER, 16), item(Material.NETHERITE_AXE).ench("SH5","EF5","UN3","MN1","FT3"), item(Material.GOLD_BLOCK, 64), item(Material.IRON_BLOCK, 64))
                row(item(Material.LEATHER, 16), item(Material.BREEZE_ROD, 32), item(Material.LEATHER, 16), item(Material.RABBIT_HIDE, 16), item(Material.NETHERITE_INGOT))
                row(item(Material.BLADE_POTTERY_SHERD), item(Material.LEATHER, 16), null, null, null)
            }
			result(CustomItem.GRAVITY_HAMMER)
        }
        recipe {
            grid {
                row(item(Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.BRAIN_CORAL_BLOCK, 16), item(Material.DRIED_KELP_BLOCK, 64), item(Material.BUBBLE_CORAL_BLOCK, 16), item(Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal())
                row(item(Material.TURTLE_SCUTE, 8), item(Material.FIRE_CORAL_BLOCK, 16), item(Material.HEART_OF_THE_SEA), item(Material.TUBE_CORAL_BLOCK, 16), item(Material.TURTLE_SCUTE, 8))
                row(item(Material.SALMON, 64), item(Material.GLOW_INK_SAC, 16), item(Material.NETHERITE_BOOTS).ench("PT4","UN3","MN1","FF4","SP3","DS3"), item(Material.GLOW_INK_SAC, 16), item(Material.COD, 64))
                row(item(Material.PRISMARINE_CRYSTALS, 32), item(Material.INK_SAC, 16), item(Material.NAUTILUS_SHELL, 4), item(Material.INK_SAC, 16), item(Material.PRISMARINE_CRYSTALS, 32))
                row(item(Material.PRISMARINE_SHARD, 32), item(Material.SEA_LANTERN, 16), item(Material.TURTLE_EGG, 4), item(Material.SEA_LANTERN, 16), item(Material.PRISMARINE_SHARD, 32))
            }
			result(CustomItem.AQUEOUS_SANDALS)
        }
        recipe {
            grid {
                row(item(Material.SPLASH_POTION).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.HOWL_POTTERY_SHERD), item(Material.MILK_BUCKET), item(Material.HEART_POTTERY_SHERD), item(Material.SPLASH_POTION).setPotion(PotionType.LONG_SLOW_FALLING))
                row(item(Material.WHITE_BANNER, 16), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.SHULKER_SHELL, 32), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.WHITE_BANNER, 16))
                row(item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.PHANTOM_MEMBRANE, 32), item(Material.NETHERITE_BOOTS).ench("PT4","UN3","MN1","FF4","SP3","DS3"), item(Material.PHANTOM_MEMBRANE, 32), item(Material.POPPED_CHORUS_FRUIT, 32))
                row(item(Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.SHULKER_SHELL, 32), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal())
                row(item(Material.SPLASH_POTION).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.FEATHER, 32), item(Material.ELYTRA).ench("UN3","MN1"), item(Material.FEATHER, 32), item(Material.SPLASH_POTION).setPotion(PotionType.LONG_SLOW_FALLING))
            }
			result(CustomItem.STABILZING_SNEAKERS)
        }
        recipe {
            grid {
                row(null, null, null, null, null)
                row(null, null, null, null, null)
                row(null, null, item(Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE).ench("DU1"), null, null)
                row(null, null, null, null, null)
                row(null, null, null, null, null)
            }
			result(CustomItem.WARDEN_SPAWNER)
        }

    }

    /*
    recipe {
        grid {
            row(null, null, null, null, null)
            row(null, null, null, null, null)
            row(null, null, null, null, null)
            row(null, null, null, null, null)
            row(null, null, null, null, null)
        }
        result(CustomItem.HOE)
    }
     */

}