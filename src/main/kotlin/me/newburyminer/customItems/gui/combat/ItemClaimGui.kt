package me.newburyminer.customItems.gui.combat

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
import me.newburyminer.customItems.loot.ItemClaimManager
import me.newburyminer.customItems.loot.LootRegistry
import me.newburyminer.customItems.loot.PlayerLootManager
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.util.Index
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class ItemClaimGui(private val player: Player, page: Int = 0): PagedGui(page) {

    override val inv: Inventory = Bukkit.createInventory(this, 54, Utils.text("${player.name}'s Claims").style(Style.style(TextDecoration.BOLD)))
    private val claims = ItemClaimManager.getAllItems(player).toMutableList()
    private val itemsPerPage = 35

    init {
        openPage(page)
    }

    override fun open(player: Player) {
        player.openInventory(inv)
    }

    override fun openPage(newPage: Int) {
        GuiLayout.clearInventory(inv)
        GuiLayout.setMaxBorder(Material.YELLOW_STAINED_GLASS_PANE, inv)

        for (i in itemsPerPage * newPage..<itemsPerPage * (newPage + 1)) {
            val item = ItemStack(claims.getOrNull(i) ?: break)
                .setTag("index", i)
                .lock()
                .setItemAction(ItemAction.OPEN_SUBMENU)

            inv.addItem(item)
        }

        // we want 0-35 items to be 1 page, 36-70 to be 2, etc
        val pages = (claims.size - 1) / itemsPerPage + 1
        GuiLayout.addArrows(newPage, pages, inv)

        GuiLayout.fillEmpty(Material.LIGHT_GRAY_STAINED_GLASS_PANE, inv)
    }

    private fun claimLootNum(index: Int) {
        val item = ItemStack(claims[index])
        ItemClaimManager.remove(player.uniqueId, claims[index])
        player.addItemorDrop(item)
        player.playSound(player.location, Sound.ENTITY_ITEM_PICKUP, 1.0F, 1.0F)

        claims.removeAt(index)
        openPage(currentPage)
    }

    override fun onClick(e: InventoryClickEvent) {
        if (checkForPageChange(e)) return
        if (e.clickedInventory == inv) e.isCancelled = true
        val clickedItem = e.clickedInventory?.getItem(e.slot)
        val action = clickedItem?.getItemAction() ?: return
        when (action) {
            ItemAction.OPEN_SUBMENU -> {
                claimLootNum(clickedItem.getTag<Int>("index") ?: return)
            }
            else -> {}
        }

    }

}