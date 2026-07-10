package me.newburyminer.customItems.loot

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface LootProvider {

    val id: String
        get() = this::class.simpleName!!
    val name: String
        get() = id
            .replace(Regex("([a-z0-9])([A-Z])"), "$1 $2") // Space between lower/digit and upper
            .replace(Regex("([A-Z])([A-Z][a-z])"), "$1 $2") // Space between consecutive capitals (e.g., XMLParser -> XML Parser)

    fun getMarker(amount: Int, context: LootContext): ItemStack
    fun getLoot(table: String, scaler: Int, player: Player, count: Int = 1): List<ItemStack>

}