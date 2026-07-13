package me.newburyminer.customItems.gui.loot

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.getItemAction
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.lock
import me.newburyminer.customItems.Utils.Companion.maxStack
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.setItemAction
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.gui.GuiItems
import me.newburyminer.customItems.gui.GuiLayout
import me.newburyminer.customItems.gui.ItemAction
import me.newburyminer.customItems.gui.PagedGui
import me.newburyminer.customItems.loot.LootRegistry
import me.newburyminer.customItems.loot.PlayerLootManager
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class LootListGui(private val player: Player, page: Int = 0): PagedGui(page) {

    override val inv: Inventory = Bukkit.createInventory(this, 54, Utils.text("${player.name}'s Loot").style(Style.style(TextDecoration.BOLD)))
    private val loot = PlayerLootManager.getAllLoot(player).toList().toMutableList()
    private val itemsPerPage = 35
    private var openLoot: Int? = null

    init {
        openPage(page)
    }

    override fun open(player: Player) {
        player.openInventory(inv)
    }

    override fun openPage(newPage: Int) {
        openLoot = null
        GuiLayout.clearInventory(inv)
        GuiLayout.setMaxBorder(Material.YELLOW_STAINED_GLASS_PANE, inv)

        for (i in itemsPerPage * newPage..<itemsPerPage * (newPage + 1)) {
            val (context, count) = loot.getOrNull(i) ?: break
            val provider = LootRegistry.getProvider(context.id)
            val item = provider.getMarker(count, context).setItemAction(ItemAction.OPEN_SUBMENU)
                .setTag("loot", i)
            inv.addItem(item)
        }

        // we want 0-35 items to be 1 page, 36-70 to be 2, etc
        val pages = (loot.size - 1) / itemsPerPage + 1
        GuiLayout.addArrows(newPage, pages, inv)

        GuiLayout.fillEmpty(Material.LIGHT_GRAY_STAINED_GLASS_PANE, inv)
    }

    private fun openLootNum(lootNum: Int) {
        openLoot = lootNum
        GuiLayout.clearInventory(inv)
        GuiLayout.setMaxBorder(Material.BLACK_STAINED_GLASS_PANE, inv)

        // Set top to the loot marker item with current count, update when opening
        val provider = LootRegistry.getProvider(loot[lootNum].first.id)
        val maxCount = loot[lootNum].second

        val openOneItem = ItemStack(Material.IRON_BLOCK)
            .lock()
            .name(Utils.text("Open one lootbox!", arrayOf(235, 217, 52)))
            .setItemAction(ItemAction.OPEN_ONE)

        val openFiveItem = ItemStack(Material.GOLD_BLOCK)
            .lock()
            .name(Utils.text("Open five lootboxes!", arrayOf(235, 217, 52)))
            .setItemAction(ItemAction.OPEN_FIVE)
        openFiveItem.amount = 5

        val openAllItem = ItemStack(Material.DIAMOND_BLOCK)
            .lock()
            .name(Utils.text("Open fifty lootboxes!", arrayOf(235, 217, 52)))
            .setItemAction(ItemAction.OPEN_FIFTY)
            .maxStack(50)
        openAllItem.amount = 50

        inv.setItem(4, provider.getMarker(loot[lootNum].second, loot[lootNum].first))
        inv.setItem(20, openOneItem)
        inv.setItem(22, openFiveItem)
        inv.setItem(24, openAllItem)
        inv.setItem(49, GuiItems.BACK_ARROW)

        GuiLayout.fillEmpty(Material.LIGHT_GRAY_STAINED_GLASS_PANE, inv)
    }

    private fun openLoot(amount: Int) {

        val lootEntry = loot[openLoot ?: return]
        val correctedAmount = amount.coerceAtMost(lootEntry.second)
        val provider = LootRegistry.getProvider(lootEntry.first.id)

        provider.getLoot(lootEntry.first.table, lootEntry.first.scaler, player, correctedAmount).forEach {
            player.addItemorDrop(it)
        }

        val pitch = when (correctedAmount) {
            1 -> 1.1F
            in 2..5 -> 1.0F
            else -> 0.9F
        }
        player.playSound(player.location, Sound.BLOCK_VAULT_OPEN_SHUTTER, 1F, pitch)

        val updatedEntry = lootEntry.first to lootEntry.second - correctedAmount
        PlayerLootManager.removeLoot(lootEntry.first, player, correctedAmount)
        loot[openLoot ?: return] = updatedEntry
        if (updatedEntry.second == 0) {
            loot.removeAt(openLoot ?: return)
            openPage(currentPage)
        } else {
            inv.setItem(4, provider.getMarker(updatedEntry.second, updatedEntry.first))
        }

    }

    override fun onClick(e: InventoryClickEvent) {
        if (checkForPageChange(e)) return
        if (e.clickedInventory == inv) e.isCancelled = true
        val clickedItem = e.clickedInventory?.getItem(e.slot)
        val action = clickedItem?.getItemAction() ?: return
        when (action) {
            ItemAction.OPEN_SUBMENU -> {
                openLootNum(clickedItem.getTag<Int>("loot") ?: return)
            }
            ItemAction.OPEN_ONE -> {
                openLoot(1)
            }
            ItemAction.OPEN_FIVE -> {
                openLoot(5)
            }
            ItemAction.OPEN_FIFTY -> {
                openLoot(50)
            }
            ItemAction.GO_BACK -> {
                openPage(currentPage)
            }
            else -> {}
        }

    }

}