package me.newburyminer.customItems.effects

enum class CustomEffectType(val title: String, val color: Array<Int>) {
    ELYTRA_DISABLED("Elytra Disabled", arrayOf(171, 79, 224)),
    ATTRIBUTE("Attribute", arrayOf(160, 168, 166)),
    ENDER_CRIT("Ender Blade Criticals", arrayOf(4, 128, 125)),
    FANG_STAFF_VEXING("Vexing Aura", arrayOf(4, 126, 191)),
    GRAVE_INVULNERABILITY("Grave Invulnerability", arrayOf(93, 113, 186)),
    LAST_PRISM_ZAP("Last Prism Electrified", arrayOf(235, 232, 59)),
    DOUBLE_GRAVE_LOOTING("Double Grave Loot", arrayOf(166, 71, 58)),
    POCKET_WORMHOLE_REMAINING("Pocket Wormhole Remaining", arrayOf(211, 158, 240)),
    SURVIVAL_BUFFS("Survival Buffs", arrayOf(43, 130, 44)),
    CHEST_LOOT_BUFFS("+25% Double Chest Loot Chance", arrayOf(145, 129, 86)),
    QUADRUPLE_CHEST_LOOT("+400% Double Chest Loot Chance", arrayOf(145, 129, 86))

    ;
}