package me.newburyminer.customItems.items.customs.tools.misc.jetpack

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.consumable
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.EventItemType
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class JetpackController: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.JETPACK_CONTROLLER

    private val material = Material.IRON_NUGGET
    private val color = arrayOf(148, 134, 111)
    private val name = text("Jetpack Controller - OFF", color)
    private val lore = Utils.loreBlockToList(
        text("Left click to toggle the jetpack on and off. Hold right click to ascend, and sneak to descend.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .consumable(eatSeconds = 10000.0F)

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                val item = ctx.item ?: return
                if (e.action != Action.LEFT_CLICK_AIR && e.action != Action.LEFT_CLICK_BLOCK) return
                val mode = e.player.getTag<Boolean>("jetpackactive") ?: false
                e.player.setTag("jetpackactive", !mode)
                item.name(text("Jetpack Controller - ${if (!mode) "ON" else "OFF"}", arrayOf(148, 134, 111), bold = true))
            }

        }

    }

}
