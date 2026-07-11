package me.newburyminer.customItems.loot

import me.newburyminer.customItems.Utils.Companion.beautify

data class Pity(
    val id: String,
    val threshold: Int
) {
    val name: String
        get() = id.beautify()
}