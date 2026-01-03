package me.newburyminer.customItems.systems

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.setAttr
import me.newburyminer.customItems.Utils.Companion.setTag
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerTeleportEvent

object EndSystem: Listener {

    private var isStarted = false
    private var isFullyStarted = false
    fun start() {
        isStarted = true
        Bukkit.getOnlinePlayers().forEach {
            it.setTag("isafk", false)
            it.setTag("afktime", 0)
            sendToCenter(it)
            it.showTitle(Title.title(Utils.text("The fight will begin in 60 seconds.", arrayOf(204, 47, 12), bold = true),
                Utils.text("You have invulnerability until then.", Utils.GRAY)))
            it.isInvulnerable = true
            it.getAttribute(Attribute.GRAVITY)?.baseValue = 0.0
        }

        Bukkit.getScheduler().runTaskLater(CustomItems.plugin, Runnable {
            val end = Bukkit.getWorlds()[2]
            Bukkit.getOnlinePlayers().forEach { it.getAttribute(Attribute.GRAVITY)?.baseValue = 0.08 }
            for (x in -100..100) for (y in 55..60) for (z in -100..100) {
                Location(end, x.toDouble(), y.toDouble(), z.toDouble()).block.type = Material.END_STONE
            }

            for (x in -10..10) for (y in 55..70) for (z in -10..10) {
                if (Location(end, x.toDouble(), y.toDouble(), z.toDouble()).block.type == Material.END_PORTAL) {
                    Location(end, x.toDouble(), y.toDouble(), z.toDouble()).block.type = Material.AIR
                }
            }
        }, 200L)

        Bukkit.getScheduler().runTaskLater(CustomItems.plugin, Runnable {
            Bukkit.getOnlinePlayers().forEach {
                it.showTitle(Title.title(Utils.text("The fight has begun.", arrayOf(204, 47, 12), bold = true),
                    Utils.text("Only one player can win.", Utils.GRAY)))
                it.isInvulnerable = false
            }
            isFullyStarted = true
        }, 1200L)
    }

    //@EventHandler fun onPlayerTeleport(e: PlayerTeleportEvent) {}
    @EventHandler fun onPlayerMove(e: PlayerMoveEvent) {
        if (!isStarted) return
        if (e.player.world == Bukkit.getWorlds()[2]) return
        if (isFullyStarted) {
            e.player.gameMode = GameMode.SPECTATOR
        }
        else if (isStarted) {
            e.player.isInvulnerable = true
        }

        sendToCenter(e.player)
    }

    @EventHandler fun onPlayerDeath(e: PlayerDeathEvent) {
        if (!isStarted) {return}
        e.player.gameMode = GameMode.SPECTATOR
        Bukkit.getScheduler().runTaskLater(CustomItems.plugin, Runnable {
            if (Bukkit.getOnlinePlayers().count { it.gameMode == GameMode.SURVIVAL } != 1) return@Runnable
            val finalPlayer = Bukkit.getOnlinePlayers().first { it.gameMode == GameMode.SURVIVAL }
            Bukkit.getOnlinePlayers().forEach {
                it.showTitle(Title.title(Utils.text("${finalPlayer.name} has won!", arrayOf(227, 198, 9), bold = true),
                    Utils.text("")))
                it.playSound(it.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.0F, 0.5F)
                sendToCenter(it)
                it.gameMode = GameMode.SURVIVAL
            }
            isFullyStarted = false
            isStarted = false
        }, 100L)
    }

    private fun sendToCenter(player: Player) {
        val end = Bukkit.getWorlds()[2]
        player.teleport(Location(end, 20.0, 80.0, 0.0))
    }

}