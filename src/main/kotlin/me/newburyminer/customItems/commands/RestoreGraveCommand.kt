package me.newburyminer.customItems.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.decodeToDoubleArray
import me.newburyminer.customItems.gui.GuiInventory
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.io.File
import java.util.Base64

class RestoreGraveCommand: BasicCommand {
    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        if  (stack.sender !is ConsoleCommandSender) return
        val restoreLoc = Bukkit.getPlayer(args[0])!!.location
        val line = args[1].toInt()
        //if (!sender.isOp) {sender.sendMessage(Component.text("You do not have permission to use this command.").color(TextColor.color(255, 0, 0))); return false}
        //if (args[0].isEmpty()) {sender.velocity = sender.velocity.add(Vector(0, 100, 0)); return false}
        val file = File("plugins/customItems/savedGraves.txt")
        val fullString = file.readText()
        val lines = fullString.split("NEWLINE")
        val currentLine = lines[line]
        //Bukkit.getLogger().info(currentLine.substring(currentLine.indexOf("LOCATION") + 8, currentLine.indexOf("ITEMSTACK")))
        //Bukkit.getLogger().info(Base64.getDecoder().decode(currentLine.substring(currentLine.indexOf("LOCATION") + 8, currentLine.indexOf("ITEMSTACK"))).decodeToDoubleArray().toString())
        val loc = Utils.deserializeLocationBytes(Base64.getDecoder().decode(currentLine.substring(currentLine.indexOf("LOCATION") + 8, currentLine.indexOf("ITEMSTACK"))))
        if (args.size > 2) {
            Bukkit.getLogger().info(loc.toString())
            return
        }
        val items = mutableListOf<ItemStack>()
        var startIndex = 0
        while (currentLine.indexOf("ITEMSTACK", startIndex) != -1) {
            val itemString = if (currentLine.indexOf("ITEMSTACK", currentLine.indexOf("ITEMSTACK", startIndex) + 1) == -1)
                currentLine.substring(currentLine.indexOf("ITEMSTACK", startIndex) + 9, currentLine.indexOf("\n", currentLine.indexOf("ITEMSTACK", startIndex) + 1))
            else
                currentLine.substring(currentLine.indexOf("ITEMSTACK", startIndex) + 9, currentLine.indexOf("ITEMSTACK", currentLine.indexOf("ITEMSTACK", startIndex) + 1))
            startIndex = currentLine.indexOf("ITEMSTACK", startIndex) + 1
            items.add(ItemStack.deserializeBytes(Base64.getDecoder().decode(itemString)))
        }

        //Bukkit.getLogger().info(loc.toString())
        //Bukkit.getLogger().info(items.toString())

        for (item in items) {
            loc.world.spawn(restoreLoc, Item::class.java) {
                it.itemStack = item
            }
        }
    }
}