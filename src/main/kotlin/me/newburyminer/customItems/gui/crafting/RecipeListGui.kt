package me.newburyminer.customItems.gui.crafting

import io.papermc.paper.datacomponent.DataComponentTypes
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.Utils.Companion.getEnumTag
import me.newburyminer.customItems.Utils.Companion.getItemAction
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.lock
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.readableName
import me.newburyminer.customItems.Utils.Companion.setEnumTag
import me.newburyminer.customItems.Utils.Companion.setItemAction
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.gui.GuiItems
import me.newburyminer.customItems.gui.GuiLayout
import me.newburyminer.customItems.gui.ItemAction
import me.newburyminer.customItems.gui.PagedGui
import me.newburyminer.customItems.recipes.Recipe
import me.newburyminer.customItems.recipes.RecipeCustom
import me.newburyminer.customItems.recipes.RecipeItem
import me.newburyminer.customItems.recipes.RecipeItemBase
import me.newburyminer.customItems.recipes.RecipeRegistry
import me.newburyminer.customItems.recipes.RecipeType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import java.util.*
import kotlin.collections.set

class RecipeListGui(page: Int): PagedGui(page) {

    override val inv: Inventory = Bukkit.createInventory(this, 54, Utils.text("Recipe List").style(Style.style(TextDecoration.BOLD)))
    private val itemsPerPage = 35
    private var currentRecipe: Recipe? = null
    private var currentCategory: RecipeType = RecipeType.TOOL

    override fun open(player: Player) {
        openPage(currentPage)
        player.openInventory(inv)
    }

    private val categoryIndex = mapOf(
        RecipeType.TOOL to 47,
        RecipeType.WEAPON to 48,
        RecipeType.ARMOR to 49,
        RecipeType.MATERIAL to 50,
        RecipeType.OTHER to 51,
    )
    override fun openPage(newPage: Int) {
        GuiLayout.clearInventory(inv)
        GuiLayout.setMaxBorder(Material.LIGHT_BLUE_STAINED_GLASS_PANE, inv)

        val recipes = RecipeRegistry.getPage(newPage, currentCategory)
        for (i in recipes.indices) {
            val recipe = recipes[i] ?: break
            inv.addItem(recipe.getResultItem()
                .lock()
                .setItemAction(ItemAction.OPEN_SUBMENU)
                .setTag("relativeindex", i)
            )
        }

        // we want 0-35 items to be 1 page, 36-70 to be 2, etc
        val pages = (RecipeRegistry.getTotalEntries(currentCategory) - 1) / itemsPerPage + 1
        GuiLayout.addArrows(newPage, pages, inv)
        RecipeType.entries.forEach { inventory.setItem(categoryIndex[it] ?: 0, getCategoryButton(it))
        }


        GuiLayout.fillEmpty(Material.LIGHT_GRAY_STAINED_GLASS_PANE, inv)
    }
    private fun getCategoryButton(type: RecipeType): ItemStack {
        val item = when (type) {
            RecipeType.TOOL -> ItemStack(Material.MINER_POTTERY_SHERD).name(Utils.text("Tools", arrayOf(219, 189, 37)))
            RecipeType.WEAPON -> ItemStack(Material.BLADE_POTTERY_SHERD).name(Utils.text("Weapons", arrayOf(219, 189, 37)))
            RecipeType.ARMOR -> ItemStack(Material.ARMS_UP_POTTERY_SHERD).name(Utils.text("Armor", arrayOf(219, 189, 37)))
            RecipeType.MATERIAL -> ItemStack(Material.PRIZE_POTTERY_SHERD).name(Utils.text("Materials", arrayOf(219, 189, 37)))
            RecipeType.OTHER -> ItemStack(Material.FLOW_POTTERY_SHERD).name(Utils.text("Other", arrayOf(219, 189, 37)))
        }
        if (type == currentCategory) item.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
        return GuiItems.getLocked(item.setEnumTag("category", type))
            .setItemAction(ItemAction.CHANGE_CATEGORY)
    }

    private fun openRecipe(number: Int, player: Player) {
        val recipes = RecipeRegistry.getPage(currentPage, currentCategory)
        val recipe = recipes[number] ?: return
        currentRecipe = recipe

        GuiLayout.clearInventory(inv)
        GuiLayout.setCraftingBorder(Material.LIGHT_BLUE_STAINED_GLASS_PANE, inv)

        for (row in recipe.items) {
            for (recipeItem in row) {
                val item = recipeItem?.getItem()
                if (item == null)  inv.addItem(GuiItems.getFiller(Material.LIGHT_GRAY_STAINED_GLASS_PANE))
                else inv.addItem(
                    recipeItem
                        .getItem()
                        .lock()
                        .setTag("salt", UUID.randomUUID().toString())
                )
            }
        }

        inv.setItem(25, recipe.getResultItem().lock())
        inv.setItem(34, getFillButton(recipe, player.inventory))
        inv.setItem(49, GuiItems.BACK_ARROW)
    }

