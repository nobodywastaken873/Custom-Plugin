package me.newburyminer.customItems.items.behaviors

import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.helpers.cycleDown
import me.newburyminer.customItems.helpers.cycleUp
import org.bukkit.Material
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack

const val ITEM_TAG: String = "itemindex"
interface ScrollCycler {
    fun getCycleItems(item: ItemStack): Array<Material>

    fun scrollCycle(item: ItemStack, e: PlayerItemHeldEvent): Boolean {

        val cycleItems = getCycleItems(item)

        val direction = if (e.newSlot == e.previousSlot + 1 || (e.previousSlot == 8 && e.newSlot == 0))
            Direction.FORWARD
        else if (e.newSlot == e.previousSlot - 1 || (e.previousSlot == 0 && e.newSlot == 8))
            Direction.BACKWARD
        else null

        if (direction != null) e.isCancelled = true
        else return false

        val current = item.getTag<Int>(ITEM_TAG) ?: 0
        val newIndex =
            if (direction == Direction.FORWARD) current.cycleUp(0..<cycleItems.size)
            else current.cycleDown(0..<cycleItems.size)
        item.type = cycleItems[newIndex]
        item.setTag(ITEM_TAG, newIndex)
        return true
    }

    enum class Direction {
        FORWARD,
        BACKWARD
    }
}