package me.newburyminer.customItems.items

import org.bukkit.entity.Player

interface CustomItemBehavior {
    fun handle(ctx: EventContext)
    val extraTasks: Map<Int, (Player) -> Unit> get() = emptyMap()
}