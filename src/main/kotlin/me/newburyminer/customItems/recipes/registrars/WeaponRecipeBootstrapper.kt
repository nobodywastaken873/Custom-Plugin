package me.newburyminer.customItems.recipes.registrars

import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.recipes.RecipeBootstrapper
import me.newburyminer.customItems.recipes.RecipeType
import org.bukkit.Material
import org.bukkit.MusicInstrument
import org.bukkit.potion.PotionType

object WeaponRecipeBootstrapper: RecipeBootstrapper {
    override val recipeType: RecipeType = RecipeType.WEAPON
    override fun bootstrap() {
        recipe {
            grid {
                row(item(Material.FIREWORK_ROCKET, 64), item(Material.FIREWORK_ROCKET, 64), null, null, null)
                row(item(Material.CROSSBOW).ench("PR4","UN3","MN1","QC3"), item(Material.IRON_BLOCK, 8), item(Material.IRON_BLOCK, 8), item(Material.IRON_BLOCK, 8), custom(CustomItem.WITHER_SKULL_ARROW, 2))
                row(item(Material.IRON_BLOCK, 8), item(Material.TNT, 64), item(Material.TNT_MINECART), item(Material.TNT_MINECART), item(Material.TNT_MINECART))
                row(item(Material.CROSSBOW).ench("PR4","UN3","MN1","QC3"), item(Material.IRON_BLOCK, 8), item(Material.IRON_BLOCK, 8), item(Material.IRON_BLOCK, 8), custom(CustomItem.WITHER_SKULL_ARROW, 2))
                row(item(Material.FIREWORK_ROCKET, 64), item(Material.FIREWORK_ROCKET, 64), null, null, null)
            }
            result(CustomItem.SURFACE_TO_AIR_MISSILE)
        }
        recipe {
            grid {
                row(item(Material.LIGHTNING_ROD, 16), item(Material.COPPER_INGOT, 32), item(Material.ARROW, 32), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.STRONG_SWIFTNESS), item(Material.NETHERITE_SCRAP, 2))
                row(item(Material.REPEATER, 8), item(Material.REPEATER, 8), item(Material.REPEATER, 8), item(Material.REPEATER, 8), item(Material.CROSSBOW).ench("PR4","UN3","MN1","QC3"))
                row(item(Material.POWERED_RAIL, 16), item(Material.POWERED_RAIL, 16), item(Material.ACTIVATOR_RAIL, 16), item(Material.ACTIVATOR_RAIL, 16), custom(CustomItem.MEGAMIX_DISC_TRACK))
                row(item(Material.REPEATER, 8), item(Material.REPEATER, 8), item(Material.REPEATER, 8), item(Material.REPEATER, 8), item(Material.CROSSBOW).ench("PR4","UN3","MN1","QC3"))
                row(item(Material.LIGHTNING_ROD, 16), item(Material.COPPER_INGOT, 32), item(Material.ARROW, 32), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.STRONG_SWIFTNESS), item(Material.NETHERITE_SCRAP, 2))
            }
            result(CustomItem.REDSTONE_REPEATER)
        }
        recipe {
            grid {
                row(item(Material.FIRE_CHARGE, 16), item(Material.ARROW, 64), item(Material.ARROW, 64), item(Material.ARROW, 64), item(Material.FIRE_CHARGE, 16))
                row(item(Material.BLAZE_POWDER, 16), item(Material.TNT, 8), item(Material.END_CRYSTAL, 16), item(Material.TNT, 8), item(Material.BLAZE_POWDER, 16))
                row(item(Material.GLOWSTONE_DUST, 16), item(Material.RESPAWN_ANCHOR, 16), item(Material.CROSSBOW), item(Material.RESPAWN_ANCHOR, 16), item(Material.GLOWSTONE_DUST, 16))
                row(item(Material.BLAZE_POWDER, 16), item(Material.TNT, 8), item(Material.END_CRYSTAL, 16), item(Material.TNT, 8), item(Material.BLAZE_POWDER, 16))
                row(item(Material.FIRE_CHARGE, 16), item(Material.ARROW, 64), item(Material.ARROW, 64), item(Material.ARROW, 64), item(Material.FIRE_CHARGE, 16))
            }
            result(CustomItem.LANDMINE_LAUNCHER)
        }
        recipe {
            grid {
                row(item(Material.STRING, 32), item(Material.STICK, 32), item(Material.ARROW, 64), item(Material.STICK, 32), item(Material.POINTED_DRIPSTONE, 8))
                row(item(Material.ARMADILLO_SCUTE, 16), custom(CustomItem.TOOL_HANDLE), item(Material.ENCHANTED_BOOK).storeEnch("PR4"), custom(CustomItem.TOOL_HANDLE), item(Material.BLUE_GLAZED_TERRACOTTA, 4))
                row(item(Material.ARROW, 64), item(Material.BOW).ench("PW5", "FL1", "PU2", "UN3", "MN1"), item(Material.PUFFERFISH, 16), item(Material.CROSSBOW).ench("PR4","UN3","MN1","QC3"), item(Material.ARROW, 64))
                row(item(Material.BLUE_GLAZED_TERRACOTTA, 4), custom(CustomItem.TOOL_HANDLE), item(Material.ENCHANTED_BOOK).storeEnch("PR4"), custom(CustomItem.TOOL_HANDLE), item(Material.ARMADILLO_SCUTE, 16))
                row(item(Material.POINTED_DRIPSTONE, 8), item(Material.STICK, 32), item(Material.ARROW, 64), item(Material.STICK, 32), item(Material.STRING, 32))
            }
            result(CustomItem.DUAL_BARRELED_CROSSBOW)
        }
        recipe {
            grid {
                row(item(Material.FIRE_CHARGE, 16), item(Material.IRON_INGOT, 64), item(Material.IRON_CHAIN, 16), item(Material.IRON_INGOT, 64), item(Material.FIRE_CHARGE, 16))
                row(item(Material.IRON_INGOT, 64), custom(CustomItem.STEEL_PLATING), item(Material.MUSIC_DISC_PIGSTEP), custom(CustomItem.STEEL_PLATING), item(Material.IRON_INGOT, 64))
                row(item(Material.IRON_CHAIN, 16), item(Material.GUNPOWDER, 64), item(Material.CROSSBOW).ench("ML1","UN3","MN1","QC3"), item(Material.GUNPOWDER, 64), item(Material.IRON_CHAIN, 16))
                row(item(Material.IRON_INGOT, 64), item(Material.PIGLIN_BANNER_PATTERN), item(Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.PIGLIN_BANNER_PATTERN), item(Material.IRON_INGOT, 64))
                row(item(Material.FIRE_CHARGE, 16), item(Material.IRON_INGOT, 64), item(Material.IRON_CHAIN, 16), item(Material.IRON_INGOT, 64), item(Material.FIRE_CHARGE, 16))
            }
            result(CustomItem.PORTABLE_CANNON)
        }
        recipe {
            grid {
                row(item(Material.SPYGLASS), item(Material.ENDER_EYE, 16), item(Material.FIREWORK_ROCKET, 64).firework(3), item(Material.ENDER_EYE, 16), item(Material.SPYGLASS))
                row(custom(CustomItem.CLOUD_FRAGMENT), item(Material.FIREWORK_STAR, 64), item(Material.GUNPOWDER, 32), item(Material.FIRE_CHARGE, 32), custom(CustomItem.CLOUD_FRAGMENT))
                row(item(Material.FIREWORK_ROCKET, 64).firework(3), item(Material.CROSSBOW).ench("PR4","UN3","MN1","QC3"), item(Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE), item(Material.CROSSBOW).ench("PR4","UN3","MN1","QC3"), item(Material.FIREWORK_ROCKET, 64).firework(3))
                row(custom(CustomItem.CLOUD_FRAGMENT), item(Material.FIRE_CHARGE, 32), item(Material.GUNPOWDER, 32), item(Material.FIREWORK_STAR, 64), custom(CustomItem.CLOUD_FRAGMENT))
                row(item(Material.SPYGLASS), item(Material.ENDER_EYE, 16), item(Material.FIREWORK_ROCKET, 64).firework(3), item(Material.ENDER_EYE, 16), item(Material.SPYGLASS))
            }
            result(CustomItem.RPG_LAUNCHER)
        }
        recipe {
            grid {
                row(item(Material.FIREWORK_ROCKET, 64).firework(3), item(Material.SCULK_CATALYST, 8), item(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.PRISMARINE_SHARD, 16), item(Material.FIREWORK_ROCKET, 64).firework(3))
                row(item(Material.QUARTZ, 64), custom(CustomItem.STEEL_PLATING, 2), item(Material.CROSSBOW).ench("PR4","UN3","MN1","QC3"), custom(CustomItem.REINFORCED_HANDLE, 4), item(Material.WIND_CHARGE, 64))
                row(item(Material.TIPPED_ARROW, 64).setPotion(PotionType.STRONG_HARMING), item(Material.CROSSBOW).ench("PR4","UN3","MN1","QC3"), item(Material.DRAGON_HEAD, 4), item(Material.CROSSBOW).ench("PR4","UN3","MN1","QC3"), item(Material.TIPPED_ARROW, 64).setPotion(PotionType.STRONG_HARMING))
                row(item(Material.WIND_CHARGE, 64), custom(CustomItem.REINFORCED_HANDLE, 4), item(Material.CROSSBOW).ench("PR4","UN3","MN1","QC3"), custom(CustomItem.STEEL_PLATING, 2), item(Material.QUARTZ, 64))
                row(item(Material.FIREWORK_ROCKET, 64).firework(3), item(Material.PRISMARINE_CRYSTALS, 16), item(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.NAUTILUS_SHELL, 8), item(Material.FIREWORK_ROCKET, 64).firework(3))
            }
            result(CustomItem.SNIPER_RIFLE)
        }
        recipe {
            grid {
                row(item(Material.SCULK, 64), item(Material.ENCHANTED_BOOK).storeEnch("IN1"), item(Material.NETHER_STAR, 2), item(Material.ENCHANTED_BOOK).storeEnch("QC3"), item(Material.SCULK, 64))
                row(item(Material.ENDER_PEARL, 16), item(Material.SCULK_CATALYST, 8), custom(CustomItem.ENCHANTED_CATALYST), item(Material.SCULK_CATALYST, 8), item(Material.ENDER_PEARL, 16))
                row(item(Material.ENDER_PEARL, 16), custom(CustomItem.FRAGMENT_OF_SOUND, 2), item(Material.CROSSBOW).ench("PR4","UN3","MN1","QC3"), custom(CustomItem.FRAGMENT_OF_SOUND, 2), item(Material.ENDER_PEARL, 16))
                row(item(Material.ENDER_PEARL, 16), item(Material.SCULK_CATALYST, 8), custom(CustomItem.WARDEN_CARAPACE), item(Material.SCULK_CATALYST, 8), item(Material.ENDER_PEARL, 16))
                row(item(Material.SCULK, 64), item(Material.ENCHANTED_BOOK).storeEnch("QC3"), custom(CustomItem.SHADOW_DISC_CORE), item(Material.ENCHANTED_BOOK).storeEnch("IN1"), item(Material.SCULK, 64))
            }
            result(CustomItem.SONIC_CROSSBOW)
        }
        recipe {
            grid {
                row(item(Material.GHAST_TEAR, 8), item(Material.QUARTZ, 32), item(Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE).ench("DU1"), item(Material.AMETHYST_SHARD, 32), item(Material.GHAST_TEAR, 8))
                row(item(Material.ENCHANTED_BOOK).storeEnch("PW5"), item(Material.OMINOUS_BOTTLE, 4).setOminous(0), item(Material.EMERALD, 32), item(Material.OMINOUS_BOTTLE, 4).setOminous(1), item(Material.ENDER_EYE, 16))
                row(custom(CustomItem.ENCHANTED_CATALYST), item(Material.EMERALD, 32), custom(CustomItem.TOTEM_CORE), item(Material.EMERALD, 32), custom(CustomItem.ENCHANTED_CATALYST))
                row(item(Material.ENDER_EYE, 16), item(Material.OMINOUS_BOTTLE, 4).setOminous(2), item(Material.EMERALD, 32), item(Material.OMINOUS_BOTTLE, 4).setOminous(3), item(Material.ENCHANTED_BOOK).storeEnch("PW5"))
                row(item(Material.GHAST_TEAR, 8), item(Material.AMETHYST_SHARD, 32), item(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE).ench("DU1"), item(Material.QUARTZ, 32), item(Material.GHAST_TEAR, 8))
            }
            result(CustomItem.FANGED_STAFF)
        }
        recipe {
            grid {
                row(null, null, item(Material.WIND_CHARGE, 32), item(Material.FISHING_ROD).ench("UN3", "MN1"), item(Material.IRON_INGOT, 64))
                row(null, null, item(Material.FISHING_ROD).ench("UN3", "MN1"), item(Material.LEAD, 32), item(Material.PRISMARINE_SHARD, 32))
                row(null, null, item(Material.NETHERITE_SWORD), null, null)
                row(item(Material.PRISMARINE_SHARD, 32), item(Material.LEAD, 32), item(Material.FISHING_ROD).ench("UN3", "MN1"), null, null)
                row(item(Material.IRON_INGOT, 64), item(Material.FISHING_ROD).ench("UN3", "MN1"), item(Material.WIND_CHARGE, 32), null, null)
            }
            result(CustomItem.HOOKED_CUTLASS)
        }
        recipe {
            grid {
                row(item(Material.FEATHER, 32), item(Material.PAPER, 32), item(Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.PAPER, 32), item(Material.QUARTZ, 32))
                row(item(Material.SUGAR, 16), custom(CustomItem.ENRICHED_FEATHER, 2), item(Material.GOAT_HORN).goatHorn(MusicInstrument.DREAM_GOAT_HORN), custom(CustomItem.ENRICHED_FEATHER, 2), item(Material.SUGAR, 16))
                row(item(Material.SUGAR, 16), item(Material.GOAT_HORN).goatHorn(MusicInstrument.CALL_GOAT_HORN), item(Material.NETHERITE_SWORD).ench("SH5","UN3","MN1","FA2"), item(Material.GOAT_HORN).goatHorn(MusicInstrument.YEARN_GOAT_HORN), item(Material.SUGAR, 16))
                row(item(Material.SUGAR, 16), custom(CustomItem.ENRICHED_FEATHER, 2), item(Material.GOAT_HORN).goatHorn(MusicInstrument.ADMIRE_GOAT_HORN), custom(CustomItem.ENRICHED_FEATHER, 2), item(Material.SUGAR, 16))
                row(item(Material.QUARTZ, 32), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.WIND_CHARGED), item(Material.ENCHANTED_BOOK).storeEnch("WB1"), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.WIND_CHARGED), item(Material.FEATHER, 32))
            }
            result(CustomItem.GALE_BLADE)
        }
        recipe {
            grid {
                row(item(Material.WHITE_CANDLE, 16), custom(CustomItem.CONDENSED_ICE, 2), item(Material.MUSIC_DISC_TEARS), custom(CustomItem.CONDENSED_ICE, 2), item(Material.LIGHT_BLUE_CANDLE, 16))
                row(custom(CustomItem.CONDENSED_ICE, 2), item(Material.NETHERITE_INGOT), item(Material.BLUE_ICE, 8), item(Material.SKELETON_SKULL, 2), custom(CustomItem.CONDENSED_ICE, 2))
                row(item(Material.TIPPED_ARROW, 64).setPotion(PotionType.STRONG_TURTLE_MASTER), item(Material.BLUE_ICE, 8), item(Material.NETHERITE_SWORD).ench("SH5","UN3","MN1","FA2"), item(Material.BLUE_ICE, 8), item(Material.TIPPED_ARROW, 64).setPotion(PotionType.STRONG_TURTLE_MASTER))
                row(custom(CustomItem.CONDENSED_ICE, 2), item(Material.SKELETON_SKULL, 2), item(Material.BLUE_ICE, 8), item(Material.NETHERITE_INGOT), custom(CustomItem.CONDENSED_ICE, 2))
                row(item(Material.LIGHT_BLUE_CANDLE, 16), custom(CustomItem.CONDENSED_ICE, 2), item(Material.MUSIC_DISC_TEARS), custom(CustomItem.CONDENSED_ICE, 2), item(Material.WHITE_CANDLE, 16))
            }
            result(CustomItem.FROZEN_SHARD)
        }
        recipe {
            grid {
                row(custom(CustomItem.CONDENSED_DEEPSLATE), item(Material.ENCHANTED_BOOK).storeEnch("BR4"), item(Material.IRON_BLOCK, 64), item(Material.ENCHANTED_BOOK).storeEnch("DN5"), custom(CustomItem.CONDENSED_DEEPSLATE))
                row(custom(CustomItem.CONDENSED_DEEPSLATE), item(Material.ENCHANTED_BOOK).storeEnch("BR4"), item(Material.COAL_BLOCK, 64), item(Material.ENCHANTED_BOOK).storeEnch("DN5"), custom(CustomItem.CONDENSED_DEEPSLATE))
                row(item(Material.DEEPSLATE_IRON_ORE, 16), item(Material.SKULL_BANNER_PATTERN), item(Material.NETHERITE_AXE).ench("SH5","UN3","MN1"), item(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.DEEPSLATE_IRON_ORE, 16))
                row(null, item(Material.COPPER_CHAIN, 16), item(Material.MUSIC_DISC_RELIC), item(Material.COPPER_CHAIN, 16), null)
                row(null, item(Material.IRON_CHAIN, 16), custom(CustomItem.REINFORCED_HANDLE, 4), item(Material.IRON_CHAIN, 16), null)
            }
            result(CustomItem.HEAVY_GREATHAMMER)
        }
        recipe {
            grid {
                row(null, item(Material.NETHERITE_SCRAP, 2), item(Material.IRON_BLOCK, 32), item(Material.COPPER_BLOCK, 32), item(Material.ANVIL, 16))
                row(null, item(Material.RABBIT_HIDE, 16), item(Material.GOLD_BLOCK, 32), item(Material.HEAVY_CORE), item(Material.COPPER_BLOCK, 32))
                row(null, item(Material.LEATHER, 16), item(Material.NETHERITE_AXE).ench("SH5","UN3","MN1"), item(Material.GOLD_BLOCK, 32), item(Material.IRON_BLOCK, 32))
                row(item(Material.LEATHER, 16), custom(CustomItem.REINFORCED_HANDLE, 4), item(Material.LEATHER, 16), item(Material.RABBIT_HIDE, 16), item(Material.NETHERITE_SCRAP, 2))
                row(item(Material.BLADE_POTTERY_SHERD), item(Material.LEATHER, 16), null, null, null)
            }
            result(CustomItem.GRAVITY_HAMMER)
        }
        recipe {
            grid {
                row(item(Material.TIPPED_ARROW, 32).setPotion(PotionType.STRONG_HEALING), item(Material.NETHER_WART, 16), item(Material.GLISTERING_MELON_SLICE, 32), item(Material.NETHER_WART, 16), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.STRONG_HEALING))
                row(item(Material.ENCHANTED_BOOK).storeEnch("SM5"), item(Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.SPLASH_POTION).setPotion(PotionType.STRONG_HEALING), item(Material.DANGER_POTTERY_SHERD), item(Material.ENCHANTED_BOOK).storeEnch("TH3"))
                row(item(Material.FERMENTED_SPIDER_EYE, 16), item(Material.SPLASH_POTION).setPotion(PotionType.STRONG_HEALING), item(Material.NETHERITE_AXE).ench("SH5","UN3","MN1"), item(Material.SPLASH_POTION).setPotion(PotionType.STRONG_HEALING), item(Material.FERMENTED_SPIDER_EYE, 16))
                row(item(Material.ENCHANTED_BOOK).storeEnch("TH3"), item(Material.HEARTBREAK_POTTERY_SHERD), item(Material.SPLASH_POTION).setPotion(PotionType.STRONG_HEALING), item(Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.ENCHANTED_BOOK).storeEnch("SM5"))
                row(item(Material.TIPPED_ARROW, 32).setPotion(PotionType.STRONG_HEALING), item(Material.REDSTONE, 16), item(Material.GLISTERING_MELON_SLICE, 32), item(Material.REDSTONE, 16), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.STRONG_HEALING))
            }
            result(CustomItem.AXE_OF_PEACE)
        }
        recipe {
            grid {
                row(item(Material.BLAZE_POWDER, 16), item(Material.END_ROD, 16), item(Material.ENDER_EYE, 16), item(Material.END_ROD, 16), item(Material.BLAZE_ROD, 16))
                row(item(Material.END_ROD, 16), item(Material.CRYING_OBSIDIAN, 32), item(Material.DRAGON_HEAD, 2), item(Material.OBSIDIAN, 32), item(Material.END_ROD, 16))
                row(item(Material.ENDER_EYE, 16), item(Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.NETHERITE_SWORD).ench("SH5","FA2","UN3","MN1"), item(Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.ENDER_EYE, 16))
                row(item(Material.END_ROD, 16), item(Material.OBSIDIAN, 32), custom(CustomItem.REINFORCED_HANDLE, 2), item(Material.CRYING_OBSIDIAN, 32), item(Material.END_ROD, 16))
                row(item(Material.BLAZE_ROD, 16), item(Material.END_ROD, 16), item(Material.ENDER_EYE, 16), item(Material.END_ROD, 16), item(Material.BLAZE_POWDER, 16))
            }
            result(CustomItem.ENDER_BLADE)
        }
        recipe {
            grid {
                row(item(Material.CLOSED_EYEBLOSSOM, 32), custom(CustomItem.CONDENSED_DEEPSLATE), custom(CustomItem.WARDEN_CARAPACE), custom(CustomItem.CONDENSED_DEEPSLATE), item(Material.SCULK, 64))
                row(custom(CustomItem.CONDENSED_DEEPSLATE), item(Material.COAL_BLOCK, 32), custom(CustomItem.STEEL_PLATING, 2), item(Material.COAL_BLOCK, 32), custom(CustomItem.CONDENSED_DEEPSLATE))
                row(item(Material.CRYING_OBSIDIAN, 64), custom(CustomItem.STEEL_PLATING, 2), item(Material.NETHERITE_SWORD).ench("SH5","FA2","UN3","MN1"), custom(CustomItem.STEEL_PLATING, 2), item(Material.CRYING_OBSIDIAN, 64))
                row(custom(CustomItem.CONDENSED_DEEPSLATE), item(Material.OBSIDIAN, 64), custom(CustomItem.REINFORCED_HANDLE, 2), item(Material.OBSIDIAN, 64), custom(CustomItem.CONDENSED_DEEPSLATE))
                row(item(Material.SCULK, 64), custom(CustomItem.CONDENSED_DEEPSLATE), custom(CustomItem.FRAGMENT_OF_SOUND, 4), custom(CustomItem.CONDENSED_DEEPSLATE), item(Material.CLOSED_EYEBLOSSOM, 32))
            }
            result(CustomItem.DARK_STEEL_RAPIER)
        }
        recipe {
            grid {
                row(null, item(Material.SWEET_BERRIES, 32), custom(CustomItem.CONDENSED_DEEPSLATE), item(Material.WITHER_ROSE, 16), null)
                row(item(Material.SWEET_BERRIES, 32), item(Material.SUSPICIOUS_STEW), custom(CustomItem.WARDEN_CARAPACE), item(Material.SUSPICIOUS_STEW), item(Material.WITHER_ROSE, 16))
                row(custom(CustomItem.CONDENSED_DEEPSLATE), item(Material.ENCHANTED_BOOK).storeEnch("BR4"), item(Material.NETHERITE_SWORD).ench("SH5","FA2","UN3","MN1"), item(Material.ENCHANTED_BOOK).storeEnch("BR4"), custom(CustomItem.CONDENSED_DEEPSLATE))
                row(item(Material.WITHER_ROSE, 16), custom(CustomItem.SHADOW_DISC_CORE), custom(CustomItem.REINFORCED_HANDLE, 4), custom(CustomItem.SHADOW_DISC_CORE), item(Material.SWEET_BERRIES, 32))
                row(null, item(Material.WITHER_ROSE, 16), custom(CustomItem.CONDENSED_DEEPSLATE), item(Material.SWEET_BERRIES, 32), null)
            }
            result(CustomItem.BARBED_BLADE)
        }

    }
}