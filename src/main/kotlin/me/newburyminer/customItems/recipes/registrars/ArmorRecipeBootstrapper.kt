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
                row(item(Material.SUGAR, 16), item(Material.BLAZE_POWDER, 16), item(Material.MAGMA_CREAM, 16), item(Material.GHAST_TEAR, 16), item(Material.GLISTERING_MELON_SLICE, 16))
                row(item(Material.NETHER_WART, 16), item(Material.BREWING_STAND, 16), item(Material.REDSTONE_BLOCK, 16), item(Material.BEACON), item(Material.GOLDEN_CARROT, 16))
                row(item(Material.TURTLE_HELMET), item(Material.REDSTONE_ORE, 8), item(Material.NETHERITE_HELMET).ench("PT4","UN3","MN1","AA1","RS3"), item(Material.REDSTONE_ORE, 8), item(Material.PUFFERFISH, 16))
                row(item(Material.BREEZE_ROD, 16), item(Material.BEACON), item(Material.REDSTONE_BLOCK, 16), item(Material.BREWING_STAND, 16), item(Material.RABBIT_FOOT, 16))
                row(item(Material.COBWEB, 16), item(Material.SLIME_BLOCK, 16), item(Material.FERMENTED_SPIDER_EYE, 16), item(Material.SPIDER_EYE, 16), item(Material.PHANTOM_MEMBRANE, 16))
            }
            result(CustomItem.DRINKING_HAT)
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
                row(item(Material.ENCHANTED_BOOK).storeEnch("SN3"), custom(CustomItem.CLOUD_FRAGMENT, 4), custom(CustomItem.ENRICHED_FEATHER), custom(CustomItem.CLOUD_FRAGMENT, 4), item(Material.ENCHANTED_BOOK).storeEnch("SN3"))
                row(custom(CustomItem.CLOUD_FRAGMENT, 4), item(Material.POWDER_SNOW_BUCKET), item(Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.POWDER_SNOW_BUCKET), custom(CustomItem.CLOUD_FRAGMENT, 4))
                row(custom(CustomItem.ENRICHED_FEATHER), item(Material.SNOW_BLOCK, 32), item(Material.NETHERITE_LEGGINGS).ench("PT4","UN3","MN1","SN3"), item(Material.SNOW_BLOCK, 32), custom(CustomItem.ENRICHED_FEATHER))
                row(custom(CustomItem.CLOUD_FRAGMENT, 4), item(Material.POWDER_SNOW_BUCKET), item(Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.POWDER_SNOW_BUCKET), custom(CustomItem.CLOUD_FRAGMENT, 4))
                row(item(Material.ENCHANTED_BOOK).storeEnch("SN3"), custom(CustomItem.CLOUD_FRAGMENT, 4), custom(CustomItem.ENRICHED_FEATHER), custom(CustomItem.CLOUD_FRAGMENT, 4), item(Material.ENCHANTED_BOOK).storeEnch("SN3"))
            }
            result(CustomItem.HERMESS_TROUSERS)
        }
        recipe {
            grid {
                row(item(Material.ENCHANTED_BOOK).storeEnch("FP4"), item(Material.MAGMA_BLOCK, 32), item(Material.COPPER_CHESTPLATE).ench("FP4", "UN3", "MN1"), item(Material.MAGMA_BLOCK, 32), item(Material.ENCHANTED_BOOK).storeEnch("FP4"))
                row(item(Material.MAGMA_BLOCK, 32), item(Material.BLAZE_POWDER, 32), custom(CustomItem.FIRE_RESISTANT_RESIN, 4), item(Material.BLAZE_ROD, 32), item(Material.MAGMA_BLOCK, 32))
                row(item(Material.TIPPED_ARROW, 64).setPotion(PotionType.LONG_FIRE_RESISTANCE), custom(CustomItem.STEEL_PLATING), item(Material.NETHERITE_CHESTPLATE).ench("PT4","UN3","MN1"), custom(CustomItem.STEEL_PLATING), item(Material.TIPPED_ARROW, 64).setPotion(PotionType.LONG_FIRE_RESISTANCE))
                row(item(Material.MAGMA_BLOCK, 32), item(Material.BLAZE_ROD, 32), item(Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.BLAZE_POWDER, 32), item(Material.MAGMA_BLOCK, 32))
                row(item(Material.ENCHANTED_BOOK).storeEnch("FP4"), item(Material.MAGMA_BLOCK, 32), item(Material.COPPER_CHESTPLATE).ench("FP4", "UN3", "MN1"), item(Material.MAGMA_BLOCK, 32), item(Material.ENCHANTED_BOOK).storeEnch("FP4"))
            }
            result(CustomItem.MOLTEN_CHESTPLATE)
        }
        recipe {
            grid {
                row(item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING), custom(CustomItem.CLOUD_FRAGMENT, 4), item(Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), custom(CustomItem.CLOUD_FRAGMENT, 4), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.STRONG_SWIFTNESS))
                row(custom(CustomItem.CLOUD_FRAGMENT, 4), item(Material.PINK_TULIP, 16), item(Material.SUGAR, 16), item(Material.OXEYE_DAISY, 16), custom(CustomItem.CLOUD_FRAGMENT, 4))
                row(custom(CustomItem.SHULKER_BULLET_ARROW, 4), custom(CustomItem.ENRICHED_FEATHER), item(Material.NETHERITE_BOOTS).ench("PT4","UN3","MN1","FF4","SP3","DS3"), custom(CustomItem.ENRICHED_FEATHER), custom(CustomItem.SHULKER_BULLET_ARROW, 4))
                row(custom(CustomItem.CLOUD_FRAGMENT, 4), item(Material.LILY_OF_THE_VALLEY, 16), item(Material.SUGAR, 16), item(Material.AZURE_BLUET, 16), custom(CustomItem.CLOUD_FRAGMENT, 4))
                row(item(Material.TIPPED_ARROW, 32).setPotion(PotionType.STRONG_SWIFTNESS), custom(CustomItem.CLOUD_FRAGMENT, 4), item(Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), custom(CustomItem.CLOUD_FRAGMENT, 4), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING))
            }
            result(CustomItem.CLOUD_BOOTS)
        }
        recipe {
            grid {
                row(custom(CustomItem.CONDENSED_INVISIBILITY), item(Material.GOLDEN_CARROT, 64), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_INVISIBILITY), item(Material.FERMENTED_SPIDER_EYE, 32), custom(CustomItem.CONDENSED_INVISIBILITY))
                row(item(Material.STRIPPED_PALE_OAK_LOG, 32), item(Material.ENCHANTED_BOOK).storeEnch("SN3"), custom(CustomItem.STEEL_PLATING), item(Material.ENCHANTED_BOOK).storeEnch("SN3"), item(Material.PALE_MOSS_BLOCK, 64))
                row(item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_INVISIBILITY), item(Material.CALCITE, 32), item(Material.NETHERITE_HELMET).ench("PT4","UN3","MN1","AA1","RS3"), item(Material.CALCITE, 32), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_INVISIBILITY))
                row(item(Material.PALE_MOSS_BLOCK, 64), item(Material.ENCHANTED_BOOK).storeEnch("SN3"), custom(CustomItem.STEEL_PLATING), item(Material.ENCHANTED_BOOK).storeEnch("SN3"), item(Material.STRIPPED_PALE_OAK_LOG, 32))
                row(custom(CustomItem.CONDENSED_INVISIBILITY), item(Material.FERMENTED_SPIDER_EYE, 32), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_INVISIBILITY), item(Material.GOLDEN_CARROT, 64), custom(CustomItem.CONDENSED_INVISIBILITY))
            }
            result(CustomItem.INVISIBILITY_CLOAK)
        }
        recipe {
            grid {
                row(item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.FEATHER, 32), item(Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.FEATHER, 32), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING))
                row(item(Material.PURPUR_BLOCK, 32), custom(CustomItem.ENRICHED_FEATHER), item(Material.SPLASH_POTION).setPotion(PotionType.WIND_CHARGED), custom(CustomItem.ENRICHED_FEATHER), item(Material.PURPUR_BLOCK, 32))
                row(item(Material.RABBIT_HIDE, 4), item(Material.SPLASH_POTION).setPotion(PotionType.WIND_CHARGED), item(Material.NETHERITE_BOOTS), item(Material.SPLASH_POTION).setPotion(PotionType.WIND_CHARGED), item(Material.RABBIT_HIDE, 4))
                row(item(Material.GUSTER_BANNER_PATTERN), custom(CustomItem.CLOUD_FRAGMENT, 4), item(Material.SPLASH_POTION).setPotion(PotionType.WIND_CHARGED), custom(CustomItem.CLOUD_FRAGMENT, 4), item(Material.GUSTER_BANNER_PATTERN))
                row(item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.END_STONE, 64), item(Material.END_STONE_BRICKS, 64), item(Material.END_STONE, 64), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING))
            }
            result(CustomItem.MOON_BOOTS)
        }
        recipe {
            grid {
                row(item(Material.SOUL_LANTERN, 32), item(Material.FLOWER_BANNER_PATTERN), item(Material.DRAGON_HEAD, 4), item(Material.FLOWER_BANNER_PATTERN), item(Material.LANTERN, 32))
                row(item(Material.SPYGLASS), item(Material.SPECTRAL_ARROW, 64), item(Material.OPEN_EYEBLOSSOM, 64), item(Material.SPECTRAL_ARROW, 64), item(Material.SPYGLASS))
                row(item(Material.ENDER_EYE, 32), item(Material.TINTED_GLASS, 32), item(Material.NETHERITE_HELMET).ench("PT4","UN3","MN1","AA1","RS3"), item(Material.TINTED_GLASS, 32), item(Material.ENDER_EYE, 32))
                row(item(Material.SPYGLASS), item(Material.SPECTRAL_ARROW, 64), custom(CustomItem.STEEL_PLATING), item(Material.SPECTRAL_ARROW, 64), item(Material.SPYGLASS))
                row(item(Material.LANTERN, 32), item(Material.FLOWER_BANNER_PATTERN), item(Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.FLOWER_BANNER_PATTERN), item(Material.SOUL_LANTERN, 32))
            }
            result(CustomItem.XRAY_GOGGLES)
        }
        recipe {
            grid {
                row(item(Material.GLOWSTONE, 32), item(Material.FIREWORK_STAR, 16), item(Material.ENCHANTED_BOOK).storeEnch("FF4"), item(Material.FIREWORK_STAR, 16), item(Material.GLOWSTONE, 32))
                row(item(Material.RESIN_BRICK, 32), item(Material.FIREWORK_ROCKET, 64).firework(3), custom(CustomItem.CLOUD_FRAGMENT, 4), item(Material.FIREWORK_ROCKET, 64).firework(3), custom(CustomItem.STEEL_PLATING))
                row(item(Material.SPLASH_POTION).setPotion(PotionType.STRONG_LEAPING), item(Material.ELYTRA).ench("UN3","MN1"), item(Material.NETHERITE_CHESTPLATE).ench("PT4","UN3","MN1"), item(Material.ELYTRA).ench("UN3","MN1"), item(Material.SPLASH_POTION).setPotion(PotionType.STRONG_LEAPING))
                row(custom(CustomItem.STEEL_PLATING), item(Material.FIREWORK_ROCKET, 64).firework(3), custom(CustomItem.CLOUD_FRAGMENT, 4), item(Material.FIREWORK_ROCKET, 64).firework(3), item(Material.RESIN_BRICK, 32))
                row(item(Material.GLOWSTONE, 32), item(Material.FIREWORK_STAR, 16), item(Material.ENCHANTED_BOOK).storeEnch("FF4"), item(Material.FIREWORK_STAR, 16), item(Material.GLOWSTONE, 32))
            }
            result(CustomItem.MECHANIZED_ELYTRA)
        }
        recipe {
            grid {
                row(item(Material.GLOW_BERRIES, 32), item(Material.SPECTRAL_ARROW, 64), custom(CustomItem.WARDEN_CARAPACE), item(Material.ENDER_EYE, 32), item(Material.GLOWSTONE, 32))
                row(item(Material.GOLDEN_CARROT, 32), item(Material.RAW_GOLD_BLOCK, 16), custom(CustomItem.STEEL_PLATING), item(Material.GOLD_BLOCK, 32), item(Material.GOLDEN_CARROT, 32))
                row(item(Material.PUFFERFISH, 8), item(Material.SCULK_SHRIEKER, 16), item(Material.NETHERITE_HELMET).ench("PT4","UN3","MN1","AA1","RS3"), item(Material.SCULK_SHRIEKER, 16), item(Material.PUFFERFISH, 8))
                row(item(Material.GOLDEN_CARROT, 32), item(Material.GOLD_BLOCK, 32), custom(CustomItem.STEEL_PLATING), item(Material.RAW_GOLD_BLOCK, 16), item(Material.GOLDEN_CARROT, 32))
                row(item(Material.GLOWSTONE, 32), item(Material.ENDER_EYE, 32), item(Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.SPECTRAL_ARROW, 64), item(Material.GLOW_BERRIES, 32))
            }
            result(CustomItem.WELDING_HELMET)
        }
        recipe {
            grid {
                row(item(Material.TIPPED_ARROW, 64).setPotion(PotionType.STRONG_POISON), item(Material.MOSSY_COBBLESTONE, 32), item(Material.PUFFERFISH, 32), item(Material.MOSSY_COBBLESTONE, 32), item(Material.WITHER_SKELETON_SKULL, 8))
                row(item(Material.MOURNER_POTTERY_SHERD), item(Material.SPIDER_EYE, 32), custom(CustomItem.STEEL_PLATING), item(Material.ROTTEN_FLESH, 32), item(Material.BURN_POTTERY_SHERD))
                row(item(Material.GOLDEN_APPLE, 32).setPotion(PotionType.LONG_FIRE_RESISTANCE), item(Material.WITHER_ROSE, 32), item(Material.NETHERITE_CHESTPLATE).ench("PT4","UN3","MN1"), item(Material.WITHER_ROSE, 32), item(Material.GOLDEN_APPLE, 32))
                row(item(Material.PITCHER_PLANT, 32), item(Material.ROTTEN_FLESH, 32), custom(CustomItem.STEEL_PLATING), item(Material.SPIDER_EYE, 32), item(Material.TORCHFLOWER, 32))
                row(item(Material.WITHER_SKELETON_SKULL, 8), item(Material.PUFFERFISH_BUCKET), item(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.PUFFERFISH_BUCKET), item(Material.TIPPED_ARROW, 64).setPotion(PotionType.STRONG_POISON))
            }
            result(CustomItem.ANTI_VENOM_SHIRT)
        }
        recipe {
            grid {
                row(item(Material.FERMENTED_SPIDER_EYE, 32), custom(CustomItem.CONDENSED_ICE), custom(CustomItem.WARDEN_CARAPACE), custom(CustomItem.CONDENSED_ICE), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_WEAKNESS))
                row(item(Material.HONEYCOMB, 16), item(Material.SCULK, 32), custom(CustomItem.STEEL_PLATING), item(Material.MUSIC_DISC_CREATOR_MUSIC_BOX), item(Material.HONEYCOMB, 16))
                row(item(Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.ENCHANTED_GOLDEN_APPLE, 2), item(Material.NETHERITE_LEGGINGS).ench("PT4","UN3","MN1","SN3"), item(Material.ENCHANTED_GOLDEN_APPLE, 2), item(Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal())
                row(item(Material.HONEYCOMB, 16), item(Material.MUSIC_DISC_CREATOR), custom(CustomItem.STEEL_PLATING), item(Material.SCULK, 32), item(Material.HONEYCOMB, 16))
                row(item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_WEAKNESS), custom(CustomItem.CONDENSED_ICE), custom(CustomItem.WARDEN_HEART), custom(CustomItem.CONDENSED_ICE), item(Material.FERMENTED_SPIDER_EYE, 32))
            }
            result(CustomItem.ENERGY_RESTORING_PANTS)
        }
        recipe {
            grid {
                row(item(Material.SPLASH_POTION).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.HOWL_POTTERY_SHERD), item(Material.MILK_BUCKET), item(Material.HEART_POTTERY_SHERD), item(Material.SPLASH_POTION).setPotion(PotionType.LONG_SLOW_FALLING))
                row(item(Material.WHITE_BANNER, 16), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.SHULKER_SHELL, 48), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.WHITE_BANNER, 16))
                row(item(Material.POPPED_CHORUS_FRUIT, 32), item(Material.PHANTOM_MEMBRANE, 16), item(Material.NETHERITE_BOOTS).ench("PT4","UN3","MN1","FF4","SP3","DS3"), item(Material.PHANTOM_MEMBRANE, 16), item(Material.POPPED_CHORUS_FRUIT, 32))
                row(item(Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING), custom(CustomItem.STEEL_PLATING, 2), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal())
                row(item(Material.SPLASH_POTION).setPotion(PotionType.LONG_SLOW_FALLING), item(Material.FEATHER, 32), item(Material.ELYTRA).ench("UN3","MN1"), item(Material.FEATHER, 32), item(Material.SPLASH_POTION).setPotion(PotionType.LONG_SLOW_FALLING))
            }
            result(CustomItem.STABILZING_SNEAKERS)
        }
        recipe {
            grid {
                row(item(Material.NETHERITE_INGOT), item(Material.BLAZE_POWDER, 32), custom(CustomItem.WARDEN_CARAPACE), item(Material.BLAZE_POWDER, 32), item(Material.NETHERITE_INGOT))
                row(item(Material.ENCHANTED_BOOK).storeEnch("DN5"), item(Material.ENCHANTED_GOLDEN_APPLE), custom(CustomItem.STEEL_PLATING), item(Material.ENCHANTED_GOLDEN_APPLE), item(Material.ENCHANTED_BOOK).storeEnch("SM5"))
                row(item(Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), custom(CustomItem.STEEL_PLATING), item(Material.NETHERITE_CHESTPLATE).ench("PT4","UN3","MN1"), custom(CustomItem.STEEL_PLATING), item(Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal())
                row(item(Material.ENCHANTED_BOOK).storeEnch("SH5"), item(Material.ENCHANTED_GOLDEN_APPLE), item(Material.MUSIC_DISC_LAVA_CHICKEN), item(Material.ENCHANTED_GOLDEN_APPLE), item(Material.ENCHANTED_BOOK).storeEnch("BA5"))
                row(item(Material.NETHERITE_INGOT), item(Material.BLAZE_POWDER, 32), custom(CustomItem.WARDEN_HEART), item(Material.BLAZE_POWDER, 32), item(Material.NETHERITE_INGOT))
            }
            result(CustomItem.BERSERKER_CHESTPLATE)
        }
        recipe {
            grid {
                row(item(Material.DIAMOND, 8), custom(CustomItem.CONDENSED_ICE), custom(CustomItem.WARDEN_CARAPACE), custom(CustomItem.CONDENSED_ICE), item(Material.DIAMOND, 8))
                row(item(Material.RAW_GOLD, 8), item(Material.SCULK_CATALYST, 16), custom(CustomItem.STEEL_PLATING), item(Material.SCULK, 64), item(Material.RAW_GOLD, 8))
                row(item(Material.IRON_BLOCK, 32), item(Material.ENCHANTED_GOLDEN_APPLE), item(Material.NETHERITE_LEGGINGS).ench("PT4","UN3","MN1","SN3"), item(Material.ENCHANTED_GOLDEN_APPLE), item(Material.IRON_BLOCK, 32))
                row(item(Material.RAW_GOLD, 8), item(Material.SCULK, 64), custom(CustomItem.STEEL_PLATING), item(Material.SCULK_CATALYST, 16), item(Material.RAW_GOLD, 8))
                row(item(Material.DIAMOND, 8), custom(CustomItem.CONDENSED_ICE), custom(CustomItem.TOTEM_CORE), custom(CustomItem.CONDENSED_ICE), item(Material.DIAMOND, 8))
            }
            result(CustomItem.SHADOW_LEGS)
        }
    }
}