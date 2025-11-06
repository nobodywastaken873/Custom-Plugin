package me.newburyminer.customItems.entity.components.utils

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.entity.EntityWrapper
import org.bukkit.Bukkit

interface SpellInterface {

    val spellDuration: Int

    fun startCasting(wrapper: EntityWrapper): Boolean {
        if (wrapper.isCasting()) return false
        wrapper.setCasting(true)

        Bukkit.getScheduler().runTaskLater(CustomItems.plugin, Runnable {
            wrapper.setCasting(false)
        }, spellDuration.toLong())
        return true
    }

}