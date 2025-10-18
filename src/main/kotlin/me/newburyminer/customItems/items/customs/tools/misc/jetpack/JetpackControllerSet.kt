package me.newburyminer.customItems.items.customs.tools.misc.jetpack

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class JetpackControllerSet: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.JETPACK_CONTROLLER_SET

    private val material = Material.IRON_INGOT
    private val color = arrayOf(99, 75, 75)
    private val name = text("Jetpack + Controller Set", color)
    private val lore = Utils.loreBlockToList(
        text("Right click to recieve a jetpack and jetpack controller.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEvent -> {
                val item = ctx.item ?: return
                if (!ctx.itemType.isHand()) return
                item.amount -= 1
                e.player.addItemorDrop(ItemRegistry.get(CustomItem.JETPACK))
                e.player.addItemorDrop(ItemRegistry.get(CustomItem.JETPACK_CONTROLLER))
            }

        }

    }

}