package me.newburyminer.customItems.gui

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getItemAction
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.lock
import me.newburyminer.customItems.Utils.Companion.lore
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.setItemAction
import me.newburyminer.customItems.Utils.Companion.setListTag
import me.newburyminer.customItems.Utils.Companion.setTag
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import kotlin.math.pow

class CompassGui(val player: Player): CustomGui() {
    override val inv: Inventory = Bukkit.createInventory(this, 54, Utils.text("Tracking Compass").style(Style.style(TextDecoration.BOLD)))

    init {
        val compassCost = getCompassCost(player)
        val compassInfo = ItemStack(Material.PAPER)
            .name(Utils.text("INFO", arrayOf(247, 207, 2)))
            .loreBlock(
                Utils.text("Click on the compass above to begin tracking, each subsequent use of the compass will cost more. Upon clicking the compass above, the materials will be consumed, and you will be asked to enter a player's name in chat. You cannot track players who are AFK, so make sure to run /afk CHECKALL beforehand.", arrayOf(191, 191, 187)),
                Utils.text(""),
                Utils.text("When a player is tracked, they will be given a 1 minute warning before tracking begins. You can still track them if they logout. They cannot use elytra or use an enderchest while being tracked.", arrayOf(191, 191, 187)),
            )
            .lock()

        GuiLayout.setCircleBorder(Material.RED_STAINED_GLASS_PANE, inv)
        inv.setItem(22, compassCost)
        inv.setItem(31, compassInfo)
        GuiLayout.fillEmpty(Material.LIGHT_GRAY_STAINED_GLASS_PANE, inv)
    }

    override fun open(player: Player) {
        player.openInventory(inv)
    }

    private fun getCompassCost(player: Player): ItemStack {
        val costs = getCost(player).toMutableList()
        val item = ItemStack(Material.COMPASS)
            .name(Utils.text("READ BELOW BEFORE CLICKING THIS", arrayOf(247, 207, 2)))
            .lore(
                Utils.text("Cost: ", arrayOf(222, 138, 53)),
                Utils.text("${costs[0]} Raw Iron Blocks", arrayOf(156, 146, 135)),
                Utils.text("${costs[1]} Raw Gold Blocks", arrayOf(222, 194, 53)),
                Utils.text("${costs[2]} Diamonds", arrayOf(92, 237, 225)),
                Utils.text("${costs[3]} Ancient Debris", arrayOf(71, 54, 40)),
                Utils.text("${costs[4]} Totem(s) of Undying", arrayOf(247, 207, 5)),
            )
            .setItemAction(ItemAction.OPEN_SUBMENU)

        return item
    }

    private fun getCost(player: Player): Array<Int> {
        val uses = (player.getTag<Int>("compassuses") ?: 0) + 1
        val rawIronBlocks = (6 * uses.toDouble().pow(0.3)).toInt()
        val rawGoldBlocks = (2 * uses.toDouble().pow(0.3)).toInt()
        val diamonds = (10 * uses.toDouble().pow(0.3)).toInt()
        val netherite = uses.toDouble().pow(0.6).toInt()
        val totems = (uses.toDouble().pow(0.5)).toInt()
        return arrayOf(rawIronBlocks, rawGoldBlocks, diamonds, netherite, totems)
    }

    override fun onClick(e: InventoryClickEvent) {
        e.isCancelled = true
        val clickedInventory = e.clickedInventory ?: return
        val clickedItem = clickedInventory.getItem(e.slot)

        val action = clickedItem?.getItemAction() ?: return
        if (action != ItemAction.OPEN_SUBMENU) return

        val costs = getCost(player)
        val neededAmounts = linkedMapOf(
            Pair(Material.RAW_IRON_BLOCK, costs[0]),
            Pair(Material.RAW_GOLD_BLOCK, costs[1]),
            Pair(Material.DIAMOND, costs[2]),
            Pair(Material.ANCIENT_DEBRIS, costs[3]),
            Pair(Material.TOTEM_OF_UNDYING, costs[4]),
        )
        val amounts = linkedMapOf(
            Pair(Material.RAW_IRON_BLOCK, 0),
            Pair(Material.RAW_GOLD_BLOCK, 0),
            Pair(Material.DIAMOND, 0),
            Pair(Material.ANCIENT_DEBRIS, 0),
            Pair(Material.TOTEM_OF_UNDYING, 0),
        )

        for (item in player.inventory) {
            if (item == null) continue
            if (item.type in amounts.keys) {
                amounts[item.type] = (amounts[item.type] ?: 0) + item.amount
            }
        }
        for (mat in amounts.keys) {
            if (neededAmounts[mat] ?: 0 > amounts[mat] ?: 0) {
                e.whoClicked.sendMessage(Utils.text("You do not have enough materials.", Utils.FAILED_COLOR))
                (e.whoClicked as Player).playSound(e.whoClicked, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F)
                e.isCancelled = true
                return
            }
        }

        for (item in player.inventory) {
            if (item == null) continue
            if (item.type !in neededAmounts.keys) continue
            val currentItemAmount = item.amount
            val currentNeededAmount = neededAmounts[item.type]!!
            if (currentNeededAmount == 0) continue
            if (currentNeededAmount >= currentItemAmount) {
                neededAmounts[item.type] = neededAmounts[item.type]!! - item.amount
                item.amount = 0
            } else {
                item.amount -= neededAmounts[item.type]!!
                neededAmounts[item.type] = 0
            }
        }
        e.whoClicked.setTag("lookingforname", true)
        e.whoClicked.sendMessage(Utils.text("Enter who you want to track: ", Utils.SUCCESS_COLOR))
        (e.whoClicked as Player).playSound(e.whoClicked, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F)
        Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
            player.closeInventory()
        })
    }

}