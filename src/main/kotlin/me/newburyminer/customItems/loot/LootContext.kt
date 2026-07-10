package me.newburyminer.customItems.loot

import me.newburyminer.customItems.CustomItems

data class LootContext(
    val id: String,
    val table: String,
    val scaler: Int
) {

    init {
        val invalid = listOf("-", ":")
        if (invalid.any {it in id || it in table}) {
            CustomItems.plugin.logger.warning("LootContext $id is invalid.")
        }
    }

    fun toStringData(): String {
        return "$id-$table-$scaler"
    }

    companion object {
        fun fromStringData(string: String): LootContext {
            val items = string.split("-")
            return LootContext(items[0], items[1], items[2].toInt())
        }
    }
}