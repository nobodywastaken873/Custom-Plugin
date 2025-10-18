package me.newburyminer.customItems.gui

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getItemAction
import me.newburyminer.customItems.Utils.Companion.getListTag
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.graveTeleportCooldown
import me.newburyminer.customItems.Utils.Companion.graveTeleportOnCooldown
import me.newburyminer.customItems.Utils.Companion.lock
import me.newburyminer.customItems.Utils.Companion.lore
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.niceName
import me.newburyminer.customItems.Utils.Companion.setItemAction
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectManager
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class GraveListGui(private val player: Player, page: Int = 0): PagedGui(page) {

    override val inv: Inventory = Bukkit.createInventory(this, 54, Utils.text("${player.name}'s Graves").style(Style.style(TextDecoration.BOLD)))
    private val graves = player.getListTag<Location>("gravelist") ?: mutableListOf()
    private val itemsPerPage = 35
    private var openGrave: Int? = null

    init {
        openPage(page)
    }

    override fun open(player: Player) {
        player.openInventory(inv)
    }

    override fun openPage(newPage: Int) {
        openGrave = null
        GuiLayout.clearInventory(inv)
        GuiLayout.setMaxBorder(Material.BLACK_STAINED_GLASS_PANE, inv)

        for (i in itemsPerPage * newPage..<itemsPerPage * (newPage + 1)) {
            val graveLocation = graves.getOrNull(i) ?: break
            inv.addItem(getGraveLabel(graveLocation, i, ItemAction.OPEN_SUBMENU))
        }

        // we want 0-35 items to be 1 page, 36-70 to be 2, etc
        val pages = (graves.size - 1) / itemsPerPage + 1
        GuiLayout.addArrows(newPage, pages, inv)

        GuiLayout.fillEmpty(Material.LIGHT_GRAY_STAINED_GLASS_PANE, inv)
    }

    private fun openGrave(graveNum: Int) {
        openGrave = graveNum
        GuiLayout.clearInventory(inv)
        GuiLayout.setMaxBorder(Material.BLACK_STAINED_GLASS_PANE, inv)

        val cost = (player.getTag<Int>("totalgravetps") ?: 0) + 1
        val teleportItem = ItemStack(Material.ENDER_PEARL)
            .lock()
            .name(Utils.text("Teleport to grave", arrayOf(247, 207, 2)))
            .loreBlock(
                Utils.text("This will teleport you to the exact location of the grave, you will recieve 5 seconds of stability and invulnerability.", arrayOf(191, 191, 187)),
                Utils.text(""),
                Utils.text("Cost: ", arrayOf(191, 191, 187)),
                Utils.text("$cost Diamonds", arrayOf(92, 237, 225))
            )
            .setItemAction(ItemAction.TELEPORT_GRAVE)

        val deleteItem = ItemStack(Material.RED_CONCRETE)
            .lock()
            .name(Utils.text("Delete grave", arrayOf(247, 207, 2)))
            .loreBlock(Utils.text("This will remove the grave from this list, the grave will not be removed from the world.", arrayOf(191, 191, 187)))
            .setItemAction(ItemAction.DELETE_GRAVE)

        inv.setItem(4, getGraveLabel(graves[graveNum], graveNum, null))
        inv.setItem(20, teleportItem)
        inv.setItem(24, deleteItem)
        inv.setItem(49, GuiItems.BACK_ARROW)

        GuiLayout.fillEmpty(Material.LIGHT_GRAY_STAINED_GLASS_PANE, inv)
    }

    private fun teleportToGrave(graveNum: Int) {
        // Check that teleporting to the grave is not on cooldown
        if (player.graveTeleportOnCooldown()) {
            val cd = player.graveTeleportCooldown()
            //e.whoClicked.sendMessage(cd.toString())
            player.sendMessage(Utils.text("You cannot teleport to graves for another ${cd / 20 / 60}m, ${cd / 20 % 60}s.", Utils.FAILED_COLOR))
            player.sendMessage(Utils.text("After dying, you cannot teleport to any graves for 5 minutes.", Utils.FAILED_COLOR))
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
            return
        }

        // Make sure they have enough diamonds
        val diamondCost = (player.getTag<Int>("totalgravetps") ?: 0) + 1
        var totalDias = 0
        player.inventory.forEach {
            if (it?.type == Material.DIAMOND)
                totalDias += it.amount
        }
        if (totalDias < diamondCost) {
            player.sendMessage(Utils.text("You do not have enough materials.", Utils.FAILED_COLOR))
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
            return
        }

        // Remove the diamonds
        var toRemove = diamondCost
        for (item in player.inventory) {
            if (toRemove == 0) break
            if (item?.type != Material.DIAMOND) continue
            if (toRemove >= item.amount) {toRemove -= item.amount; item.amount = 0}
            else {item.amount -= toRemove; toRemove = 0}
        }

        // Teleport the player (diamond cost is # of tps + 1)
        player.setTag("totalgravetps", diamondCost)
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0F, 1.0F)
            EffectManager.applyEffect(player, CustomEffectType.GRAVE_INVULNERABILITY, 5 * 20)
            player.teleport(graves[graveNum].clone().add(Vector(0.5, 0.0, 0.5)))
            player.closeInventory()
        })

    }

    override fun onClick(e: InventoryClickEvent) {
        if (checkForPageChange(e)) return
        if (e.clickedInventory == inv) e.isCancelled = true
        val clickedItem = e.clickedInventory?.getItem(e.slot)
        val action = clickedItem?.getItemAction() ?: return
        when (action) {
            ItemAction.OPEN_SUBMENU -> {
                openGrave(clickedItem.getTag<Int>("grave") ?: return)
            }
            ItemAction.TELEPORT_GRAVE -> {
                teleportToGrave(openGrave ?: return)
            }
            ItemAction.DELETE_GRAVE -> {
                graves.removeAt(openGrave ?: return)
                openPage(currentPage)
            }
            ItemAction.GO_BACK -> {
                openPage(currentPage)
            }
            else -> {}
        }

    }

    private fun getGraveLabel(graveLocation: Location, number: Int, action: ItemAction?): ItemStack {
        val graveItem = ItemStack(Material.CHEST)
            .lock()
            .name(Utils.text("Grave #${number+1}", arrayOf(247, 207, 2)))
            .lore(
                Utils.text("X: ${graveLocation.x}, Y: ${graveLocation.y}, Z: ${graveLocation.z}", arrayOf(191, 191, 187)),
                Utils.text("World: ${graveLocation.world.niceName()}", arrayOf(191, 191, 187))
            )
            .setTag("grave", number)
            .setItemAction(action)
        return graveItem
    }

}