package me.newburyminer.customItems.structures

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.setTag
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.math.pow

class LootListener: Listener {

    @EventHandler fun onItemSpawn(e: ItemSpawnEvent) {
        if (e.entity.itemStack.type != Material.PAPER) return
        if (!e.entity.itemStack.itemMeta.hasCustomModelDataComponent()) return
        val wholeTag = e.entity.itemStack.itemMeta.customModelDataComponent.strings.first()
        val splitIndex = if (wholeTag.indexOf("normal") != -1) wholeTag.indexOf("normal") else wholeTag.indexOf("ominous")
        val structure = CustomStructure.get(wholeTag.substring(0, splitIndex - 1))
        val difficulty = wholeTag.substring(splitIndex, wholeTag.substring(splitIndex).indexOf("_")+splitIndex)
        val type = wholeTag.substring(wholeTag.substring(splitIndex).indexOf("_")+1+splitIndex)
        val color = when (difficulty + type) {
            "normalspawner" -> arrayOf(255, 146, 20)
            "ominousspawner" -> arrayOf(54, 114, 245)
            "normalvault" -> arrayOf(255, 200, 20)
            "ominousvault" -> arrayOf(27, 136, 166)
            else -> arrayOf(0,0,0)
        }

        val newItem = e.entity.itemStack
        val newMeta = newItem.itemMeta
        val newComponent = newMeta.customModelDataComponent
        newComponent.strings = mutableListOf(difficulty + "_" + type)
        newMeta.setCustomModelDataComponent(newComponent)
        newItem.itemMeta = newMeta
        newItem.setTag("lootquality", e.entity.location.length())
        newItem.setTag("structure", structure.tag)
        newItem.setTag("difficulty", difficulty)
        newItem.setTag("type", type)
        newItem.name(Utils.text(structure.text + " " + difficulty.capitalize() + " " + type.capitalize(), color))
        e.entity.itemStack = newItem
    }

    @EventHandler fun onPlayerRightClick(e: PlayerInteractEvent) {
        if (e.item == null) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        if (e.item!!.type != Material.PAPER) return
        if (e.item!!.getTag<String>("structure") == null) return

        val structure = CustomStructure.get(e.item!!.getTag<String>("structure")!!)
        val type = e.item!!.getTag<String>("type")!!
        val difficulty = e.item!!.getTag<String>("difficulty")!!
        val table = structure.loot.get(type, difficulty)

        for (item in table.roll(e.item!!.getTag<Double>("lootquality")!!.pow(0.3))) {
            e.player.addItemorDrop(item)
        }
        e.item!!.amount -= 1
    }

}