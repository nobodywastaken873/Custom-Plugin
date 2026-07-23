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
                row(null, null, item(Material.WIND_CHARGE, 16), item(Material.FISHING_ROD).ench("MN1","UN3"), item(Material.IRON_INGOT, 32))
                row(null, null, item(Material.FISHING_ROD).ench("MN1","UN3"), item(Material.LEAD, 32), item(Material.PRISMARINE_SHARD, 32))
                row(null, null, item(Material.NETHERITE_SWORD), null, null)
                row(item(Material.PRISMARINE_SHARD, 32), item(Material.LEAD, 32), item(Material.FISHING_ROD).ench("MN1","UN3"), null, null)
                row(item(Material.IRON_INGOT, 32), item(Material.FISHING_ROD).ench("MN1","UN3"), item(Material.WIND_CHARGE, 16), null, null)
            }
            result(CustomItem.HOOKED_CUTLASS)
            transfer(20)
        }
        recipe {
            grid {
                row(null, null, item(Material.NETHERITE_SCRAP), item(Material.NETHERITE_SCRAP), null)
                row(null, item(Material.NETHERITE_SCRAP), item(Material.NETHERITE_INGOT), null, null)
                row(null, item(Material.NETHERITE_SCRAP), item(Material.NETHERITE_INGOT), null, null)
                row(null, item(Material.NETHERITE_SWORD), null, null, null)
                row(null, null, null, null, null)
            }
            result(CustomItem.NETHERITE_CLAYMORE)
            transfer(28)
        }
        recipe {
            grid {
                row(null, null, item(Material.NETHERITE_SCRAP), null, null)
                row(null, item(Material.NETHERITE_SCRAP), item(Material.NETHERITE_INGOT), item(Material.NETHERITE_SCRAP), null)
                row(null, item(Material.NETHERITE_SCRAP), item(Material.NETHERITE_SWORD), item(Material.NETHERITE_SCRAP), null)
                row(null, null, item(Material.NETHERITE_SCRAP), null, null)
                row(null, null, null, null, null)
            }
            result(CustomItem.NETHERITE_BROADSWORD)
            transfer(20)
        }
        recipe {
            grid {
                row(null, null, item(Material.NETHERITE_SCRAP), null, null)
                row(null, null, item(Material.NETHERITE_SCRAP), null, null)
                row(null, item(Material.NETHERITE_SCRAP), item(Material.NETHERITE_INGOT), item(Material.NETHERITE_SCRAP), null)
                row(null, item(Material.NETHERITE_SCRAP), item(Material.NETHERITE_SPEAR), item(Material.NETHERITE_SCRAP), null)
                row(null, null, null, null, null)
            }
            result(CustomItem.NETHERITE_PIKE)
            transfer(29)
        }
        recipe {
            grid {
                row(null, null, null, item(Material.NETHERITE_SCRAP), null)
                row(null, null, item(Material.NETHERITE_SCRAP), item(Material.NETHERITE_SCRAP), null)
                row(null, null, item(Material.NETHERITE_SCRAP), item(Material.NETHERITE_INGOT), null)
                row(null, null, item(Material.NETHERITE_SPEAR), item(Material.NETHERITE_SCRAP), null)
                row(null, null, null, null, null)
            }
            result(CustomItem.NETHERITE_GLAIVE)
            transfer(29)
        }
        recipe {
            grid {
                row(item(Material.NETHER_BRICK, 32), item(Material.ARROW, 32), item(Material.CARROT_ON_A_STICK), item(Material.STRING, 32), item(Material.BRICK, 32))
                row(item(Material.ARMADILLO_SCUTE, 8), item(Material.SADDLE), item(Material.EXPLORER_POTTERY_SHERD), item(Material.SADDLE), item(Material.ARMADILLO_SCUTE, 8))
                row(item(Material.LEAD, 16), item(Material.ARMS_UP_POTTERY_SHERD), item(Material.CROSSBOW).ench("QC3","PR4","UN3","MN1"), item(Material.SNORT_POTTERY_SHERD), item(Material.LEAD, 16))
                row(item(Material.ARMADILLO_SCUTE, 8), item(Material.SADDLE), item(Material.ARCHER_POTTERY_SHERD), item(Material.SADDLE), item(Material.ARMADILLO_SCUTE, 8))
                row(item(Material.BRICK, 32), item(Material.STRING, 32), item(Material.WARPED_FUNGUS_ON_A_STICK), item(Material.ARROW, 32), item(Material.NETHER_BRICK, 32))
            }
            result(CustomItem.RIDABLE_CROSSBOW)
        }
        recipe {
            grid {
                row(item(Material.TIPPED_ARROW, 32).setPotion(PotionType.STRONG_HEALING), item(Material.NETHER_WART, 16), item(Material.GLISTERING_MELON_SLICE, 32), item(Material.NETHER_WART, 16), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.STRONG_HEALING))
                row(item(Material.ENCHANTED_BOOK).storeEnch("SM5"), item(Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.SPLASH_POTION).setPotion(PotionType.STRONG_HEALING), item(Material.DANGER_POTTERY_SHERD), item(Material.ENCHANTED_BOOK).storeEnch("TH3"))
                row(item(Material.FERMENTED_SPIDER_EYE, 16), item(Material.SPLASH_POTION).setPotion(PotionType.STRONG_HEALING), item(Material.NETHERITE_AXE).ench("SH5","MN1","UN3"), item(Material.SPLASH_POTION).setPotion(PotionType.STRONG_HEALING), item(Material.FERMENTED_SPIDER_EYE, 16))
                row(item(Material.ENCHANTED_BOOK).storeEnch("TH3"), item(Material.HEARTBREAK_POTTERY_SHERD), item(Material.SPLASH_POTION).setPotion(PotionType.STRONG_HEALING), item(Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.ENCHANTED_BOOK).storeEnch("SM5"))
                row(item(Material.TIPPED_ARROW, 32).setPotion(PotionType.STRONG_HEALING), item(Material.REDSTONE, 16), item(Material.GLISTERING_MELON_SLICE, 32), item(Material.REDSTONE, 16), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.STRONG_HEALING))
            }
            result(CustomItem.AXE_OF_PEACE)
            transfer(20)
        }
        recipe {
            grid {
                row(item(Material.BLAZE_POWDER, 32), item(Material.END_ROD, 32), item(Material.ENDER_EYE, 32), item(Material.END_ROD, 32), item(Material.BLAZE_ROD, 32))
                row(item(Material.END_ROD, 32), item(Material.CRYING_OBSIDIAN, 32), item(Material.DRAGON_HEAD, 2), item(Material.OBSIDIAN, 32), item(Material.END_ROD, 32))
                row(item(Material.ENDER_EYE, 32), item(Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.NETHERITE_SWORD).ench("FA2","UN3","SH5","MN1"), item(Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.ENDER_EYE, 32))
                row(item(Material.END_ROD, 32), item(Material.OBSIDIAN, 32), item(Material.DRAGON_HEAD, 2), item(Material.CRYING_OBSIDIAN, 32), item(Material.END_ROD, 32))
                row(item(Material.BLAZE_ROD, 32), item(Material.END_ROD, 32), item(Material.ENDER_EYE, 32), item(Material.END_ROD, 32), item(Material.BLAZE_POWDER, 32))
            }
            result(CustomItem.ENDER_BLADE)
            transfer(20)
        }
        recipe {
            grid {
                row(item(Material.WIND_CHARGE, 32), item(Material.COPPER_CHAIN, 16), item(Material.COBWEB, 32), item(Material.COPPER_CHAIN, 16), item(Material.STRING, 32))
                row(item(Material.LEAD, 32), item(Material.HONEY_BLOCK, 16), item(Material.PUFFERFISH_BUCKET), item(Material.SLIME_BLOCK, 16), item(Material.LEAD, 32))
                row(item(Material.IRON_CHAIN, 32), item(Material.PUFFERFISH_BUCKET), item(Material.NETHERITE_SWORD).ench("FA2","UN3","SH5","MN1"), item(Material.PUFFERFISH_BUCKET), item(Material.IRON_CHAIN, 32))
                row(item(Material.LEAD, 32), item(Material.SLIME_BLOCK, 16), item(Material.PUFFERFISH_BUCKET), item(Material.HONEY_BLOCK, 16), item(Material.LEAD, 32))
                row(item(Material.STRING, 32), item(Material.COPPER_CHAIN, 16), item(Material.COBWEB, 32), item(Material.COPPER_CHAIN, 16), item(Material.WIND_CHARGE, 32))
            }
            result(CustomItem.TETHERING_SICKLE)
            transfer(20)
        }
        recipe {
            grid {
                row(null, item(Material.NETHERITE_SCRAP, 2), item(Material.IRON_BLOCK, 16), item(Material.COPPER_BLOCK, 32), item(Material.ANVIL, 16))
                row(null, item(Material.RABBIT_HIDE, 16), item(Material.GOLD_BLOCK, 32), item(Material.HEAVY_CORE), item(Material.COPPER_BLOCK, 32))
                row(null, item(Material.LEATHER, 16), item(Material.NETHERITE_AXE).ench("SH5","MN1","UN3"), item(Material.GOLD_BLOCK, 32), item(Material.IRON_BLOCK, 16))
                row(item(Material.LEATHER, 16), custom(CustomItem.REINFORCED_HANDLE, 2), item(Material.LEATHER, 16), item(Material.RABBIT_HIDE, 16), item(Material.NETHERITE_SCRAP, 2))
                row(item(Material.BLADE_POTTERY_SHERD), item(Material.LEATHER, 16), null, null, null)
            }
            result(CustomItem.GRAVITY_HAMMER)
            transfer(20)
        }
        recipe {
            grid {
                row(item(Material.WHITE_CANDLE, 16), custom(CustomItem.CONDENSED_ICE), item(Material.MUSIC_DISC_TEARS), custom(CustomItem.CONDENSED_ICE), item(Material.LIGHT_BLUE_CANDLE, 16))
                row(custom(CustomItem.CONDENSED_ICE), item(Material.NETHERITE_INGOT), item(Material.BLUE_ICE, 16), item(Material.SKELETON_SKULL, 2), custom(CustomItem.CONDENSED_ICE))
                row(item(Material.TIPPED_ARROW, 64).setPotion(PotionType.STRONG_TURTLE_MASTER), item(Material.BLUE_ICE, 16), item(Material.NETHERITE_SWORD).ench("FA2","UN3","SH5","MN1"), item(Material.BLUE_ICE, 16), item(Material.TIPPED_ARROW, 64).setPotion(PotionType.STRONG_TURTLE_MASTER))
                row(custom(CustomItem.CONDENSED_ICE), item(Material.SKELETON_SKULL, 2), item(Material.BLUE_ICE, 16), item(Material.NETHERITE_INGOT), custom(CustomItem.CONDENSED_ICE))
                row(item(Material.LIGHT_BLUE_CANDLE, 16), custom(CustomItem.CONDENSED_ICE), item(Material.MUSIC_DISC_TEARS), custom(CustomItem.CONDENSED_ICE), item(Material.WHITE_CANDLE, 16))
            }
            result(CustomItem.FROZEN_SHARD)
            transfer(20)
        }
        recipe {
            grid {
                row(item(Material.FEATHER, 32), item(Material.PAPER, 32), item(Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.PAPER, 32), item(Material.QUARTZ, 32))
                row(item(Material.SUGAR, 16), custom(CustomItem.ENRICHED_FEATHER), item(Material.GOAT_HORN).goatHorn(MusicInstrument.DREAM_GOAT_HORN), custom(CustomItem.ENRICHED_FEATHER), item(Material.SUGAR, 16))
                row(item(Material.SUGAR, 16), item(Material.GOAT_HORN).goatHorn(MusicInstrument.CALL_GOAT_HORN), item(Material.NETHERITE_SWORD).ench("FA2","UN3","SH5","MN1"), item(Material.GOAT_HORN).goatHorn(MusicInstrument.YEARN_GOAT_HORN), item(Material.SUGAR, 16))
                row(item(Material.SUGAR, 16), custom(CustomItem.ENRICHED_FEATHER), item(Material.GOAT_HORN).goatHorn(MusicInstrument.ADMIRE_GOAT_HORN), custom(CustomItem.ENRICHED_FEATHER), item(Material.SUGAR, 16))
                row(item(Material.QUARTZ, 32), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.WIND_CHARGED), item(Material.ENCHANTED_BOOK).storeEnch("WB1"), item(Material.TIPPED_ARROW, 32).setPotion(PotionType.WIND_CHARGED), item(Material.FEATHER, 32))
            }
            result(CustomItem.GALE_BLADE)
            transfer(20)
        }
        recipe {
            grid {
                row(item(Material.SPYGLASS), item(Material.ENDER_EYE, 16), item(Material.FIREWORK_ROCKET, 64).firework(3), item(Material.ENDER_EYE, 16), item(Material.SPYGLASS))
                row(item(Material.FIREWORK_STAR, 32), item(Material.FIRE_CHARGE, 16), item(Material.GUNPOWDER, 32), item(Material.FIRE_CHARGE, 16), item(Material.FIREWORK_STAR, 32))
                row(item(Material.FIREWORK_ROCKET, 64).firework(3), item(Material.CROSSBOW).ench("QC3","PR4","UN3","MN1"), item(Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.CROSSBOW).ench("QC3","PR4","UN3","MN1"), item(Material.FIREWORK_ROCKET, 64).firework(3))
                row(item(Material.FIREWORK_STAR, 32), item(Material.FIRE_CHARGE, 16), item(Material.GUNPOWDER, 32), item(Material.FIRE_CHARGE, 16), item(Material.FIREWORK_STAR, 32))
                row(item(Material.SPYGLASS), item(Material.ENDER_EYE, 16), item(Material.FIREWORK_ROCKET, 64).firework(3), item(Material.ENDER_EYE, 16), item(Material.SPYGLASS))
            }
            result(CustomItem.RPG_LAUNCHER)
        }
        recipe {
            grid {
                row(item(Material.FIREWORK_ROCKET, 64).firework(1), item(Material.FIREWORK_ROCKET, 64).firework(1), null, null, null)
                row(item(Material.CROSSBOW).ench("QC3","PR4","UN3","MN1"), item(Material.IRON_BLOCK, 8), item(Material.IRON_BLOCK, 8), item(Material.IRON_BLOCK, 8), custom(CustomItem.WITHER_SKULL_ARROW, 2))
                row(item(Material.IRON_BLOCK, 8), item(Material.TNT, 64), item(Material.TNT_MINECART), item(Material.TNT_MINECART), item(Material.TNT_MINECART))
                row(item(Material.CROSSBOW).ench("QC3","PR4","UN3","MN1"), item(Material.IRON_BLOCK, 8), item(Material.IRON_BLOCK, 8), item(Material.IRON_BLOCK, 8), custom(CustomItem.WITHER_SKULL_ARROW, 2))
                row(item(Material.FIREWORK_ROCKET, 64).firework(1), item(Material.FIREWORK_ROCKET, 64).firework(1), null, null, null)
            }
            result(CustomItem.SURFACE_TO_AIR_MISSILE)
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
                row(item(Material.FIRE_CHARGE, 16), item(Material.IRON_INGOT, 32), item(Material.IRON_CHAIN, 16), item(Material.IRON_INGOT, 32), item(Material.FIRE_CHARGE, 16))
                row(item(Material.IRON_INGOT, 32), custom(CustomItem.STEEL_PLATING), item(Material.MUSIC_DISC_PIGSTEP), custom(CustomItem.STEEL_PLATING), item(Material.IRON_INGOT, 32))
                row(item(Material.IRON_CHAIN, 16), item(Material.GUNPOWDER, 64), item(Material.CROSSBOW).ench("QC3","MN1","UN3"), item(Material.GUNPOWDER, 64), item(Material.IRON_CHAIN, 16))
                row(item(Material.IRON_INGOT, 32), item(Material.PIGLIN_BANNER_PATTERN), item(Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).checkOriginal(), item(Material.PIGLIN_BANNER_PATTERN), item(Material.IRON_INGOT, 32))
                row(item(Material.FIRE_CHARGE, 16), item(Material.IRON_INGOT, 32), item(Material.IRON_CHAIN, 16), item(Material.IRON_INGOT, 32), item(Material.FIRE_CHARGE, 16))
            }
            result(CustomItem.PORTABLE_CANNON)
        }
        recipe {
            grid {
                row(item(Material.CLOSED_EYEBLOSSOM, 32), custom(CustomItem.CONDENSED_DEEPSLATE), custom(CustomItem.WARDEN_CARAPACE), custom(CustomItem.CONDENSED_DEEPSLATE), item(Material.SCULK, 64))
                row(custom(CustomItem.CONDENSED_DEEPSLATE), item(Material.COAL_BLOCK, 32), custom(CustomItem.STEEL_PLATING), item(Material.COAL_BLOCK, 32), custom(CustomItem.CONDENSED_DEEPSLATE))
                row(item(Material.CRYING_OBSIDIAN, 64), custom(CustomItem.STEEL_PLATING), item(Material.NETHERITE_SWORD).ench("UN3","MN1","FA2","SH5"), custom(CustomItem.STEEL_PLATING), item(Material.CRYING_OBSIDIAN, 64))
                row(custom(CustomItem.CONDENSED_DEEPSLATE), item(Material.OBSIDIAN, 64), custom(CustomItem.REINFORCED_HANDLE), item(Material.OBSIDIAN, 64), custom(CustomItem.CONDENSED_DEEPSLATE))
                row(item(Material.SCULK, 64), custom(CustomItem.CONDENSED_DEEPSLATE), custom(CustomItem.FRAGMENT_OF_SOUND, 2), custom(CustomItem.CONDENSED_DEEPSLATE), item(Material.CLOSED_EYEBLOSSOM, 32))
            }
            result(CustomItem.DARK_STEEL_RAPIER)
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



    }
}