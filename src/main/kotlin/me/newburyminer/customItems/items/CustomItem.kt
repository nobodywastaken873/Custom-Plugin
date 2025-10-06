package me.newburyminer.customItems.items

import me.newburyminer.customItems.CustomItems

enum class CustomItem(val cds: Array<String> = arrayOf(), var realName: String = "") {
    ALL,
    JERRY_IDOL,
    VILLAGER_ATOMIZER,
    VILLAGER,
    GOLDEN_ZOMBIE,
    FANGED_STAFF(arrayOf("Fangs", "Vexing")),
    TOTEM_CORE,
    FLETCHER_UPGRADE,
    DRIPSTONE_ARROW,
    ENDER_PEARL_ARROW,
    WITHER_SKULL_ARROW,
    LLAMA_SPIT_ARROW,
    SHULKER_BULLET_ARROW,
    WIND_HOOK(arrayOf("")),
    INPUT_DEVICES,
    MINECART_MATERIALS,
    ACTUAL_REDSTONE,
    CONTAINERS,
    REDSTONE_AMALGAMATION,
    REDSTONE_REPEATER,
    POLARIZED_MAGNET,
    LAST_PRISM(arrayOf("Beam", "Zap")),
    MULTI_LOAD_CROSSBOW,
    VEINY_PICKAXE(arrayOf("")),
    TREECAPITATOR,
    EXCAVATOR,
    PEW_MATIC_HORN(arrayOf("")),
    TRADING_SCRAMBLER,
    EXPERIENCE_FLASK,
    NETHERITE_MULTITOOL,
    HOEVEL,
    AXEPICK,
    NETHERITE_MATTOCK,
    POCKETKNIFE_MULTITOOL,
    HOE,
    HOOKED_CUTLASS,
    AXE_OF_PEACE,
    ENDER_BLADE(arrayOf("")),
    HEAVY_GREATHAMMER,
    CRESTED_DAGGER,
    FIERY_SHARD,
    MYSTICAL_GREEN_APPLE,
    ENDER_NODE,
    SOUL_CRYSTAL,
    NETHERITE_COATING,
    WITHER_COATING,
    REINFORCING_STRUTS,
    SHULKER_FRUIT,
    WARDEN_SPAWNER,
    WITHER_SPAWNER,
    BASTION_SPAWNER,
    MONUMENT_SPAWNER,
    DESERT_SPAWNER,
    SURFACE_TO_AIR_MISSILE(arrayOf("")),
    TRIPLE_SWIPE_SWORD(arrayOf("")),
    WIND_CHARGE_CANNON(arrayOf("")),
    SNIPER_RIFLE(arrayOf("")),
    GRAVITY_HAMMER(arrayOf("")),
    RIDABLE_CROSSBOW(arrayOf("")),
    LANDMINE_LAUNCHER(arrayOf("")),
    MOON_BOOTS,
    COWBOY_HAT,
    DOUBLE_JUMP_BOOTS(arrayOf("")),
    TURTLE_SHELL(arrayOf("")),
    DRINKING_HAT,
    HERMESS_TROUSERS,
    SHADOW_LEGS(arrayOf("")),
    BERSERKER_CHESTPLATE,
    XRAY_GOGGLES(arrayOf("")),
    REPELLANT_PANTS,
    MACE_SHIELDED_PLATING,
    MOLTEN_CHESTPLATE,
    TOOLBELT,
    CLOUD_BOOTS,
    INVISIBILITY_CLOAK,
    AQUEOUS_SANDALS(arrayOf("")),
    ENCRUSTED_PANTS,
    WELDING_HELMET,
    ANTI_VENOM_SHIRT,
    ENERGY_RESTORING_PANTS,
    STABILZING_SNEAKERS,
    JETPACK_CONTROLLER_SET,
    JETPACK,
    JETPACK_CONTROLLER,
    HARD_HAT,
    STEEL_TOED_BOOTS,
    ASSASSINS_HOOD,
    ASSASSINS_ROBE,
    ASSASSINS_LEGGINGS,
    ASSASSINS_LOAFERS,
    WARRIOR_HELM,
    WARRIOR_CHESTPLATE(arrayOf("")),
    WARRIOR_GREAVES,
    WARRIOR_BOOTS,
    MINERS_HELM,
    TRACKING_COMPASS,
    REINFORCED_CAGE,
    DARK_STEEL_RAPIER(arrayOf("")),
    FROZEN_SHARD(arrayOf("")),
    BARBED_BLADE(arrayOf("")),
    SONIC_CROSSBOW(arrayOf("")),
    DUAL_BARRELED_CROSSBOW,
    MECHANIZED_ELYTRA(arrayOf("Boost")),
    WARDEN_HEART,
    FRAGMENT_OF_SOUND,
    WARDEN_CARAPACE,
    SHADOW_DISC_CORE,


    /*
    TEST2(1, 0),
    REINFORCED_IRON_SWORD(2, 0),
    EATABLE_BLOCK(3, 0),
    WOODCUTTER(4, 0),
    NETHERITE_CHESTPLATE(5, 0),
    VERY_ENCHANT(6, 0),
    POT(7, 0),
    FIRE(8, 0),
    OMMY(9, 0),
    VILLAG_A(10, 1),*/
    ;

    val id: Int
        get() {
            return this.ordinal
        }

    init {
        var newName = ""
        for (word in this.name.split("_")) {
            var newWord = word.lowercase()
            newWord = newWord[0].uppercase() + if (newWord.length > 1) newWord.substring(1) + " " else " "
            newName += newWord
        }
        newName = newName.substring(0, newName.lastIndex)
        this.realName = newName
    }

    fun getCooldown(order: String = ""): String {
        return this.name + "_" + order
    }
}