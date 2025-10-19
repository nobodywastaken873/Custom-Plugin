package me.newburyminer.customItems.gui

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.recipes.Recipe
import me.newburyminer.customItems.recipes.Recipes
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

class CraftingGui: CustomGui() {
    override val inv: Inventory = Bukkit.createInventory(this, 54, Utils.text("Custom Crafting Table").style(Style.style(TextDecoration.BOLD)))
    private val resultSlot = 25

    init {
        GuiLayout.setCraftingBorder(Material.LIGHT_BLUE_STAINED_GLASS_PANE, inv)
        inv.setItem(resultSlot, GuiItems.getFiller(Material.LIGHT_GRAY_STAINED_GLASS_PANE))
    }

    override fun open(player: Player) {
        player.openInventory(inv)
    }

    override fun onClick(e: InventoryClickEvent) {
        val clickedInventory = e.clickedInventory ?: return
        val clickedItem = clickedInventory.getItem(e.slot)

        // Clicked on any locked slot
        if (clickedItem?.getTag<Boolean>("locked") == true) { e.isCancelled = true; return }

        // Clicked on the result item, while it is not locked
        else if (clickedInventory.holder is CraftingGui && e.slot == resultSlot && clickedItem?.type != Material.LIGHT_GRAY_STAINED_GLASS_PANE) {
            if (e.action != InventoryAction.PICKUP_ALL) { e.isCancelled = true; return }

            val result = Recipes.checkForRecipe(inv) ?: return
            Recipes.takeRecipeIngredients(inv, result)

            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                updateResult()

                (e.whoClicked as Player).playSound(e.whoClicked, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.0F, 0.95F)
                //for (allplayer in Bukkit.getServer().onlinePlayers) allplayer.sendMessage(Utils.text("${(e.whoClicked as Player).name} has crafted ${result!!.resultItem.getCustom()!!.realName}"))
            })
        }

        // Clicked anywhere else
        else {
            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                updateResult()
            })
        }
    }

    override fun onDrag(e: InventoryDragEvent) {
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            updateResult()
        })
    }

    private fun updateResult() {
        val result = Recipes.checkForRecipe(inv)
        if (result == null) inv.setItem(25, GuiItems.getFiller(Material.LIGHT_GRAY_STAINED_GLASS_PANE))
        else inv.setItem(25, result.getResultItem())
    }

    override fun onClose(e: InventoryCloseEvent) {

        for (slotNum in arrayOf(1, 2, 3, 4, 5, 10,11,12,13,14,19,20,21,22,23,28,29,30,31,32,37,38,39,40,41)) {
            val slot = inv.getItem(slotNum) ?: continue
            if (slot.getTag<Boolean>("locked") == true) continue
            (e.player as Player).addItemorDrop(slot)
        }

    }

}