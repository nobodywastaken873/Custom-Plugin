package me.newburyminer.customItems.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import me.newburyminer.customItems.gui.CraftingGui
import org.bukkit.entity.Player

class Craft : BasicCommand {
    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        if (stack.sender !is Player) return
        val player = stack.sender as Player
        //if (!sender.isOp) {sender.sendMessage(Component.text("You do not have permission to use this command.").color(TextColor.color(255, 0, 0))); return false}
        //if (args[0].isEmpty()) {sender.velocity = sender.velocity.add(Vector(0, 100, 0)); return false}
        CraftingGui().open(player)
    }
}