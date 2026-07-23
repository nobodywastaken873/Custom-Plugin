package me.newburyminer.customItems.recipes.registrars

import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.recipes.RecipeBootstrapper
import me.newburyminer.customItems.recipes.RecipeType
import org.bukkit.Material


object ToolRecipeBootstrapper: RecipeBootstrapper {
    override val recipeType: RecipeType = RecipeType.TOOL
    override fun bootstrap() {
        recipe {
            grid {
                row(item(Material.IRON_BARS, 32), item(Material.IRON_CHAIN, 32), item(Material.IRON_BARS, 32), item(Material.IRON_CHAIN, 32), item(Material.IRON_BARS, 32))
                row(item(Material.IRON_CHAIN, 32), item(Material.CHAINMAIL_HELMET), item(Material.COPPER_CHAIN, 32), item(Material.CHAINMAIL_CHESTPLATE), item(Material.IRON_CHAIN, 32))
                row(item(Material.IRON_BARS, 32), item(Material.COBWEB, 16), item(Material.LEAD, 16), item(Material.COBWEB, 16), item(Material.IRON_BARS, 32))
                row(item(Material.IRON_CHAIN, 32), item(Material.CHAINMAIL_LEGGINGS), item(Material.COPPER_CHAIN, 32), item(Material.CHAINMAIL_BOOTS), item(Material.IRON_CHAIN, 32))
                row(item(Material.IRON_BARS, 32), item(Material.IRON_CHAIN, 32), item(Material.IRON_BARS, 32), item(Material.IRON_CHAIN, 32), item(Material.IRON_BARS, 32))
            }
            result(CustomItem.REINFORCED_CAGE)
        }
        recipe {
            grid {
                row(null, null, item(Material.CLAY, 4), null, null)
                row(null, item(Material.SAND, 4), item(Material.COARSE_DIRT, 4), item(Material.ICE, 4), null)
                row(item(Material.PUMPKIN, 4), item(Material.OAK_LEAVES, 4), item(Material.COMPASS, 4), item(Material.GRASS_BLOCK, 4), item(Material.DRIPSTONE_BLOCK, 4))
                row(null, item(Material.SNOW_BLOCK, 4), item(Material.MOSS_BLOCK, 4), item(Material.GRAVEL, 4), null)
                row(null, null, item(Material.MUD, 4), null, null)
            }
            result(CustomItem.NATURE_COMPASS)
        }
        recipe {
            grid {
                row(null, null, item(Material.BLAZE_POWDER, 8), null, null)
                row(null, item(Material.GLOW_BERRIES, 32), item(Material.GREEN_DYE, 32), item(Material.GLOW_INK_SAC, 16), null)
                row(item(Material.QUARTZ, 32), item(Material.LIME_DYE, 32), item(Material.EMERALD, 32), item(Material.LIME_DYE, 32), item(Material.QUARTZ, 32))
                row(null, item(Material.GLOW_INK_SAC, 16), item(Material.GREEN_DYE, 32), item(Material.GLOW_BERRIES, 32), null)
                row(null, null, item(Material.BLAZE_POWDER, 8), null, null)
            }
            result(CustomItem.DULL_REFRESHING_EMERALD)
        }
        recipe {
            grid {
                row(item(Material.DARK_OAK_LOG, 16), item(Material.OAK_LEAVES, 32), item(Material.ACACIA_LOG, 16), item(Material.SPRUCE_LEAVES, 32), item(Material.JUNGLE_LOG, 16))
                row(item(Material.OAK_LOG, 16), item(Material.GOLDEN_AXE), item(Material.WOODEN_AXE), item(Material.GOLDEN_AXE), item(Material.DARK_OAK_LEAVES, 32))
                row(item(Material.MANGROVE_LOG, 16), item(Material.DIAMOND_AXE), item(Material.NETHERITE_AXE), item(Material.STONE_AXE), item(Material.BIRCH_LOG, 16))
                row(item(Material.ACACIA_LEAVES, 32), item(Material.GOLDEN_AXE), item(Material.IRON_AXE), item(Material.GOLDEN_AXE), item(Material.OAK_LOG, 16))
                row(item(Material.CHERRY_LOG, 16), item(Material.BIRCH_LEAVES, 32), item(Material.PALE_OAK_LOG, 16), item(Material.JUNGLE_LEAVES, 32), item(Material.SPRUCE_LOG, 16))
            }
            result(CustomItem.TREECAPITATOR)
        }
        recipe {
            grid {
                row(item(Material.BARREL, 2), item(Material.FLETCHING_TABLE, 2), item(Material.GRINDSTONE, 2), item(Material.SMOKER, 2), item(Material.BLAST_FURNACE, 2))
                row(item(Material.FURNACE, 2), item(Material.DIAMOND_PICKAXE), item(Material.EMERALD, 24), item(Material.DIAMOND_AXE), item(Material.CARTOGRAPHY_TABLE, 2))
                row(item(Material.ENCHANTING_TABLE, 2), item(Material.EMERALD, 24), item(Material.GLOW_INK_SAC, 8), item(Material.EMERALD, 24), item(Material.LOOM, 2))
                row(item(Material.ANVIL, 2), item(Material.DIAMOND_AXE), item(Material.EMERALD, 24), item(Material.DIAMOND_PICKAXE), item(Material.BREWING_STAND, 2))
                row(item(Material.CRAFTING_TABLE, 2), item(Material.CRAFTER, 2), item(Material.LECTERN, 2), item(Material.STONECUTTER, 2), item(Material.CAULDRON, 2))
            }
            result(CustomItem.TRADING_SCRAMBLER)
        }
        recipe {
            grid {
                row(item(Material.ROTTEN_FLESH, 32), item(Material.RAW_GOLD_BLOCK, 2), item(Material.FERMENTED_SPIDER_EYE, 16), item(Material.RAW_GOLD_BLOCK, 2), item(Material.ROTTEN_FLESH, 32))
                row(item(Material.RAW_GOLD_BLOCK, 2), item(Material.GOLDEN_APPLE, 4), item(Material.GILDED_BLACKSTONE, 16), item(Material.GOLDEN_APPLE, 4), item(Material.RAW_GOLD_BLOCK, 2))
                row(item(Material.BLAZE_POWDER, 16), item(Material.NETHER_GOLD_ORE, 16), item(Material.ENCHANTED_GOLDEN_APPLE, 2), item(Material.DEEPSLATE_GOLD_ORE, 16), item(Material.BLAZE_POWDER, 16))
                row(item(Material.RAW_GOLD_BLOCK, 2), item(Material.GOLDEN_APPLE, 4), item(Material.GOLD_ORE, 16), item(Material.GOLDEN_APPLE, 4), item(Material.RAW_GOLD_BLOCK, 2))
                row(item(Material.COBBLESTONE, 64), item(Material.RAW_GOLD_BLOCK, 2), item(Material.BLAZE_ROD, 16), item(Material.RAW_GOLD_BLOCK, 2), item(Material.COBBLESTONE, 64))
            }
            result(CustomItem.GOLDEN_ZOMBIE)
        }
        recipe {
            grid {
                row(item(Material.PALE_OAK_BOAT), item(Material.RAIL, 64), item(Material.MANGROVE_BOAT), item(Material.POWERED_RAIL, 64), item(Material.ACACIA_BOAT))
                row(item(Material.MINECART), item(Material.GREEN_BED), item(Material.ENDER_PEARL, 16), item(Material.EMERALD_ORE, 4), item(Material.FURNACE_MINECART))
                row(item(Material.CHERRY_BOAT), item(Material.CHEST, 32), item(Material.HEART_OF_THE_SEA), item(Material.BARREL, 8), item(Material.DARK_OAK_BOAT))
                row(item(Material.FURNACE_MINECART), item(Material.EMERALD_ORE, 4), item(Material.ENDER_PEARL, 16), item(Material.BROWN_BED), item(Material.MINECART))
                row(item(Material.JUNGLE_BOAT), item(Material.DETECTOR_RAIL, 64), item(Material.SPRUCE_BOAT), item(Material.ACTIVATOR_RAIL, 64), item(Material.BIRCH_BOAT))
            }
            result(CustomItem.VILLAGER_ATOMIZER)
        }
        recipe {
            grid {
                row(item(Material.LIME_GLAZED_TERRACOTTA, 32), item(Material.SADDLE), item(Material.GLOBE_BANNER_PATTERN), item(Material.GLISTERING_MELON_SLICE, 16), item(Material.GREEN_GLAZED_TERRACOTTA, 32))
                row(item(Material.EXPERIENCE_BOTTLE, 8), item(Material.CARTOGRAPHY_TABLE, 16), item(Material.DIAMOND, 8), item(Material.FLETCHING_TABLE, 16), item(Material.PAINTING, 8))
                row(item(Material.NAME_TAG, 5), item(Material.DAYLIGHT_DETECTOR, 8), custom(CustomItem.DULL_REFRESHING_EMERALD), item(Material.DAYLIGHT_DETECTOR, 8), item(Material.NAME_TAG, 5))
                row(item(Material.PAINTING, 8), item(Material.FLETCHING_TABLE, 16), item(Material.DIAMOND, 8), item(Material.CARTOGRAPHY_TABLE, 16), item(Material.EXPERIENCE_BOTTLE, 8))
                row(item(Material.GREEN_GLAZED_TERRACOTTA, 32), item(Material.GLISTERING_MELON_SLICE, 16), item(Material.GLOBE_BANNER_PATTERN), item(Material.SADDLE), item(Material.LIME_GLAZED_TERRACOTTA, 32))
            }
            result(CustomItem.REFRESHING_EMERALD)
        }
        recipe {
            grid {
                row(null, null, item(Material.SHEARS), null, null)
                row(null, item(Material.IRON_INGOT, 24), item(Material.SHEARS), item(Material.FEATHER, 24), null)
                row(item(Material.FLINT, 32), item(Material.FLINT_AND_STEEL), item(Material.SHEARS), item(Material.BRUSH), item(Material.COPPER_INGOT, 32))
                row(null, item(Material.IRON_INGOT, 24), item(Material.SHEARS), item(Material.STICK, 32), null)
                row(null, null, item(Material.SHEARS), null, null)
            }
            result(CustomItem.POCKETKNIFE_MULTITOOL)
        }
        recipe {
            grid {
                row(null, null, null, null, null)
                row(null, null, item(Material.NETHERITE_PICKAXE), null, null)
                row(null, item(Material.NETHERITE_AXE), item(Material.PHANTOM_MEMBRANE, 16), item(Material.NETHERITE_SHOVEL), null)
                row(null, null, item(Material.NETHERITE_HOE), null, null)
                row(null, null, null, null, null)
            }
            result(CustomItem.NETHERITE_MULTITOOL)
        }
        recipe {
            grid {
                row(null, item(Material.MELON_SEEDS, 16), item(Material.MELON, 16), item(Material.BEETROOT, 16), null)
                row(null, item(Material.PUMPKIN_SEEDS, 16), item(Material.NETHERITE_HOE), item(Material.POTATO, 16), null)
                row(null, item(Material.WHEAT_SEEDS, 16), item(Material.DIAMOND, 16), item(Material.CARROT, 16), null)
                row(null, item(Material.WHEAT, 16), item(Material.IRON_INGOT, 32), item(Material.BEETROOT_SEEDS, 16), null)
                row(null, null, item(Material.PUMPKIN, 16), null, null)
            }
            result(CustomItem.HOE)
        }
        recipe {
            grid {
                row(item(Material.LIME_STAINED_GLASS, 32), item(Material.KELP, 32), item(Material.EMERALD_BLOCK, 32), item(Material.KELP, 32), item(Material.GREEN_STAINED_GLASS, 32))
                row(item(Material.LIME_BUNDLE), item(Material.RABBIT_STEW), item(Material.OMINOUS_BOTTLE, 4).setOminous(0), item(Material.BEETROOT_SOUP), item(Material.LIME_BUNDLE))
                row(item(Material.MUSHROOM_STEW), item(Material.OMINOUS_BOTTLE, 4).setOminous(3), item(Material.EMERALD_ORE, 8), item(Material.OMINOUS_BOTTLE, 4).setOminous(1), item(Material.SUSPICIOUS_STEW))
                row(item(Material.LIME_BUNDLE), item(Material.SLIME_BALL, 32), item(Material.OMINOUS_BOTTLE, 4).setOminous(2), item(Material.SLIME_BALL, 32), item(Material.LIME_BUNDLE))
                row(item(Material.OBSIDIAN, 16), item(Material.SEA_PICKLE, 32), item(Material.EMERALD_BLOCK, 32), item(Material.SEA_PICKLE, 32), item(Material.OBSIDIAN, 16))
            }
            result(CustomItem.JERRY_IDOL)
        }
        recipe {
            grid {
                row(item(Material.SPYGLASS), item(Material.SOUL_CAMPFIRE, 32), item(Material.CANDLE, 16), item(Material.CAMPFIRE, 32), item(Material.SPYGLASS))
                row(item(Material.FIREWORK_ROCKET, 32).firework(1), item(Material.DISC_FRAGMENT_5, 4), item(Material.COMPASS, 16), item(Material.DISC_FRAGMENT_5, 4), item(Material.CLOCK, 16))
                row(item(Material.BLACK_CANDLE, 16), item(Material.COMPASS, 16), item(Material.RECOVERY_COMPASS, 4), item(Material.COMPASS, 16), item(Material.BLACK_CANDLE, 16))
                row(item(Material.CLOCK, 16), item(Material.DISC_FRAGMENT_5, 4), item(Material.COMPASS, 16), item(Material.DISC_FRAGMENT_5, 4), item(Material.FIREWORK_ROCKET, 32).firework(1))
                row(item(Material.SPYGLASS), item(Material.CAMPFIRE, 32), item(Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.SOUL_CAMPFIRE, 32), item(Material.SPYGLASS))
            }
            result(CustomItem.TRACKING_COMPASS)
        }
        recipe {
            grid {
                row(item(Material.STRING, 64), item(Material.ARROW, 64), null, null, null)
                row(item(Material.STRING, 64), item(Material.WIND_CHARGE, 64), item(Material.FEATHER, 32), item(Material.FIREWORK_ROCKET, 64).firework(1), null)
                row(item(Material.STRING, 64), item(Material.BOW).ench("IN1","PW5","UN3"), item(Material.CROSSBOW).ench("MN1","PR4","UN3"), item(Material.TRIDENT).ench("MN1","LY3","UN3"), item(Material.TRIPWIRE_HOOK, 64))
                row(item(Material.STRING, 64), item(Material.WIND_CHARGE, 64), item(Material.FEATHER, 32), item(Material.FIREWORK_ROCKET, 64).firework(1), null)
                row(item(Material.STRING, 64), item(Material.ARROW, 64), null, null, null)
            }
            result(CustomItem.WIND_HOOK)
        }
        recipe {
            grid {
                row(null, null, item(Material.REDSTONE, 32), null, null)
                row(null, item(Material.CRAFTING_TABLE, 8), item(Material.COPPER_CHEST, 8), item(Material.CRAFTING_TABLE, 8), null)
                row(null, item(Material.CRAFTER, 32), item(Material.CHEST, 64), item(Material.CRAFTER, 32), null)
                row(null, item(Material.CRAFTING_TABLE, 8), item(Material.COPPER_CHEST, 8), item(Material.CRAFTING_TABLE, 8), null)
                row(null, null, item(Material.REDSTONE, 32), null, null)
            }
            result(CustomItem.REDSTONE_BOX)
        }
        recipe {
            grid {
                row(null, null, item(Material.DEEPSLATE_REDSTONE_ORE, 8), null, null)
                row(null, item(Material.RAIL, 32), item(Material.CHEST_MINECART), item(Material.POWERED_RAIL, 32), null)
                row(item(Material.REDSTONE, 16), item(Material.FURNACE_MINECART), item(Material.TNT_MINECART), item(Material.HOPPER_MINECART), item(Material.REDSTONE, 16))
                row(null, item(Material.ACTIVATOR_RAIL, 32), item(Material.MINECART), item(Material.DETECTOR_RAIL, 32), null)
                row(null, null, item(Material.DEEPSLATE_REDSTONE_ORE, 8), null, null)
            }
            result(CustomItem.MINECART_MATERIALS)
        }
        recipe {
            grid {
                row(null, null, item(Material.DEEPSLATE_REDSTONE_ORE, 8), null, null)
                row(null, item(Material.OAK_BUTTON, 32), item(Material.STONE_PRESSURE_PLATE, 32), item(Material.REDSTONE, 16), null)
                row(null, item(Material.OAK_PRESSURE_PLATE, 32), item(Material.LEVER, 32), item(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, 32), null)
                row(null, item(Material.REDSTONE, 16), item(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 32), item(Material.STONE_BUTTON, 32), null)
                row(null, null, item(Material.DEEPSLATE_REDSTONE_ORE, 8), null, null)
            }
            result(CustomItem.INPUT_DEVICES)
        }
        recipe {
            grid {
                row(null, null, item(Material.DEEPSLATE_REDSTONE_ORE, 8), null, null)
                row(null, item(Material.STICKY_PISTON, 16), item(Material.BARREL, 32), item(Material.DROPPER, 16), null)
                row(item(Material.SLIME_BLOCK, 4), item(Material.CRAFTER, 16), item(Material.NOTE_BLOCK, 32), item(Material.HOPPER, 32), item(Material.SLIME_BLOCK, 4))
                row(null, item(Material.DISPENSER, 16), item(Material.CHEST, 32), item(Material.PISTON, 16), null)
                row(null, null, item(Material.DEEPSLATE_REDSTONE_ORE, 8), null, null)
            }
            result(CustomItem.CONTAINERS)
        }
        recipe {
            grid {
                row(null, null, item(Material.DEEPSLATE_REDSTONE_ORE, 8), null, null)
                row(null, item(Material.OBSERVER, 16), item(Material.REDSTONE, 16), item(Material.COMPARATOR, 16), null)
                row(null, item(Material.REDSTONE_TORCH, 32), item(Material.REDSTONE_BLOCK, 16), item(Material.REDSTONE_TORCH, 32), null)
                row(null, item(Material.REPEATER, 16), item(Material.REDSTONE, 16), item(Material.OBSERVER, 16), null)
                row(null, null, item(Material.DEEPSLATE_REDSTONE_ORE, 8), null, null)
            }
            result(CustomItem.ACTUAL_REDSTONE)
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
                row(item(Material.LAPIS_ORE, 16), item(Material.DEEPSLATE_LAPIS_ORE, 16), item(Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE).ench("DU1"), item(Material.DEEPSLATE_COPPER_ORE, 16), item(Material.COPPER_ORE, 16))
                row(item(Material.IRON_BLOCK, 16), item(Material.IRON_PICKAXE), item(Material.COPPER_BLOCK, 16), item(Material.NETHERITE_PICKAXE), item(Material.DEEPSLATE_DIAMOND_ORE, 16))
                row(item(Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE).ench("DU1"), item(Material.TNT, 6), item(Material.END_CRYSTAL, 8), item(Material.TNT, 6), item(Material.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE).ench("DU1"))
                row(item(Material.COAL_ORE, 16), item(Material.NETHERITE_PICKAXE), item(Material.COPPER_BLOCK, 16), item(Material.GOLDEN_PICKAXE), item(Material.GOLD_BLOCK, 16))
                row(item(Material.REDSTONE_ORE, 16), item(Material.DEEPSLATE_REDSTONE_ORE, 16), item(Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE).ench("DU1"), item(Material.DEEPSLATE_IRON_ORE, 16), item(Material.IRON_ORE, 16))
            }
            result(CustomItem.VEINY_PICKAXE)
        }
        recipe {
            grid {
                row(item(Material.DIAMOND, 16), item(Material.STONE, 32), item(Material.AMETHYST_SHARD, 32), item(Material.STONE, 32), item(Material.DIAMOND, 16))
                row(item(Material.PISTON, 16), item(Material.GUNPOWDER, 32), item(Material.TNT, 48), item(Material.GUNPOWDER, 32), item(Material.DROPPER, 16))
                row(item(Material.REDSTONE_TORCH, 16), item(Material.TNT, 48), item(Material.NETHERITE_PICKAXE).ench("EF5","MN1","UN3"), item(Material.TNT, 48), item(Material.REDSTONE_TORCH, 16))
                row(item(Material.DROPPER, 16), item(Material.GUNPOWDER, 32), item(Material.TNT, 48), item(Material.GUNPOWDER, 32), item(Material.PISTON, 16))
                row(item(Material.DIAMOND, 16), item(Material.DEEPSLATE, 32), item(Material.AMETHYST_SHARD, 32), item(Material.DEEPSLATE, 32), item(Material.DIAMOND, 16))
            }
            result(CustomItem.EXCAVATOR)
            transfer(20)
        }
        recipe {
            grid {
                row(item(Material.SCULK, 64), item(Material.SCULK_VEIN, 32), item(Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.SCULK_VEIN, 32), item(Material.SCULK, 64))
                row(item(Material.SCULK_SHRIEKER, 16), item(Material.ECHO_SHARD, 10), item(Material.MUSIC_DISC_OTHERSIDE), item(Material.ECHO_SHARD, 10), item(Material.SCULK_SENSOR, 16))
                row(item(Material.SCULK_CATALYST, 8), item(Material.MUSIC_DISC_5), item(Material.EXPERIENCE_BOTTLE, 64), item(Material.MUSIC_DISC_5), item(Material.SCULK_CATALYST, 8))
                row(item(Material.SCULK_SENSOR, 16), item(Material.ECHO_SHARD, 10), item(Material.MUSIC_DISC_OTHERSIDE), item(Material.ECHO_SHARD, 10), item(Material.SCULK_SHRIEKER, 16))
                row(item(Material.SCULK, 64), item(Material.SCULK_VEIN, 32), item(Material.GLASS_BOTTLE, 64), item(Material.SCULK_VEIN, 32), item(Material.SCULK, 64))
            }
            result(CustomItem.EXPERIENCE_FLASK)
        }
        recipe {
            grid {
                row(item(Material.PALE_MOSS_BLOCK, 8), item(Material.WARPED_WART_BLOCK, 8), item(Material.DIAMOND, 16), item(Material.CLAY, 8), item(Material.SOUL_SAND, 8))
                row(item(Material.ACACIA_LEAVES, 8), item(Material.AZALEA_LEAVES, 8), item(Material.LEATHER, 32), item(Material.COARSE_DIRT, 8), item(Material.MUD, 8))
                row(item(Material.MOSS_BLOCK, 8), item(Material.NETHERITE_SHOVEL).ench("EF5","MN1","UN3"), item(Material.STICK, 64), item(Material.NETHERITE_HOE).ench("EF5","MN1","UN3"), item(Material.SNOW_BLOCK, 8))
                row(item(Material.OAK_LEAVES, 8), item(Material.HAY_BLOCK, 8), item(Material.STRING, 32), item(Material.DIRT, 8), item(Material.GRAVEL, 8))
                row(item(Material.NETHER_WART_BLOCK, 8), item(Material.SAND, 8), item(Material.DIAMOND, 16), item(Material.SOUL_SOIL, 8), item(Material.PALE_MOSS_BLOCK, 8))
            }
            result(CustomItem.HOEVEL)
            transfer(19)
        }
        recipe {
            grid {
                row(item(Material.RAW_COPPER, 8), item(Material.RAW_IRON, 8), item(Material.DIAMOND, 16), item(Material.PALE_OAK_DOOR, 8), item(Material.DARK_OAK_FENCE_GATE, 8))
                row(item(Material.GRANITE, 8), item(Material.COBBLESTONE, 8), item(Material.LEATHER, 32), item(Material.OAK_LOG, 8), item(Material.SPRUCE_LOG, 8))
                row(item(Material.COBBLED_DEEPSLATE, 8), item(Material.NETHERITE_PICKAXE).ench("EF5","MN1","UN3"), item(Material.STICK, 64), item(Material.NETHERITE_AXE).ench("EF5","MN1","UN3"), item(Material.BIRCH_LOG, 8))
                row(item(Material.DIORITE, 8), item(Material.ANDESITE, 8), item(Material.STRING, 32), item(Material.DARK_OAK_LOG, 8), item(Material.JUNGLE_LOG, 8))
                row(item(Material.RAW_GOLD, 8), item(Material.COAL, 8), item(Material.DIAMOND, 16), item(Material.CRIMSON_TRAPDOOR, 8), item(Material.MANGROVE_SLAB, 8))
            }
            result(CustomItem.AXEPICK)
            transfer(19)
        }
        recipe {
            grid {
                row(item(Material.DIAMOND_ORE), item(Material.DIAMOND, 4), item(Material.LEATHER, 32), item(Material.DIAMOND, 4), item(Material.SUSPICIOUS_SAND))
                row(item(Material.DIAMOND, 4), item(Material.ANCIENT_DEBRIS), item(Material.BLAZE_ROD, 16), item(Material.ANCIENT_DEBRIS), item(Material.DIAMOND, 4))
                row(item(Material.REDSTONE_ORE), custom(CustomItem.HOEVEL), item(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE).checkOriginal(), custom(CustomItem.AXEPICK), item(Material.SUSPICIOUS_GRAVEL))
                row(item(Material.DIAMOND, 4), item(Material.ANCIENT_DEBRIS), item(Material.BREEZE_ROD, 16), item(Material.ANCIENT_DEBRIS), item(Material.DIAMOND, 4))
                row(item(Material.DEEPSLATE_COAL_ORE), item(Material.DIAMOND, 4), item(Material.RABBIT_HIDE, 32), item(Material.DIAMOND, 4), item(Material.MUDDY_MANGROVE_ROOTS))
            }
            result(CustomItem.NETHERITE_MATTOCK)
            transfer(21)
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
                row(item(Material.REDSTONE_TORCH, 32), item(Material.BLAZE_ROD, 16), item(Material.MAGMA_CREAM, 32), item(Material.BLAZE_ROD, 16), item(Material.REDSTONE_TORCH, 32))
                row(item(Material.REDSTONE_BLOCK, 8), item(Material.FIREWORK_ROCKET, 32).firework(1), item(Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.FIREWORK_ROCKET, 32).firework(1), item(Material.REDSTONE_BLOCK, 8))
                row(item(Material.REPEATER, 16), item(Material.FIREWORK_ROCKET, 32).firework(1), item(Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.FIREWORK_ROCKET, 32).firework(1), item(Material.REPEATER, 16))
                row(item(Material.COMPARATOR, 16), item(Material.FIREWORK_ROCKET, 32).firework(1), item(Material.IRON_BLOCK, 32), item(Material.FIREWORK_ROCKET, 32).firework(1), item(Material.COMPARATOR, 16))
                row(item(Material.LAVA_BUCKET), item(Material.CAMPFIRE, 32), item(Material.SOUL_CAMPFIRE, 32), item(Material.CAMPFIRE, 32), item(Material.LAVA_BUCKET))
            }
            result(CustomItem.JETPACK_CONTROLLER_SET)
        }
        recipe {
            grid {
                row(null, null, item(Material.DIAMOND), null, null)
                row(null, item(Material.AMETHYST_SHARD, 8), item(Material.BLAZE_ROD, 2), item(Material.RAW_GOLD, 4), null)
                row(null, item(Material.ENDER_PEARL, 8), item(Material.ENDER_EYE, 4), item(Material.ENDER_PEARL, 8), null)
                row(null, item(Material.RAW_GOLD, 4), item(Material.BLAZE_ROD, 2), item(Material.AMETHYST_SHARD, 8), null)
                row(null, null, item(Material.DIAMOND), null, null)
            }
            result(CustomItem.POCKET_WORMHOLE)
        }
        recipe {
            grid {
                row(item(Material.PRISMARINE_CRYSTALS, 8), item(Material.EMERALD, 32), item(Material.DIAMOND, 8), item(Material.LAPIS_LAZULI, 32), item(Material.PRISMARINE_CRYSTALS, 8))
                row(item(Material.LAPIS_LAZULI, 32), item(Material.NAUTILUS_SHELL), item(Material.EMERALD_ORE, 8), item(Material.NAUTILUS_SHELL), item(Material.EMERALD, 32))
                row(item(Material.DIAMOND, 8), item(Material.EMERALD_ORE, 8), custom(CustomItem.REFRESHING_EMERALD), item(Material.EMERALD_ORE, 8), item(Material.DIAMOND, 8))
                row(item(Material.EMERALD, 32), item(Material.NAUTILUS_SHELL), item(Material.DEEPSLATE_EMERALD_ORE), item(Material.NAUTILUS_SHELL), item(Material.LAPIS_LAZULI, 32))
                row(item(Material.PRISMARINE_CRYSTALS, 8), item(Material.LAPIS_LAZULI, 32), item(Material.HEART_OF_THE_SEA), item(Material.EMERALD, 32), item(Material.PRISMARINE_CRYSTALS, 8))
            }
            result(CustomItem.REFINED_REFRESHING_EMERALD)
        }
        recipe {
            grid {
                row(null, item(Material.BAMBOO_CHEST_RAFT), item(Material.ECHO_SHARD), item(Material.SPRUCE_CHEST_BOAT), null)
                row(item(Material.JUNGLE_CHEST_BOAT), item(Material.CHEST_MINECART), item(Material.COPPER_CHEST, 8), item(Material.CHEST_MINECART), item(Material.ACACIA_CHEST_BOAT))
                row(item(Material.SHULKER_SHELL, 4), item(Material.CHEST, 16), item(Material.ENDER_CHEST, 2), item(Material.CHEST, 16), item(Material.SHULKER_SHELL, 4))
                row(item(Material.DARK_OAK_CHEST_BOAT), item(Material.CHEST_MINECART), item(Material.COPPER_CHEST, 8), item(Material.CHEST_MINECART), item(Material.MANGROVE_CHEST_BOAT))
                row(null, item(Material.CHERRY_CHEST_BOAT), item(Material.ECHO_SHARD), item(Material.PALE_OAK_CHEST_BOAT), null)
            }
            result(CustomItem.ITEM_NODE)
        }
        recipe {
            grid {
                row(item(Material.WAXED_COPPER_BLOCK, 16), item(Material.CROSSBOW), item(Material.GUSTER_BANNER_PATTERN), item(Material.DIAMOND, 8), item(Material.WAXED_OXIDIZED_COPPER, 16))
                row(item(Material.COPPER_CHAIN, 16), item(Material.ENCHANTED_BOOK).storeEnch("DN5"), item(Material.BREEZE_ROD, 16), item(Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.CROSSBOW))
                row(item(Material.ENCHANTED_BOOK).storeEnch("BR4"), item(Material.WIND_CHARGE, 32), item(Material.HEAVY_CORE), item(Material.WIND_CHARGE, 32), item(Material.ENCHANTED_BOOK).storeEnch("WB1"))
                row(item(Material.CROSSBOW), item(Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.BREEZE_ROD, 16), item(Material.ENCHANTED_BOOK).storeEnch("DN5"), item(Material.COPPER_CHAIN, 16))
                row(item(Material.WAXED_COPPER_BLOCK, 16), item(Material.DIAMOND, 8), item(Material.GUSTER_BANNER_PATTERN), item(Material.CROSSBOW), item(Material.WAXED_OXIDIZED_COPPER, 16))
            }
            result(CustomItem.WIND_CHARGE_CANNON)
        }



    }
}