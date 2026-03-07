package me.newburyminer.customItems.items.behaviors

import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.helpers.cycleUp
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface ClickCycler {

    fun cycleItem(item: ItemStack, cycleItems: List<Material>) {
        val toolNum = (item.getTag<Int>("itemindex") ?: 0).cycleUp(0..<cycleItems.size)

        item.type = cycleItems[toolNum]
        item.setTag("itemindex", toolNum)
    }

}