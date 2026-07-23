package me.newburyminer.customItems.recipes.registrars

import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.recipes.RecipeBootstrapper
import me.newburyminer.customItems.recipes.RecipeBuilder
import me.newburyminer.customItems.recipes.RecipeType
import org.bukkit.Material
import org.bukkit.potion.PotionType

object ArmorRecipeBootstrapper: RecipeBootstrapper {
    override val recipeType: RecipeType = RecipeType.ARMOR
    override fun bootstrap() {
        recipe {
            grid {
                row(null, null, item(Material.LEATHER, 24), null, null)
                row(item(Material.SUGAR, 16), item(Material.IRON_HORSE_ARMOR), item(Material.DIAMOND_HORSE_ARMOR), item(Material.LEATHER_HORSE_ARMOR), item(Material.RABBIT_FOOT, 2))
                row(item(Material.SADDLE), item(Material.GOLDEN_HORSE_ARMOR), item(Material.NETHERITE_HORSE_ARMOR), item(Material.GOLDEN_HORSE_ARMOR), item(Material.SADDLE))
                row(item(Material.RABBIT_FOOT, 2), item(Material.LEATHER_HORSE_ARMOR), item(Material.DIAMOND_HORSE_ARMOR), item(Material.IRON_HORSE_ARMOR), item(Material.SUGAR, 16))
                row(null, null, item(Material.LEATHER, 24), null, null)
            }
            result(CustomItem.PEGASUS_ARMOR)
        }
        recipe {
            grid {
                row(item(Material.CYAN_GLAZED_TERRACOTTA, 4), item(Material.REDSTONE_TORCH, 16), item(Material.QUARTZ_BLOCK, 16), item(Material.LEVER, 16), item(Material.BROWN_GLAZED_TERRACOTTA, 4))
                row(item(Material.SCRAPE_POTTERY_SHERD), item(Material.STICK, 32), item(Material.SCAFFOLDING, 16), item(Material.STICK, 32), item(Material.MINER_POTTERY_SHERD))
                row(item(Material.PISTON, 16), item(Material.SCAFFOLDING, 16), item(Material.NETHERITE_LEGGINGS), item(Material.SCAFFOLDING, 16), item(Material.PISTON, 16))
                row(item(Material.FRIEND_POTTERY_SHERD), item(Material.STICK, 32), item(Material.SCAFFOLDING, 16), item(Material.STICK, 32), item(Material.SHELTER_POTTERY_SHERD))
                row(item(Material.GREEN_GLAZED_TERRACOTTA, 4), item(Material.LEVER, 16), item(Material.QUARTZ_BLOCK, 16), item(Material.REDSTONE_TORCH, 16), item(Material.BLUE_GLAZED_TERRACOTTA, 4))
            }
            result(CustomItem.TOOLBELT)
            transfer(20)
        }
        recipe {
            grid {
                row(item(Material.SPLASH_POTION).setPotion(PotionType.WIND_CHARGED), item(Material.COBBLESTONE, 8), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.COBBLESTONE, 8), item(Material.SPLASH_POTION).setPotion(PotionType.WIND_CHARGED))
                row(item(Material.COBBLESTONE, 8), item(Material.FEATHER, 16), item(Material.RABBIT_HIDE, 8), item(Material.FEATHER, 16), item(Material.COBBLESTONE, 8))
                row(item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.GUSTER_BANNER_PATTERN), item(Material.NETHERITE_BOOTS), item(Material.GUSTER_BANNER_PATTERN), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING))
                row(item(Material.COBBLESTONE, 8), item(Material.FEATHER, 16), item(Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.FEATHER, 16), item(Material.COBBLESTONE, 8))
                row(item(Material.SPLASH_POTION).setPotion(PotionType.WIND_CHARGED), item(Material.COBBLESTONE, 8), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.COBBLESTONE, 8), item(Material.SPLASH_POTION).setPotion(PotionType.WIND_CHARGED))
            }
            result(CustomItem.MOON_BOOTS)
            transfer(20)
        }
        recipe {
            grid {
                row(null, null, item(Material.RAW_IRON_BLOCK, 32), null, null)
                row(null, item(Material.OCHRE_FROGLIGHT, 4), item(Material.DIAMOND_PICKAXE), item(Material.COPPER_BULB, 4), null)
                row(item(Material.ENCHANTED_BOOK).storeEnch("EF5"), item(Material.DIAMOND_PICKAXE), item(Material.NETHERITE_HELMET), item(Material.DIAMOND_PICKAXE), item(Material.ENCHANTED_BOOK).storeEnch("EF5"))
                row(null, item(Material.VERDANT_FROGLIGHT, 4), item(Material.DIAMOND_PICKAXE), item(Material.PEARLESCENT_FROGLIGHT, 4), null)
                row(null, null, item(Material.RAW_GOLD_BLOCK, 32), null, null)
            }
            result(CustomItem.MINERS_HELM)
            transfer(20)
        }
        recipe {
            grid {
                row(null, null, item(Material.LAPIS_BLOCK, 64), null, null)
                row(null, item(Material.RABBIT_HIDE, 32), item(Material.DIAMOND_PICKAXE), item(Material.BLUE_DYE, 32), null)
                row(item(Material.ENCHANTED_BOOK).storeEnch("EF5"), item(Material.DIAMOND_PICKAXE), item(Material.NETHERITE_LEGGINGS), item(Material.DIAMOND_PICKAXE), item(Material.ENCHANTED_BOOK).storeEnch("EF5"))
                row(null, item(Material.IRON_NUGGET, 32), item(Material.DIAMOND_PICKAXE), item(Material.LEATHER, 32), null)
                row(null, null, item(Material.DIAMOND_BLOCK, 8), null, null)
            }
            result(CustomItem.MINERS_JEANS)
            transfer(20)
        }
        recipe {
            grid {
                row(null, null, item(Material.COAL_BLOCK, 64), null, null)
                row(null, item(Material.SLIME_BALL, 32), item(Material.DIAMOND_PICKAXE), item(Material.RESIN_BRICK, 32), null)
                row(item(Material.ENCHANTED_BOOK).storeEnch("EF5"), item(Material.DIAMOND_PICKAXE), item(Material.NETHERITE_BOOTS), item(Material.DIAMOND_PICKAXE), item(Material.ENCHANTED_BOOK).storeEnch("EF5"))
                row(null, item(Material.ARMADILLO_SCUTE, 32), item(Material.DIAMOND_PICKAXE), item(Material.RABBIT_FOOT, 8), null)
                row(null, null, item(Material.REDSTONE_BLOCK, 32), null, null)
            }
            result(CustomItem.MINERS_BOOTS)
            transfer(20)
        }
        recipe {
            grid {
                row(item(Material.SWEET_BERRIES, 32), item(Material.GLOW_BERRIES, 32), item(Material.MELON_SLICE, 32), item(Material.APPLE, 32), item(Material.POTATO, 32))
                row(item(Material.COD, 32), item(Material.CARROT, 32), item(Material.GOLDEN_APPLE, 8), item(Material.BEETROOT, 32), item(Material.TROPICAL_FISH, 32))
                row(item(Material.SALMON, 32), item(Material.HONEY_BOTTLE, 16), item(Material.NETHERITE_HELMET).ench("PT4","UN3","AA1","RS3","MN1"), item(Material.PUMPKIN_PIE, 32), item(Material.BEEF, 32))
                row(item(Material.PORKCHOP, 32), item(Material.MUTTON, 32), item(Material.GOLDEN_APPLE, 8), item(Material.CHICKEN, 32), item(Material.RABBIT, 32))
                row(item(Material.COOKIE, 64), item(Material.DRIED_KELP, 32), item(Material.POISONOUS_POTATO, 16), item(Material.BREAD, 64), item(Material.ROTTEN_FLESH, 32))
            }
            result(CustomItem.EDIBLE_HELMET)
            transfer(20)
        }
        recipe {
            grid {
                row(item(Material.BURN_POTTERY_SHERD), item(Material.DIAMOND, 8), item(Material.SPLASH_POTION).setPotion(PotionType.LONG_SWIFTNESS), item(Material.DIAMOND, 8), item(Material.MOURNER_POTTERY_SHERD))
                row(item(Material.ENDER_EYE, 8), item(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.EMERALD, 32), item(Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.ENDER_EYE, 8))
                row(item(Material.SPLASH_POTION).setPotion(PotionType.LONG_SWIFTNESS), item(Material.NAUTILUS_SHELL, 2), item(Material.NETHERITE_BOOTS).ench("FF4","PT4","DS3","UN3","SP3","MN1"), item(Material.NAUTILUS_SHELL, 2), item(Material.SPLASH_POTION).setPotion(PotionType.LONG_SWIFTNESS))
                row(item(Material.ENDER_EYE, 8), item(Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.EMERALD, 32), item(Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.ENDER_EYE, 8))
                row(item(Material.PRIZE_POTTERY_SHERD), item(Material.DIAMOND, 8), item(Material.SPLASH_POTION).setPotion(PotionType.LONG_SWIFTNESS), item(Material.DIAMOND, 8), item(Material.BREWER_POTTERY_SHERD))
            }
            result(CustomItem.EXPLORERS_SANDALS)
            transfer(20)
        }
        recipe {
            grid {
                row(custom(CustomItem.CLOUD_FRAGMENT), item(Material.FEATHER, 16), item(Material.LEATHER_BOOTS).ench("FF4","PT4","DS3","UN3","SP3","MN1"), item(Material.FEATHER, 16), custom(CustomItem.CLOUD_FRAGMENT))
                row(item(Material.FEATHER, 16), item(Material.RABBIT_HIDE, 8), custom(CustomItem.ENCHANTED_CATALYST), item(Material.RABBIT_HIDE, 8), item(Material.FEATHER, 16))
                row(item(Material.PHANTOM_MEMBRANE, 8), custom(CustomItem.ENRICHED_FEATHER), item(Material.NETHERITE_BOOTS).ench("FF4","PT4","DS3","UN3","SP3","MN1"), custom(CustomItem.ENRICHED_FEATHER), item(Material.PHANTOM_MEMBRANE, 8))
                row(item(Material.FEATHER, 16), item(Material.RABBIT_HIDE, 8), custom(CustomItem.ENCHANTED_CATALYST), item(Material.RABBIT_HIDE, 8), item(Material.FEATHER, 16))
                row(custom(CustomItem.CLOUD_FRAGMENT), item(Material.FEATHER, 16), item(Material.LEATHER_BOOTS).ench("FF4","PT4","DS3","UN3","SP3","MN1"), item(Material.FEATHER, 16), custom(CustomItem.CLOUD_FRAGMENT))
            }
            result(CustomItem.DOUBLE_JUMP_BOOTS)
            transfer(20)
        }
        recipe {
            grid {
                row(item(Material.SUGAR, 16), item(Material.BLAZE_POWDER, 16), item(Material.MAGMA_CREAM, 16), item(Material.GHAST_TEAR, 16), item(Material.GLISTERING_MELON_SLICE, 16))
                row(item(Material.NETHER_WART, 16), item(Material.BREWING_STAND, 16), item(Material.BEACON), item(Material.REDSTONE_BLOCK, 16), item(Material.PUFFERFISH, 16))
                row(item(Material.GOLDEN_CARROT, 16), item(Material.REDSTONE_ORE, 4), item(Material.NETHERITE_HELMET).ench("PT4","UN3","AA1","RS3","MN1"), item(Material.REDSTONE_ORE, 4), item(Material.GOLDEN_CARROT, 16))
                row(item(Material.BREEZE_ROD, 8), item(Material.REDSTONE_BLOCK, 16), item(Material.TURTLE_HELMET), item(Material.BREWING_STAND, 16), item(Material.RABBIT_FOOT, 8))
                row(item(Material.COBWEB, 16), item(Material.SLIME_BLOCK, 16), item(Material.FERMENTED_SPIDER_EYE, 16), item(Material.SPIDER_EYE, 16), item(Material.PHANTOM_MEMBRANE, 8))
            }
            result(CustomItem.DRINKING_HAT)
            transfer(20)
        }
        recipe {
            grid {
                row(item(Material.ENCHANTED_BOOK).storeEnch("SN3"), custom(CustomItem.CLOUD_FRAGMENT, 2), custom(CustomItem.ENRICHED_FEATHER), custom(CustomItem.CLOUD_FRAGMENT, 2), item(Material.ENCHANTED_BOOK).storeEnch("SN3"))
                row(custom(CustomItem.CLOUD_FRAGMENT, 2), item(Material.POWDER_SNOW_BUCKET), item(Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.POWDER_SNOW_BUCKET), custom(CustomItem.CLOUD_FRAGMENT, 2))
                row(custom(CustomItem.ENRICHED_FEATHER), item(Material.SNOW_BLOCK, 32), item(Material.NETHERITE_LEGGINGS).ench("PT4","SN3","UN3","MN1"), item(Material.SNOW_BLOCK, 32), custom(CustomItem.ENRICHED_FEATHER))
                row(custom(CustomItem.CLOUD_FRAGMENT, 2), item(Material.POWDER_SNOW_BUCKET), item(Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.POWDER_SNOW_BUCKET), custom(CustomItem.CLOUD_FRAGMENT, 2))
                row(item(Material.ENCHANTED_BOOK).storeEnch("SN3"), custom(CustomItem.CLOUD_FRAGMENT, 2), custom(CustomItem.ENRICHED_FEATHER), custom(CustomItem.CLOUD_FRAGMENT, 2), item(Material.ENCHANTED_BOOK).storeEnch("SN3"))
            }
            result(CustomItem.HERMESS_TROUSERS)
            transfer(20)
        }
        recipe {
            grid {
                row(item(Material.SOUL_LANTERN, 32), item(Material.FLOWER_BANNER_PATTERN), item(Material.OPEN_EYEBLOSSOM, 16), item(Material.FLOWER_BANNER_PATTERN), item(Material.LANTERN, 32))
                row(item(Material.SPYGLASS), item(Material.SPECTRAL_ARROW, 64), item(Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.SPECTRAL_ARROW, 64), item(Material.SPYGLASS))
                row(item(Material.ENDER_EYE, 16), item(Material.TINTED_GLASS, 16), item(Material.NETHERITE_HELMET).ench("PT4","UN3","AA1","RS3","MN1"), item(Material.TINTED_GLASS, 16), item(Material.ENDER_EYE, 16))
                row(item(Material.SPYGLASS), item(Material.SPECTRAL_ARROW, 64), custom(CustomItem.STEEL_PLATING), item(Material.SPECTRAL_ARROW, 64), item(Material.SPYGLASS))
                row(item(Material.LANTERN, 32), item(Material.FLOWER_BANNER_PATTERN), item(Material.OPEN_EYEBLOSSOM, 16), item(Material.FLOWER_BANNER_PATTERN), item(Material.SOUL_LANTERN, 32))
            }
            result(CustomItem.XRAY_GOGGLES)
            transfer(20)
        }
        recipe {
            grid {
                row(item(Material.ENCHANTED_BOOK).storeEnch("FP4"), item(Material.MAGMA_BLOCK, 32), item(Material.COPPER_CHESTPLATE).ench("FP4","MN1","UN3"), item(Material.MAGMA_BLOCK, 32), item(Material.ENCHANTED_BOOK).storeEnch("FP4"))
                row(item(Material.MAGMA_BLOCK, 32), item(Material.BLAZE_POWDER, 32), custom(CustomItem.STEEL_PLATING), item(Material.BLAZE_ROD, 32), item(Material.MAGMA_BLOCK, 32))
                row(item(Material.TIPPED_ARROW, 64).setPotion(PotionType.LONG_FIRE_RESISTANCE), custom(CustomItem.FIRE_RESISTANT_RESIN), item(Material.NETHERITE_CHESTPLATE).ench("PT4","MN1","UN3"), custom(CustomItem.FIRE_RESISTANT_RESIN), item(Material.TIPPED_ARROW, 64).setPotion(PotionType.LONG_FIRE_RESISTANCE))
                row(item(Material.MAGMA_BLOCK, 32), item(Material.BLAZE_ROD, 32), item(Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.BLAZE_POWDER, 32), item(Material.MAGMA_BLOCK, 32))
                row(item(Material.ENCHANTED_BOOK).storeEnch("FP4"), item(Material.MAGMA_BLOCK, 32), item(Material.COPPER_CHESTPLATE).ench("FP4","MN1","UN3"), item(Material.MAGMA_BLOCK, 32), item(Material.ENCHANTED_BOOK).storeEnch("FP4"))
            }
            result(CustomItem.MOLTEN_CHESTPLATE)
            transfer(20)
        }
        recipe {
            grid {
                row(item(Material.DRIED_KELP_BLOCK, 16), item(Material.BRAIN_CORAL_BLOCK, 16), item(Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.BUBBLE_CORAL_BLOCK, 16), item(Material.DRIED_KELP_BLOCK, 16))
                row(item(Material.TURTLE_SCUTE, 4), item(Material.FIRE_CORAL_BLOCK, 16), item(Material.HEART_OF_THE_SEA), item(Material.TUBE_CORAL_BLOCK, 16), item(Material.TURTLE_SCUTE, 4))
                row(item(Material.SALMON, 32), item(Material.GLOW_INK_SAC, 16), item(Material.NETHERITE_BOOTS).ench("FF4","PT4","DS3","UN3","SP3","MN1"), item(Material.GLOW_INK_SAC, 16), item(Material.COD, 32))
                row(item(Material.PRISMARINE_CRYSTALS, 16), item(Material.INK_SAC, 16), item(Material.NAUTILUS_SHELL, 4), item(Material.INK_SAC, 16), item(Material.PRISMARINE_CRYSTALS, 16))
                row(item(Material.PRISMARINE_SHARD, 16), item(Material.SEA_LANTERN, 16), item(Material.TURTLE_EGG, 4), item(Material.SEA_LANTERN, 16), item(Material.PRISMARINE_SHARD, 16))
            }
            result(CustomItem.AQUEOUS_SANDALS)
            transfer(20)
        }
        recipe {
            grid {
                row(item(Material.GLOW_BERRIES, 32), item(Material.SPECTRAL_ARROW, 64), custom(CustomItem.WARDEN_CARAPACE), item(Material.ENDER_EYE, 32), item(Material.GLOWSTONE, 32))
                row(item(Material.GOLDEN_CARROT, 32), item(Material.RAW_GOLD_BLOCK, 16), custom(CustomItem.STEEL_PLATING), item(Material.GOLD_BLOCK, 32), item(Material.GOLDEN_CARROT, 32))
                row(item(Material.PUFFERFISH, 8), item(Material.SCULK_SHRIEKER, 16), item(Material.NETHERITE_HELMET).ench("RS3","UN3","PT4","MN1","AA1"), item(Material.SCULK_SHRIEKER, 16), item(Material.PUFFERFISH, 8))
                row(item(Material.GOLDEN_CARROT, 32), item(Material.GOLD_BLOCK, 32), custom(CustomItem.STEEL_PLATING), item(Material.RAW_GOLD_BLOCK, 16), item(Material.GOLDEN_CARROT, 32))
                row(item(Material.GLOWSTONE, 32), item(Material.ENDER_EYE, 32), item(Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.SPECTRAL_ARROW, 64), item(Material.GLOW_BERRIES, 32))
            }
            result(CustomItem.WELDING_HELMET)
            transfer(20)
        }
        recipe {
            grid {
                row(item(Material.TIPPED_ARROW, 64).setPotion(PotionType.STRONG_POISON), item(Material.MOSSY_COBBLESTONE, 32), item(Material.PUFFERFISH, 32), item(Material.MOSSY_COBBLESTONE, 32), item(Material.WITHER_SKELETON_SKULL, 8))
                row(item(Material.MOURNER_POTTERY_SHERD), item(Material.SPIDER_EYE, 32), custom(CustomItem.STEEL_PLATING), item(Material.ROTTEN_FLESH, 32), item(Material.BURN_POTTERY_SHERD))
                row(item(Material.GOLDEN_APPLE, 32), item(Material.WITHER_ROSE, 32), item(Material.NETHERITE_CHESTPLATE).ench("PT4","MN1","UN3"), item(Material.WITHER_ROSE, 32), item(Material.GOLDEN_APPLE, 32))
                row(item(Material.PITCHER_PLANT, 32), item(Material.ROTTEN_FLESH, 32), custom(CustomItem.STEEL_PLATING), item(Material.SPIDER_EYE, 32), item(Material.TORCHFLOWER, 32))
                row(item(Material.WITHER_SKELETON_SKULL, 8), item(Material.PUFFERFISH_BUCKET), item(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.PUFFERFISH_BUCKET), item(Material.TIPPED_ARROW, 64).setPotion(PotionType.STRONG_POISON))
            }
            result(CustomItem.ANTI_VENOM_SHIRT)
        }
        recipe {
            grid {
                row(item(Material.FERMENTED_SPIDER_EYE, 32), custom(CustomItem.CONDENSED_ICE), custom(CustomItem.WARDEN_CARAPACE), custom(CustomItem.CONDENSED_ICE), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_WEAKNESS))
                row(item(Material.HONEYCOMB, 16), item(Material.SCULK, 32), custom(CustomItem.STEEL_PLATING), item(Material.MUSIC_DISC_CREATOR_MUSIC_BOX), item(Material.HONEYCOMB, 16))
                row(item(Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.ENCHANTED_GOLDEN_APPLE, 2), item(Material.NETHERITE_LEGGINGS).ench("SN3","UN3","PT4","MN1"), item(Material.ENCHANTED_GOLDEN_APPLE, 2), item(Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal())
                row(item(Material.HONEYCOMB, 16), item(Material.MUSIC_DISC_CREATOR), custom(CustomItem.STEEL_PLATING), item(Material.SCULK, 32), item(Material.HONEYCOMB, 16))
                row(item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_WEAKNESS), custom(CustomItem.CONDENSED_ICE), custom(CustomItem.WARDEN_HEART), custom(CustomItem.CONDENSED_ICE), item(Material.FERMENTED_SPIDER_EYE, 32))
            }
            result(CustomItem.ENERGY_RESTORING_PANTS)
            transfer(20)
        }
        recipe {
            grid {
                row(item(Material.SPLASH_POTION).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.HOWL_POTTERY_SHERD), item(Material.MILK_BUCKET), item(Material.HEART_POTTERY_SHERD), item(Material.SPLASH_POTION).setPotion(PotionType.LONG_SLOW_FALLING))
                row(item(Material.WHITE_BANNER, 16), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.SHULKER_SHELL, 48), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.WHITE_BANNER, 16))
                row(item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.PHANTOM_MEMBRANE, 16), item(Material.NETHERITE_BOOTS).ench("FF4","PT4","DS3","UN3","SP3","MN1"), item(Material.PHANTOM_MEMBRANE, 16), item(Material.POPPED_CHORUS_FRUIT, 32))
                row(item(Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING), custom(CustomItem.STEEL_PLATING, 2), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal())
                row(item(Material.SPLASH_POTION).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.FEATHER, 32), item(Material.ELYTRA).ench("MN1","UN3"), item(Material.FEATHER, 32), item(Material.SPLASH_POTION).setPotion(PotionType.LONG_SLOW_FALLING))
            }
            result(CustomItem.STABILZING_SNEAKERS)
            transfer(20)
        }
        recipe {
            grid {
                row(item(Material.GLOWSTONE, 32), item(Material.FIREWORK_STAR, 16), item(Material.ENCHANTED_BOOK).storeEnch("FF4"), item(Material.FIREWORK_STAR, 16), item(Material.GLOWSTONE, 32))
                row(item(Material.FIREWORK_ROCKET, 64).firework(3), item(Material.RESIN_BRICK, 16), custom(CustomItem.CLOUD_FRAGMENT, 4), item(Material.RESIN_BRICK, 16), item(Material.FIREWORK_ROCKET, 64).firework(3))
                row(item(Material.SPLASH_POTION).setPotion(PotionType.STRONG_LEAPING), item(Material.ELYTRA).ench("MN1","UN3"), item(Material.NETHERITE_CHESTPLATE).ench("PT4","MN1","UN3"), item(Material.ELYTRA).ench("MN1","UN3"), item(Material.SPLASH_POTION).setPotion(PotionType.STRONG_LEAPING))
                row(item(Material.FIREWORK_ROCKET, 64).firework(3), item(Material.RESIN_BRICK, 16), custom(CustomItem.STEEL_PLATING), item(Material.RESIN_BRICK, 16), item(Material.FIREWORK_ROCKET, 64).firework(3))
                row(item(Material.GLOWSTONE, 32), item(Material.FIREWORK_STAR, 16), item(Material.ENCHANTED_BOOK).storeEnch("FF4"), item(Material.FIREWORK_STAR, 16), item(Material.GLOWSTONE, 32))
            }
            result(CustomItem.MECHANIZED_ELYTRA)
        }
        recipe {
            grid {
                row(item(Material.GLOW_INK_SAC, 16), item(Material.RAW_GOLD, 8), item(Material.NETHER_STAR), item(Material.RAW_GOLD, 8), item(Material.GOLD_INGOT, 16))
                row(item(Material.AMETHYST_SHARD, 16), item(Material.NAUTILUS_SHELL), custom(CustomItem.ENCHANTED_CATALYST), item(Material.NAUTILUS_SHELL), item(Material.AMETHYST_SHARD, 16))
                row(item(Material.ECHO_SHARD, 4), custom(CustomItem.ENCHANTED_CATALYST), item(Material.NETHERITE_HELMET).ench("PT4","UN3","AA1","RS3","MN1"), custom(CustomItem.ENCHANTED_CATALYST), item(Material.ECHO_SHARD, 4))
                row(item(Material.AMETHYST_SHARD, 16), item(Material.NAUTILUS_SHELL), custom(CustomItem.ENCHANTED_CATALYST), item(Material.NAUTILUS_SHELL), item(Material.AMETHYST_SHARD, 16))
                row(item(Material.GOLD_INGOT, 16), item(Material.RAW_GOLD, 8), item(Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.RAW_GOLD, 8), item(Material.GLOW_INK_SAC, 16))
            }
            result(CustomItem.FAIRY_EARS)
            transfer(20)
        }


    }
}