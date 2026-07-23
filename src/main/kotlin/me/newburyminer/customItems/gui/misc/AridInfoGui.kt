package me.newburyminer.customItems.gui.misc

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.gui.CustomGui
import me.newburyminer.customItems.gui.GuiLayout
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class AridInfoGui: CustomGui() {
    override val inv: Inventory = Bukkit.createInventory(this, 54, Utils.text("Arid Lands Info").style(Style.style(TextDecoration.BOLD)))

    init {
        GuiLayout.setCircleBorder(Material.LIGHT_BLUE_STAINED_GLASS_PANE, inv)

        val comingSoon = ItemStack(Material.BARRIER)
            .name(Utils.text("Coming Soon!", arrayOf(247, 207, 2)))


        inv.setItem(22, comingSoon)

        GuiLayout.fillEmpty(Material.LIGHT_GRAY_STAINED_GLASS_PANE, inv)

    }

    override fun open(player: Player) {
        player.openInventory(inv)
    }

    override fun onClick(e: InventoryClickEvent) {
        e.isCancelled = true
    }
}