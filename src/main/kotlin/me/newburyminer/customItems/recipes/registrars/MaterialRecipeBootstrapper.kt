package me.newburyminer.customItems.recipes.registrars

import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.recipes.RecipeBootstrapper
import org.bukkit.Material
import org.bukkit.potion.PotionType

object MaterialRecipeBootstrapper: RecipeBootstrapper {
    override fun bootstrap() {
        recipe {
            grid {
                row(null, null, null, null, null)
                row(null, item(Material.TOTEM_OF_UNDYING), item(Material.TOTEM_OF_UNDYING), item(Material.TOTEM_OF_UNDYING), null)
                row(null, item(Material.TOTEM_OF_UNDYING), item(Material.TOTEM_OF_UNDYING), item(Material.TOTEM_OF_UNDYING), null)
                row(null, item(Material.TOTEM_OF_UNDYING), item(Material.TOTEM_OF_UNDYING), item(Material.TOTEM_OF_UNDYING), null)
                row(null, null, null, null, null)
            }
            result(CustomItem.TOTEM_CORE)
        }
        recipe {
            grid {
                row(item(Material.WHITE_DYE, 4), item(Material.LIGHT_GRAY_DYE, 4), item(Material.GRAY_DYE, 4), item(Material.BLACK_DYE, 4), null)
                row(item(Material.YELLOW_DYE, 4), item(Material.ORANGE_DYE, 4), item(Material.RED_DYE, 4), item(Material.BROWN_DYE, 4), null)
                row(item(Material.LIME_DYE, 4), item(Material.GREEN_DYE, 4), item(Material.CYAN_DYE, 4), item(Material.LIGHT_BLUE_DYE, 4), null)
                row(item(Material.PINK_DYE, 4), item(Material.MAGENTA_DYE, 4), item(Material.PURPLE_DYE, 4), item(Material.BLUE_DYE, 4), null)
                row(null, null, null, null, null)
            }
            result(CustomItem.DYE_PALETTE)
        }
        recipe {
            grid {
                row(null, item(Material.MUSIC_DISC_CHIRP), item(Material.MUSIC_DISC_BLOCKS), item(Material.MUSIC_DISC_13), null)
                row(null, item(Material.MUSIC_DISC_WARD), item(Material.MUSIC_DISC_CAT), item(Material.MUSIC_DISC_FAR), null)
                row(null, item(Material.MUSIC_DISC_WAIT), item(Material.MUSIC_DISC_MALL), item(Material.MUSIC_DISC_MELLOHI), null)
                row(null, item(Material.MUSIC_DISC_STRAD), item(Material.MUSIC_DISC_STAL), item(Material.MUSIC_DISC_11), null)
                row(null, null, null, null, null)
            }
            result(CustomItem.MEGAMIX_DISC_TRACK)
        }
        recipe {
            grid {
                row(null, null, null, null, null)
                row(null, item(Material.MUSIC_DISC_CAT), item(Material.MUSIC_DISC_OTHERSIDE), item(Material.MUSIC_DISC_13), null)
                row(null, item(Material.JUKEBOX, 4), item(Material.MUSIC_DISC_5), item(Material.JUKEBOX, 4), null)
                row(null, item(Material.MUSIC_DISC_13), item(Material.MUSIC_DISC_OTHERSIDE), item(Material.MUSIC_DISC_CAT), null)
                row(null, null, null, null, null)
            }
            result(CustomItem.SHADOW_DISC_CORE)
        }
        recipe {
            grid {
                row(null, item(Material.SCULK, 16), item(Material.DISC_FRAGMENT_5, 2), item(Material.SCULK, 16), null)
                row(item(Material.SCULK, 16), custom(CustomItem.FRAGMENT_OF_SOUND), item(Material.ECHO_SHARD, 2), custom(CustomItem.FRAGMENT_OF_SOUND), item(Material.SCULK, 16))
                row(item(Material.DISC_FRAGMENT_5, 2), item(Material.ECHO_SHARD, 2), custom(CustomItem.WARDEN_HEART), item(Material.ECHO_SHARD, 2), item(Material.DISC_FRAGMENT_5, 2))
                row(item(Material.SCULK, 16), custom(CustomItem.FRAGMENT_OF_SOUND), item(Material.ECHO_SHARD, 2), custom(CustomItem.FRAGMENT_OF_SOUND), item(Material.SCULK, 16))
                row(null, item(Material.SCULK, 16), item(Material.DISC_FRAGMENT_5, 2), item(Material.SCULK, 16), null)
            }
            result(CustomItem.WARDEN_CARAPACE)
        }
        recipe {
            grid {
                row(null, null, null, null, null)
                row(null, item(Material.COBBLED_DEEPSLATE, 32), item(Material.COBBLED_DEEPSLATE, 32), item(Material.COBBLED_DEEPSLATE, 32), null)
                row(null, item(Material.COBBLED_DEEPSLATE, 32), item(Material.COBBLED_DEEPSLATE, 32), item(Material.COBBLED_DEEPSLATE, 32), null)
                row(null, item(Material.COBBLED_DEEPSLATE, 32), item(Material.COBBLED_DEEPSLATE, 32), item(Material.COBBLED_DEEPSLATE, 32), null)
                row(null, null, null, null, null)
            }
            result(CustomItem.CONDENSED_DEEPSLATE)
        }
        recipe {
            grid {
                row(null, null, null, null, null)
                row(null, item(Material.SPLASH_POTION).setPotion(PotionType.LONG_INVISIBILITY), item(Material.SPLASH_POTION).setPotion(PotionType.LONG_INVISIBILITY), item(Material.SPLASH_POTION).setPotion(PotionType.LONG_INVISIBILITY), null)
                row(null, item(Material.SPLASH_POTION).setPotion(PotionType.LONG_INVISIBILITY), item(Material.SPLASH_POTION).setPotion(PotionType.LONG_INVISIBILITY), item(Material.SPLASH_POTION).setPotion(PotionType.LONG_INVISIBILITY), null)
                row(null, item(Material.SPLASH_POTION).setPotion(PotionType.LONG_INVISIBILITY), item(Material.SPLASH_POTION).setPotion(PotionType.LONG_INVISIBILITY), item(Material.SPLASH_POTION).setPotion(PotionType.LONG_INVISIBILITY), null)
                row(null, null, null, null, null)
            }
            result(CustomItem.CONDENSED_INVISIBILITY)
        }
        recipe {
            grid {
                row(item(Material.WHITE_DYE), item(Material.WHITE_DYE), item(Material.WHITE_DYE), item(Material.WHITE_DYE), item(Material.WHITE_DYE))
                row(item(Material.WHITE_DYE), item(Material.SNOWBALL, 4), item(Material.FEATHER, 2), item(Material.SNOWBALL, 4), item(Material.WHITE_DYE))
                row(item(Material.WHITE_DYE), item(Material.WIND_CHARGE, 2), item(Material.PHANTOM_MEMBRANE), item(Material.WIND_CHARGE, 2), item(Material.WHITE_DYE))
                row(item(Material.WHITE_DYE), item(Material.SNOWBALL, 4), item(Material.FEATHER, 2), item(Material.SNOWBALL, 4), item(Material.WHITE_DYE))
                row(item(Material.WHITE_DYE), item(Material.WHITE_DYE), item(Material.WHITE_DYE), item(Material.WHITE_DYE), item(Material.WHITE_DYE))
            }
            result(CustomItem.CLOUD_FRAGMENT)
        }
        recipe {
            grid {
                row(null, item(Material.PACKED_ICE, 2), item(Material.POWDER_SNOW_BUCKET), item(Material.PACKED_ICE, 2), null)
                row(item(Material.PACKED_ICE, 2), item(Material.SNOW_BLOCK, 2), item(Material.SNOW_BLOCK, 2), item(Material.SNOW_BLOCK, 2), item(Material.PACKED_ICE, 2))
                row(item(Material.POWDER_SNOW_BUCKET), item(Material.SNOW_BLOCK, 2), item(Material.SPLASH_POTION).setPotion(PotionType.STRONG_TURTLE_MASTER), item(Material.SNOW_BLOCK, 2), item(Material.POWDER_SNOW_BUCKET))
                row(item(Material.PACKED_ICE, 2), item(Material.SNOW_BLOCK, 2), item(Material.SNOW_BLOCK, 2), item(Material.SNOW_BLOCK, 2), item(Material.PACKED_ICE, 2))
                row(null, item(Material.PACKED_ICE, 2), item(Material.POWDER_SNOW_BUCKET), item(Material.PACKED_ICE, 2), null)
            }
            result(CustomItem.CONDENSED_ICE)
        }
        recipe {
            grid {
                row(item(Material.CHARCOAL, 4), item(Material.COAL_ORE, 2), item(Material.DEEPSLATE_IRON_ORE, 2), item(Material.IRON_ORE, 2), item(Material.CHARCOAL, 4))
                row(item(Material.IRON_ORE, 2), item(Material.COAL_BLOCK, 4), item(Material.IRON_BLOCK, 8), item(Material.COAL_BLOCK, 4), item(Material.COAL_ORE, 2))
                row(item(Material.DEEPSLATE_IRON_ORE, 2), item(Material.IRON_BLOCK, 8), item(Material.RAW_IRON_BLOCK, 8), item(Material.IRON_BLOCK, 8), item(Material.DEEPSLATE_IRON_ORE, 2))
                row(item(Material.COAL_ORE, 2), item(Material.COAL_BLOCK, 4), item(Material.IRON_BLOCK, 8), item(Material.COAL_BLOCK, 4), item(Material.IRON_ORE, 2))
                row(item(Material.CHARCOAL, 4), item(Material.IRON_ORE, 2), item(Material.DEEPSLATE_IRON_ORE, 2), item(Material.COAL_ORE, 2), item(Material.CHARCOAL, 4))
            }
            result(CustomItem.STEEL_CHUNK)
        }
        recipe {
            grid {
                row(null, item(Material.FIRE_CHARGE, 2), item(Material.LAVA_BUCKET), item(Material.FIRE_CHARGE, 2), null)
                row(item(Material.FIRE_CHARGE, 2), item(Material.BLAZE_ROD, 4), item(Material.MAGMA_BLOCK, 16), item(Material.BLAZE_ROD, 4), item(Material.FIRE_CHARGE, 2))
                row(item(Material.LAVA_BUCKET), item(Material.MAGMA_BLOCK, 16), item(Material.MAGMA_CREAM, 8), item(Material.MAGMA_BLOCK, 16), item(Material.LAVA_BUCKET))
                row(item(Material.FIRE_CHARGE, 2), item(Material.BLAZE_ROD, 4), item(Material.MAGMA_BLOCK, 16), item(Material.BLAZE_ROD, 4), item(Material.FIRE_CHARGE, 2))
                row(null, item(Material.FIRE_CHARGE, 2), item(Material.LAVA_BUCKET), item(Material.FIRE_CHARGE, 2), null)
            }
            result(CustomItem.MOLTEN_MIXTURE)
        }
        recipe {
            grid {
                row(null, null, item(Material.COPPER_CHAIN, 4), null, null)
                row(null, item(Material.RABBIT_HIDE), item(Material.STICK, 16), item(Material.RABBIT_HIDE), null)
                row(item(Material.COPPER_CHAIN, 4), item(Material.STICK, 16), item(Material.LEATHER, 8), item(Material.STICK, 16), item(Material.COPPER_CHAIN, 4))
                row(null, item(Material.RABBIT_HIDE), item(Material.STICK, 16), item(Material.RABBIT_HIDE), null)
                row(null, null, item(Material.COPPER_CHAIN, 4), null, null)
            }
            result(CustomItem.HANDLE_BINDING)
        }
        recipe {
            grid {
                row(null, item(Material.LIGHTNING_ROD, 2), item(Material.BREEZE_ROD, 4), item(Material.LIGHTNING_ROD, 2), null)
                row(null, item(Material.LIGHTNING_ROD, 2), item(Material.BREEZE_ROD, 4), item(Material.LIGHTNING_ROD, 2), null)
                row(null, item(Material.IRON_INGOT, 16), item(Material.IRON_INGOT, 16), item(Material.IRON_INGOT, 16), null)
                row(null, item(Material.IRON_CHAIN, 8), item(Material.BLAZE_ROD, 4), item(Material.IRON_CHAIN, 8), null)
                row(null, item(Material.IRON_CHAIN, 8), item(Material.BLAZE_ROD, 4), item(Material.IRON_CHAIN, 8), null)
            }
            result(CustomItem.STRENGTHENING_RODS)
        }
        recipe {
            grid {
                row(null, null, null, null, null)
                row(null, item(Material.GLOW_INK_SAC, 8), item(Material.HONEY_BOTTLE, 8), item(Material.BLAZE_POWDER, 8), null)
                row(null, item(Material.GOLD_INGOT, 16), item(Material.ENCHANTED_GOLDEN_APPLE), item(Material.GOLD_INGOT, 16), null)
                row(null, item(Material.BLAZE_POWDER, 8), item(Material.HONEY_BOTTLE, 8), item(Material.GLOW_INK_SAC, 8), null)
                row(null, null, null, null, null)
            }
            result(CustomItem.MAGICAL_FLASK)
        }
        recipe {
            grid {
                row(null, item(Material.BONE_MEAL, 4), item(Material.STRING, 8), item(Material.BONE_MEAL, 4), null)
                row(item(Material.BONE_MEAL, 4), item(Material.AMETHYST_SHARD, 4), item(Material.SUGAR, 16), item(Material.AMETHYST_SHARD, 4), item(Material.BONE_MEAL, 4))
                row(item(Material.STRING, 8), item(Material.SUGAR, 16), item(Material.BREEZE_ROD, 8), item(Material.SUGAR, 16), item(Material.STRING, 8))
                row(item(Material.BONE_MEAL, 4), item(Material.AMETHYST_SHARD, 4), item(Material.SUGAR, 16), item(Material.AMETHYST_SHARD, 4), item(Material.BONE_MEAL, 4))
                row(null, item(Material.BONE_MEAL, 4), item(Material.STRING, 8), item(Material.BONE_MEAL, 4), null)
            }
            result(CustomItem.LIGHTWEIGHT_AMALGAMATION)
        }
    }
}