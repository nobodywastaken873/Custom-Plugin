package me.newburyminer.customItems.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import me.newburyminer.customItems.gui.InfoGui
import me.newburyminer.customItems.systems.EndSystem
import org.bukkit.entity.Player

class EndFightCommand: BasicCommand {
    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        if (stack.sender !is Player) return
        if (!stack.sender.isOp) return
        EndSystem.start()
    }
}