package me.newburyminer.customItems.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import me.newburyminer.customItems.gui.loot.PityListGui
import org.bukkit.entity.Player

class PityCommand: BasicCommand {
    override fun execute(context: CommandSourceStack, args: Array<out String>) {
        if (context.sender !is Player) return
        val sender = context.sender as Player
        PityListGui(sender).open(sender)
    }

}