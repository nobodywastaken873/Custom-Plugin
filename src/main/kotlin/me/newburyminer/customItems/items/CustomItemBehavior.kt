package me.newburyminer.customItems.items

import org.bukkit.entity.Player

interface CustomItemBehavior {
    fun handle(ctx: EventContext)
    fun runTask(player: Player) {}
}