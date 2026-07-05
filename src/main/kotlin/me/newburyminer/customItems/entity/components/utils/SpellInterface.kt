package me.newburyminer.customItems.entity.components.utils

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.entity.EntityWrapper
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

interface SpellInterface {

    val spellDuration: Int
    var castingTicks: Int
    var cancelTask: BukkitTask?

    fun startCasting(wrapper: EntityWrapper): Boolean {
        if (wrapper.isCasting()) return false
        wrapper.setCasting(true)
        castingTicks = spellDuration

        cancelTask = Bukkit.getScheduler().runTaskLater(CustomItems.plugin, Runnable {
            wrapper.setCasting(false)
        }, spellDuration.toLong() + 10)
        return true
    }

    fun cancelCasting(wrapper: EntityWrapper) {
        if (castingTicks == -1) return
        wrapper.setCasting(false)
        castingTicks = -1
        cancelTask?.cancel()
    }

    fun checkValidTarget(wrapper: EntityWrapper, player: Player?): Boolean {
        return player != null &&
                player.world == wrapper.entity.world &&
                player.location.subtract(wrapper.entity.location).length() < 256 &&
                player.isValid &&
                player.isOnline
    }

}