package me.newburyminer.customItems.bosses.rendering

import org.bukkit.entity.BlockDisplay

class DisplayWrapper(display: BlockDisplay) {
    private val uuid = display.uniqueId
    private val world = display.world

    private var cached: BlockDisplay? = display
    val block: BlockDisplay?
        get() {
            if (cached?.isValid == true) return cached
            else {
                cached = world.getEntity(uuid) as BlockDisplay?
                return cached
            }
        }
}