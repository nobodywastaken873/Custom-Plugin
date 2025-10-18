package me.newburyminer.customItems.gui

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.lock
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.setItemAction
import me.newburyminer.customItems.Utils.Companion.setTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.UUID

object GuiItems {
    val NEXT_PAGE: ItemStack
        get() = ItemStack(Material.ARROW)
            .name(Utils.text("Next Page").style(Style.style(TextDecoration.BOLD)))
            .setItemAction(ItemAction.NEXT_PAGE)
            .clone()
    val PREVIOUS_PAGE: ItemStack
        get() = ItemStack(Material.ARROW)
            .name(Utils.text("Previous Page").style(Style.style(TextDecoration.BOLD)))
            .setItemAction(ItemAction.PREVIOUS_PAGE)
            .clone()
    val BACK_ARROW: ItemStack
        get() = ItemStack(Material.ARROW)
            .name(Utils.text("Go Back").style(Style.style(TextDecoration.BOLD)))
            .setItemAction(ItemAction.GO_BACK)
            .clone()

    fun getLocked(item: ItemStack, unique: Boolean = true): ItemStack {
        item.lock()
        if (unique) item.setTag("salt", UUID.randomUUID().toString())
        return item
    }

    fun getFiller(material: Material): ItemStack {
        val item = getLocked(ItemStack(material), true)
        item.name("")
        return item
    }
}