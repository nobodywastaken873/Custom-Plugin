package me.newburyminer.customItems.gui.loot

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getItemAction
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.lock
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.setItemAction
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.gui.GuiItems
import me.newburyminer.customItems.gui.GuiLayout
import me.newburyminer.customItems.gui.ItemAction
import me.newburyminer.customItems.gui.PagedGui
import me.newburyminer.customItems.systems.KothSystem
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class KothLootGui(private val player: Player, page: Int = 0): PagedGui(page) {

    override val inv: Inventory = Bukkit.createInventory(this, 54, Utils.text("Daily KOTH Loot").style(Style.style(TextDecoration.BOLD)))
    private val kothData = KothSystem.getAllRewards().toList()
    private val itemsPerPage = 35
    private var openDay: Int? = null

    init {
        openPage(page)
    }

    override fun open(player: Player) {
        player.openInventory(inv)
    }

    override fun openPage(newPage: Int) {
        openDay = null
        GuiLayout.clearInventory(inv)
        GuiLayout.setMaxBorder(Material.RED_STAINED_GLASS_PANE, inv)

        for (i in itemsPerPage * newPage..<itemsPerPage * (newPage + 1)) {
            val (date, loot) = kothData.getOrNull(i) ?: break
            val item = ItemStack(Material.CLOCK)
                .lock()
                .setTag("index", i)
                .name(Utils.text("${date.monthValue}/${date.dayOfMonth}", arrayOf(235, 210, 52)))
                .setItemAction(ItemAction.OPEN_SUBMENU)
            inv.addItem(item)
        }

        inv.setItem(49, ItemStack(Material.BARRIER)
            .lock()
            .name(Utils.text("NOTICE", Utils.FAILED_COLOR))
            .loreBlock(
                Utils.text("Rewards are subject to change up to 2 hours before the KOTH. Rewards are likely not final, especially the further out you look.")
            )
        )

        // we want 0-35 items to be 1 page, 36-70 to be 2, etc
        val pages = (kothData.size - 1) / itemsPerPage + 1
        GuiLayout.addArrows(newPage, pages, inv)

        GuiLayout.fillEmpty(Material.LIGHT_GRAY_STAINED_GLASS_PANE, inv)
    }

    private fun openLootDay(lootNum: Int) {
        openDay = lootNum
        val (_, loot) = kothData[lootNum]
        GuiLayout.clearInventory(inv)
        GuiLayout.setMaxBorder(Material.RED_STAINED_GLASS_PANE, inv)

        val firstPlace = ItemStack(Material.DIAMOND_SWORD)
            .lock()
            .name(Utils.text("1st Place Loot", arrayOf(52, 208, 235)))
        val secondPlace = ItemStack(Material.IRON_SWORD)
            .lock()
            .name(Utils.text("2nd Place Loot", arrayOf(177, 178, 179)))

        repeat(7) {inv.addItem(GuiItems.getFiller(Material.RED_STAINED_GLASS_PANE))}
        inv.setItem(4, firstPlace)
        for (i in 0..6) {
            val item = loot.first.getOrNull(i) ?: GuiItems.getFiller(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
            inv.addItem(item)
        }

        repeat(7 + 7) {inv.addItem(GuiItems.getFiller(Material.RED_STAINED_GLASS_PANE))}
        inv.setItem(31, secondPlace)
        for (i in 0..6) {
            val item = loot.second.getOrNull(i) ?: GuiItems.getFiller(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
            inv.addItem(item)
        }

        inv.setItem(49, GuiItems.BACK_ARROW)

        GuiLayout.fillEmpty(Material.LIGHT_GRAY_STAINED_GLASS_PANE, inv)
    }

    override fun onClick(e: InventoryClickEvent) {
        if (checkForPageChange(e)) return
        if (e.clickedInventory == inv) e.isCancelled = true
        val clickedItem = e.clickedInventory?.getItem(e.slot)
        val action = clickedItem?.getItemAction() ?: return
        when (action) {
            ItemAction.OPEN_SUBMENU -> {
                openLootDay(clickedItem.getTag<Int>("index") ?: return)
            }
            ItemAction.GO_BACK -> {
                openPage(currentPage)
            }
            else -> {}
        }

    }

}