package me.newburyminer.customItems.bosses.rendering

import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Entity

abstract class DisplayRenderable : Renderable {

    protected val displays = mutableListOf<DisplayWrapper>()

    override fun remove() {
        displays.forEach {it.block?.remove()}
        displays.clear()
    }

}