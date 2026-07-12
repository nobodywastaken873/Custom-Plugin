package me.newburyminer.customItems.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import me.newburyminer.customItems.gui.combat.ItemClaimGui
import me.newburyminer.customItems.gui.combat.KothLootGui
import me.newburyminer.customItems.gui.combat.LootListGui
import me.newburyminer.customItems.gui.combat.PityListGui
import org.bukkit.entity.Player

class KothLootCommand: BasicCommand {
    override fun execute(context: CommandSourceStack, args: Array<out String>) {
        if (context.sender !is Player) return
        val sender = context.sender as Player
        KothLootGui(sender).open(sender)
    }

}