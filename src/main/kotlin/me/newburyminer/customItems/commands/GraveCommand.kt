package me.newburyminer.customItems.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import me.newburyminer.customItems.gui.GraveListHolder
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class GraveCommand: BasicCommand {
    override fun execute(context: CommandSourceStack, args: Array<out String>) {
        if (context.sender !is Player) return
        val sender = context.sender as Player
        // should make custom grave list inventory to allow for multiple pages, possible allow deleting locations from the gravelist
        // only allow deleting if easy (would have to change player gravelist, and add confirmation, and delete)
        // could be used to prepare for creating grave teleport eventually (with 10 min cd or smth)
        sender.openInventory(GraveListHolder(sender, 0).inventory)
    }

}