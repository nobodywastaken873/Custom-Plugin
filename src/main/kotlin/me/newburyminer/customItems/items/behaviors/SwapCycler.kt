package me.newburyminer.customItems.items.behaviors

import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.helpers.cycleUp
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

const val GROUP_TAG: String = "groupindex"
const val STORED_GROUP_TAG: String = "storedinner"
interface SwapCycler: ScrollCycler {

    override fun getCycleItems(item: ItemStack): Array<Material> {
        return getCycleItems(item, getCurrentGroup(item))
    }

    fun getCycleItems(item: ItemStack, group: Int): Array<Material>
    fun getCurrentGroup(item: ItemStack): Int = item.getTag<Int>(GROUP_TAG) ?: 0

    fun swapCycle(item: ItemStack, groupSize: IntRange) {
        val currentGroup = item.getTag<Int>(GROUP_TAG) ?: 0
        val newGroup = currentGroup.cycleUp(groupSize)

        val storedIndexes = item.getTag<IntArray>(STORED_GROUP_TAG) ?: arrayOf(0, 0, 0, 0).toIntArray()
        val currentIndex = item.getTag<Int>(ITEM_TAG) ?: 0
        val newIndex = storedIndexes[newGroup]
        storedIndexes[currentGroup] = currentIndex

        item.setTag(GROUP_TAG, newGroup)
        item.setTag(ITEM_TAG, newIndex)
        item.setTag(STORED_GROUP_TAG, storedIndexes)
        item.type = getCycleItems(item)[newIndex]
    }
}