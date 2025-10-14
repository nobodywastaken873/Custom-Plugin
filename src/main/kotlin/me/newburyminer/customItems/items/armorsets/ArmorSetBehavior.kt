package me.newburyminer.customItems.items.armorsets

import org.bukkit.entity.Player

interface ArmorSetBehavior {
    val set: ArmorSet
    fun handle(ctx: ArmorSetEventContext)

    val period: Int
        get() = 20
    fun runTask(player: Player) {}
}