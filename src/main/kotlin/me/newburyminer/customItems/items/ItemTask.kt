package me.newburyminer.customItems.items

import org.bukkit.entity.Player

class ItemTask(val task: (Player) -> (Unit)) {
    fun run(player: Player) {
        task(player)
    }
}