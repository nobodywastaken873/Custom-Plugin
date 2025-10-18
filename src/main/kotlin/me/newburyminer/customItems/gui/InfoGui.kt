package me.newburyminer.customItems.gui

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.setTag
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class InfoGui: CustomGui() {
    override val inv: Inventory = Bukkit.createInventory(this, 54, Utils.text("Info").style(Style.style(TextDecoration.BOLD)))

    init {
        GuiLayout.setCircleBorder(Material.LIGHT_BLUE_STAINED_GLASS_PANE, inv)

        val combatInfo = ItemStack(Material.DIAMOND_SWORD)
            .name(Utils.text("Combat", arrayOf(247, 207, 2)))
            .loreBlock(Utils.text("When a player damages you or you damage another player, both of you will be put in combat for 1 minute. Logging out during this time will kill you instantly.", arrayOf(191, 191, 187)))

        val afkInfo = ItemStack(Material.RED_BED)
            .name(Utils.text("AFKing", arrayOf(247, 207, 2)))
            .loreBlock(Utils.text("You can use the /afk command to become invulnerable to damage while afking farms, however you will not be able to move. For the first 5 minutes, other players will be able to damage you. You can check what other players are afk with /afk CHECKALL.", arrayOf(191, 191, 187)))

        val trackingInfo = ItemStack(Material.COMPASS)
            .name(Utils.text("Tracking Compass", arrayOf(247, 207, 2)))
            .loreBlock(Utils.text("You can craft a tracking compass in the /craft menu, explained below. When activating the compass to track a certain player will cost extra resources. It will give the player a 1 minute warning before beginning tracking, and then will track them for 30 minutes afterwards even if they logout. They cannot use elytra or put items into their enderchest while being tracked. Afk players cannot be tracked.", arrayOf(191, 191, 187)))

        val gravesInfo = ItemStack(Material.CHEST)
            .name(Utils.text("Graves", arrayOf(247, 207, 2)))
            .loreBlock(Utils.text("Upon dying, a grave will be places where you die with all of your items. If another player killed you, they will be able to loot one random gear item from your grave. Otherwise, noone else will be able to access your grave. You can right click on your grave to open it, or sneak right click to claim all items in it. All of your graves can be viewed in /graves, where you can see their location. Additionally, by clicking on a grave in that menu, you can teleport to it. You cannot teleport to graves for 5 minutes after dying, and each teleport will cost 1 diamond more than the last.", arrayOf(191, 191, 187)))

        val durabilityInfo = ItemStack(Material.IRON_CHESTPLATE)
            .name(Utils.text("Durability", arrayOf(247, 207, 2)))
            .loreBlock(Utils.text("When an item breaks, instead of dissapearing, it will stay at 0 durability and you will not be able to use it. However, you can repair it with mending or any other method and continue to use it afterwards.", arrayOf(191, 191, 187)))

        val trimInfo = ItemStack(Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE)
            .name(Utils.text("Trim Duplication", arrayOf(247, 207, 2)))
            .loreBlock(Utils.text("When duplicating trims with a recipe, instead of getting 2 identical trims, you keep the original and get one new trim with the \"Duplicate\" tag. These duplicate trims cannot be duplicated to make more, and cannot be used in custom recipes. They can only be used for smithing.", arrayOf(191, 191, 187)))

        val griefingInfo = ItemStack(Material.LAVA_BUCKET)
            .name(Utils.text("Griefing", arrayOf(247, 207, 2)))
            .loreBlock(Utils.text("Griefing of bases and farms is not allowed. However, stealing from bases and farms is 100% allowed and encouraged.", arrayOf(191, 191, 187)))

        val customItemInfo = ItemStack(Material.NETHERITE_PICKAXE)
            .name(Utils.text("Custom Items", arrayOf(247, 207, 2)))
            .loreBlock(Utils.text("A lot of custom items have been added, you can view all of them in /recipe. Once you have gathered all of the materials, you can use /craft to open a custom crafting menu where you can craft the item. At some point, I will release a texture pack that adds custom textures for all of the items.", arrayOf(191, 191, 187)))

        val endInfo = ItemStack(Material.END_PORTAL_FRAME)
            .name(Utils.text("End", arrayOf(247, 207, 2)))
            .loreBlock(Utils.text("The End is currently locked, and will open (most likely) a couple weeks after the start of the server.", arrayOf(191, 191, 187)))

        val worldInfo = ItemStack(Material.BARRIER)
            .name(Utils.text("Worlds", arrayOf(247, 207, 2)))
            .loreBlock(Utils.text("The worldborder is currently at 40k x 40k in the overworld and nether. In a few weeks, the arid lands dimension will open with a border of 40k x 40k as well.", arrayOf(191, 191, 187)))

        inv.setItem(20, combatInfo)
        inv.setItem(21, afkInfo)
        inv.setItem(22, trackingInfo)
        inv.setItem(23, gravesInfo)
        inv.setItem(24, durabilityInfo)
        inv.setItem(29, trimInfo)
        inv.setItem(30, griefingInfo)
        inv.setItem(31, customItemInfo)
        inv.setItem(32, endInfo)
        inv.setItem(33, worldInfo)

        GuiLayout.fillEmpty(Material.LIGHT_GRAY_STAINED_GLASS_PANE, inv)

    }

    override fun open(player: Player) {
        player.openInventory(inv)
    }

    override fun onClick(e: InventoryClickEvent) {
        e.isCancelled = true
    }
}