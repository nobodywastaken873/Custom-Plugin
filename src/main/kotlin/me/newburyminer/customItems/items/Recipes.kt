package me.newburyminer.customItems.items

import me.newburyminer.customItems.Utils.Companion.basePotion
import me.newburyminer.customItems.Utils.Companion.ench
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.omimous
import me.newburyminer.customItems.Utils.Companion.storeEnch
import me.newburyminer.customItems.Utils.Companion.useEnch
import me.newburyminer.customItems.Utils.Companion.useOminous
import me.newburyminer.customItems.Utils.Companion.useOriginal
import me.newburyminer.customItems.Utils.Companion.usePotion
import me.newburyminer.customItems.Utils.Companion.useStoredEnch
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.*
import org.bukkit.potion.PotionType

class Recipes {
    companion object {
         private val craftSlots = arrayOf(
            arrayOf(1, 2, 3, 4, 5,),
            arrayOf(10,11,12,13,14),
            arrayOf(19,20,21,22,23),
            arrayOf(28,29,30,31,32),
            arrayOf(37,38,39,40,41),
        )
        //add tags onto items in menu to say check for nbt or not
        //ie item.addTag("checkEnchants", true)
        //by default the tag will not exist and will therefore be null
        //advancement + nbt check
        fun checkForRecipe(grid: Inventory): Recipe? {
            val itemGrid = mutableListOf<MutableList<ItemStack?>>()
            for (row in 0..4) {
                itemGrid.add(mutableListOf())
                for (slot in craftSlots[row]) {
                    itemGrid[row].add(grid.getItem(slot))
                }
            }
            for (recipe in recipes) {
                var completed = true
                for (row in 0..4) {
                    for (col in 0..4) {
                        val tableItem = itemGrid[row][col]
                        val recipeItem = recipe.items[row][col]
                        if (tableItem == null && recipeItem == null) continue
                        if ((tableItem == null) xor (recipeItem == null)) {completed = false; break}
                        if (tableItem!!.type != recipeItem!!.type) {completed = false; break}
                        if (recipeItem.getTag<Int>("id") != tableItem.getTag<Int>("id")) {completed = false; break}
                        if (tableItem.amount < recipeItem.amount) {completed = false; break}
                        if (recipeItem.getTag<Boolean>("checkenchant") == true) {
                            for (enchant in recipeItem.enchantments.keys) {
                                if (recipeItem.enchantments[enchant] != tableItem.enchantments[enchant]) {
                                    completed = false; break
                                }
                            }
                        }
                        if (recipeItem.getTag<Boolean>("checkstoredenchant") == true) {
                            if (tableItem.itemMeta !is EnchantmentStorageMeta) {completed = false; break}
                            if (!(tableItem.itemMeta as EnchantmentStorageMeta).hasStoredEnchants() || !(recipeItem.itemMeta as EnchantmentStorageMeta).hasStoredEnchants()) {completed = false; break}
                            val tableEnchants = tableItem.itemMeta as EnchantmentStorageMeta
                            val recipeEnchants = tableItem.itemMeta as EnchantmentStorageMeta
                            for (enchant in recipeEnchants.storedEnchants.keys) {
                                if (recipeEnchants.storedEnchants[enchant] != tableEnchants.storedEnchants[enchant]) {
                                    completed = false; break
                                }
                            }
                        }
                        if (recipeItem.getTag<Boolean>("checktrim") == true) {
                            if (tableItem.itemMeta !is ArmorMeta) {completed = false; break}
                            if (!(tableItem.itemMeta as ArmorMeta).hasTrim() || !(recipeItem.itemMeta as ArmorMeta).hasTrim()) {completed = false; break}
                            val tableTrim = (tableItem.itemMeta as ArmorMeta).trim
                            val recipeTrim = (recipeItem.itemMeta as ArmorMeta).trim
                            if (tableTrim?.pattern != recipeTrim?.pattern || tableTrim?.material != recipeTrim?.material) {completed = false; break}
                        }
                        if (recipeItem.getTag<Boolean>("checkominous") == true) {
                            if (tableItem.itemMeta !is OminousBottleMeta) {completed = false; break}
                            if (!(tableItem.itemMeta as OminousBottleMeta).hasAmplifier() || !(recipeItem.itemMeta as OminousBottleMeta).hasAmplifier()) {completed = false; break}
                            if ((recipeItem.itemMeta as OminousBottleMeta).amplifier != (tableItem.itemMeta as OminousBottleMeta).amplifier) completed = false
                        }
                        if (recipeItem.getTag<Boolean>("checkpotion") == true) {
                            if (tableItem.itemMeta !is PotionMeta) {completed = false; break}
                            if (!(tableItem.itemMeta as PotionMeta).hasBasePotionType() || !(recipeItem.itemMeta as PotionMeta).hasBasePotionType()) {completed = false; break}
                            if ((recipeItem.itemMeta as PotionMeta).basePotionType != (tableItem.itemMeta as PotionMeta).basePotionType) completed = false
                        }
                        if (recipeItem.getTag<Boolean>("checkhorn") == true) {
                            if (tableItem.itemMeta !is MusicInstrumentMeta) {completed = false; break}
                            //if (!(tableItem.itemMeta as MusicInstrumentMeta).get() || !(recipeItem.itemMeta as MusicInstrumentMeta).hasBasePotionType()) {completed = false; break}
                            if ((recipeItem.itemMeta as MusicInstrumentMeta).instrument != (tableItem.itemMeta as MusicInstrumentMeta).instrument) completed = false
                        }
                        if (recipeItem.getTag<Boolean>("checkoriginal") == true) {
                            if (recipeItem.enchantments[CustomEnchantments.DUPLICATE] != tableItem.enchantments[CustomEnchantments.DUPLICATE]) {
                                completed = false; break
                            }
                        }
                    }
                    if (!completed) break
                }
                if (completed) return recipe
            }
            return null
        }

        private val recipes: MutableList<Recipe> = mutableListOf()

        fun init() {
            /*recipes.add(
                Recipe(arrayOf(
                    arrayOf(Material.TURTLE_HELMET, Material.TOTEM_OF_UNDYING, arrayOf(Material.DRAGON_BREATH, 16), Material.TOTEM_OF_UNDYING, Material.TURTLE_HELMET),
                    arrayOf(Material.TOTEM_OF_UNDYING, arrayOf(Material.EMERALD_ORE, 5), arrayOf(ItemStack(Material.OMINOUS_BOTTLE).omimous(1).useOminous(), 8), arrayOf(Material.EMERALD_ORE, 5), Material.TOTEM_OF_UNDYING),
                    arrayOf(arrayOf(Material.DRAGON_BREATH, 16), arrayOf(ItemStack(Material.OMINOUS_BOTTLE).omimous(4).useOminous(), 8), arrayOf(Material.NETHER_STAR, 2), arrayOf(ItemStack(Material.OMINOUS_BOTTLE).omimous(2).useOminous(), 8), arrayOf(Material.DRAGON_BREATH, 16)),
                    arrayOf(Material.TOTEM_OF_UNDYING, arrayOf(Material.EMERALD_ORE, 5), arrayOf(ItemStack(Material.OMINOUS_BOTTLE).omimous(3).useOminous(), 8), arrayOf(Material.EMERALD_ORE, 5), Material.TOTEM_OF_UNDYING),
                    arrayOf(Material.TURTLE_HELMET, Material.TOTEM_OF_UNDYING, arrayOf(Material.DRAGON_BREATH, 16), Material.TOTEM_OF_UNDYING, Material.TURTLE_HELMET),
                ), CustomItem.JERRY_IDOL)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.LEAD, 16), Material.BAMBOO_RAFT, arrayOf(Material.IRON_BLOCK, 64), Material.BIRCH_BOAT, arrayOf(Material.LEAD, 16)),
                    arrayOf(Material.MANGROVE_BOAT, arrayOf(Material.SHULKER_SHELL, 4), arrayOf(Material.SCULK_CATALYST, 20), arrayOf(Material.SHULKER_SHELL, 4), Material.SPRUCE_BOAT),
                    arrayOf(arrayOf(Material.ACTIVATOR_RAIL, 64), arrayOf(Material.ECHO_SHARD, 20), Material.DEEPSLATE_EMERALD_ORE, arrayOf(Material.ECHO_SHARD, 20), arrayOf(Material.POWERED_RAIL, 64)),
                    arrayOf(Material.DARK_OAK_BOAT, arrayOf(Material.SHULKER_SHELL, 4), arrayOf(Material.COBWEB, 64), arrayOf(Material.SHULKER_SHELL, 4), Material.JUNGLE_BOAT),
                    arrayOf(arrayOf(Material.LEAD, 16), Material.CHERRY_BOAT, arrayOf(Material.DETECTOR_RAIL, 64), Material.ACACIA_BOAT, arrayOf(Material.LEAD, 16)),
                ), CustomItem.VILLAGER_ATOMIZER)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.ROTTEN_FLESH, 64), arrayOf(Material.RAW_GOLD_BLOCK, 8), arrayOf(Material.FERMENTED_SPIDER_EYE, 64), arrayOf(Material.RAW_GOLD_BLOCK, 8), arrayOf(Material.ROTTEN_FLESH, 64)),
                    arrayOf(arrayOf(Material.RAW_GOLD_BLOCK, 8), arrayOf(Material.GOLD_ORE, 64), arrayOf(Material.ENCHANTED_GOLDEN_APPLE, 2), arrayOf(Material.DEEPSLATE_GOLD_ORE, 64), arrayOf(Material.RAW_GOLD_BLOCK, 8)),
                    arrayOf(arrayOf(Material.GOLDEN_APPLE, 64), arrayOf(Material.ENCHANTED_GOLDEN_APPLE, 2), arrayOf(Material.ZOMBIE_HEAD, 5), arrayOf(Material.ENCHANTED_GOLDEN_APPLE, 2), arrayOf(Material.GOLDEN_APPLE, 64)),
                    arrayOf(arrayOf(Material.RAW_GOLD_BLOCK, 8), arrayOf(Material.GILDED_BLACKSTONE, 64), arrayOf(Material.ENCHANTED_GOLDEN_APPLE, 2), arrayOf(Material.NETHER_GOLD_ORE, 64), arrayOf(Material.RAW_GOLD_BLOCK, 8)),
                    arrayOf(arrayOf(Material.ROTTEN_FLESH, 64), arrayOf(Material.RAW_GOLD_BLOCK, 8), arrayOf(Material.BLAZE_ROD, 64), arrayOf(Material.RAW_GOLD_BLOCK, 8), arrayOf(Material.ROTTEN_FLESH, 64)),
                ), CustomItem.GOLDEN_ZOMBIE)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, Material.TOTEM_OF_UNDYING, Material.TOTEM_OF_UNDYING, Material.TOTEM_OF_UNDYING, null),
                    arrayOf(null, Material.TOTEM_OF_UNDYING, Material.TOTEM_OF_UNDYING, Material.TOTEM_OF_UNDYING, null),
                    arrayOf(null, Material.TOTEM_OF_UNDYING, Material.TOTEM_OF_UNDYING, Material.TOTEM_OF_UNDYING, null),
                    arrayOf(null, null, null, null, null),
                ), CustomItem.TOTEM_CORE)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(ItemStack(Material.CROSSBOW).ench("QC3", "PR4", "MN1", "UN3").useEnch(), arrayOf(Material.FIRE_CHARGE, 64), arrayOf(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, 12), arrayOf(Material.BREEZE_ROD, 64), ItemStack(Material.BOW).ench("PW5", "UN3", "PU2", "FL1", "MN1").useEnch()),
                    arrayOf(Material.GOAT_HORN, arrayOf(Material.ENCHANTED_GOLDEN_APPLE, 10), 6, arrayOf(Material.NETHER_STAR, 6), Material.GOAT_HORN),
                    arrayOf(arrayOf(Material.GLOW_INK_SAC, 16), 6, arrayOf(Material.WITHER_ROSE, 64), 6, arrayOf(Material.GLOW_INK_SAC, 16)),
                    arrayOf(Material.GOAT_HORN, Material.SPYGLASS, 6, Material.SPYGLASS, Material.GOAT_HORN),
                    arrayOf(ItemStack(Material.BOW).ench("PW5", "UN3", "PU2", "FL1", "IN1").useEnch(), arrayOf(Material.BLAZE_ROD, 64), arrayOf(Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, 12), arrayOf(Material.ENDER_EYE, 64), ItemStack(Material.CROSSBOW).ench("QC3", "MS1", "MN1", "UN3").useEnch()),
                ), CustomItem.FANGED_STAFF)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.DRAGON_BREATH, 16), arrayOf(Material.FEATHER, 64), arrayOf(Material.SHULKER_SHELL, 8), arrayOf(Material.FLINT, 32), arrayOf(Material.DRAGON_BREATH, 16)),
                    arrayOf(arrayOf(Material.FLINT, 32), arrayOf(Material.FLETCHING_TABLE, 8), arrayOf(Material.TIPPED_ARROW, 32), arrayOf(Material.FLETCHING_TABLE, 8), arrayOf(Material.FEATHER, 64)),
                    arrayOf(arrayOf(Material.ENDER_PEARL, 16), arrayOf(Material.TIPPED_ARROW, 32), Material.NETHERITE_INGOT, arrayOf(Material.TIPPED_ARROW, 32), arrayOf(Material.WITHER_SKELETON_SKULL, 5)),
                    arrayOf(arrayOf(Material.FEATHER, 64), arrayOf(Material.FLETCHING_TABLE, 8), arrayOf(Material.TIPPED_ARROW, 32), arrayOf(Material.FLETCHING_TABLE, 8), arrayOf(Material.FLINT, 32)),
                    arrayOf(arrayOf(Material.DRAGON_BREATH, 16), arrayOf(Material.FLINT, 32), arrayOf(Material.POINTED_DRIPSTONE, 64), arrayOf(Material.FEATHER, 64), arrayOf(Material.DRAGON_BREATH, 16)),
                ), CustomItem.FLETCHER_UPGRADE)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.BREEZE_ROD, 64), ItemStack(Material.FISHING_ROD).ench("LS3","LR3","UN3","MN1").useEnch(), ItemStack(Material.LEATHER_BOOTS).ench("FF4","PT4","UN3","MN1","DS3","SP3").useEnch(), ItemStack(Material.BOW).ench("PW5", "UN3", "PU2", "FL1", "IN1").useEnch(), arrayOf(Material.BREEZE_ROD, 64)),
                    arrayOf(Material.ELYTRA, ItemStack(Material.GOAT_HORN).horn(MusicInstrument.CALL_GOAT_HORN).useHorn(), ItemStack(Material.GOAT_HORN).horn(MusicInstrument.FEEL_GOAT_HORN).useHorn(), ItemStack(Material.GOAT_HORN).horn(MusicInstrument.SEEK_GOAT_HORN).useHorn(), arrayOf(Material.FEATHER, 64)),
                    arrayOf(arrayOf(Material.STRING, 64), ItemStack(Material.GOAT_HORN).horn(MusicInstrument.SING_GOAT_HORN).useHorn(), arrayOf(Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, 16), ItemStack(Material.GOAT_HORN).horn(MusicInstrument.DREAM_GOAT_HORN).useHorn(), arrayOf(Material.STRING, 64)),
                    arrayOf(arrayOf(Material.FEATHER, 64), ItemStack(Material.GOAT_HORN).horn(MusicInstrument.ADMIRE_GOAT_HORN).useHorn(), ItemStack(Material.GOAT_HORN).horn(MusicInstrument.PONDER_GOAT_HORN).useHorn(), ItemStack(Material.GOAT_HORN).horn(MusicInstrument.YEARN_GOAT_HORN).useHorn(), Material.ELYTRA),
                    arrayOf(arrayOf(Material.BREEZE_ROD, 64), ItemStack(Material.BOW).ench("PW5", "UN3", "PU2", "FL1", "IN1").useEnch(), ItemStack(Material.LEATHER_BOOTS).ench("FF4","PT4","UN3","MN1","DS3","SP3").useEnch(), ItemStack(Material.FISHING_ROD).ench("LS3","LR3","UN3","MN1").useEnch(), arrayOf(Material.BREEZE_ROD, 64)),
                ), CustomItem.WIND_HOOK)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.BREEZE_ROD, 64), ItemStack(Material.FISHING_ROD).ench("LS3","LR3","UN3","MN1").useEnch(), ItemStack(Material.LEATHER_BOOTS).ench("FF4","PT4","UN3","MN1","DS3","SP3").useEnch(), ItemStack(Material.BOW).ench("PW5", "UN3", "PU2", "FL1", "IN1").useEnch(), arrayOf(Material.BREEZE_ROD, 64)),
                    arrayOf(Material.ELYTRA, ItemStack(Material.GOAT_HORN).horn(MusicInstrument.CALL_GOAT_HORN).useHorn(), ItemStack(Material.GOAT_HORN).horn(MusicInstrument.FEEL_GOAT_HORN).useHorn(), ItemStack(Material.GOAT_HORN).horn(MusicInstrument.SEEK_GOAT_HORN).useHorn(), arrayOf(Material.FEATHER, 64)),
                    arrayOf(arrayOf(Material.STRING, 64), ItemStack(Material.GOAT_HORN).horn(MusicInstrument.SING_GOAT_HORN).useHorn(), arrayOf(Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, 16), ItemStack(Material.GOAT_HORN).horn(MusicInstrument.DREAM_GOAT_HORN).useHorn(), arrayOf(Material.STRING, 64)),
                    arrayOf(arrayOf(Material.FEATHER, 64), ItemStack(Material.GOAT_HORN).horn(MusicInstrument.ADMIRE_GOAT_HORN).useHorn(), ItemStack(Material.GOAT_HORN).horn(MusicInstrument.PONDER_GOAT_HORN).useHorn(), ItemStack(Material.GOAT_HORN).horn(MusicInstrument.YEARN_GOAT_HORN).useHorn(), Material.ELYTRA),
                    arrayOf(arrayOf(Material.BREEZE_ROD, 64), ItemStack(Material.BOW).ench("PW5", "UN3", "PU2", "FL1", "IN1").useEnch(), ItemStack(Material.LEATHER_BOOTS).ench("FF4","PT4","UN3","MN1","DS3","SP3").useEnch(), ItemStack(Material.FISHING_ROD).ench("LS3","LR3","UN3","MN1").useEnch(), arrayOf(Material.BREEZE_ROD, 64)),
                ), CustomItem.WIND_HOOK)
            )*/
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(Material.PALE_OAK_BOAT, arrayOf(Material.RAIL, 64), Material.MANGROVE_BOAT, arrayOf(Material.POWERED_RAIL, 64), Material.ACACIA_BOAT),
                    arrayOf(Material.MINECART, Material.GREEN_BED, arrayOf(Material.ENDER_PEARL, 16), arrayOf(Material.EMERALD_ORE, 8), Material.FURNACE_MINECART),
                    arrayOf(Material.CHERRY_BOAT, arrayOf(Material.CHEST, 32), Material.CONDUIT, arrayOf(Material.BARREL, 8), Material.DARK_OAK_BOAT),
                    arrayOf(Material.FURNACE_MINECART, arrayOf(Material.EMERALD_ORE, 8), arrayOf(Material.ENDER_PEARL, 16), Material.BROWN_BED, Material.MINECART),
                    arrayOf(Material.JUNGLE_BOAT, arrayOf(Material.DETECTOR_RAIL, 64), Material.SPRUCE_BOAT, arrayOf(Material.ACTIVATOR_RAIL, 64), Material.BIRCH_BOAT),
                ), CustomItem.VILLAGER_ATOMIZER)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.LIME_STAINED_GLASS, 32), Material.TOTEM_OF_UNDYING, arrayOf(Material.EMERALD_BLOCK, 32), Material.TOTEM_OF_UNDYING, arrayOf(Material.GREEN_STAINED_GLASS, 32)),
                    arrayOf(Material.TOTEM_OF_UNDYING, Material.RABBIT_STEW, arrayOf(ItemStack(Material.OMINOUS_BOTTLE).omimous(0).useOminous(), 8), Material.BEETROOT_SOUP, Material.TOTEM_OF_UNDYING),
                    arrayOf(Material.MUSHROOM_STEW, arrayOf(ItemStack(Material.OMINOUS_BOTTLE).omimous(3).useOminous(), 8), arrayOf(Material.NETHER_STAR, 2), arrayOf(ItemStack(Material.OMINOUS_BOTTLE).omimous(1).useOminous(), 8), Material.SUSPICIOUS_STEW),
                    arrayOf(Material.TOTEM_OF_UNDYING, arrayOf(Material.SLIME_BALL, 32), arrayOf(ItemStack(Material.OMINOUS_BOTTLE).omimous(2).useOminous(), 8), arrayOf(Material.SLIME_BALL, 32), Material.TOTEM_OF_UNDYING),
                    arrayOf(arrayOf(Material.OBSIDIAN, 32), Material.TOTEM_OF_UNDYING, arrayOf(Material.EMERALD_BLOCK, 32), Material.TOTEM_OF_UNDYING, arrayOf(Material.OBSIDIAN, 32)),
                ), CustomItem.JERRY_IDOL)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.ROTTEN_FLESH, 32), arrayOf(Material.RAW_GOLD_BLOCK, 4), arrayOf(Material.FERMENTED_SPIDER_EYE, 32), arrayOf(Material.RAW_GOLD_BLOCK, 4), arrayOf(Material.ROTTEN_FLESH, 32)),
                    arrayOf(arrayOf(Material.RAW_GOLD_BLOCK, 4), arrayOf(Material.GOLDEN_APPLE, 4), arrayOf(Material.GILDED_BLACKSTONE, 16), arrayOf(Material.GOLDEN_APPLE, 4), arrayOf(Material.RAW_GOLD_BLOCK, 4)),
                    arrayOf(arrayOf(Material.BLAZE_POWDER, 16), arrayOf(Material.NETHER_GOLD_ORE, 16), arrayOf(Material.ENCHANTED_GOLDEN_APPLE, 2), arrayOf(Material.DEEPSLATE_GOLD_ORE, 16), arrayOf(Material.BLAZE_POWDER, 16)),
                    arrayOf(arrayOf(Material.RAW_GOLD_BLOCK, 4), arrayOf(Material.GOLDEN_APPLE, 4), arrayOf(Material.GOLD_ORE, 16), arrayOf(Material.GOLDEN_APPLE, 4), arrayOf(Material.RAW_GOLD_BLOCK, 4)),
                    arrayOf(arrayOf(Material.COBBLESTONE, 64), arrayOf(Material.RAW_GOLD_BLOCK, 4), arrayOf(Material.BLAZE_ROD, 16), arrayOf(Material.RAW_GOLD_BLOCK, 4), arrayOf(Material.COBBLESTONE, 64)),
                ), CustomItem.GOLDEN_ZOMBIE)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.ARROW, 64), arrayOf(Material.FLINT, 32), arrayOf(Material.WIND_CHARGE, 32), arrayOf(Material.FLINT, 32), arrayOf(Material.ARROW, 64)),
                    arrayOf(arrayOf(Material.FEATHER, 32), arrayOf(Material.FLETCHING_TABLE, 8), arrayOf(Material.STICK, 32), arrayOf(Material.FLETCHING_TABLE, 8), arrayOf(Material.FEATHER, 32)),
                    arrayOf(arrayOf(Material.ENDER_PEARL, 16), arrayOf(Material.STICK, 32), arrayOf(Material.TIPPED_ARROW, 32), arrayOf(Material.STICK, 32), arrayOf(Material.POINTED_DRIPSTONE, 64)),
                    arrayOf(arrayOf(Material.FEATHER, 32), arrayOf(Material.FLETCHING_TABLE, 8), arrayOf(Material.STICK, 32), arrayOf(Material.FLETCHING_TABLE, 8), arrayOf(Material.FEATHER, 32)),
                    arrayOf(arrayOf(Material.ARROW, 64), arrayOf(Material.FLINT, 32), arrayOf(Material.WITHER_SKELETON_SKULL, 1), arrayOf(Material.FLINT, 32), arrayOf(Material.ARROW, 64)),
                ), CustomItem.FLETCHER_UPGRADE)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.BARREL, 4), arrayOf(Material.FLETCHING_TABLE, 4), arrayOf(Material.GRINDSTONE, 4), arrayOf(Material.SMOKER, 4), arrayOf(Material.BLAST_FURNACE, 4)),
                    arrayOf(arrayOf(Material.FURNACE, 4), Material.DIAMOND_PICKAXE, arrayOf(Material.EMERALD, 32), Material.DIAMOND_AXE, arrayOf(Material.CARTOGRAPHY_TABLE, 4)),
                    arrayOf(arrayOf(Material.ENCHANTING_TABLE, 4), arrayOf(Material.EMERALD, 32), arrayOf(Material.GLOW_INK_SAC, 16), arrayOf(Material.EMERALD, 32), arrayOf(Material.LOOM, 4)),
                    arrayOf(arrayOf(Material.ANVIL, 4), Material.DIAMOND_AXE, arrayOf(Material.EMERALD, 32), Material.DIAMOND_PICKAXE, arrayOf(Material.BREWING_STAND, 4)),
                    arrayOf(arrayOf(Material.CRAFTING_TABLE, 4), arrayOf(Material.CRAFTER, 4), arrayOf(Material.LECTERN, 4), arrayOf(Material.STONECUTTER, 4), arrayOf(Material.CAULDRON, 4)),
                ), CustomItem.TRADING_SCRAMBLER)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, Material.NETHERITE_PICKAXE, null, null),
                    arrayOf(null, Material.NETHERITE_AXE, arrayOf(Material.PHANTOM_MEMBRANE, 64), Material.NETHERITE_SHOVEL, null),
                    arrayOf(null, null, Material.NETHERITE_HOE, null, null),
                    arrayOf(null, null, null, null, null),
                ), CustomItem.NETHERITE_MULTITOOL)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.RAW_COPPER, 32), arrayOf(Material.RAW_IRON, 32), arrayOf(Material.DIAMOND, 16), arrayOf(Material.PALE_OAK_DOOR, 32), arrayOf(Material.DARK_OAK_FENCE_GATE, 32)),
                    arrayOf(arrayOf(Material.GRANITE, 32), arrayOf(Material.COBBLESTONE, 32), arrayOf(Material.STICK, 64), arrayOf(Material.OAK_LOG, 32), arrayOf(Material.SPRUCE_LOG, 32)),
                    arrayOf(arrayOf(Material.COBBLED_DEEPSLATE, 32), ItemStack(Material.NETHERITE_PICKAXE).ench("EF5", "UN3", "MN1").useEnch(), arrayOf(Material.STRING, 64), ItemStack(Material.NETHERITE_AXE).ench("EF5", "UN3", "MN1").useEnch(), arrayOf(Material.BIRCH_LOG, 32)),
                    arrayOf(arrayOf(Material.DIORITE, 32), arrayOf(Material.ANDESITE, 32), arrayOf(Material.STICK, 64), arrayOf(Material.DARK_OAK_LOG, 32), arrayOf(Material.JUNGLE_LOG, 32)),
                    arrayOf(arrayOf(Material.RAW_GOLD, 32), arrayOf(Material.COAL, 32), arrayOf(Material.DIAMOND, 16), arrayOf(Material.CRIMSON_TRAPDOOR, 32), arrayOf(Material.MANGROVE_SLAB, 32)),
                ), CustomItem.AXEPICK)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, arrayOf(Material.MELON, 32), null, null),
                    arrayOf(null, arrayOf(Material.PUMPKIN_SEEDS, 32), arrayOf(Material.MELON_SEEDS, 32), arrayOf(Material.POTATO, 32), null),
                    arrayOf(null, arrayOf(Material.WHEAT_SEEDS, 32), Material.NETHERITE_HOE, arrayOf(Material.CARROT, 32), null),
                    arrayOf(null, arrayOf(Material.WHEAT, 32), arrayOf(Material.BEETROOT, 32), arrayOf(Material.BEETROOT_SEEDS, 32), null),
                    arrayOf(null, null, arrayOf(Material.PUMPKIN, 32), null, null),
                ), CustomItem.HOE)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.PALE_MOSS_BLOCK, 32), arrayOf(Material.WARPED_WART_BLOCK, 32), arrayOf(Material.DIAMOND, 16), arrayOf(Material.CLAY, 32), arrayOf(Material.SOUL_SAND, 32)),
                    arrayOf(arrayOf(Material.ACACIA_LEAVES, 32), arrayOf(Material.AZALEA_LEAVES, 32), arrayOf(Material.STICK, 64), arrayOf(Material.COARSE_DIRT, 32), arrayOf(Material.MUD, 32)),
                    arrayOf(arrayOf(Material.MOSS_BLOCK, 32), ItemStack(Material.NETHERITE_SHOVEL).ench("EF5", "UN3", "MN1").useEnch(), arrayOf(Material.STRING, 64), ItemStack(Material.NETHERITE_HOE).ench("EF5", "UN3", "MN1").useEnch(), arrayOf(Material.SNOW_BLOCK, 32)),
                    arrayOf(arrayOf(Material.OAK_LEAVES, 32), arrayOf(Material.HAY_BLOCK, 32), arrayOf(Material.STICK, 64), arrayOf(Material.DIRT, 32), arrayOf(Material.GRAVEL, 32)),
                    arrayOf(arrayOf(Material.PALE_MOSS_BLOCK, 32), arrayOf(Material.NETHER_WART_BLOCK, 32), arrayOf(Material.DIAMOND, 16), arrayOf(Material.SAND, 32), arrayOf(Material.SOUL_SOIL, 32)),
                ), CustomItem.HOEVEL)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.LAPIS_ORE, 16), arrayOf(Material.DEEPSLATE_LAPIS_ORE, 16), ItemStack(Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE).useOriginal(), arrayOf(Material.DEEPSLATE_COPPER_ORE, 16), arrayOf(Material.COPPER_ORE, 16)),
                    arrayOf(arrayOf(Material.IRON_BLOCK, 16), Material.IRON_PICKAXE, arrayOf(Material.COPPER_BLOCK, 16), Material.NETHERITE_PICKAXE, arrayOf(Material.DEEPSLATE_DIAMOND_ORE, 16)),
                    arrayOf(ItemStack(Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE).useOriginal(), arrayOf(Material.TNT, 16), arrayOf(Material.END_CRYSTAL, 16), arrayOf(Material.TNT, 16), ItemStack(Material.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE).useOriginal()),
                    arrayOf(arrayOf(Material.COAL_ORE, 16), Material.NETHERITE_PICKAXE, arrayOf(Material.COPPER_BLOCK, 16), Material.GOLDEN_PICKAXE, arrayOf(Material.GOLD_BLOCK, 16)),
                    arrayOf(arrayOf(Material.REDSTONE_ORE, 16), arrayOf(Material.DEEPSLATE_REDSTONE_ORE, 16), ItemStack(Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE).useOriginal(), arrayOf(Material.DEEPSLATE_IRON_ORE, 16), arrayOf(Material.IRON_ORE, 16)),
                ), CustomItem.VEINY_PICKAXE)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.DARK_OAK_LOG, 16), arrayOf(Material.OAK_LEAVES, 64), arrayOf(Material.ACACIA_LOG, 16), arrayOf(Material.SPRUCE_LEAVES, 64), arrayOf(Material.JUNGLE_LOG, 16)),
                    arrayOf(arrayOf(Material.OAK_LOG, 32), Material.GOLDEN_AXE, Material.WOODEN_AXE, Material.GOLDEN_AXE, arrayOf(Material.DARK_OAK_LEAVES, 64)),
                    arrayOf(arrayOf(Material.MANGROVE_LOG, 16), Material.DIAMOND_AXE, Material.NETHERITE_AXE, Material.STONE_AXE, arrayOf(Material.BIRCH_LOG, 16)),
                    arrayOf(arrayOf(Material.ACACIA_LEAVES, 64), Material.GOLDEN_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, arrayOf(Material.OAK_LOG, 32)),
                    arrayOf(arrayOf(Material.CHERRY_LOG, 16), arrayOf(Material.BIRCH_LEAVES, 64), arrayOf(Material.PALE_OAK_LOG, 16), arrayOf(Material.JUNGLE_LEAVES, 64), arrayOf(Material.SPRUCE_LOG, 16)),
                ), CustomItem.TREECAPITATOR)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, Material.SHEARS, null, null),
                    arrayOf(null, arrayOf(Material.IRON_INGOT, 32), Material.SHEARS, arrayOf(Material.FEATHER, 32), null),
                    arrayOf(arrayOf(Material.FLINT, 32), Material.FLINT_AND_STEEL, Material.SHEARS, Material.BRUSH, arrayOf(Material.COPPER_INGOT, 32)),
                    arrayOf(null, arrayOf(Material.IRON_INGOT, 32), Material.SHEARS, arrayOf(Material.STICK, 32), null),
                    arrayOf(null, null, Material.SHEARS, null, null),
                ), CustomItem.POCKETKNIFE_MULTITOOL)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.DIAMOND, 16), arrayOf(Material.STONE, 32), arrayOf(Material.AMETHYST_SHARD, 32), arrayOf(Material.STONE, 32), arrayOf(Material.DIAMOND, 16)),
                    arrayOf(arrayOf(Material.PISTON, 16), arrayOf(Material.GUNPOWDER, 32), arrayOf(Material.TNT, 32), arrayOf(Material.GUNPOWDER, 32), arrayOf(Material.DROPPER, 16)),
                    arrayOf(arrayOf(Material.REDSTONE_TORCH, 16), arrayOf(Material.TNT, 32), ItemStack(Material.NETHERITE_PICKAXE).ench("EF5", "UN3", "MN1").useEnch(), arrayOf(Material.TNT, 32), arrayOf(Material.REDSTONE_TORCH, 16)),
                    arrayOf(arrayOf(Material.DROPPER, 16), arrayOf(Material.GUNPOWDER, 32), arrayOf(Material.TNT, 32), arrayOf(Material.GUNPOWDER, 32), arrayOf(Material.PISTON, 16)),
                    arrayOf(arrayOf(Material.DIAMOND, 16), arrayOf(Material.DEEPSLATE, 32), arrayOf(Material.AMETHYST_SHARD, 32), arrayOf(Material.DEEPSLATE, 32), arrayOf(Material.DIAMOND, 16)),
                ), CustomItem.EXCAVATOR)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(Material.DIAMOND_ORE, arrayOf(Material.DIAMOND, 8), arrayOf(Material.SUSPICIOUS_SAND, 2), arrayOf(Material.DIAMOND, 8), Material.MYCELIUM),
                    arrayOf(null, Material.ANCIENT_DEBRIS, Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, Material.ANCIENT_DEBRIS, null),
                    arrayOf(null, CustomItem.AXEPICK, arrayOf(Material.NETHER_STAR, 2), CustomItem.HOEVEL, null),
                    arrayOf(null, Material.ANCIENT_DEBRIS, Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, Material.ANCIENT_DEBRIS, null),
                    arrayOf(Material.DEEPSLATE_COAL_ORE, arrayOf(Material.DIAMOND, 8), arrayOf(Material.SUSPICIOUS_GRAVEL, 2), arrayOf(Material.DIAMOND, 8), Material.MUDDY_MANGROVE_ROOTS),
                ), CustomItem.NETHERITE_MATTOCK)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.LIGHT_GRAY_CONCRETE, 32), arrayOf(Material.IRON_INGOT, 16), arrayOf(Material.SEA_LANTERN, 32), arrayOf(Material.IRON_INGOT, 16), arrayOf(Material.BLUE_CONCRETE, 32)),
                    arrayOf(arrayOf(Material.IRON_INGOT, 16), arrayOf(Material.PRISMARINE_SHARD, 16), Material.HEART_OF_THE_SEA, arrayOf(Material.PRISMARINE_CRYSTALS, 16), arrayOf(Material.IRON_INGOT, 16)),
                    arrayOf(arrayOf(Material.DARK_PRISMARINE, 32), arrayOf(Material.PRISMARINE, 32), Material.HEAVY_CORE, arrayOf(Material.PRISMARINE, 32), arrayOf(Material.DARK_PRISMARINE, 32)),
                    arrayOf(arrayOf(Material.IRON_INGOT, 16), arrayOf(Material.PRISMARINE_CRYSTALS, 16), Material.HEART_OF_THE_SEA, arrayOf(Material.PRISMARINE_SHARD, 16), arrayOf(Material.IRON_INGOT, 16)),
                    arrayOf(arrayOf(Material.RED_CONCRETE, 32), arrayOf(Material.IRON_INGOT, 16), arrayOf(Material.SEA_LANTERN, 32), arrayOf(Material.IRON_INGOT, 16), arrayOf(Material.LIGHT_GRAY_CONCRETE, 32)),
                ), CustomItem.POLARIZED_MAGNET)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.WAXED_COPPER_BLOCK, 32), Material.CROSSBOW, Material.GUSTER_BANNER_PATTERN, arrayOf(Material.DIAMOND, 8), arrayOf(Material.WAXED_OXIDIZED_COPPER, 32)),
                    arrayOf(ItemStack(Material.ENCHANTED_BOOK).storeEnch("WB1").useStoredEnch(), Material.MUSIC_DISC_PRECIPICE, arrayOf(Material.BREEZE_ROD, 16), ItemStack(Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).useOriginal(), Material.CROSSBOW),
                    arrayOf(Material.FLOW_BANNER_PATTERN, arrayOf(Material.WIND_CHARGE, 32), Material.HEAVY_CORE, arrayOf(Material.WIND_CHARGE, 32), Material.FLOW_BANNER_PATTERN),
                    arrayOf(Material.CROSSBOW, ItemStack(Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE).useOriginal(), arrayOf(Material.BREEZE_ROD, 16), Material.MUSIC_DISC_CREATOR, ItemStack(Material.ENCHANTED_BOOK).storeEnch("WB1").useStoredEnch()),
                    arrayOf(arrayOf(Material.WAXED_COPPER_BLOCK, 32), arrayOf(Material.DIAMOND, 8), Material.GUSTER_BANNER_PATTERN, Material.CROSSBOW, arrayOf(Material.WAXED_OXIDIZED_COPPER, 32)),
                ), CustomItem.WIND_CHARGE_CANNON)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.NETHER_BRICK, 32), arrayOf(Material.ARROW, 32), Material.CARROT_ON_A_STICK, arrayOf(Material.STRING, 32), arrayOf(Material.BRICK, 32)),
                    arrayOf(arrayOf(Material.ARMADILLO_SCUTE, 8), Material.SADDLE, Material.EXPLORER_POTTERY_SHERD, Material.SADDLE, arrayOf(Material.ARMADILLO_SCUTE, 8)),
                    arrayOf(arrayOf(Material.LEAD, 16), Material.ARMS_UP_POTTERY_SHERD, ItemStack(Material.CROSSBOW).ench("QC3", "PR4", "MN1", "UN3"), Material.SNORT_POTTERY_SHERD, arrayOf(Material.LEAD, 16)),
                    arrayOf(arrayOf(Material.ARMADILLO_SCUTE, 8), Material.SADDLE, Material.ARCHER_POTTERY_SHERD, Material.SADDLE, arrayOf(Material.ARMADILLO_SCUTE, 8)),
                    arrayOf(arrayOf(Material.BRICK, 32), arrayOf(Material.STRING, 32), Material.WARPED_FUNGUS_ON_A_STICK, arrayOf(Material.ARROW, 32), arrayOf(Material.NETHER_BRICK, 32)),
                ), CustomItem.RIDABLE_CROSSBOW)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, arrayOf(Material.LEATHER, 32), null, null),
                    arrayOf(arrayOf(Material.SUGAR, 32), Material.IRON_HORSE_ARMOR, Material.DIAMOND_HORSE_ARMOR, Material.LEATHER_HORSE_ARMOR, arrayOf(Material.RABBIT_FOOT, 2)),
                    arrayOf(Material.SADDLE, Material.GOLDEN_HORSE_ARMOR, Material.NETHERITE_HELMET, Material.GOLDEN_HORSE_ARMOR, Material.SADDLE),
                    arrayOf(arrayOf(Material.RABBIT_FOOT, 2), Material.LEATHER_HORSE_ARMOR, Material.DIAMOND_HORSE_ARMOR, Material.IRON_HORSE_ARMOR, arrayOf(Material.SUGAR, 32)),
                    arrayOf(null, null, arrayOf(Material.LEATHER, 32), null, null),
                ), CustomItem.COWBOY_HAT)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, arrayOf(Material.RAW_IRON_BLOCK, 16), null, null),
                    arrayOf(null, arrayOf(Material.OCHRE_FROGLIGHT, 2), Material.DIAMOND_PICKAXE, arrayOf(Material.COPPER_BULB, 2), null),
                    arrayOf(ItemStack(Material.ENCHANTED_BOOK).storeEnch("EF5").useStoredEnch(), Material.DIAMOND_PICKAXE, Material.NETHERITE_HELMET, Material.DIAMOND_PICKAXE, ItemStack(Material.ENCHANTED_BOOK).storeEnch("EF5").useStoredEnch()),
                    arrayOf(null, arrayOf(Material.VERDANT_FROGLIGHT, 2), Material.DIAMOND_PICKAXE, arrayOf(Material.PEARLESCENT_FROGLIGHT, 2), null),
                    arrayOf(null, null, arrayOf(Material.RAW_GOLD_BLOCK, 16), null, null),
                ), CustomItem.MINERS_HELM)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.CYAN_GLAZED_TERRACOTTA, 8), arrayOf(Material.REDSTONE_TORCH, 32), arrayOf(Material.QUARTZ_BLOCK, 16), arrayOf(Material.LEVER, 32), arrayOf(Material.BROWN_GLAZED_TERRACOTTA, 8)),
                    arrayOf(Material.SCRAPE_POTTERY_SHERD, arrayOf(Material.STICK, 32), arrayOf(Material.SCAFFOLDING, 32), arrayOf(Material.STICK, 32), Material.MINER_POTTERY_SHERD),
                    arrayOf(arrayOf(Material.STICKY_PISTON, 32), arrayOf(Material.SCAFFOLDING, 32), Material.NETHERITE_LEGGINGS, arrayOf(Material.SCAFFOLDING, 32), arrayOf(Material.STICKY_PISTON, 32)),
                    arrayOf(Material.FRIEND_POTTERY_SHERD, arrayOf(Material.STICK, 32), arrayOf(Material.SCAFFOLDING, 32), arrayOf(Material.STICK, 32), Material.SHELTER_POTTERY_SHERD),
                    arrayOf(arrayOf(Material.GREEN_GLAZED_TERRACOTTA, 8), arrayOf(Material.LEVER, 32), arrayOf(Material.QUARTZ_BLOCK, 16), arrayOf(Material.REDSTONE_TORCH, 32), arrayOf(Material.BLUE_GLAZED_TERRACOTTA, 8)),
                ), CustomItem.TOOLBELT)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.REDSTONE_TORCH, 32), arrayOf(Material.BLAZE_ROD, 32), arrayOf(Material.MAGMA_CREAM, 32), arrayOf(Material.BLAZE_ROD, 32), arrayOf(Material.REDSTONE_TORCH, 32)),
                    arrayOf(arrayOf(Material.REDSTONE_BLOCK, 16), arrayOf(Material.FIREWORK_ROCKET, 64), ItemStack(Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).useOriginal(), arrayOf(Material.FIREWORK_ROCKET, 64), arrayOf(Material.REDSTONE_BLOCK, 16)),
                    arrayOf(arrayOf(Material.REPEATER, 32), arrayOf(Material.FIREWORK_ROCKET, 64), ItemStack(Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE).useOriginal(), arrayOf(Material.FIREWORK_ROCKET, 64), arrayOf(Material.REPEATER, 32)),
                    arrayOf(arrayOf(Material.COMPARATOR, 32), arrayOf(Material.FIREWORK_ROCKET, 64), arrayOf(Material.IRON_BLOCK, 32), arrayOf(Material.FIREWORK_ROCKET, 64), arrayOf(Material.COMPARATOR, 32)),
                    arrayOf(Material.LAVA_BUCKET, arrayOf(Material.CAMPFIRE, 32), arrayOf(Material.SOUL_CAMPFIRE, 32), arrayOf(Material.CAMPFIRE, 32), Material.LAVA_BUCKET),
                ), CustomItem.JETPACK_CONTROLLER_SET)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(Material.SPYGLASS, arrayOf(Material.SOUL_CAMPFIRE, 32), arrayOf(Material.CANDLE, 16), arrayOf(Material.CAMPFIRE, 32), Material.SPYGLASS),
                    arrayOf(arrayOf(Material.FIREWORK_ROCKET, 32), arrayOf(Material.DISC_FRAGMENT_5, 4), arrayOf(Material.COMPASS, 16), arrayOf(Material.DISC_FRAGMENT_5, 4), arrayOf(Material.CLOCK, 16)),
                    arrayOf(arrayOf(Material.BLACK_CANDLE, 16), arrayOf(Material.COMPASS, 16), arrayOf(Material.RECOVERY_COMPASS, 4), arrayOf(Material.COMPASS, 16), arrayOf(Material.BLACK_CANDLE, 16)),
                    arrayOf(arrayOf(Material.CLOCK, 16), arrayOf(Material.DISC_FRAGMENT_5, 4), arrayOf(Material.COMPASS, 16), arrayOf(Material.DISC_FRAGMENT_5, 4), arrayOf(Material.FIREWORK_ROCKET, 32)),
                    arrayOf(Material.SPYGLASS, arrayOf(Material.CAMPFIRE, 32), ItemStack(Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE).useOriginal(), arrayOf(Material.SOUL_CAMPFIRE, 32), Material.SPYGLASS),
                ), CustomItem.TRACKING_COMPASS)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.SCULK, 64), arrayOf(Material.SCULK_VEIN, 64), ItemStack(Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE).useOriginal(), arrayOf(Material.SCULK_VEIN, 64), arrayOf(Material.SCULK, 64)),
                    arrayOf(arrayOf(Material.SCULK_SHRIEKER, 32), arrayOf(Material.ECHO_SHARD, 16), Material.MUSIC_DISC_OTHERSIDE, arrayOf(Material.ECHO_SHARD, 16), arrayOf(Material.SCULK_SENSOR, 32)),
                    arrayOf(arrayOf(Material.SCULK_CATALYST, 8), Material.MUSIC_DISC_5, arrayOf(Material.EXPERIENCE_BOTTLE, 64), Material.MUSIC_DISC_5, arrayOf(Material.SCULK_CATALYST, 8)),
                    arrayOf(arrayOf(Material.SCULK_SENSOR, 32), arrayOf(Material.ECHO_SHARD, 16), Material.MUSIC_DISC_OTHERSIDE, arrayOf(Material.ECHO_SHARD, 16), arrayOf(Material.SCULK_SHRIEKER, 32)),
                    arrayOf(arrayOf(Material.SCULK, 64), arrayOf(Material.SCULK_VEIN, 64), arrayOf(Material.GLASS_BOTTLE, 64), arrayOf(Material.SCULK_VEIN, 64), arrayOf(Material.SCULK, 64)),
                ), CustomItem.EXPERIENCE_FLASK)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(Material.ENCHANTED_BOOK, Material.MUSIC_DISC_CAT, arrayOf(Material.EXPERIENCE_BOTTLE, 16), Material.MUSIC_DISC_13, Material.ENCHANTED_BOOK),
                    arrayOf(arrayOf(Material.GLOW_BERRIES, 32), arrayOf(Material.SCULK, 64), arrayOf(Material.APPLE, 32), arrayOf(Material.SCULK, 64), arrayOf(Material.GLOW_BERRIES, 32)),
                    arrayOf(arrayOf(Material.SOUL_TORCH, 32), arrayOf(Material.APPLE, 32), Material.ENCHANTED_GOLDEN_APPLE, arrayOf(Material.APPLE, 32), arrayOf(Material.SOUL_TORCH, 32)),
                    arrayOf(arrayOf(Material.GLOW_BERRIES, 32), arrayOf(Material.SCULK, 64), arrayOf(Material.APPLE, 32), arrayOf(Material.SCULK, 64), arrayOf(Material.GLOW_BERRIES, 32)),
                    arrayOf(Material.ENCHANTED_BOOK, Material.MUSIC_DISC_13, arrayOf(Material.EXPERIENCE_BOTTLE, 16), Material.MUSIC_DISC_CAT, Material.ENCHANTED_BOOK),
                ), CustomItem.MYSTICAL_GREEN_APPLE)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.IRON_BARS, 32), arrayOf(Material.CHAIN, 32), arrayOf(Material.IRON_BARS, 32), arrayOf(Material.CHAIN, 32), arrayOf(Material.IRON_BARS, 32)),
                    arrayOf(arrayOf(Material.CHAIN, 32), ItemStack(Material.CHAINMAIL_BOOTS).useEnch().ench("PT4", "UN3","MN1"), arrayOf(Material.SHULKER_SHELL, 4), ItemStack(Material.CHAINMAIL_LEGGINGS).useEnch().ench("PT4", "UN3","MN1"), arrayOf(Material.CHAIN, 32)),
                    arrayOf(arrayOf(Material.IRON_BARS, 32), arrayOf(Material.COBWEB, 32), arrayOf(Material.LEAD, 16), arrayOf(Material.COBWEB, 32), arrayOf(Material.IRON_BARS, 32)),
                    arrayOf(arrayOf(Material.CHAIN, 32), ItemStack(Material.CHAINMAIL_CHESTPLATE).useEnch().ench("PT4", "UN3","MN1"), arrayOf(Material.SHULKER_SHELL, 4), ItemStack(Material.CHAINMAIL_HELMET).useEnch().ench("PT4", "UN3","MN1"), arrayOf(Material.CHAIN, 32)),
                    arrayOf(arrayOf(Material.IRON_BARS, 32), arrayOf(Material.CHAIN, 32), arrayOf(Material.IRON_BARS, 32), arrayOf(Material.CHAIN, 32), arrayOf(Material.IRON_BARS, 32)),
                ), CustomItem.REINFORCED_CAGE)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, CustomItem.INPUT_DEVICES, arrayOf(Material.COPPER_GRATE, 32), CustomItem.MINECART_MATERIALS, null),
                    arrayOf(null, arrayOf(Material.COPPER_GRATE, 32), arrayOf(Material.HONEY_BLOCK, 16), arrayOf(Material.COPPER_GRATE, 32), null),
                    arrayOf(null, CustomItem.ACTUAL_REDSTONE, arrayOf(Material.COPPER_GRATE, 32), CustomItem.CONTAINERS, null),
                    arrayOf(null, null, null, null, null),
                ), CustomItem.REDSTONE_AMALGAMATION)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, arrayOf(Material.DEEPSLATE_REDSTONE_ORE, 16), null, null),
                    arrayOf(null, arrayOf(Material.OAK_BUTTON, 64), arrayOf(Material.STONE_PRESSURE_PLATE, 64), arrayOf(Material.REDSTONE, 32), null),
                    arrayOf(null, arrayOf(Material.OAK_PRESSURE_PLATE, 64), arrayOf(Material.LEVER, 64), arrayOf(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, 64), null),
                    arrayOf(null, arrayOf(Material.REDSTONE, 32), arrayOf(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 64), arrayOf(Material.STONE_BUTTON, 64), null),
                    arrayOf(null, null, arrayOf(Material.DEEPSLATE_REDSTONE_ORE, 16), null, null),
                ), CustomItem.INPUT_DEVICES)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, arrayOf(Material.DEEPSLATE_REDSTONE_ORE, 16), null, null),
                    arrayOf(null, arrayOf(Material.RAIL, 64), Material.CHEST_MINECART, arrayOf(Material.POWERED_RAIL, 64), null),
                    arrayOf(arrayOf(Material.REDSTONE, 32), Material.FURNACE_MINECART, Material.TNT_MINECART, Material.HOPPER_MINECART, arrayOf(Material.REDSTONE, 32)),
                    arrayOf(null, arrayOf(Material.ACTIVATOR_RAIL, 64), Material.MINECART, arrayOf(Material.DETECTOR_RAIL, 64), null),
                    arrayOf(null, null, arrayOf(Material.DEEPSLATE_REDSTONE_ORE, 16), null, null),
                ), CustomItem.MINECART_MATERIALS)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, arrayOf(Material.DEEPSLATE_REDSTONE_ORE, 16), null, null),
                    arrayOf(null, arrayOf(Material.OBSERVER, 16), arrayOf(Material.REDSTONE, 32), arrayOf(Material.COMPARATOR, 16), null),
                    arrayOf(null, arrayOf(Material.REDSTONE_TORCH, 32), arrayOf(Material.REDSTONE_BLOCK, 16), arrayOf(Material.REDSTONE_TORCH, 32), null),
                    arrayOf(null, arrayOf(Material.REPEATER, 16), arrayOf(Material.REDSTONE, 32), arrayOf(Material.OBSERVER, 16), null),
                    arrayOf(null, null, arrayOf(Material.DEEPSLATE_REDSTONE_ORE, 16), null, null),
                ), CustomItem.ACTUAL_REDSTONE)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, arrayOf(Material.DEEPSLATE_REDSTONE_ORE, 16), null, null),
                    arrayOf(null, arrayOf(Material.STICKY_PISTON, 16), arrayOf(Material.BARREL, 32), arrayOf(Material.DROPPER, 16), null),
                    arrayOf(arrayOf(Material.SLIME_BLOCK, 8), arrayOf(Material.CRAFTER, 16), arrayOf(Material.NOTE_BLOCK, 32), arrayOf(Material.HOPPER, 32), arrayOf(Material.SLIME_BLOCK, 8)),
                    arrayOf(null, arrayOf(Material.DISPENSER, 16), arrayOf(Material.CHEST, 32), arrayOf(Material.PISTON, 16), null),
                    arrayOf(null, null, arrayOf(Material.DEEPSLATE_REDSTONE_ORE, 16), null, null),
                ), CustomItem.CONTAINERS)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.GOLDEN_APPLE, 4), arrayOf(Material.POPPED_CHORUS_FRUIT, 32), arrayOf(Material.CHORUS_FRUIT, 64), arrayOf(Material.POPPED_CHORUS_FRUIT, 32), arrayOf(Material.GOLDEN_APPLE, 4)),
                    arrayOf(arrayOf(Material.POPPED_CHORUS_FRUIT, 32), arrayOf(Material.CHORUS_FLOWER, 32), arrayOf(Material.DISPENSER, 16), arrayOf(Material.CHORUS_FLOWER, 32), arrayOf(Material.POPPED_CHORUS_FRUIT, 32)),
                    arrayOf(arrayOf(Material.CHORUS_FRUIT, 64), arrayOf(Material.SHULKER_SHELL, 16), arrayOf(Material.CHORUS_FRUIT, 64), arrayOf(Material.SHULKER_SHELL, 16), arrayOf(Material.CHORUS_FRUIT, 64)),
                    arrayOf(arrayOf(Material.POPPED_CHORUS_FRUIT, 32), arrayOf(Material.CHORUS_FLOWER, 32), ItemStack(Material.DIAMOND_PICKAXE).useEnch().ench("EF5","UN3","MN1"), arrayOf(Material.CHORUS_FLOWER, 32), arrayOf(Material.POPPED_CHORUS_FRUIT, 32)),
                    arrayOf(arrayOf(Material.GOLDEN_APPLE, 4), arrayOf(Material.POPPED_CHORUS_FRUIT, 32), arrayOf(Material.CHORUS_FRUIT, 64), arrayOf(Material.POPPED_CHORUS_FRUIT, 32), arrayOf(Material.GOLDEN_APPLE, 4)),
                ), CustomItem.SHULKER_FRUIT)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, arrayOf(Material.BLAZE_ROD, 8), arrayOf(Material.CHORUS_FRUIT, 16), arrayOf(Material.BLAZE_ROD, 8), null),
                    arrayOf(arrayOf(Material.BLAZE_POWDER, 8), arrayOf(Material.ENDER_EYE, 16), arrayOf(Material.CRYING_OBSIDIAN, 16), arrayOf(Material.ENDER_PEARL, 16), arrayOf(Material.BLAZE_POWDER, 8)),
                    arrayOf(arrayOf(Material.ENDER_CHEST, 4), arrayOf(Material.OBSIDIAN, 16), Material.DRAGON_HEAD, arrayOf(Material.OBSIDIAN, 16), arrayOf(Material.ENDER_CHEST, 4)),
                    arrayOf(arrayOf(Material.BLAZE_POWDER, 8), arrayOf(Material.ENDER_PEARL, 16), arrayOf(Material.CRYING_OBSIDIAN, 16), arrayOf(Material.ENDER_EYE, 16), arrayOf(Material.BLAZE_POWDER, 8)),
                    arrayOf(null, arrayOf(Material.BLAZE_ROD, 8), arrayOf(Material.CHORUS_FRUIT, 16), arrayOf(Material.BLAZE_ROD, 8), null),
                ), CustomItem.ENDER_NODE)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.BLAZE_POWDER, 16), arrayOf(Material.END_ROD, 16), arrayOf(Material.ENDER_EYE, 16), arrayOf(Material.END_ROD, 16), arrayOf(Material.BLAZE_ROD, 16)),
                    arrayOf(arrayOf(Material.END_ROD, 16), arrayOf(Material.CRYING_OBSIDIAN, 32), Material.DRAGON_HEAD, arrayOf(Material.OBSIDIAN, 32), arrayOf(Material.END_ROD, 16)),
                    arrayOf(arrayOf(Material.ENDER_EYE, 16), ItemStack(Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE).useOriginal(), ItemStack(Material.NETHERITE_SWORD).useEnch().ench("SH5","FA2","UN3","MN1"), ItemStack(Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).useOriginal(), arrayOf(Material.ENDER_EYE, 16)),
                    arrayOf(arrayOf(Material.END_ROD, 16), arrayOf(Material.OBSIDIAN, 32), Material.DRAGON_HEAD, arrayOf(Material.CRYING_OBSIDIAN, 32), arrayOf(Material.END_ROD, 16)),
                    arrayOf(arrayOf(Material.BLAZE_ROD, 16), arrayOf(Material.END_ROD, 16), arrayOf(Material.ENDER_EYE, 16), arrayOf(Material.END_ROD, 16), arrayOf(Material.BLAZE_POWDER, 16)),
                ), CustomItem.ENDER_BLADE)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.STRING, 64), arrayOf(Material.ARROW, 64), null, null, null),
                    arrayOf(arrayOf(Material.STRING, 64), arrayOf(Material.WIND_CHARGE, 64), arrayOf(Material.FEATHER, 32), arrayOf(Material.FIREWORK_ROCKET, 64), null),
                    arrayOf(arrayOf(Material.STRING, 64), ItemStack(Material.BOW).useEnch().ench("PW5","UN3","IN1"), ItemStack(Material.CROSSBOW).useEnch().ench("PR4","UN3","MN1"), ItemStack(Material.TRIDENT).useEnch().ench("LY3","UN3","MN1"), arrayOf(Material.TRIPWIRE_HOOK, 64)),
                    arrayOf(arrayOf(Material.STRING, 64), arrayOf(Material.WIND_CHARGE, 64), arrayOf(Material.FEATHER, 32), arrayOf(Material.FIREWORK_ROCKET, 64), null),
                    arrayOf(arrayOf(Material.STRING, 64), arrayOf(Material.ARROW, 64), null, null, null),
                ), CustomItem.WIND_HOOK)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.FIREWORK_ROCKET, 64), arrayOf(Material.FIREWORK_ROCKET, 64), Material.TNT_MINECART, null, null),
                    arrayOf(arrayOf(Material.IRON_BLOCK, 8), arrayOf(Material.IRON_BLOCK, 8), arrayOf(Material.IRON_BLOCK, 8), Material.TNT_MINECART, null),
                    arrayOf(ItemStack(Material.CROSSBOW).useEnch().ench("PR4","UN3","MN1","QC3"), ItemStack(Material.CROSSBOW).useEnch().ench("PR4","UN3","MN1","QC3"), ItemStack(Material.CROSSBOW).useEnch().ench("PR4","UN3","MN1","QC3"), arrayOf(Material.IRON_BLOCK, 8), arrayOf(Material.TNT, 32)),
                    arrayOf(arrayOf(Material.IRON_BLOCK, 8), arrayOf(Material.IRON_BLOCK, 8), arrayOf(Material.IRON_BLOCK, 8), Material.TNT_MINECART, null),
                    arrayOf(arrayOf(Material.FIREWORK_ROCKET, 64), arrayOf(Material.FIREWORK_ROCKET, 64), Material.TNT_MINECART, null, null),
                ), CustomItem.SURFACE_TO_AIR_MISSILE)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.SUGAR, 16), arrayOf(Material.BLAZE_POWDER, 16), arrayOf(Material.MAGMA_CREAM, 16), arrayOf(Material.GHAST_TEAR, 16), arrayOf(Material.GLISTERING_MELON_SLICE, 16)),
                    arrayOf(arrayOf(Material.NETHER_WART, 16), arrayOf(Material.BREWING_STAND, 16), arrayOf(Material.REDSTONE_BLOCK, 32), Material.BEACON, arrayOf(Material.GOLDEN_CARROT, 16)),
                    arrayOf(Material.TURTLE_HELMET, arrayOf(Material.REDSTONE_ORE, 16), ItemStack(Material.NETHERITE_HELMET).useEnch().ench("PT4","UN3","MN1","AA1","RS3"), arrayOf(Material.REDSTONE_ORE, 16), arrayOf(Material.PUFFERFISH, 16)),
                    arrayOf(arrayOf(Material.BREEZE_ROD, 16), Material.BEACON, arrayOf(Material.REDSTONE_BLOCK, 32), arrayOf(Material.BREWING_STAND, 16), arrayOf(Material.RABBIT_FOOT, 16)),
                    arrayOf(arrayOf(Material.COBWEB, 16), arrayOf(Material.SLIME_BLOCK, 16), arrayOf(Material.FERMENTED_SPIDER_EYE, 16), arrayOf(Material.SPIDER_EYE, 16), arrayOf(Material.PHANTOM_MEMBRANE, 16)),
                ), CustomItem.DRINKING_HAT)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(arrayOf(Material.SOUL_LANTERN, 32), Material.FLOWER_BANNER_PATTERN, arrayOf(Material.DRAGON_HEAD, 4), Material.FLOWER_BANNER_PATTERN, arrayOf(Material.LANTERN, 32)),
                    arrayOf(Material.SPYGLASS, arrayOf(Material.SPECTRAL_ARROW, 64), arrayOf(Material.OPEN_EYEBLOSSOM, 32), arrayOf(Material.SPECTRAL_ARROW, 64), Material.SPYGLASS),
                    arrayOf(arrayOf(Material.ENDER_EYE, 32), arrayOf(Material.TINTED_GLASS, 32), ItemStack(Material.NETHERITE_HELMET).useEnch().ench("PT4","UN3","MN1","AA1","RS3"), arrayOf(Material.TINTED_GLASS, 32), arrayOf(Material.ENDER_EYE, 32)),
                    arrayOf(Material.SPYGLASS, arrayOf(Material.SPECTRAL_ARROW, 64), arrayOf(Material.OPEN_EYEBLOSSOM, 32), arrayOf(Material.SPECTRAL_ARROW, 64), Material.SPYGLASS),
                    arrayOf(arrayOf(Material.LANTERN, 32), Material.FLOWER_BANNER_PATTERN, ItemStack(Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).useOriginal(), Material.FLOWER_BANNER_PATTERN, arrayOf(Material.SOUL_LANTERN, 32)),
                ), CustomItem.XRAY_GOGGLES)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, Material.NETHERITE_INGOT, arrayOf(Material.IRON_BLOCK, 64), arrayOf(Material.COPPER_BLOCK, 64), arrayOf(Material.ANVIL, 16)),
                    arrayOf(null, arrayOf(Material.RABBIT_HIDE, 16), arrayOf(Material.GOLD_BLOCK, 64), Material.HEAVY_CORE, arrayOf(Material.COPPER_BLOCK, 64)),
                    arrayOf(null, arrayOf(Material.LEATHER, 16), ItemStack(Material.NETHERITE_AXE).useEnch().ench("SH5","EF5","UN3","MN1","FT3"), arrayOf(Material.GOLD_BLOCK, 64), arrayOf(Material.IRON_BLOCK, 64)),
                    arrayOf(arrayOf(Material.LEATHER, 16), arrayOf(Material.BREEZE_ROD, 32), arrayOf(Material.LEATHER, 16), arrayOf(Material.RABBIT_HIDE, 16), Material.NETHERITE_INGOT),
                    arrayOf(Material.BLADE_POTTERY_SHERD, arrayOf(Material.LEATHER, 16), null, null, null),
                ), CustomItem.GRAVITY_HAMMER)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(ItemStack(Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE).useOriginal(), arrayOf(Material.BRAIN_CORAL_BLOCK, 16), arrayOf(Material.DRIED_KELP_BLOCK, 64), arrayOf(Material.BUBBLE_CORAL_BLOCK, 16), ItemStack(Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE).useOriginal()),
                    arrayOf(arrayOf(Material.TURTLE_SCUTE, 8), arrayOf(Material.FIRE_CORAL_BLOCK, 16), Material.HEART_OF_THE_SEA, arrayOf(Material.TUBE_CORAL_BLOCK, 16), arrayOf(Material.TURTLE_SCUTE, 8)),
                    arrayOf(arrayOf(Material.SALMON, 64), arrayOf(Material.GLOW_INK_SAC, 16), ItemStack(Material.NETHERITE_BOOTS).useEnch().ench("PT4","UN3","MN1","FF4","SP3","DS3"), arrayOf(Material.GLOW_INK_SAC, 16), arrayOf(Material.COD, 64)),
                    arrayOf(arrayOf(Material.PRISMARINE_CRYSTALS, 32), arrayOf(Material.INK_SAC, 16), arrayOf(Material.NAUTILUS_SHELL, 4), arrayOf(Material.INK_SAC, 16), arrayOf(Material.PRISMARINE_CRYSTALS, 32)),
                    arrayOf(arrayOf(Material.PRISMARINE_SHARD, 32), arrayOf(Material.SEA_LANTERN, 16), arrayOf(Material.TURTLE_EGG, 4), arrayOf(Material.SEA_LANTERN, 16), arrayOf(Material.PRISMARINE_SHARD, 32)),
                ), CustomItem.AQUEOUS_SANDALS)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(ItemStack(Material.SPLASH_POTION).usePotion().basePotion(PotionType.LONG_SLOW_FALLING), Material.HOWL_POTTERY_SHERD, Material.MILK_BUCKET, Material.HEART_POTTERY_SHERD, ItemStack(Material.SPLASH_POTION).usePotion().basePotion(PotionType.LONG_SLOW_FALLING)),
                    arrayOf(arrayOf(Material.WHITE_BANNER, 16), ItemStack(Material.TIPPED_ARROW, 32).usePotion().basePotion(PotionType.LONG_SLOW_FALLING), arrayOf(Material.SHULKER_SHELL, 32), ItemStack(Material.TIPPED_ARROW, 32).usePotion().basePotion(PotionType.LONG_SLOW_FALLING), arrayOf(Material.WHITE_BANNER, 16)),
                    arrayOf(arrayOf(Material.POPPED_CHORUS_FRUIT, 32), arrayOf(Material.PHANTOM_MEMBRANE, 32), ItemStack(Material.NETHERITE_BOOTS).useEnch().ench("PT4","UN3","MN1","FF4","SP3","DS3"), arrayOf(Material.PHANTOM_MEMBRANE, 32), arrayOf(Material.POPPED_CHORUS_FRUIT, 32)),
                    arrayOf(ItemStack(Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE).useOriginal(), ItemStack(Material.TIPPED_ARROW, 32).usePotion().basePotion(PotionType.LONG_SLOW_FALLING), arrayOf(Material.SHULKER_SHELL, 32), ItemStack(Material.TIPPED_ARROW, 32).usePotion().basePotion(PotionType.LONG_SLOW_FALLING), ItemStack(Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE).useOriginal()),
                    arrayOf(ItemStack(Material.SPLASH_POTION).usePotion().basePotion(PotionType.LONG_SLOW_FALLING), arrayOf(Material.FEATHER, 32), ItemStack(Material.ELYTRA).useEnch().ench("UN3","MN1"), arrayOf(Material.FEATHER, 32), ItemStack(Material.SPLASH_POTION).usePotion().basePotion(PotionType.LONG_SLOW_FALLING)),
                ), CustomItem.STABILZING_SNEAKERS)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, ItemStack(Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE).ench("DU1"), null, null),
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, null, null, null),
                ), CustomItem.WARDEN_SPAWNER)
            )
            /*

            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, null, null, null),
                ), null)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, Material.STRING, null, null),
                    arrayOf(null, Material.STRING, Material.STRING, Material.STRING, null),
                    arrayOf(null, null, Material.STRING, null, null),
                    arrayOf(null, null, null, null, null),
                ), 0)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, arrayOf(Material.IRON_INGOT, 20), null, null),
                    arrayOf(null, arrayOf(Material.IRON_INGOT, 20), 0, arrayOf(Material.IRON_INGOT, 20), null),
                    arrayOf(null, null, arrayOf(Material.IRON_INGOT, 20), null, null),
                    arrayOf(null, null, null, null, null),
                ), 1)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, arrayOf(Material.IRON_BLOCK, 1), null, null),
                    arrayOf(null, arrayOf(Material.IRON_BLOCK, 1), arrayOf(Material.IRON_SWORD, 1), arrayOf(Material.IRON_BLOCK, 1), null),
                    arrayOf(null, null, arrayOf(Material.IRON_BLOCK, 1), null, null),
                    arrayOf(null, null, null, null, null),
                ), 2)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, arrayOf(Material.IRON_BLOCK, 6), null, null),
                    arrayOf(null, arrayOf(Material.IRON_BLOCK, 1), arrayOf(ItemStack(Material.DIAMOND_CHESTPLATE).ench("PT4", "UN3").useEnch(), 1), null, null),
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, null, null, null),
                ), 5)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, arrayOf(Material.IRON_BLOCK, 6), null, null),
                    arrayOf(null, arrayOf(Material.IRON_BLOCK, 1), arrayOf(ItemStack(Material.DIAMOND_CHESTPLATE).trim(ArmorTrim(TrimMaterial.DIAMOND, TrimPattern.RIB)).useTrim(), 1), null, null),
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, null, null, null),
                ), 6)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, arrayOf(Material.IRON_BLOCK, 6), null, null),
                    arrayOf(null, arrayOf(Material.IRON_BLOCK, 1), arrayOf(ItemStack(Material.OMINOUS_BOTTLE).omimous(4).useOminous(), 2), null, null),
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, null, null, null),
                ), 7)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, arrayOf(Material.IRON_BLOCK, 6), null, null),
                    arrayOf(null, arrayOf(Material.IRON_BLOCK, 1), arrayOf(ItemStack(Material.ENCHANTED_BOOK).storeEnch("FF4").useStoredEnch(), 1), null, null),
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, null, null, null),
                ), 8)
            )
            recipes.add(
                Recipe(arrayOf(
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, arrayOf(Material.IRON_BLOCK, 6), null, null),
                    arrayOf(null, arrayOf(Material.IRON_BLOCK, 1), arrayOf(ItemStack(Material.TIPPED_ARROW).basePotion(PotionType.POISON).usePotion(), 1), null, null),
                    arrayOf(null, null, null, null, null),
                    arrayOf(null, null, null, null, null),
                ), 9)
            )*/
        }

        fun getPage(page: Int): MutableList<Recipe?> {
            //return a list of recipes from page-1*24 to page*24 non inclusive
            val pageRecipes = mutableListOf<Recipe?>()
            for (i in (page-1)*35..<page*35) {
                pageRecipes.add(recipes.getOrNull(i))
            }
            return pageRecipes
        }

        fun getRecipe(index: Int): Recipe {
            return recipes[index]
        }
    }
}