package me.newburyminer.customItems.items

import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.setTag
import org.bukkit.Material
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack

interface ItemCycler {
    fun getCycleItems(item: ItemStack): Array<Material>

    fun cycleItem(item: ItemStack, e: PlayerItemHeldEvent) {

        val cycleItems = getCycleItems(item)

        val direction = if (e.newSlot == e.previousSlot + 1 || (e.previousSlot == 8 && e.newSlot == 0)) {
            Direction.FORWARD
        } else if (e.newSlot == e.previousSlot - 1 || (e.previousSlot == 0 && e.newSlot == 8)) {
            Direction.BACKWARD
        } else {
            null
        }

        if (direction != null) e.isCancelled = true
        else return

        val current = item.getTag<Int>("toolindex") ?: 0
        val newIndex = if (direction == Direction.FORWARD) {
            if (current == getCycleItems(item).lastIndex) 0
            else current + 1
        } else {
            if (current == 0) cycleItems.lastIndex
            else current - 1
        }
        item.type = cycleItems[newIndex]
        item.setTag("toolindex", newIndex)
    }

    enum class Direction {
        FORWARD,
        BACKWARD
    }
}