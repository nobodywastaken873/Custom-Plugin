package me.newburyminer.customItems.bosses

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.bosses.definitions.warden.WardenInstance
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

object BossManager: BukkitRunnable() {
    private val activeInstances = mutableListOf<BossInstance>()

    fun removePlayer(player: Player) {
        activeInstances.forEach { it.removePlayer(player) }
    }

    fun cancelAllBosses() {
        activeInstances.forEach {
            it.endBoss()
        }
        activeInstances.clear()
    }

    override fun run() {
        activeInstances.removeIf { it.getToRemove() }
        activeInstances.forEach {
            it.tick()
        }
    }

    private fun isActive(boss: CustomBossType): Boolean {
        return activeInstances.any { it.bossType == boss }
    }

    fun spawnBoss(boss: CustomBossType, difficulty: BossDifficulty, spawner: Player, players: List<Player>): Boolean {
        if (isActive(boss)) {
            spawner.sendMessage(text("This boss is already alive. Please try again later.", Utils.FAILED_COLOR))
            return false
        }

        val instance = when (boss) {
            CustomBossType.WARDEN -> {
                WardenInstance(players.toMutableList(), difficulty)
            }
            CustomBossType.GUARDIAN -> TODO()
            CustomBossType.WITHER -> TODO()
            CustomBossType.PIGLIN -> TODO()
            CustomBossType.HUSK -> TODO()
        }

        players.forEach {
            it.teleport(instance.getCenter().add(Vector(0.01, 0.01, 0.01)))
            it.gameMode = GameMode.ADVENTURE
            it.sendMessage(text("Hit the boss to begin.", Utils.GRAY))
        }

        activeInstances.add(instance)

        return true
    }

}