    // All for grid filling from crafting
    private fun getFillButton(recipe: Recipe, playerInv: PlayerInventory): ItemStack {
        val ingredientComparison = compareMaterials(recipe, playerInv)
        val baseItem =
            if (hasMaterials(recipe, playerInv))
                ItemStack(Material.LIME_CONCRETE_POWDER)
                    .name(Utils.text("Click to fill grid!", Utils.SUCCESS_COLOR))
                    .setItemAction(ItemAction.FILL_RECIPE)
            else
                ItemStack(Material.RED_CONCRETE_POWDER)
                    .name(Utils.text("Not enough materials.", Utils.FAILED_COLOR))

        val lines = mutableListOf<Component>()
        ingredientComparison.forEach { (base, remaining) ->
            val itemName =
                if (base is RecipeItem) base.getItem().type.readableName()
                else (base as RecipeCustom).getItem().getCustom()?.readableName() ?: ""

            if (remaining <= 0)
                lines.add(Utils.text("☑ $itemName", Utils.SUCCESS_COLOR))
            else
                lines.add(Utils.text("☒ $itemName - Missing $remaining", Utils.FAILED_COLOR))
        }

        lines.sortBy{ (it as TextComponent).content() }
        baseItem.loreList(lines)
        return baseItem
    }
    private fun fillRecipe(player: Player) {
        val recipe = currentRecipe ?: return
        if (!hasMaterials(recipe, player.inventory)) return
        player.closeInventory()
        val grid = CraftingGui()
        grid.open(player)
        val metIngredients = recipe.ingredients.toMutableMap()

        for (item in player.inventory.contents) {
            if (item == null) continue
            metIngredients.keys.forEach { key ->
                if (key.itemMatches(item)) {
                    val toSubtract = item.amount.coerceAtMost(metIngredients[key] ?: 0)

                    val gridItem = ItemStack(item)
                    gridItem.amount = toSubtract
                    addToGrid(gridItem, grid, recipe)

                    metIngredients[key] = (metIngredients[key] ?: 0) - toSubtract
                    item.amount -= toSubtract

                    return@forEach
                }
            }
            metIngredients.keys.removeIf { key -> metIngredients[key] == 0 }
        }

        grid.updateResult()
    }
    private fun addToGrid(item: ItemStack, grid: CraftingGui, recipe: Recipe) {
        val craftInv = grid.inventory
        for (row in 0..4) for (col in 0..4) {
            val gridBase = recipe.items[row][col] ?: continue
            if (gridBase.itemMatches(item)) {
                val slot = (row) * 9 + (1 + col)

                if (gridBase.itemMatches(craftInv.getItem(slot))) {
                    val toAdd = (gridBase.amount - (craftInv.getItem(slot)?.amount ?: 0)).coerceAtMost(item.amount)
                    craftInv.getItem(slot)?.amount += toAdd
                    item.amount -= toAdd
                }
                else {
                    val newItem = ItemStack(item)
                    val toAdd = gridBase.amount.coerceAtMost(item.amount)
                    newItem.amount = toAdd

                    craftInv.setItem(slot, newItem)
                    item.amount -= toAdd
                }
            }
            if (item.amount == 0) return
        }
    }
    private fun compareMaterials(recipe: Recipe, playerInv: Inventory): Map<RecipeItemBase, Int> {
        val metIngredients = recipe.ingredients.toMutableMap()
        for (item in playerInv.contents) {
            metIngredients.keys.forEach { key ->
                if (key.itemMatches(item)) {
                    metIngredients[key] = (metIngredients[key]!! - (item?.amount ?: 0)).coerceAtLeast(0)
                    return@forEach
                }
            }
        }
        return metIngredients.toMap()
    }
    private fun hasMaterials(recipe: Recipe, playerInv: Inventory): Boolean {
        return compareMaterials(recipe, playerInv).values.all { it <= 0 }
    }

    override fun onClick(e: InventoryClickEvent) {
        if (checkForPageChange(e)) return
        if (e.clickedInventory == inv) e.isCancelled = true
        if (e.action == InventoryAction.CLONE_STACK && e.whoClicked.gameMode == GameMode.CREATIVE) e.isCancelled = false
        val clickedItem = e.clickedInventory?.getItem(e.slot)
        val action = clickedItem?.getItemAction() ?: return
        when (action) {
            ItemAction.OPEN_SUBMENU -> {
                openRecipe(clickedItem.getTag<Int>("relativeindex") ?: return, e.whoClicked as Player)
            }
            ItemAction.GO_BACK -> {
                openPage(currentPage)
            }
            ItemAction.FILL_RECIPE -> {
                Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                    fillRecipe(e.whoClicked as Player)
                })
            }
            ItemAction.CHANGE_CATEGORY -> {
                currentCategory = clickedItem.getEnumTag<RecipeType>("category") ?: RecipeType.WEAPON
                openPage(0)
                currentPage = 0
            }
            else -> {}
        }
    }

}