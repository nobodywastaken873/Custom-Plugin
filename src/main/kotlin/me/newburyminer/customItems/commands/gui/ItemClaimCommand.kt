package me.newburyminer.customItems.commands.gui

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import me.newburyminer.customItems.gui.loot.ItemClaimGui
import org.bukkit.entity.Player

class ItemClaimCommand: BasicCommand {
    override fun execute(context: CommandSourceStack, args: Array<out String>) {
        if (context.sender !is Player) return
        val sender = context.sender as Player
        ItemClaimGui(sender).open(sender)
    }

}