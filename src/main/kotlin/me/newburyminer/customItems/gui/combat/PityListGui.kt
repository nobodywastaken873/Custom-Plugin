package me.newburyminer.customItems.gui.combat

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.beautify
import me.newburyminer.customItems.Utils.Companion.getItemAction
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.lock
import me.newburyminer.customItems.Utils.Companion.lore
import me.newburyminer.customItems.Utils.Companion.maxStack
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.round
import me.newburyminer.customItems.Utils.Companion.setItemAction
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.gui.GuiItems
import me.newburyminer.customItems.gui.GuiLayout
import me.newburyminer.customItems.gui.ItemAction
import me.newburyminer.customItems.gui.PagedGui
import me.newburyminer.customItems.loot.LootRegistry
import me.newburyminer.customItems.loot.PlayerLootManager
import me.newburyminer.customItems.loot.PlayerPityManager
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class PityListGui(private val player: Player, page: Int = 0): PagedGui(page) {

    override val inv: Inventory = Bukkit.createInventory(this, 54, Utils.text("${player.name}'s Pity List").style(Style.style(TextDecoration.BOLD)))
    private val allPity = PlayerPityManager.getAllPity(player).toList()
    private val itemsPerPage = 35

    init {
        openPage(page)
    }

    override fun open(player: Player) {
        player.openInventory(inv)
    }

    override fun openPage(newPage: Int) {
        GuiLayout.clearInventory(inv)
        GuiLayout.setMaxBorder(Material.PINK_STAINED_GLASS_PANE, inv)

        for (i in itemsPerPage * newPage..<itemsPerPage * (newPage + 1)) {
            val entry = allPity.getOrNull(i) ?: break
            val pityItem = ItemStack(entry.second.material)
                .lock()
                .name(Utils.text(entry.first.beautify(), arrayOf(163, 91, 153)))
                .lore(
                    Utils.text("Progress: ${(100 * entry.second.progress).round(1)}%")
                )
            inv.addItem(pityItem)
        }

        // we want 0-35 items to be 1 page, 36-70 to be 2, etc
        val pages = (allPity.size - 1) / itemsPerPage + 1
        GuiLayout.addArrows(newPage, pages, inv)

        GuiLayout.fillEmpty(Material.LIGHT_GRAY_STAINED_GLASS_PANE, inv)
    }

    override fun onClick(e: InventoryClickEvent) {
        if (checkForPageChange(e)) return
        if (e.clickedInventory == inv) e.isCancelled = true
        val clickedItem = e.clickedInventory?.getItem(e.slot)
        val action = clickedItem?.getItemAction() ?: return
    }

}