package me.newburyminer.customItems.commands.operator

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.gui.crafting.RecipeCreationGui
import org.bukkit.entity.Player

class MakeRecipeCommand: BasicCommand {
    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        if (stack.sender !is Player) return
        val sender = stack.sender as Player
        if (!sender.isOp) {sender.sendMessage(
            Utils.text("You do not have permission to use this command.", arrayOf(255, 0, 0)))
            return
        }

        RecipeCreationGui().open(sender)
    }
}