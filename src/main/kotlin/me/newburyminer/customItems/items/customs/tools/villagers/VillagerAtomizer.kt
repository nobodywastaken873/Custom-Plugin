package me.newburyminer.customItems.items.customs.tools.villagers

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.convertVillagerLevel
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.lore
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.ItemStack

class VillagerAtomizer: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.VILLAGER_ATOMIZER

    private val material = Material.SEA_PICKLE
    private val color = arrayOf(115, 86, 50)
    private val name = text("Villager Atomizer", color)
    private val lore = Utils.loreBlockToList(
        text("Right click a villager to pick it up and turn it into item form.", Utils.GRAY)
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEntityEvent -> {
                if (!ctx.itemType.isHand()) return
                if (e.rightClicked !is Villager) return
                e.isCancelled = true
                val newItem = ItemRegistry.get(CustomItem.VILLAGER)
                val villager: Villager = e.rightClicked as Villager

                val snapshot = villager.createSnapshot()!!.asString
                newItem.setTag("storedvillager", snapshot)

                newItem.lore(
                    text("Profession: ${villager.profession}", arrayOf(255, 255, 255)),
                    text("Level: ${convertVillagerLevel(villager.villagerLevel)}", arrayOf(255, 255, 255)),
                )
                villager.remove()
                e.player.addItemorDrop(newItem)
            }

        }

    }

}
