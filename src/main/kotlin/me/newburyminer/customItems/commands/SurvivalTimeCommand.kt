package me.newburyminer.customItems.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.round
import me.newburyminer.customItems.systems.SurvivalTimeSystem
import org.bukkit.entity.Player
import java.time.Duration
import kotlin.math.roundToInt

class SurvivalTimeCommand: BasicCommand {
    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        if (stack.sender !is Player) return
        val player = stack.sender as Player

        val totalTime = SurvivalTimeSystem.getSurvived(player)
        val buffFraction = SurvivalTimeSystem.getBuffFraction(totalTime)

        val duration = Duration.ofSeconds(totalTime.toLong())
        val buffStartDuration = Duration.ofSeconds((4 * 3600L - totalTime.toLong()).coerceAtLeast(0))
        val buffMaxDuration = Duration.ofSeconds((20 * 3600L - totalTime.toLong()).coerceAtLeast(0))

        player.sendMessage(Utils.text("You have survived for ${duration.toHours()} hours and ${duration.toMinutesPart()} minutes. " +
                if (buffFraction > 0.0) "This gives you ${(10 * buffFraction).round(2)} mining efficiency and +${(40 * buffFraction).round(3)}% chance for double chest loot, ${(buffFraction * 100).roundToInt()}% of the total buffs. " else {""} +
                if (buffFraction <= 0.0000001) "You have ${buffStartDuration.toHours()} hours and ${buffStartDuration.toMinutesPart()} minutes until you start receiving buffs." else {""} +
                if (buffFraction < 0.9999999 && buffFraction > 0.0000001) "You have ${buffMaxDuration.toHours()} hours and ${buffMaxDuration.toMinutesPart()} minutes until you have max buffs." else {""},
        Utils.SUCCESS_COLOR))
    }
}