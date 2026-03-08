package me.newburyminer.customItems.recipes.registrars

import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.recipes.RecipeBootstrapper
import org.bukkit.Material


object ToolRecipeBootstrapper: RecipeBootstrapper {
    override fun bootstrap() {
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
                row(item(Material.LIME_STAINED_GLASS, 32), item(Material.TOTEM_OF_UNDYING), item(Material.EMERALD_BLOCK, 32), item(Material.TOTEM_OF_UNDYING), item(Material.GREEN_STAINED_GLASS, 32))
                row(item(Material.LIME_BUNDLE), item(Material.RABBIT_STEW), item(Material.OMINOUS_BOTTLE, 8).setOminous(0), item(Material.BEETROOT_SOUP), item(Material.LIME_BUNDLE))
                row(item(Material.MUSHROOM_STEW), item(Material.OMINOUS_BOTTLE, 8).setOminous(3), item(Material.NETHER_STAR, 1), item(Material.OMINOUS_BOTTLE, 8).setOminous(1), item(Material.SUSPICIOUS_STEW))
                row(item(Material.LIME_BUNDLE), item(Material.SLIME_BALL, 32), item(Material.OMINOUS_BOTTLE, 8).setOminous(2), item(Material.SLIME_BALL, 32), item(Material.LIME_BUNDLE))
                row(item(Material.OBSIDIAN, 32), item(Material.TOTEM_OF_UNDYING), item(Material.EMERALD_BLOCK, 32), item(Material.TOTEM_OF_UNDYING), item(Material.OBSIDIAN, 32))
            }
            result(CustomItem.JERRY_IDOL)
        }
        recipe {
            grid {
                row(item(Material.ROTTEN_FLESH, 32), item(Material.RAW_GOLD_BLOCK, 2), item(Material.FERMENTED_SPIDER_EYE, 32), item(Material.RAW_GOLD_BLOCK, 2), item(Material.ROTTEN_FLESH, 32))
                row(item(Material.RAW_GOLD_BLOCK, 2), item(Material.GOLDEN_APPLE, 4), item(Material.GILDED_BLACKSTONE, 16), item(Material.GOLDEN_APPLE, 4), item(Material.RAW_GOLD_BLOCK, 2))
                row(item(Material.BLAZE_POWDER, 16), item(Material.NETHER_GOLD_ORE, 16), item(Material.ENCHANTED_GOLDEN_APPLE, 2), item(Material.DEEPSLATE_GOLD_ORE, 16), item(Material.BLAZE_POWDER, 16))
                row(item(Material.RAW_GOLD_BLOCK, 2), item(Material.GOLDEN_APPLE, 4), item(Material.GOLD_ORE, 16), item(Material.GOLDEN_APPLE, 4), item(Material.RAW_GOLD_BLOCK, 2))
                row(item(Material.COBBLESTONE, 64), item(Material.RAW_GOLD_BLOCK, 2), item(Material.BLAZE_ROD, 16), item(Material.RAW_GOLD_BLOCK, 2), item(Material.COBBLESTONE, 64))
            }
            result(CustomItem.GOLDEN_ZOMBIE)
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
                row(item(Material.LIME_GLAZED_TERRACOTTA, 32), item(Material.SADDLE), item(Material.GLOBE_BANNER_PATTERN), item(Material.GLISTERING_MELON_SLICE, 16), item(Material.GREEN_GLAZED_TERRACOTTA, 32))
                row(item(Material.EXPERIENCE_BOTTLE, 8), item(Material.CARTOGRAPHY_TABLE, 16), item(Material.DIAMOND, 8), item(Material.FLETCHING_TABLE, 16), item(Material.PAINTING, 8))
                row(item(Material.NAME_TAG, 5), item(Material.DAYLIGHT_DETECTOR, 8), item(Material.EMERALD_BLOCK, 32), item(Material.DAYLIGHT_DETECTOR, 8), item(Material.NAME_TAG, 5))
                row(item(Material.PAINTING, 8), item(Material.FLETCHING_TABLE, 16), item(Material.DIAMOND, 8), item(Material.CARTOGRAPHY_TABLE, 16), item(Material.EXPERIENCE_BOTTLE, 8))
                row(item(Material.GREEN_GLAZED_TERRACOTTA, 32), item(Material.GLISTERING_MELON_SLICE, 16), item(Material.GLOBE_BANNER_PATTERN), item(Material.SADDLE), item(Material.LIME_GLAZED_TERRACOTTA, 32))
            }
            result(CustomItem.REFRESHING_EMERALD)
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
                row(item(Material.ENDER_PEARL, 16), item(Material.LAPIS_LAZULI, 32), item(Material.RABBIT_FOOT, 2), item(Material.LAPIS_LAZULI, 32), item(Material.REDSTONE, 32))
                row(item(Material.NETHER_WART, 16), item(Material.TURTLE_SCUTE, 8), item(Material.GLASS_BOTTLE, 64), item(Material.TURTLE_SCUTE, 8), item(Material.NETHER_WART, 16))
                row(item(Material.EXPERIENCE_BOTTLE, 64), item(Material.GLOWSTONE, 32), item(Material.BREWING_STAND, 32), item(Material.GLOWSTONE, 32), item(Material.EXPERIENCE_BOTTLE, 64))
                row(item(Material.NETHER_WART, 16), item(Material.TURTLE_SCUTE, 8), item(Material.GLASS_BOTTLE, 64), item(Material.TURTLE_SCUTE, 8), item(Material.NETHER_WART, 16))
                row(item(Material.REDSTONE, 32), item(Material.LAPIS_LAZULI, 32), item(Material.RABBIT_FOOT, 2), item(Material.LAPIS_LAZULI, 23), item(Material.ENDER_PEARL, 16))
            }
            result(CustomItem.CLERIC_UPGRADE)
        }
        recipe {
            grid {
                row(item(Material.BELL, 2), item(Material.IRON_BLOCK, 16), item(Material.DIAMOND_HELMET), item(Material.COAL_BLOCK, 8), item(Material.BELL, 2))
                row(item(Material.COAL_BLOCK, 8), item(Material.IRON_HELMET), item(Material.CHAINMAIL_HELMET), item(Material.IRON_CHESTPLATE), item(Material.IRON_BLOCK, 16))
                row(item(Material.DIAMOND_CHESTPLATE), item(Material.CHAINMAIL_CHESTPLATE), item(Material.BLAST_FURNACE, 32), item(Material.CHAINMAIL_LEGGINGS), item(Material.DIAMOND_LEGGINGS))
                row(item(Material.IRON_BLOCK, 16), item(Material.IRON_LEGGINGS), item(Material.CHAINMAIL_BOOTS), item(Material.IRON_BOOTS), item(Material.COAL_BLOCK, 8))
                row(item(Material.BELL, 2), item(Material.COAL_BLOCK, 8), item(Material.DIAMOND_BOOTS), item(Material.IRON_BLOCK, 16), item(Material.BELL, 2))
            }
            result(CustomItem.ARMORSMITH_UPGRADE)
        }
        recipe {
            grid {
                row(item(Material.LEATHER, 8), item(Material.STICK, 16), item(Material.COAL, 16), item(Material.STICK, 16), item(Material.LEATHER, 8))
                row(item(Material.STICK, 16), item(Material.BELL), item(Material.DIAMOND_AXE), item(Material.IRON_INGOT, 32), item(Material.STICK, 16))
                row(item(Material.COAL, 16), item(Material.DIAMOND_HOE), item(Material.SMITHING_TABLE, 32), item(Material.DIAMOND_PICKAXE), item(Material.COAL, 16))
                row(item(Material.STICK, 16), item(Material.IRON_INGOT, 32), item(Material.DIAMOND_SHOVEL), item(Material.BELL), item(Material.STICK, 16))
                row(item(Material.LEATHER, 8), item(Material.STICK, 16), item(Material.COAL, 16), item(Material.STICK, 16), item(Material.LEATHER, 8))
            }
            result(CustomItem.TOOLSMITH_UPGRADE)
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
                row(null, item(Material.OBSERVER, 16), item(Material.REDSTONE, 16), item(Material.COMPARATOR, 16), null)
                row(null, item(Material.REDSTONE_TORCH, 32), item(Material.REDSTONE_BLOCK, 16), item(Material.REDSTONE_TORCH, 32), null)
                row(null, item(Material.REPEATER, 16), item(Material.REDSTONE, 16), item(Material.OBSERVER, 16), null)
                row(null, null, item(Material.DEEPSLATE_REDSTONE_ORE, 8), null, null)
            }
            result(CustomItem.ACTUAL_REDSTONE)
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
                row(item(Material.IRON_BARS, 32), item(Material.IRON_CHAIN, 32), item(Material.IRON_BARS, 32), item(Material.IRON_CHAIN, 32), item(Material.IRON_BARS, 32))
                row(item(Material.IRON_CHAIN, 32), item(Material.CHAINMAIL_BOOTS), item(Material.COPPER_CHAIN, 32), item(Material.CHAINMAIL_LEGGINGS), item(Material.IRON_CHAIN, 32))
                row(item(Material.IRON_BARS, 32), item(Material.COBWEB, 16), item(Material.LEAD, 16), item(Material.COBWEB, 16), item(Material.IRON_BARS, 32))
                row(item(Material.IRON_CHAIN, 32), item(Material.CHAINMAIL_CHESTPLATE), item(Material.COPPER_CHAIN, 32), item(Material.CHAINMAIL_HELMET), item(Material.IRON_CHAIN, 32))
                row(item(Material.IRON_BARS, 32), item(Material.IRON_CHAIN, 32), item(Material.IRON_BARS, 32), item(Material.IRON_CHAIN, 32), item(Material.IRON_BARS, 32))
            }
            result(CustomItem.REINFORCED_CAGE)
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
                row(null, null, null, null, null)
                row(null, custom(CustomItem.TOOL_HANDLE), item(Material.NETHERITE_PICKAXE), custom(CustomItem.TOOL_HANDLE), null)
                row(null, item(Material.NETHERITE_AXE), item(Material.PHANTOM_MEMBRANE, 16), item(Material.NETHERITE_SHOVEL), null)
                row(null, custom(CustomItem.TOOL_HANDLE), item(Material.NETHERITE_HOE), custom(CustomItem.TOOL_HANDLE), null)
                row(null, null, null, null, null)
            }
            result(CustomItem.NETHERITE_MULTITOOL)
        }
        recipe {
            grid {
                row(item(Material.RAW_COPPER, 8), item(Material.RAW_IRON, 8), item(Material.DIAMOND, 16), item(Material.PALE_OAK_DOOR, 8), item(Material.DARK_OAK_FENCE_GATE, 8))
                row(item(Material.GRANITE, 8), item(Material.COBBLESTONE, 8), custom(CustomItem.TOOL_HANDLE, 2), item(Material.OAK_LOG, 8), item(Material.SPRUCE_LOG, 8))
                row(item(Material.COBBLED_DEEPSLATE, 8), item(Material.NETHERITE_PICKAXE).ench("EF5", "UN3", "MN1"), custom(CustomItem.TOOL_HANDLE, 2), item(Material.NETHERITE_AXE).ench("EF5", "UN3", "MN1"), item(Material.BIRCH_LOG, 8))
                row(item(Material.DIORITE, 8), item(Material.ANDESITE, 8), custom(CustomItem.TOOL_HANDLE, 2), item(Material.DARK_OAK_LOG, 8), item(Material.JUNGLE_LOG, 8))
                row(item(Material.RAW_GOLD, 8), item(Material.COAL, 8), item(Material.DIAMOND, 16), item(Material.CRIMSON_TRAPDOOR, 8), item(Material.MANGROVE_SLAB, 8))
            }
            result(CustomItem.AXEPICK)
        }
        recipe {
            grid {
                row(item(Material.PALE_MOSS_BLOCK, 8), item(Material.WARPED_WART_BLOCK, 8), item(Material.DIAMOND, 16), item(Material.CLAY, 8), item(Material.SOUL_SAND, 8))
                row(item(Material.ACACIA_LEAVES, 8), item(Material.AZALEA_LEAVES, 8), custom(CustomItem.TOOL_HANDLE, 2), item(Material.COARSE_DIRT, 8), item(Material.MUD, 8))
                row(item(Material.MOSS_BLOCK, 8), item(Material.NETHERITE_SHOVEL).ench("EF5", "UN3", "MN1"), custom(CustomItem.TOOL_HANDLE, 2), item(Material.NETHERITE_HOE).ench("EF5", "UN3", "MN1"), item(Material.SNOW_BLOCK, 8))
                row(item(Material.OAK_LEAVES, 8), item(Material.HAY_BLOCK, 8), custom(CustomItem.TOOL_HANDLE, 2), item(Material.DIRT, 8), item(Material.GRAVEL, 8))
                row(item(Material.PALE_MOSS_BLOCK, 8), item(Material.NETHER_WART_BLOCK, 8), item(Material.DIAMOND, 16), item(Material.SAND, 8), item(Material.SOUL_SOIL, 8))
            }
            result(CustomItem.HOEVEL)
        }
        recipe {
            grid {
                row(item(Material.DIAMOND_ORE), item(Material.DIAMOND, 4), custom(CustomItem.REINFORCED_HANDLE), item(Material.DIAMOND, 4), item(Material.SUSPICIOUS_SAND))
                row(item(Material.DIAMOND, 4), item(Material.ANCIENT_DEBRIS), custom(CustomItem.REINFORCED_HANDLE), item(Material.ANCIENT_DEBRIS), item(Material.DIAMOND, 4))
                row(item(Material.REDSTONE_ORE), custom(CustomItem.AXEPICK), item(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE), custom(CustomItem.HOEVEL), item(Material.SUSPICIOUS_GRAVEL))
                row(item(Material.DIAMOND, 4), item(Material.ANCIENT_DEBRIS), custom(CustomItem.REINFORCED_HANDLE), item(Material.ANCIENT_DEBRIS), item(Material.DIAMOND, 4))
                row(item(Material.DEEPSLATE_COAL_ORE), item(Material.DIAMOND, 4), custom(CustomItem.REINFORCED_HANDLE), item(Material.DIAMOND, 4), item(Material.MUDDY_MANGROVE_ROOTS))
            }
            result(CustomItem.NETHERITE_MATTOCK)
        }
        recipe {
            grid {
                row(null, item(Material.MELON_SEEDS, 32), item(Material.MELON, 32), item(Material.BEETROOT, 32), null)
                row(null, item(Material.PUMPKIN_SEEDS, 32), item(Material.NETHERITE_HOE), item(Material.POTATO, 32), null)
                row(null, item(Material.WHEAT_SEEDS, 32), custom(CustomItem.TOOL_HANDLE), item(Material.CARROT, 32), null)
                row(null, item(Material.WHEAT, 32), custom(CustomItem.TOOL_HANDLE), item(Material.BEETROOT_SEEDS, 32), null)
                row(null, null, item(Material.PUMPKIN, 32), null, null)
            }
            result(CustomItem.HOE)
        }
        recipe {
            grid {
                row(item(Material.LAPIS_ORE, 16), item(Material.DEEPSLATE_LAPIS_ORE, 16), item(Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE).ench("DU1"), item(Material.DEEPSLATE_COPPER_ORE, 16), item(Material.COPPER_ORE, 16))
                row(item(Material.IRON_BLOCK, 16), item(Material.IRON_PICKAXE), item(Material.COPPER_BLOCK, 32), item(Material.NETHERITE_PICKAXE), item(Material.DEEPSLATE_DIAMOND_ORE, 16))
                row(item(Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE).ench("DU1"), item(Material.TNT, 6), item(Material.END_CRYSTAL, 8), item(Material.TNT, 6), item(Material.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE).ench("DU1"))
                row(item(Material.COAL_ORE, 16), item(Material.NETHERITE_PICKAXE), custom(CustomItem.TOOL_HANDLE, 4), item(Material.GOLDEN_PICKAXE), item(Material.GOLD_BLOCK, 16))
                row(item(Material.REDSTONE_ORE, 16), item(Material.DEEPSLATE_REDSTONE_ORE, 16), item(Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE).ench("DU1"), item(Material.DEEPSLATE_IRON_ORE, 16), item(Material.IRON_ORE, 16))
            }
            result(CustomItem.VEINY_PICKAXE)
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
                row(item(Material.DIAMOND, 16), item(Material.STONE, 32), item(Material.AMETHYST_SHARD, 32), item(Material.STONE, 32), item(Material.DIAMOND, 16))
                row(item(Material.PISTON, 16), item(Material.GUNPOWDER, 32), item(Material.TNT, 48), item(Material.GUNPOWDER, 32), item(Material.DROPPER, 16))
                row(item(Material.REDSTONE_TORCH, 16), item(Material.TNT, 48), item(Material.NETHERITE_PICKAXE).ench("EF5", "UN3", "MN1"), item(Material.TNT, 48), item(Material.REDSTONE_TORCH, 16))
                row(item(Material.DROPPER, 16), item(Material.GUNPOWDER, 32), custom(CustomItem.TOOL_HANDLE, 4), item(Material.GUNPOWDER, 32), item(Material.PISTON, 16))
                row(item(Material.DIAMOND, 16), item(Material.DEEPSLATE, 32), item(Material.AMETHYST_SHARD, 32), item(Material.DEEPSLATE, 32), item(Material.DIAMOND, 16))
            }
            result(CustomItem.EXCAVATOR)
        }
        recipe {
            grid {
                row(item(Material.REDSTONE_TORCH, 32), item(Material.BLAZE_ROD, 16), item(Material.MAGMA_CREAM, 32), item(Material.BLAZE_ROD, 16), item(Material.REDSTONE_TORCH, 32))
                row(item(Material.REDSTONE_BLOCK, 8), item(Material.FIREWORK_ROCKET, 32), item(Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.FIREWORK_ROCKET, 32), item(Material.REDSTONE_BLOCK, 8))
                row(item(Material.REPEATER, 16), item(Material.FIREWORK_ROCKET, 32), item(Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.FIREWORK_ROCKET, 32), item(Material.REPEATER, 16))
                row(item(Material.COMPARATOR, 16), item(Material.FIREWORK_ROCKET, 32), item(Material.IRON_BLOCK, 32), item(Material.FIREWORK_ROCKET, 32), item(Material.COMPARATOR, 16))
                row(item(Material.LAVA_BUCKET), item(Material.CAMPFIRE, 32), item(Material.SOUL_CAMPFIRE, 32), item(Material.CAMPFIRE, 32), item(Material.LAVA_BUCKET))
            }
            result(CustomItem.JETPACK_CONTROLLER_SET)
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
                row(item(Material.IRON_INGOT, 16), item(Material.LAVA_BUCKET), item(Material.FIREWORK_STAR, 8), item(Material.LAVA_BUCKET), item(Material.IRON_INGOT, 16))
                row(item(Material.LAVA_BUCKET), item(Material.MAGMA_BLOCK, 16), item(Material.CAMPFIRE), item(Material.MAGMA_BLOCK, 16), item(Material.LAVA_BUCKET))
                row(item(Material.ENCHANTED_BOOK).storeEnch("FP4"), item(Material.CAMPFIRE), custom(CustomItem.FIRE_RESISTANT_RESIN), item(Material.CAMPFIRE), item(Material.ENCHANTED_BOOK).storeEnch("FP4"))
                row(item(Material.LAVA_BUCKET), item(Material.MAGMA_BLOCK, 16), item(Material.CAMPFIRE), item(Material.MAGMA_BLOCK, 16), item(Material.LAVA_BUCKET))
                row(item(Material.IRON_INGOT, 16), item(Material.LAVA_BUCKET), item(Material.FIREWORK_STAR, 8), item(Material.LAVA_BUCKET), item(Material.IRON_INGOT, 16))
            }
            result(CustomItem.NETHERITE_COATING)
        }
        recipe {
            grid {
                row(null, item(Material.IRON_BLOCK, 2), item(Material.OBSIDIAN, 4), item(Material.IRON_BLOCK, 2), null)
                row(item(Material.IRON_BLOCK, 2), item(Material.COBBLED_DEEPSLATE, 32), item(Material.GUNPOWDER, 8), item(Material.COBBLED_DEEPSLATE, 32), item(Material.IRON_BLOCK, 2))
                row(item(Material.OBSIDIAN, 4), item(Material.GUNPOWDER, 8), item(Material.WITHER_SKELETON_SKULL), item(Material.GUNPOWDER, 8), item(Material.OBSIDIAN, 4))
                row(item(Material.IRON_BLOCK, 2), item(Material.COBBLED_DEEPSLATE, 32), item(Material.GUNPOWDER, 8), item(Material.COBBLED_DEEPSLATE, 32), item(Material.IRON_BLOCK, 2))
                row(null, item(Material.IRON_BLOCK, 2), item(Material.OBSIDIAN, 4), item(Material.IRON_BLOCK, 2), null)
            }
            result(CustomItem.WITHER_COATING)
        }
        recipe {
            grid {
                row(item(Material.BLAST_FURNACE, 4), item(Material.LAVA_BUCKET), item(Material.FURNACE, 8), item(Material.LAVA_BUCKET), item(Material.BLAST_FURNACE, 4))
                row(item(Material.CHARCOAL, 32), item(Material.GOLD_ORE, 8), item(Material.DEEPSLATE_DIAMOND_ORE, 2), item(Material.DEEPSLATE_IRON_ORE, 8), item(Material.AMETHYST_BLOCK, 32))
                row(item(Material.LIGHTNING_ROD), item(Material.DEEPSLATE_DIAMOND_ORE, 2), custom(CustomItem.FIRE_RESISTANT_RESIN, 2), item(Material.DEEPSLATE_DIAMOND_ORE, 2), item(Material.LIGHTNING_ROD))
                row(item(Material.AMETHYST_BLOCK, 32), item(Material.DEEPSLATE_GOLD_ORE, 8), item(Material.DEEPSLATE_DIAMOND_ORE, 2), item(Material.IRON_ORE, 8), item(Material.CHARCOAL, 32))
                row(item(Material.BLAST_FURNACE, 4), item(Material.LAVA_BUCKET), item(Material.FURNACE, 8), item(Material.LAVA_BUCKET), item(Material.BLAST_FURNACE, 4))
            }
            result(CustomItem.FIERY_SHARD)
        }
        recipe {
            grid {
                row(null, item(Material.IRON_HELMET).ench("PT4"), item(Material.CRYING_OBSIDIAN, 32), item(Material.IRON_CHESTPLATE).ench("PT4"), null)
                row(item(Material.IRON_BLOCK, 16), item(Material.ENCHANTED_BOOK).storeEnch("MN1"), item(Material.TURTLE_SCUTE, 4), item(Material.ENCHANTED_BOOK).storeEnch("UN3"), item(Material.IRON_BLOCK, 16))
                row(item(Material.NETHERITE_SCRAP, 2), item(Material.TURTLE_SCUTE, 4), custom(CustomItem.STEEL_PLATING, 2), item(Material.TURTLE_SCUTE, 4), item(Material.NETHERITE_SCRAP, 2))
                row(item(Material.IRON_BLOCK, 16), item(Material.ENCHANTED_BOOK).storeEnch("UN3"), item(Material.TURTLE_SCUTE, 4), item(Material.ENCHANTED_BOOK).storeEnch("MN1"), item(Material.IRON_BLOCK, 16))
                row(null, item(Material.IRON_LEGGINGS).ench("PT4"), item(Material.CRYING_OBSIDIAN, 32), item(Material.IRON_BOOTS).ench("PT4"), null)
            }
            result(CustomItem.REINFORCING_STRUTS)
        }
        recipe {
            grid {
                row(item(Material.GLOW_INK_SAC, 8), item(Material.AMETHYST_SHARD, 16), item(Material.HONEYCOMB, 16), item(Material.AMETHYST_SHARD, 16), item(Material.RAW_GOLD, 32))
                row(item(Material.QUARTZ, 16), item(Material.ENDER_PEARL, 16), item(Material.DIAMOND, 12), item(Material.ENDER_PEARL, 16), item(Material.QUARTZ, 16))
                row(item(Material.HONEYCOMB, 16), item(Material.DIAMOND, 12), custom(CustomItem.ENCHANTED_CATALYST), item(Material.DIAMOND, 12), item(Material.HONEYCOMB, 16))
                row(item(Material.QUARTZ, 16), item(Material.ENDER_PEARL, 16), item(Material.DIAMOND, 12), item(Material.ENDER_PEARL, 16), item(Material.QUARTZ, 16))
                row(item(Material.RAW_GOLD, 32), item(Material.AMETHYST_SHARD, 16), item(Material.HONEYCOMB, 16), item(Material.AMETHYST_SHARD, 16), item(Material.GLOW_INK_SAC, 8))
            }
            result(CustomItem.SOUL_CRYSTAL)
        }
        recipe {
            grid {
                row(item(Material.GOLDEN_APPLE, 4), item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.CHORUS_FRUIT, 64), item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.GOLDEN_APPLE, 4))
                row(item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.CHORUS_FLOWER, 16), item(Material.DISPENSER, 16), item(Material.CHORUS_FLOWER, 16), item(Material.POPPED_CHORUS_FRUIT, 32))
                row(item(Material.CHORUS_FRUIT, 64), item(Material.SHULKER_SHELL, 16), custom(CustomItem.DYE_PALETTE), item(Material.SHULKER_SHELL, 16), item(Material.CHORUS_FRUIT, 64))
                row(item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.CHORUS_FLOWER, 16), item(Material.DIAMOND_PICKAXE).ench("EF5", "UN3", "MN1"), item(Material.CHORUS_FLOWER, 16), item(Material.POPPED_CHORUS_FRUIT, 32))
                row(item(Material.GOLDEN_APPLE, 4), item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.CHORUS_FRUIT, 64), item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.GOLDEN_APPLE, 4))
            }
            result(CustomItem.SHULKER_FRUIT)
        }
        recipe {
            grid {
                row(item(Material.ENCHANTED_BOOK), item(Material.MUSIC_DISC_CAT), item(Material.EXPERIENCE_BOTTLE, 16), item(Material.MUSIC_DISC_13), item(Material.ENCHANTED_BOOK))
                row(item(Material.GLOW_BERRIES, 32), item(Material.SCULK, 16), item(Material.APPLE, 32), item(Material.SCULK, 16), item(Material.GLOW_BERRIES, 32))
                row(item(Material.SOUL_TORCH, 32), item(Material.APPLE, 32), item(Material.ENCHANTED_GOLDEN_APPLE), item(Material.APPLE, 32), item(Material.SOUL_TORCH, 32))
                row(item(Material.GLOW_BERRIES, 32), item(Material.SCULK, 16), item(Material.APPLE, 32), item(Material.SCULK, 16), item(Material.GLOW_BERRIES, 32))
                row(item(Material.ENCHANTED_BOOK), item(Material.MUSIC_DISC_13), item(Material.EXPERIENCE_BOTTLE, 16), item(Material.MUSIC_DISC_CAT), item(Material.ENCHANTED_BOOK))
            }
			result(CustomItem.MYSTICAL_GREEN_APPLE)
        }
    }
}