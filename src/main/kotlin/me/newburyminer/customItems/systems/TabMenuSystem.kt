package me.newburyminer.customItems.systems

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.effects.EffectManager
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

object TabMenuSystem: BukkitRunnable() {

    interface Provider {
        fun getLines(player: Player): List<Component>
    }

    private val providers: List<Provider> = listOf(
        KothSystem,
        EffectManager
    )

    override fun run() {
        for (player in Bukkit.getOnlinePlayers()) {
            player.sendPlayerListFooter(getFullComponent(player))
        }
    }

    private fun getFullComponent(player: Player): Component {
        var baseComponent = Utils.text("\n")
        providers.forEachIndexed { index, provider ->
            val lines = provider.getLines(player)
            lines.forEachIndexed { index, component ->
                baseComponent = baseComponent.append(component)
                if (index != lines.lastIndex) baseComponent = baseComponent.append(Utils.text("\n"))
            }
            if (index != providers.lastIndex) baseComponent = baseComponent.append(Utils.text("\n"))
        }
        return baseComponent
    }

}