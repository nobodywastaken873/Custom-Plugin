package me.newburyminer.customItems.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.gui.GuiInventory
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.DoubleChestInventory
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class Craft : BasicCommand {
    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        if (stack.sender !is Player) return
        val player = stack.sender as Player
        //if (!sender.isOp) {sender.sendMessage(Component.text("You do not have permission to use this command.").color(TextColor.color(255, 0, 0))); return false}
        //if (args[0].isEmpty()) {sender.velocity = sender.velocity.add(Vector(0, 100, 0)); return false}
        val craftingTable = GuiInventory("craft").inventory
        craftingTable.contents = GuiInventory.craftInv
        player.openInventory(craftingTable)
    }
}