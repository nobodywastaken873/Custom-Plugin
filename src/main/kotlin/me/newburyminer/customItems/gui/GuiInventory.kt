package me.newburyminer.customItems.gui

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.lock
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.recipes.Recipe
import me.newburyminer.customItems.recipes.Recipes
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

class GuiInventory(val name: String): InventoryHolder {
    companion object {
        val craftInv: Array<ItemStack?> = arrayOf(
            blue(), null, null, null, null, null, blue(), blue(), blue(),
            blue(), null, null, null, null, null, blue(), blue(), blue(),
            blue(), null, null, null, null, null, blue(), gray(), blue(),
            blue(), null, null, null, null, null, blue(), blue(), blue(),
            blue(), null, null, null, null, null, blue(), blue(), blue(),
            blue(), blue(), blue(), blue(), blue(), blue(), blue(), blue(), blue(),
        )
        val recipeInv: Array<ItemStack?> = arrayOf(
            blue(), null, null, null, null, null, null, null, blue(),
            blue(), null, null, null, null, null, null, null, blue(),
            blue(), null, null, null, null, null, null, null, blue(),
            blue(), null, null, null, null, null, null, null, blue(),
            blue(), null, null, null, null, null, null, null, blue(),
            blue(), blue(), blue(), blue(), blue(), blue(), blue(), blue(), blue(),
        )
        val compassInv: Array<ItemStack?> = arrayOf(
            blue(), blue(), blue(), blue(), blue(), blue(), blue(), blue(), blue(),
            blue(), gray(), gray(), gray(), gray(), gray(), gray(), gray(), blue(),
            blue(), gray(), gray(), gray(), compassCost(), gray(), gray(), gray(), blue(),
            blue(), gray(), gray(), gray(), compassInfo(), gray(), gray(), gray(), blue(),
            blue(), gray(), gray(), gray(), gray(), gray(), gray(), gray(), blue(),
            blue(), blue(), blue(), blue(), blue(), blue(), blue(), blue(), blue(),
        )
        val infoInv: Array<ItemStack?> = arrayOf(
            blue(), blue(), blue(), blue(), blue(), blue(), blue(), blue(), blue(),
            blue(), gray(), gray(), gray(), gray(), gray(), gray(), gray(), blue(),
            blue(), gray(), combatInfo(), afkInfo(), trackingInfo(), gravesInfo(), durabilityInfo(), gray(), blue(),
            blue(), gray(), trimInfo(), griefingInfo(), customItemInfo(), endInfo(), worldInfo(), gray(), blue(),
            blue(), gray(), gray(), gray(), gray(), gray(), gray(), gray(), blue(),
            blue(), blue(), blue(), blue(), blue(), blue(), blue(), blue(), blue(),
        )

        //create recipe gui based on thing, maybe add int based on index so listener can reference the given recipe
        fun recipes(page: Int): Array<ItemStack?> {
            val newRecipeInv = recipeInv.clone()
            val recipes = Recipes.getPage(page)
            if (page != 1) newRecipeInv[46] = direction(false, page-1)
            if (recipes[recipes.size-1] != null) newRecipeInv[52] = direction(true, page+1)
            val recipeSlots = arrayOf(1,2,3,4,5,6,7,10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43)
            for (i in 0..<recipes.size) {
                val recipe = recipes[i]
                if (recipe == null) {
                    newRecipeInv[recipeSlots[i]] = gray()
                    continue
                }
                newRecipeInv[recipeSlots[i]] = recipe.getResultItem()
            }
            return newRecipeInv
        }

        fun recipe(recipe: Recipe, backPage: Int): Array<ItemStack?> {
            val recipeInv = craftInv.clone()
            val recipeSlots = arrayOf(1, 2, 3, 4, 5, 10,11,12,13,14,19,20,21,22,23,28,29,30,31,32,37,38,39,40,41)
            var index = 0
            for (row in recipe.items) {
                for (recipeItem in row) {
                    val item = recipeItem?.getItem()
                    if (item == null) recipeInv[recipeSlots[index]] = gray()
                    else recipeInv[recipeSlots[index]] = item.lock()
                    ++index
                }
            }
            recipeInv[25] = recipe.getResultItem().lock()
            recipeInv[49] = back(backPage)
            return recipeInv
        }

        private fun blue(): ItemStack {
            val base = ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
            val newMeta = base.itemMeta
            newMeta.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, "locked"), PersistentDataType.BOOLEAN, true)
            base.itemMeta = newMeta
            return base
        }
        fun gray(unique: Boolean = false): ItemStack {
            val base = ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
            val newMeta = base.itemMeta
            newMeta.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, "locked"), PersistentDataType.BOOLEAN, true)
            if (unique) newMeta.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, "uniquecomponent"), PersistentDataType.STRING, UUID.randomUUID().toString())
            base.itemMeta = newMeta
            return base
        }
        fun direction(direction: Boolean, nextPage: Int): ItemStack {
            val base = ItemStack(Material.ARROW)
            val newMeta = base.itemMeta
            newMeta.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, "page"), PersistentDataType.INTEGER, nextPage)
            if (direction) newMeta.itemName(Utils.text("Next page"))
            else newMeta.itemName(Utils.text("Previous page"))
            base.itemMeta = newMeta
            return base
        }
        fun back(backPage: Int): ItemStack {
            val base = ItemStack(Material.ARROW)
            val newMeta = base.itemMeta
            newMeta.persistentDataContainer.set(NamespacedKey(CustomItems.plugin, "page"), PersistentDataType.INTEGER, backPage)
            newMeta.itemName(Utils.text("Back"))
            base.itemMeta = newMeta
            return base
        }
        private fun compassInfo(): ItemStack {
            val base = ItemStack(Material.PAPER)
            base.name(Utils.text("INFO", arrayOf(247, 207, 2)))
            base.loreBlock(
                Utils.text("Click on the compass above to begin tracking, each subsequent use of the compass will cost more. Upon clicking the compass above, the materials will be consumed, and you will be asked to enter a player's name in chat. You cannot track players who are AFK, so make sure to run /afk CHECKALL beforehand.", arrayOf(191, 191, 187)),
                Utils.text(""),
                Utils.text("When a player is tracked, they will be given a 1 minute warning before tracking begins. You can still track them if they logout. They cannot use elytra or use an enderchest while being tracked.", arrayOf(191, 191, 187)),
            )
            base.setTag("locked", true)
            return base
        }
        fun compassCost(): ItemStack {
            val base = ItemStack(Material.COMPASS)
            base.name(Utils.text("READ BELOW BEFORE CLICKING THIS", arrayOf(247, 207, 2)))
            return base
        }
        private fun combatInfo(): ItemStack {
            val base = ItemStack(Material.DIAMOND_SWORD)
            base.name(Utils.text("Combat", arrayOf(247, 207, 2)))
            base.loreBlock(
                Utils.text("When a player damages you or you damage another player, both of you will be put in combat for 1 minute. Logging out during this time will kill you instantly.", arrayOf(191, 191, 187))
            )
            base.setTag("locked", true)
            return base
        }
        private fun afkInfo(): ItemStack {
            val base = ItemStack(Material.RED_BED)
            base.name(Utils.text("AFKing", arrayOf(247, 207, 2)))
            base.loreBlock(
                Utils.text("You can use the /afk command to become invulnerable to damage while afking farms, however you will not be able to move. For the first 5 minutes, other players will be able to damage you. You can check what other players are afk with /afk CHECKALL.", arrayOf(191, 191, 187))
            )
            base.setTag("locked", true)
            return base
        }
        private fun trackingInfo(): ItemStack {
            val base = ItemStack(Material.COMPASS)
            base.name(Utils.text("Tracking Compass", arrayOf(247, 207, 2)))
            base.loreBlock(
                Utils.text("You can craft a tracking compass in the /craft menu, explained below. When activating the compass to track a certain player will cost extra resources. It will give the player a 1 minute warning before beginning tracking, and then will track them for 30 minutes afterwards even if they logout. They cannot use elytra or put items into their enderchest while being tracked. Afk players cannot be tracked.", arrayOf(191, 191, 187))
            )
            base.setTag("locked", true)
            return base
        }
        private fun gravesInfo(): ItemStack {
            val base = ItemStack(Material.CHEST)
            base.name(Utils.text("Graves", arrayOf(247, 207, 2)))
            base.loreBlock(
                Utils.text("Upon dying, a grave will be places where you die with all of your items. If another player killed you, they will be able to loot one random gear item from your grave. Otherwise, noone else will be able to access your grave. You can right click on your grave to open it, or sneak right click to claim all items in it. All of your graves can be viewed in /graves, where you can see their location. Additionally, by clicking on a grave in that menu, you can teleport to it. You cannot teleport to graves for 5 minutes after dying, and each teleport will cost 1 diamond more than the last.", arrayOf(191, 191, 187))
            )
            base.setTag("locked", true)
            return base
        }
        private fun durabilityInfo(): ItemStack {
            val base = ItemStack(Material.IRON_CHESTPLATE)
            base.name(Utils.text("Durability", arrayOf(247, 207, 2)))
            base.loreBlock(
                Utils.text("When an item breaks, instead of dissapearing, it will stay at 0 durability and you will not be able to use it. However, you can repair it with mending or any other method and continue to use it afterwards.", arrayOf(191, 191, 187))
            )
            base.setTag("locked", true)
            return base
        }
        private fun trimInfo(): ItemStack {
            val base = ItemStack(Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE)
            base.name(Utils.text("Trim Duplication", arrayOf(247, 207, 2)))
            base.loreBlock(
                Utils.text("When duplicating trims with a recipe, instead of getting 2 identical trims, you keep the original and get one new trim with the \"Duplicate\" tag. These duplicate trims cannot be duplicated to make more, and cannot be used in custom recipes. They can only be used for smithing.", arrayOf(191, 191, 187))
            )
            base.setTag("locked", true)
            return base
        }
        private fun griefingInfo(): ItemStack {
            val base = ItemStack(Material.LAVA_BUCKET)
            base.name(Utils.text("Griefing", arrayOf(247, 207, 2)))
            base.loreBlock(
                Utils.text("Griefing of bases and farms is not allowed. However, stealing from bases and farms is 100% allowed.", arrayOf(191, 191, 187))
            )
            base.setTag("locked", true)
            return base
        }
        private fun customItemInfo(): ItemStack {
            val base = ItemStack(Material.NETHERITE_PICKAXE)
            base.name(Utils.text("Custom Items", arrayOf(247, 207, 2)))
            base.loreBlock(
                Utils.text("A lot of custom items have been added, you can view all of them in /recipe. Once you have gathered all of the materials, you can use /craft to open a custom crafting menu where you can craft the item. At some point, I will release a texture pack that adds custom textures for all of the items.", arrayOf(191, 191, 187))
            )
            base.setTag("locked", true)
            return base
        }
        private fun endInfo(): ItemStack {
            val base = ItemStack(Material.END_PORTAL_FRAME)
            base.name(Utils.text("End", arrayOf(247, 207, 2)))
            base.loreBlock(
                Utils.text("The End is currently locked, and will open (most likely) a couple weeks after the start of the server.", arrayOf(191, 191, 187))
            )
            base.setTag("locked", true)
            return base
        }
        private fun worldInfo(): ItemStack {
            val base = ItemStack(Material.BARRIER)
            base.name(Utils.text("Worlds", arrayOf(247, 207, 2)))
            base.loreBlock(
                Utils.text("The worldborder is currently at 40k x 40k in the overworld and nether. In a few weeks, the arid lands dimension will open with a border of 40k x 40k as well.", arrayOf(191, 191, 187))
            )
            base.setTag("locked", true)
            return base
        }
    }

    private var inventory = Bukkit.createInventory(this, 54, Utils.text("Custom Crafting", arrayOf(66, 99, 245)))
    override fun getInventory(): Inventory {
        return inventory
    }
}