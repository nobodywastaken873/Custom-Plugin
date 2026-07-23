package me.newburyminer.customItems.commands.operator

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import me.newburyminer.customItems.systems.AridLandsSystem
import me.newburyminer.customItems.systems.EndSystem
import org.bukkit.entity.Player

class AridOpenCommand: BasicCommand {
    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        if (stack.sender !is Player) return
        if (!stack.sender.isOp) return
        AridLandsSystem.spawnPortals()
    }
}