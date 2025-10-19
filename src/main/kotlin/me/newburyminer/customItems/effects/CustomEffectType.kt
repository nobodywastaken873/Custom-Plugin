package me.newburyminer.customItems.effects

import me.newburyminer.customItems.Utils
import net.kyori.adventure.text.Component

enum class CustomEffectType(val title: String, val color: Array<Int>) {
    ELYTRA_DISABLED("Elytra Disabled", arrayOf(171, 79, 224)),
    ATTRIBUTE("Attribute", arrayOf(160, 168, 166)),
    ENDER_CRIT("Ender Blade Criticals", arrayOf(4, 128, 125)),
    FANG_STAFF_VEXING("Vexing Aura", arrayOf(4, 126, 191)),
    GRAVE_INVULNERABILITY("Grave Invulnerability", arrayOf(93, 113, 186)),
    LAST_PRISM_ZAP("Last Prism Electrified", arrayOf(235, 232, 59)),

    ;
}