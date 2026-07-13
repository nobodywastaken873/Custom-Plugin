package me.newburyminer.customItems.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import me.newburyminer.customItems.gui.loot.LootListGui
import org.bukkit.entity.Player

class LootCommand: BasicCommand {
    override fun execute(context: CommandSourceStack, args: Array<out String>) {
        if (context.sender !is Player) return
        val sender = context.sender as Player
        LootListGui(sender).open(sender)
    }

}