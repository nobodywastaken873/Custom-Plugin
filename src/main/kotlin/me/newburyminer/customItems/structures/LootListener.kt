package me.newburyminer.customItems.structures

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.readableName
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.loot.LootContext
import me.newburyminer.customItems.loot.PlayerLootManager
import me.newburyminer.customItems.mobprovider.MobContext
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.math.pow
import kotlin.math.roundToInt

class LootListener: Listener {

    @EventHandler fun onItemSpawn(e: ItemSpawnEvent) {
        if (e.entity.itemStack.type != Material.PAPER) return
        if (!e.entity.itemStack.itemMeta.hasCustomModelDataComponent()) return

        // Retrieve structure reference
        val wholeTag = e.entity.itemStack.itemMeta.customModelDataComponent.strings.first()
        val reference = StructureRegistry.lookupLootTag(wholeTag)
        val definition = reference.structure

        val difficulty = MobContext(e.entity.location.length(), reference.difficulty, e.entity.location).difficulty
        val lootContext = LootContext(definition.lootProvider.id, "vault", difficulty.roundToInt().coerceAtMost(30))

        val lootGetter = e.entity.location.getNearbyPlayers(10.0).sortedBy { e.entity.location.subtract(it.location).length() }.firstOrNull() ?: return
        PlayerLootManager.addLoot(lootContext, lootGetter)

        /*val color = reference.getColor()

        // Initialize item
        val newItem = e.entity.itemStack
        val newMeta = newItem.itemMeta

        // Add custom model data
        val newComponent = newMeta.customModelDataComponent
        newComponent.strings = mutableListOf(reference.difficulty.name + "_" + reference.difficulty.name)
        newMeta.setCustomModelDataComponent(newComponent)
        newItem.itemMeta = newMeta

        // Configure name, lootquality, etc on new item
        newItem.setTag("lootquality", (e.entity.location.length() / 200.0).roundToInt() * 200)
        newItem.setTag("loottag", wholeTag)
        newItem.name(Utils.text(reference.structure.name + " " + reference.difficulty.readableName() + " " + reference.type.readableName(), color))
        e.entity.itemStack = newItem*/
    }

    // needs to be redone
    /*@EventHandler fun onPlayerRightClick(e: PlayerInteractEvent) {
        if (e.item == null) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        val item = e.item ?: return
        if (item.type != Material.PAPER) return
        if (item.getTag<String>("structure") == null) return

        val structure = CustomStructure2.get(item.getTag<String>("structure")!!)
        val type = item.getTag<String>("type")!!
        val difficulty = item.getTag<String>("difficulty")!!
        val table = structure.loot.get(type, difficulty)

        for (item in table.roll(item.getTag<Int>("lootquality")!!.toDouble().pow(0.3))) {
            e.player.addItemorDrop(item)
        }
        item.amount -= 1
    }*/

}