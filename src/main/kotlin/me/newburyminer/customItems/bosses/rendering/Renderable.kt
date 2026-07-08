package me.newburyminer.customItems.bosses.rendering

import org.bukkit.World

interface Renderable {

    fun spawn(world: World)

    fun update()

    fun remove()

}