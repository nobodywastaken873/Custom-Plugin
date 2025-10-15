package me.newburyminer.customItems.items.customs.tools.spawners

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.isBeingTracked
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entities.bosses.CustomBoss
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class MonumentSpawner: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MONUMENT_SPAWNER

    private val material = Material.POPPED_CHORUS_FRUIT
    private val color = arrayOf(117, 228, 230)
    private val name = text("Monument Spawner", color)
    private val lore = Utils.loreBlockToList(
        text("Right click to consume this item and begin the custom guardian boss. It will teleport players within 10 blocks of you as well. You cannot use this while being tracked, while in combat, or if someone else is fighting the boss already.", Utils.GRAY)
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEvent -> {
                if (!ctx.itemType.isHand()) return
                val item = ctx.item ?: return
                if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
                if (e.player.isBeingTracked()) return

                val boss = CustomBoss.GUARDIAN
                if (boss.isAlive()) {
                    e.player.sendMessage(text("This boss is already alive. Please try again later.", Utils.FAILED_COLOR))
                    return
                }

                item.amount -= 1
                for (player in e.player.location.getNearbyPlayers(20.0)) {
                    player.teleport(boss.getCenter())
                    player.gameMode = GameMode.ADVENTURE
                    player.sendMessage(text("Hit the boss to begin.", Utils.GRAY))
                }

                //CustomItems.bossListener.monumentSummon()
            }

        }

    }

}
