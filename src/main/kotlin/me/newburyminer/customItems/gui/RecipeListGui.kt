package me.newburyminer.customItems.gui

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getItemAction
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.lock
import me.newburyminer.customItems.Utils.Companion.setItemAction
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.recipes.Recipes
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

class RecipeListGui(page: Int): PagedGui(page) {

    override val inv: Inventory = Bukkit.createInventory(this, 54, Utils.text("Recipe List").style(Style.style(TextDecoration.BOLD)))
    private val itemsPerPage = 35
    init {
        openPage(page)
    }

    override fun open(player: Player) {
        player.openInventory(inv)
    }

    override fun openPage(newPage: Int) {
        GuiLayout.clearInventory(inv)
        GuiLayout.setMaxBorder(Material.LIGHT_BLUE_STAINED_GLASS_PANE, inv)

        val recipes = Recipes.getPage(newPage)
        for (i in recipes.indices) {
            val recipe = recipes[i] ?: break
            inv.addItem(recipe.getResultItem()
                .lock()
                .setItemAction(ItemAction.OPEN_SUBMENU)
                .setTag("relativeindex", i)
            )
        }

        // we want 0-35 items to be 1 page, 36-70 to be 2, etc
        val pages = (Recipes.getTotalEntries() - 1) / itemsPerPage + 1
        GuiLayout.addArrows(newPage, pages, inv)

        GuiLayout.fillEmpty(Material.LIGHT_GRAY_STAINED_GLASS_PANE, inv)
    }

    private fun openRecipe(number: Int) {
        val recipes = Recipes.getPage(currentPage)
        val recipe = recipes[number] ?: return

        GuiLayout.clearInventory(inv)
        GuiLayout.setCraftingBorder(Material.LIGHT_BLUE_STAINED_GLASS_PANE, inv)

        for (row in recipe.items) {
            for (recipeItem in row) {
                val item = recipeItem?.getItem()
                if (item == null)  inv.addItem(GuiItems.getFiller(Material.LIGHT_GRAY_STAINED_GLASS_PANE))
                else inv.addItem(recipeItem.getItem().lock())
            }
        }

        inv.setItem(25, recipe.getResultItem().lock())
        inv.setItem(49, GuiItems.BACK_ARROW)
    }

    override fun onClick(e: InventoryClickEvent) {
        if (checkForPageChange(e)) return
        if (e.clickedInventory == inv) e.isCancelled = true
        val clickedItem = e.clickedInventory?.getItem(e.slot)
        val action = clickedItem?.getItemAction() ?: return
        when (action) {
            ItemAction.OPEN_SUBMENU -> {
                openRecipe(clickedItem.getTag<Int>("relativeindex") ?: return)
            }
            ItemAction.GO_BACK -> {
                openPage(currentPage)
            }
            else -> {}
        }
    }

}