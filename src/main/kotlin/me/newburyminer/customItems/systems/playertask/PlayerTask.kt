package me.newburyminer.customItems.systems.playertask

import org.bukkit.entity.Player

class PlayerTask(val task: (Player) -> (Unit)) {
    fun run(player: Player) {
        task(player)
    }
}